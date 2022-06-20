package com.heima.wemedia.service.impl;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heima.common.utils.thrad.WmThreadLocalUtil;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.dtos.WmNewsDto;
import com.heima.model.wemedia.dtos.WmNewsPageReqDto;
import com.heima.model.wemedia.pojos.WmMaterial;
import com.heima.model.wemedia.pojos.WmNews;
import com.heima.model.wemedia.pojos.WmNewsMaterial;
import com.heima.wemedia.contens.WmNewsMessageConstants;
import com.heima.wemedia.mapper.WmMaterialMapper;
import com.heima.wemedia.mapper.WmNewsMapper;
import com.heima.wemedia.mapper.WmNewsMaterialMapper;
import com.heima.wemedia.service.WmNewsService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: heima-leadnews
 * @description: 文章列表
 * @author: hello.xaioyu
 * @create: 2022-06-06 15:31
 **/
@Service
public class WmNewsServiceImpl implements WmNewsService {

    @Autowired
    private WmNewsMapper wmNewsMapper;

    @Autowired
    private WmMaterialMapper wmMaterialMapper;

    @Autowired
    private WmNewsMaterialMapper wmNewsMaterialMapper;

    /**
     * 按条件查询自媒体文章
     *
     * @param dto
     * @return
     */
    @Override
    public ResponseResult findAllNews(WmNewsPageReqDto dto) {
        //1.检查参数
        if (dto == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        //添加查询条件
        //2.分页条件查询
        IPage page = new Page(dto.getPage(), dto.getSize());
        QueryWrapper<WmNews> qw = new QueryWrapper();


        if (dto.getChannelId() != null) {
            qw.lambda().eq(WmNews::getChannelId, dto.getChannelId());
        }
        if (dto.getStatus() != null) {
            qw.lambda().eq(WmNews::getStatus, dto.getStatus());
        }
        //增加查询范围
        if (dto.getBeginPubDate() != null || dto.getEndPubDate() != null) {

            qw.lambda().between(WmNews::getPublishTime, dto.getBeginPubDate(), dto.getEndPubDate());

        }
        //模糊查询
        if (dto.getKeyword() != null) {
            qw.lambda().like(WmNews::getTitle, dto.getKeyword());

        }
        //顺便来拍一下序
        qw.lambda().orderByDesc(WmNews::getCreatedTime);

        //查询用户当前的文章
        Integer wmUser = WmThreadLocalUtil.getWmUser();
        qw.lambda().eq(WmNews::getUserId, wmUser);

        IPage page1 = wmNewsMapper.selectPage(page, qw);

        //根据查询条件查询返回

        ResponseResult responseResult = new PageResponseResult(dto.getPage(), dto.getSize(), (int) page1.getTotal());
        responseResult.setData(page1.getRecords());
        return responseResult;
    }

    @Autowired
    WmNewsTaskServiceImpl wmNewsTaskService;

    @Autowired
    private WmNewsAutoScanServiceImpl wmNewsAutoScanService;
    /**
     * 发布文章或保存草稿
     *
     * @param dto
     * @return
     */
    @Override
    public ResponseResult submitNews(WmNewsDto dto) {
        //条件判断先
        if (dto == null || dto.getContent() == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        //1.保存或修改文章
        WmNews wmNews = new WmNews();
        BeanUtils.copyProperties(dto, wmNews); //拷贝属性
        List<String> images = dto.getImages(); //封面图片
        if (images != null) {
            //根据,号进行分割然后变成字符串
            String imageStr = StringUtils.join(images, ",");
            wmNews.setImages(imageStr);
        }
        //如果当前封面图片是-1就是自动
        if (dto.getType().equals(-1)) {
            wmNews.setType((short) 5); //这里设置为空就是为了我们给他分配封面图片
        }
        //调用方法完成保存
        saveOrUpdateWmNews(wmNews);

        //2.判断是否为草稿 如果为草稿结束当前方法
        if (dto.getStatus().equals(WmNews.Status.NORMAL.getCode())) {
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }

        //3.不是草稿，保存文章内容图片与素材的关系
        //获取到文章内容
        List<String> materials = eectractUrlInfo(dto.getContent());
        saveRelativeInfoForContent(materials, wmNews.getId(), (short) 0);
        //4.不是草稿，保存文章封面图片与素材的关系
        saveRelativeInfoForCover(dto, wmNews, materials);

        //文章审核并发布 这是另外一个线程了
       // wmNewsAutoScanService.autoScanWmNews(wmNews.getId());
        //把任务添加到延迟任务队列
        wmNewsTaskService.addNewsToTask(wmNews.getId(),wmNews.getPublishTime());
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }


    /**
     * 第一个功能：如果当前封面类型为自动，则设置封面类型的数据
     * 匹配规则：
     * 1，如果内容图片大于等于1，小于3  单图  type 1
     * 2，如果内容图片大于等于3  多图  type 3
     * 3，如果内容没有图片，无图  type 0
     * <p>
     * 第二个功能：保存封面图片与素材的关系
     *
     * @param dto
     * @param wmNews
     * @param materials
     */
    private void saveRelativeInfoForCover(WmNewsDto dto, WmNews wmNews, List<String> materials) {

        if (wmNews.getType() == (short) 5) {
            List<String> images = dto.getImages();

            if (materials.size() >= 3) {
                //鉴定为多
                wmNews.setType((short) 3);
                //这里选用的自动，然后他就是没有上传封面图片呗，那么我们就要从内容里面匹配给他，气死了
                images = materials.stream().limit(3).collect(Collectors.toList());
            } else if (materials.size() >= 1 && materials.size() < 3) {
                //鉴定为那啥，单图
                wmNews.setType((short) 1);
                //从文章内容里面分配图片给到封面图片
                images = materials.stream().limit(1).collect(Collectors.toList());

            } else {//鉴定为无图，狠狠的无图
                wmNews.setType((short) 0);

            }

            //截取集合中的图片，转换成字符串存储到数据库里
            //加这个判断的作用是过滤掉无图的栽种，没图数据库图片url就可以不添加数据了
            if (images.size() > 0 && images != null) {

                String imagesUrl = StringUtils.join(images, ",");
                wmNews.setImages(imagesUrl);
            }
            //最后都要保存到数据库，因为这个崽崽他选了自动分配封面，所以我们需要给他分配一遍，然后修改数据库的封面url值即可
            wmNewsMapper.updateById(wmNews);

            //任务二把封面图片的关系和素材关系存到中间表
            saveRelativeInfoForContent(images, wmNews.getId(), (short) 1);

        }

    }


    /**
     * 处理文章内容图片与素材的关系
     *
     * @param materials 图片素材url
     * @param id        文章id
     * @param type      类型 0 就是内容 1代表封面图片
     */
    private void saveRelativeInfoForContent(List<String> materials, Integer id, short type) {
        //批量往中间表里面添加数据
        //1.首先查询出来跟素材表图片路径匹配的路劲
        QueryWrapper<WmMaterial> qw = new QueryWrapper(); //查询的是素材表需要获取素材表的id
        qw.lambda().in(WmMaterial::getUrl, materials);
        //抽取出来的id
        List<WmMaterial> wmMaterials = wmMaterialMapper.selectList(qw);
        List<Integer> MaterialId = new ArrayList<>();
        for (WmMaterial wmMaterial : wmMaterials) {
            Integer id1 = wmMaterial.getId();
            MaterialId.add(id1);
        }

        //向数据库添加数据
        wmNewsMaterialMapper.saveRelations(MaterialId, id, type);


    }


    /**
     * 提取文章内容中的图片信息
     */
    private List<String> eectractUrlInfo(String content) {


        List<Map> maps = JSON.parseArray(content, Map.class);
        List<String> materials = new ArrayList<>(); //创建一个集合用来装获取到的图信息

        for (Map map : maps) {
            if (map.get("type").equals("image")) {

                String images = (String) map.get("value"); //获取文章图片

                materials.add(images);
            }

        }

        return materials;
    }


    /**
     * 保存或者修改
     *
     * @param wmNews
     */
    private void saveOrUpdateWmNews(WmNews wmNews) {
        //补全属性
        wmNews.setUserId(WmThreadLocalUtil.getWmUser());
        wmNews.setCreatedTime(new Date());
        wmNews.setSubmitedTime(new Date());
        wmNews.setEnable((short) 1);//默认上架
        //判断是保存还是修改
        if (wmNews.getId() == null) {
            //保存

            wmNewsMapper.insert(wmNews);


        } else {
            //修改方法 先删除他们的关系
            QueryWrapper<WmNewsMaterial> qw = new QueryWrapper();
            qw.lambda().eq(WmNewsMaterial::getId, wmNews.getId());
            wmNewsMaterialMapper.delete(qw);

            wmNewsMapper.updateById(wmNews);

        }


    }

    /**
     * 查看文章详情
     *
     * @param id 文章id
     * @return 返回文章信息或者抛出异常
     */
    @Override
    public ResponseResult findOneNews(Integer id) {
        //判断文章是否存在，不存在就抛出异常
        if (id == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        QueryWrapper<WmNews> qw = new QueryWrapper();
        qw.lambda().eq(WmNews::getId, id);
        WmNews wmNews = wmNewsMapper.selectOne(qw);

        if (wmNews == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NEWS_NOT_EXIST);
        }

        //文章id存在返回出去

        return ResponseResult.okResult(wmNews);
    }


    /**
     * 删除文章
     * '当前状态\r\n  0 草稿\r\n  1 提交（待审核）\r\n  2 审核失败\r\n 3 人工审核\r\n  4 人工审核通过\r\n
     * 8 审核通过（待发布）\r\n   9 已发布',
     * `publish_time` datetime DEFAULT NULL COMMENT '定时发布时间，不定时则为空',
     *
     * @param id 文章id
     * @return 返回删除成功或者抛出异常
     */
    @Override
    public ResponseResult delNews(Integer id) {
        //判断文章是否存在
        if (id == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        QueryWrapper<WmNews> qw = new QueryWrapper();
        qw.lambda().eq(WmNews::getId, id);
        WmNews wmNews = wmNewsMapper.selectOne(qw);

        if (wmNews == null) {
            //抛出文章不存在的异常
            return ResponseResult.errorResult(AppHttpCodeEnum.NEWS_NOT_EXIST);
        }

        //判断文章是否已经发布
        if (wmNews.getStatus()==9){
            //文章已发布，宁不可以这样做
            return ResponseResult.errorResult(501,"这b文章已经发布了，宁别这样");
        }

        //都符合规定那么就安排删除成功
         wmNewsMapper.deleteById(id);

        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }


    @Autowired
    private KafkaTemplate kafkaTemplate;

    /**
     * 文章上架或者下架
     * @param wmNewsDto  enable 0上架 1下架
     * @return
     */
    @Override
    public ResponseResult downOrUp(WmNewsDto wmNewsDto) {

        if (wmNewsDto.getId()==null||wmNewsDto.getEnable()==null){

            return ResponseResult.errorResult(501,"文章id不可缺少");
        }
        //查文章是否存在
        QueryWrapper<WmNews> qw = new QueryWrapper();
        qw.lambda().eq(WmNews::getId, wmNewsDto.getId());
        WmNews wmNews = wmNewsMapper.selectOne(qw);

        if (wmNews == null) {
            //抛出文章不存在的异常
            return ResponseResult.errorResult(AppHttpCodeEnum.NEWS_NOT_EXIST);
        }
        //先查询文章的状态，如果是还没发布的就不支持上架下架操作直接抛异常
        if (wmNews.getStatus()!=9){
            return ResponseResult.errorResult(501,"哦，谢特这文章还没发布呢，宁别这样好么");
        }

        wmNews.setEnable(wmNewsDto.getEnable());
        wmNewsMapper.updateById(wmNews);
         //发送消息给文章方法
        if (wmNews!=null) {
            Long articleId = wmNews.getArticleId();
                Map map=new HashMap();
                map.put("id",articleId);
            map.put("enable",wmNewsDto.getEnable());
            kafkaTemplate.send(WmNewsMessageConstants.WM_NEWS_UP_OR_DOWN_TOPIC,JSON.toJSONString(map));
        }
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }


}

package com.heima.wemedia.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.heima.apis.article.IArticleClient;

import com.heima.file.service.FileStorageService;

import com.heima.model.article.dto.ArticleDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.pojos.WmChannel;
import com.heima.model.wemedia.pojos.WmNews;
import com.heima.model.wemedia.pojos.WmSensitive;
import com.heima.model.wemedia.pojos.WmUser;

import com.heima.utils.common.SensitiveWordUtil;
import com.heima.wemedia.mapper.*;
import com.heima.wemedia.service.WmNewsAutoScanService;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericData;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@Slf4j
@Transactional
public class WmNewsAutoScanServiceImpl implements WmNewsAutoScanService {

    @Autowired
    private WmNewsMapper wmNewsMapper;

    @Autowired
    private WmNewsMaterialMapper wmNewsMaterialMapper;//中间表

    //媒体素材表
    @Autowired
    private WmMaterialMapper wmMaterialMapper;

    /**
     * 自媒体文章审核
     *
     * @param id 自媒体文章id
     */
    @Override
    @Async
    public void autoScanWmNews(Integer id) {
        //查询自媒体文章
        WmNews wmNews = wmNewsMapper.selectById(id);
        if (wmNews == null) {
            throw new RuntimeException("文章不存在");

        }


        //从内容中提取文本和图片
        Map<String, Object> map = handleTextAndImages(wmNews);

        List<String> image = (List<String>) map.get("image");
        List<String> text = (List<String>) map.get("text");
        Stream<String> limit = image.stream().limit(1);
        System.out.println(limit);
        System.out.println(image + "" + text);
        String text1 = map.get("text").toString();

         handleSensitiveScan(text1,wmNews);
        //审核成功，保存app端相关的内容数据
        ResponseResult responseResult = saveAppArtcile(wmNews);
        //修改状态值发布
        wmNews.setStatus((short) 9);
        wmNews.setReason("审核成功");
        wmNews.setArticleId((long) responseResult.getData());//回填id
        wmNewsMapper.updateById(wmNews);

    }

    @Autowired
    private WmchannelMapper wmchannelMapper; //查询文章频道

    @Autowired
    private WmUserMapper wmUserMapper;//查询发布文章用户的昵称

    @Autowired
    private IArticleClient iArticleClient;

    /**
     * 保存文章到app端
     *
     * @param wmNews 自媒体文章信息
     */
    private ResponseResult saveAppArtcile(WmNews wmNews) {
        ArticleDto dto = new ArticleDto();
        //属性的拷贝
        BeanUtils.copyProperties(wmNews, dto);
        //文章的布局
        dto.setLayout(wmNews.getType());
        //频道
        WmChannel wmChannel = wmchannelMapper.selectById(wmNews.getChannelId());
        if (wmChannel != null) {
            dto.setChannelName(wmChannel.getName());
        }

        //作者
        dto.setAuthorId(wmNews.getUserId().longValue());
        WmUser wmUser = wmUserMapper.selectById(wmNews.getUserId());
        if (wmUser != null) {
            dto.setAuthorName(wmUser.getName());
        }

        //设置文章id
        if (wmNews.getArticleId() != null) {
            dto.setId(wmNews.getArticleId());
        }
        dto.setCreatedTime(new Date());

        ResponseResult responseResult = iArticleClient.saveArticle(dto);

        return responseResult;
    }

    /**
     * 提取内容中的文本和图片
     *
     * @param wmNews
     * @return
     */
    private Map<String, Object> handleTextAndImages(WmNews wmNews) {
        //转成json
        String content = wmNews.getContent();
        List<Map> maps = JSON.parseArray(content, Map.class);

        Map<String, Object> textAndImages = new HashMap();
        List<String> text = new ArrayList<>();
        List<String> Images = new ArrayList<>();

        for (Map map : maps) {
            if (map.get("type").equals("text")) {
                //鉴定为文本
                String vule = (String) map.get("value");
                text.add(vule);
            }
            if (map.get("type").equals("image")) {
                String value = (String) map.get("value");
                Images.add(value);

            }
        }

        textAndImages.put("text", text);
        textAndImages.put("image", Images);

        return textAndImages;
    }

    @Autowired
    private WmSensitiveMapper wmSensitiveMapper;

    /**
     * 自管理的敏感词审核
     * @param content
     * @param wmNews
     * @return
     */
    private boolean handleSensitiveScan(String content, WmNews wmNews) {

        boolean flag = true;

        //获取所有的敏感词
        List<WmSensitive> wmSensitives = wmSensitiveMapper.selectList(Wrappers.<WmSensitive>lambdaQuery().select(WmSensitive::getSensitives));
        List<String> sensitiveList = wmSensitives.stream().map(WmSensitive::getSensitives).collect(Collectors.toList());

        //初始化敏感词库
        SensitiveWordUtil.initMap(sensitiveList);

        //查看文章中是否包含敏感词
        Map<String, Integer> map = SensitiveWordUtil.matchWords(content);
        if(map.size() >0){


            wmNews.setStatus((short)2);
            wmNews.setReason("当前当前文章中存在违规内容"+map);

            wmNewsMapper.updateById(wmNews);
            flag = false;
        }

        return flag;
    }

}
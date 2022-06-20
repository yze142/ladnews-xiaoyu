package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heima.common.utils.thrad.WmThreadLocalUtil;
import com.heima.file.service.impl.MinIOFileStorageService;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.dtos.WmMaterialDto;
import com.heima.model.wemedia.pojos.WmMaterial;
import com.heima.model.wemedia.pojos.WmNewsMaterial;
import com.heima.wemedia.mapper.WmMaterialMapper;
import com.heima.wemedia.mapper.WmNewsMaterialMapper;
import com.heima.wemedia.service.WmMaterialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

/**
 * @program: heima-leadnews
 * @description: 自媒实现类
 * @author: hello.xaioyu
 * @create: 2022-06-05 23:19
 **/
@Service
@Slf4j
public class WmMaterialServiceImpl implements WmMaterialService {

    @Autowired
    private MinIOFileStorageService fileStorageService;

    @Autowired
    private WmMaterialMapper wmMaterialMapper;

    @Autowired
    private WmNewsMaterialMapper wmNewsMaterialMapper;

    /**
     * 上传图片素材
     *
     * @param multipartFile
     * @return
     */
    @Override
    public ResponseResult uploadPicture(MultipartFile multipartFile) {

        String uploadImgFile = null;
        try {
            //获取文件名字然后拼接
            String replace = UUID.randomUUID().toString().replace("-", "");//这个方法就是替换
            String originalFilename = multipartFile.getOriginalFilename();
            String substring = originalFilename.substring(originalFilename.indexOf("."));//获取文件后缀名
            String newFile = replace + substring;
            //执行上传文件的操作
            uploadImgFile = fileStorageService.uploadImgFile("", newFile, multipartFile.getInputStream());
            log.info("上传图片到{}成功", uploadImgFile);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("上传文件失败");

        }

        //把路径添加到数据库并且返回出去
        WmMaterial wmMaterial = new WmMaterial();
        wmMaterial.setUserId(WmThreadLocalUtil.getWmUser());
        wmMaterial.setUrl(uploadImgFile);
        wmMaterial.setIsCollection((short) 0);
        wmMaterial.setType((short) 0);
        wmMaterial.setCreatedTime(new Date());
        wmMaterialMapper.insert(wmMaterial);

        //4.返回结果

        return ResponseResult.okResult(wmMaterial);
    }

    /**
     * 查看图片列表
     *
     * @param dto 1代表收藏 0代表没收藏
     * @return
     */
    @Override
    public ResponseResult findList(WmMaterialDto dto) {
        //1.检查参数
        dto.checkParam();

        QueryWrapper<WmMaterial> qw = new QueryWrapper();


        //判断用户传入的参数是否是带有收藏的
        if (dto.getIsCollection() != null && dto.getIsCollection() == 1) {
            //鉴定为需要查询收藏的
            //添加一个查询条件为收藏
            qw.lambda().eq(WmMaterial::getIsCollection, dto.getIsCollection());

        }
        //获取用户id
        Integer UserId = WmThreadLocalUtil.getWmUser();
        qw.lambda().eq(WmMaterial::getUserId, UserId);
        qw.orderByAsc("created_time");

        //分页查询用户
        IPage page = new Page((dto.getPage() - 1 * dto.getSize()), dto.getSize());


        IPage page1 = wmMaterialMapper.selectPage(page, qw);


        ResponseResult responseResult = new PageResponseResult(dto.getPage(), dto.getSize(), (int) page1.getTotal());

        responseResult.setData(page1.getRecords());
        return responseResult;
    }

    /**
     * 删除图片
     *
     * @param id 素材id
     * @return
     */
    @Override
    public ResponseResult deleteMaterial(Integer id) {
        //根据素材id查询是否被占用 DELETE_NULL DELETE_FAILED
        if (id == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DELETE_NULL);
        }
        //判断文章和素材中间表里面是否有这张图片的引用
        QueryWrapper<WmNewsMaterial> qw = new QueryWrapper();
        qw.lambda().eq(WmNewsMaterial::getMaterialId, id);
        WmNewsMaterial wmNewsMaterial = wmNewsMaterialMapper.selectOne(qw);
        //没有被引用就可以删除了
        if (wmNewsMaterial != null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DELETE_FAILED);
        }
        //去素材表里面删除图
        QueryWrapper<WmMaterial> qw2 = new QueryWrapper();
        qw2.lambda().eq(WmMaterial::getId, id);
        wmMaterialMapper.delete(qw2);


        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    /**
     * 收藏和取消收藏融为一体图片 is_collection 0代表未收藏，1代表收藏
     *
     * @param id
     * @return
     */
    @Override
    public ResponseResult CollectionMaterial(Integer id,Short IsCollection) {
        if (id == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        try {
            WmMaterial wmMaterial = new WmMaterial();
            wmMaterial.setIsCollection(IsCollection);
            wmMaterial.setId(id);

            wmMaterialMapper.updateById(wmMaterial);
        } catch (Exception e) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }


        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }



}

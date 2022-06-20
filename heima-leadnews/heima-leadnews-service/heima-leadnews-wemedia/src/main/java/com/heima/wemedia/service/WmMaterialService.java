package com.heima.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmMaterialDto;
import com.heima.model.wemedia.pojos.WmMaterial;
import org.springframework.web.multipart.MultipartFile;

public interface WmMaterialService {

    /**
     * 图片上传
     * @param multipartFile
     * @return
     */
    public ResponseResult uploadPicture(MultipartFile multipartFile);

    /**
     * 查看图片列表
     * @param dto
     * @return
     */
    ResponseResult findList(WmMaterialDto dto);


    /**
     * 删除图片
     * @param id
     * @return
     */
    ResponseResult deleteMaterial(Integer id);

    /**
     * 收藏图片
     * @param id
     * @return
     */
    ResponseResult CollectionMaterial(Integer id,Short IsCollection);
}
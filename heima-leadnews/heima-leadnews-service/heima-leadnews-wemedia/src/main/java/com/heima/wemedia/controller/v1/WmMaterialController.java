package com.heima.wemedia.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmMaterialDto;
import com.heima.wemedia.service.WmMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/material")
public class WmMaterialController {

    @Autowired
    private WmMaterialService wmMaterialService;

    /**
     * 上传图片
     *
     * @param multipartFile
     * @return
     */
    @PostMapping("/upload_picture")
    public ResponseResult uploadPicture(MultipartFile multipartFile) {
        return wmMaterialService.uploadPicture(multipartFile);
    }

    /**
     * 查看图片素材列表
     *
     * @param dto
     * @return
     */
    @PostMapping("/list")
    public ResponseResult findList(@RequestBody WmMaterialDto dto) {
        return wmMaterialService.findList(dto);
    }

    /**
     * 删除图片
     * /api/v1/material/del_picture/{id}
     */
    @GetMapping("/del_picture/{id}")
    public ResponseResult deleteMaterial(@PathVariable("id")Integer id){

          return wmMaterialService.deleteMaterial(id);
   }

    /** 收藏
     * /api/v1/material/collect/{id}
     */
   @GetMapping("/collect/{id}")
   public ResponseResult CollectionMaterial(@PathVariable("id")Integer id){

       return wmMaterialService.CollectionMaterial(id,(short)1);
   }

    /** 取消收藏
     * /api/v1/material/cancel_collect/{id}
     */
    @GetMapping("/cancel_collect/{id}")
    public ResponseResult CancelCollectionMaterial(@PathVariable("id")Integer id){

        return wmMaterialService.CollectionMaterial(id,(short)0);
    }




}
package com.heima.admin.controller.v1;

import com.heima.apis.admin.feign.IArSensitiveClient;
import com.heima.model.admin.dto.SensitiveDto;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: heima-leadnews
 * @description:
 * @author: hello.xaioyu
 * @create: 2022-06-16 23:57
 **/
@RequestMapping("/api/v1/sensitive")
@RestController
public class AdSensitiveController {

    @Autowired
    private IArSensitiveClient iArSensitiveClient;

    /**
     * 查询敏感词列表
     * @param sensitiveDto
     * @return
     */
    @RequestMapping("/list")
    public ResponseResult selectSensutiveList(@RequestBody SensitiveDto sensitiveDto){

     return    iArSensitiveClient.selectSensutiveList(sensitiveDto);

    }


    @DeleteMapping("/del/{id}")
    public ResponseResult delSensitive(@PathVariable("id") Integer id){

        return iArSensitiveClient.delSensitive(id);
    }






}

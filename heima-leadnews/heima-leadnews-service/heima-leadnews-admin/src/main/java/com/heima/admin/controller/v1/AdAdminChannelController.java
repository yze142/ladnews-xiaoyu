package com.heima.admin.controller.v1;

import com.heima.apis.admin.feign.IArSensitiveClient;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmChannelDto;
import com.heima.model.wemedia.pojos.WmChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: heima-leadnews
 * @description: 频道管理
 * @author: hello.xaioyu
 * @create: 2022-06-17 16:03
 **/
@RestController
@RequestMapping("api/v1/channel/")
public class AdAdminChannelController {

    @Autowired
    private IArSensitiveClient iArSensitiveClient;


    /**
     * 删除频道
     * `/api/v1/channel/del/{id}`
     */
    @DeleteMapping("del/{id}")
    public ResponseResult delChannel(@PathVariable("id")Integer id){
        return iArSensitiveClient.delChannel(id);
    };

    /**
     * 模糊分页查询
     */
    @PostMapping("/list")
    public ResponseResult selectChannel(@RequestBody WmChannelDto wmChannelDto){
        return iArSensitiveClient.selectChannel(wmChannelDto);
    };

    /**
     * 新增
     * `/api/v1/channel/save`
     */
    @PostMapping("/save")
    public ResponseResult saveChannel(@RequestBody WmChannel wmChannel){

        return iArSensitiveClient.saveChannel(wmChannel);
    };






}

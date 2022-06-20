package com.heima.apis.admin.feign;

import com.heima.model.admin.dto.SensitiveDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmChannelDto;
import com.heima.model.wemedia.pojos.WmChannel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "leadnews-admin")
public interface IArSensitiveClient {

    /**
     * 查看铭感词
     * @param sensitiveDto
     * @return
     */
    @PostMapping("/api/v1/sensitive/list")
    public ResponseResult selectSensutiveList( @RequestBody SensitiveDto sensitiveDto);


    /**
     * 是删除敏感词
     * @param id
     * @return
     */
       @DeleteMapping("/api/v1/sensitive/del/{id}")
    public ResponseResult delSensitive(@PathVariable("id")Integer id);


    /**
     * 删除频道
     * `/api/v1/channel/del/{id}`
     */
    @GetMapping("/api/v1/channel/del/{id}")
    public ResponseResult delChannel(@PathVariable("id")Integer id);

    /**
     * 模糊分页查询
     */
     @PostMapping("/api/v1/channel/list")
    public ResponseResult selectChannel(@RequestBody WmChannelDto wmChannelDto);

    /**
     * 新增
     * `/api/v1/channel/save`
     */
    @PostMapping("/api/v1/channel/save")
    public ResponseResult saveChannel(@RequestBody WmChannel wmChannel);


}

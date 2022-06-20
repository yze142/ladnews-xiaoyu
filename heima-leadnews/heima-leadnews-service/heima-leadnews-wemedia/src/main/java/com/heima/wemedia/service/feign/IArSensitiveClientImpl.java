package com.heima.wemedia.service.feign;

import com.heima.apis.admin.feign.IArSensitiveClient;
import com.heima.model.admin.dto.SensitiveDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmChannelDto;
import com.heima.model.wemedia.pojos.WmChannel;
import com.heima.wemedia.service.WmSensitiveService;
import com.heima.wemedia.service.WmchannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: heima-leadnews
 * @description:
 * @author: hello.xaioyu
 * @create: 2022-06-16 23:19
 **/
@RestController
public class IArSensitiveClientImpl implements IArSensitiveClient {

    @Autowired
    private WmSensitiveService wmSensitiveService;

    /**
     * 查询敏感词列表
     *
     * @param sensitiveDto
     * @return
     */
    @Override
    @PostMapping("/api/v1/sensitive/list")
    public ResponseResult selectSensutiveList(@RequestBody SensitiveDto sensitiveDto) {


        return wmSensitiveService.selectSensutiveList(sensitiveDto);
    }

    @Override
    @DeleteMapping("/api/v1/sensitive/del/{id}")
    public ResponseResult delSensitive(@PathVariable("id") Integer id) {
        return wmSensitiveService.delSensitive(id);
    }

    @Autowired
    private WmchannelService wmchannelService;

    /**
     * 删除频道
     * `/api/v1/channel/del/{id}`
     */
    @Override
    @GetMapping("/api/v1/channel/del/{id}")
    public ResponseResult delChannel(@PathVariable("id") Integer id) {

        return   wmchannelService.delChannel(id);

    }



    /**
     * 模糊分页查询
     */
    @Override
    @PostMapping("/api/v1/channel/list")
    public ResponseResult selectChannel(@RequestBody WmChannelDto wmChannelDto) {
        return wmchannelService.selectChannel(wmChannelDto);
    }

    /**
     * 新增
     * `/api/v1/channel/save`
     */
    @Override
    @PostMapping("/api/v1/channel/save")
    public ResponseResult saveChannel(@RequestBody WmChannel wmChannel) {
        return wmchannelService.seveChannel(wmChannel);
    }


}

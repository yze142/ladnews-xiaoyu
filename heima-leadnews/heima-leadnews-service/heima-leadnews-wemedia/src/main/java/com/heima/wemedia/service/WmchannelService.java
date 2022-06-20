package com.heima.wemedia.service;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmChannelDto;
import com.heima.model.wemedia.dtos.WmNewsDto;
import com.heima.model.wemedia.pojos.WmChannel;

//频道
public interface WmchannelService {


    /**
     * 查询所有频道
     * @return
     */
    ResponseResult findAll();


    /**
     * 删除频道
     */
    ResponseResult delChannel(Integer id);

    /**
     * 查看频道
     */
    ResponseResult selectChannel(WmChannelDto wmChannelDto);


    /**
     * 新增频道
     */
    ResponseResult  seveChannel(WmChannel wmChannel);

}

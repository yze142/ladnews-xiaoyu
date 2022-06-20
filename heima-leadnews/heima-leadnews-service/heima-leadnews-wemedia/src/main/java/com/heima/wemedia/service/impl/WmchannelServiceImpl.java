package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heima.common.utils.thrad.WmThreadLocalUtil;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.dtos.WmChannelDto;
import com.heima.model.wemedia.dtos.WmNewsDto;
import com.heima.model.wemedia.pojos.WmChannel;
import com.heima.model.wemedia.pojos.WmNews;
import com.heima.model.wemedia.pojos.WmSensitive;
import com.heima.wemedia.mapper.WmNewsMapper;
import com.heima.wemedia.mapper.WmchannelMapper;
import com.heima.wemedia.service.WmchannelService;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @program: heima-leadnews
 * @description: 频道实现类
 * @author: hello.xaioyu
 * @create: 2022-06-06 15:13
 **/
@Service
public class WmchannelServiceImpl implements WmchannelService {

    @Autowired
    private WmchannelMapper wmchannelMapper;

    @Autowired
    private WmNewsMapper wmNewsMapper;

    /**
     * 查询所有频道
     *
     * @return
     */
    @Override
    public ResponseResult findAll() {
        //直接查询然后返回即可
        List<WmChannel> wmChannels = wmchannelMapper.selectList(null);

        return ResponseResult.okResult(wmChannels);
    }

    /**
     * 删除频道
     *
     * @param id
     * @return
     */
    @Override
    public ResponseResult delChannel(Integer id) {
        wmchannelMapper.deleteById(id);

        return ResponseResult.errorResult(AppHttpCodeEnum.SUCCESS);
    }

    /**
     * 分页查询频道
     *
     * @return
     */
    @Override
    public ResponseResult selectChannel(WmChannelDto wmChannelDto) {
        if (wmChannelDto == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        String name = wmChannelDto.getName();
        Integer page = wmChannelDto.getPage();
        Integer size = wmChannelDto.getSize();

        QueryWrapper<WmChannel> qw = new QueryWrapper();
        //判断是否携带文字查询
        if (name != null) {

            qw.lambda().like(WmChannel::getName, wmChannelDto.getName());
        }
        if (page == null || size == null) {
            page = 0;
            size = 10;
        }

        IPage page1 = new Page(page, size);

        IPage page2 = wmchannelMapper.selectPage(page1, qw);
        List<WmSensitive> records = page2.getRecords();

        return ResponseResult.okResult(records);
    }




    /**
     * 新增频道
     * @return
     */
    @Override
    public ResponseResult seveChannel(WmChannel wmChannel) {
         wmchannelMapper.insert(wmChannel);

        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }


}

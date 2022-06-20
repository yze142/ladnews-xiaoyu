package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heima.model.admin.dto.SensitiveDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.pojos.WmSensitive;
import com.heima.wemedia.mapper.WmSensitiveMapper;
import com.heima.wemedia.service.WmSensitiveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: heima-leadnews
 * @description:
 * @author: hello.xaioyu
 * @create: 2022-06-16 22:40
 **/
@Service
@Slf4j
public class WmSenSitiveServiceImpl implements WmSensitiveService {


    @Autowired
    private WmSensitiveMapper wmSensitiveMapper;

    /**
     * 删除铭感词
     *
     * @param id
     * @return
     */
    @Override
    public ResponseResult delSensitive(Integer id) {
        wmSensitiveMapper.deleteById(id);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }


    /**
     * 查询铭感词
     * @param sensitiveDto
     * @return
     */
    @Override
    public ResponseResult selectSensutiveList(SensitiveDto sensitiveDto) {
        if (sensitiveDto == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        String name = sensitiveDto.getName();
        Integer page = sensitiveDto.getPage();
        Integer size = sensitiveDto.getSize();

        QueryWrapper<WmSensitive> qw = new QueryWrapper();
        //判断是否携带文字查询
        if (name != null) {

            qw.lambda().like(WmSensitive::getSensitives, sensitiveDto.getName());
        }
        if (page == null || size == null) {
            page = 0;
            size = 10;
        }

        IPage page1 = new Page(page, size);

        IPage page2 = wmSensitiveMapper.selectPage(page1, qw);
        List<WmSensitive> records = page2.getRecords();

        return ResponseResult.okResult(records);
    }


}

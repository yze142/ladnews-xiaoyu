package com.heima.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heima.model.admin.ApUserRealname;
import com.heima.model.admin.dto.AuthDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.user.mapper.ApUserAuthMapper;
import com.heima.user.service.ApUserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: heima-leadnews
 * @description: 用户审核
 * @author: hello.xaioyu
 * @create: 2022-06-17 19:28
 **/
@Service
public class ApUserAuthServiceImpl implements ApUserAuthService {

  @Autowired
  private ApUserAuthMapper apUserAuthMapper;


    /**
     * 查询用户审核
     * @param authDto
     * @return
     */
    @Override
    public ResponseResult selectUserAuth(AuthDto authDto) {

       if (authDto==null){

      return      ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
       }
       QueryWrapper<ApUserRealname> qw=new QueryWrapper();
       //不为空就是增加了条件
       if (authDto.getStatus()!=null){
           qw.lambda().eq(ApUserRealname::getStatus,authDto.getStatus());
       }
       //设置分页
       IPage page=new Page(authDto.getPage(),authDto.getSize());

        IPage page1 = apUserAuthMapper.selectPage(page, qw);


        return ResponseResult.okResult(page1.getRecords());
    }
}

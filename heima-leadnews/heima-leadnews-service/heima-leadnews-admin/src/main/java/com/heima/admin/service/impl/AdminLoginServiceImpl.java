package com.heima.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.heima.admin.mapper.AdminLoginMapper;
import com.heima.admin.service.AdminLoginService;
import com.heima.model.admin.AdUser;
import com.heima.model.admin.dto.AdminDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.utils.common.AppJwtUtil;
import com.heima.utils.common.MD5Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: heima-leadnews
 * @description:
 * @author: hello.xaioyu
 * @create: 2022-06-16 15:08
 **/
@Service
@Slf4j
public class AdminLoginServiceImpl implements AdminLoginService {
  @Autowired
  private AdminLoginMapper adminLoginMapper;

    /**
     * 用户登录
     * @param adminDto
     * @return
     */
    @Override
    public ResponseResult adminLoginService(AdminDto adminDto) {

        //获取用户名和密码
        QueryWrapper<AdUser> qw=new QueryWrapper();
        qw.lambda().eq(AdUser::getName,adminDto.getName());
        AdUser adUser = adminLoginMapper.selectOne(qw);

        String name = adminDto.getName();
        String password = adminDto.getPassword();
        if (adUser==null){
            return ResponseResult.errorResult(1002,"用户不存在");
        }

        //进行加盐对比 LOGIN_PASSWORD_ERROR表示密码错误的枚举
        String md5Password = MD5Utils.encodeWithSalt(password, adUser.getSalt());
        if (!md5Password.equals(adUser.getPassword())){

            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
        }
        //正确就查询数据进行返回
        String token = AppJwtUtil.getToken(adUser.getId().longValue());

        Map<String, Object> map=new HashMap();
        map.put("user",adUser);
        map.put("token",token);

        return ResponseResult.okResult(map);
    }
}

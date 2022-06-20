package com.heima.user.feign;

import com.heima.apis.admin.feign.ADauthClient;
import com.heima.model.admin.dto.AuthDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.user.service.ApUserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: heima-leadnews
 * @description: 用户审核
 * @author: hello.xaioyu
 * @create: 2022-06-17 18:08
 **/
@RestController
public class ADauthClientImpl implements ADauthClient {

    @Autowired
    private ApUserAuthService apUserAuthService;

    /**
     * 查询用户审核列表
     * @param authDto
     * @return
     */
    @Override
    @PostMapping("/api/v1/auth/list")
    public ResponseResult selectUserAuth(@RequestBody AuthDto authDto) {

        return apUserAuthService.selectUserAuth(authDto);
    }
}

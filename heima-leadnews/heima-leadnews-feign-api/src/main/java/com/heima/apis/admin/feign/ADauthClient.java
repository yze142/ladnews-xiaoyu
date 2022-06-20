package com.heima.apis.admin.feign;

import com.heima.model.admin.dto.AuthDto;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

//身份验证
@FeignClient(value = "leadnews-admin-auth")
public interface ADauthClient {

    /** 查询用户审核列表
     * `/api/v1/auth/list`
     */
    @PostMapping("/api/v1/auth/list")
    public ResponseResult selectUserAuth(@RequestBody AuthDto authDto);



}

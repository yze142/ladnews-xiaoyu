package com.heima.admin.controller.v1;

import com.heima.apis.admin.feign.ADauthClient;
import com.heima.model.admin.ApUserRealname;
import com.heima.model.admin.dto.AuthDto;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: heima-leadnews
 * @description: 用户审核
 * @author: hello.xaioyu
 * @create: 2022-06-17 19:54
 **/
@RestController
@RequestMapping("/api/v1/auth")
public class AdAdminAuthController {

    @Autowired
    private ADauthClient aDauthClient;

    /**
     * 查看用户审核列表
     * /list
     */
   @PostMapping("/list")
    public ResponseResult selectUserAuth(@RequestBody AuthDto authDto){

   return     aDauthClient.selectUserAuth(authDto);
   }


}

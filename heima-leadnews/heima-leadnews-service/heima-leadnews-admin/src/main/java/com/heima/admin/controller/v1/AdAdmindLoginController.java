package com.heima.admin.controller.v1;

import com.heima.admin.service.AdminLoginService;
import com.heima.model.admin.dto.AdminDto;
import com.heima.model.admin.dto.SensitiveDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: heima-leadnews
 * @description: 后台用户登录
 * @author: hello.xaioyu
 * @create: 2022-06-16 14:38
 **/
@RequestMapping("/login")
@RestController
public class AdAdmindLoginController {

    @Autowired
    private AdminLoginService adminLoginService;




    /**
     * 用户登录
     * @param adminDto
     * @return
     */
    @PostMapping("/in")
    public ResponseResult  adminLogin(@RequestBody AdminDto adminDto){

        return adminLoginService.adminLoginService(adminDto);
    }





}

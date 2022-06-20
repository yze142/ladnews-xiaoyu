package com.heima.admin.service;

import com.heima.model.admin.dto.AdminDto;
import com.heima.model.common.dtos.ResponseResult;

public interface AdminLoginService {

    /**
     * 用户登录
     */
     ResponseResult adminLoginService(AdminDto adminDto);

}

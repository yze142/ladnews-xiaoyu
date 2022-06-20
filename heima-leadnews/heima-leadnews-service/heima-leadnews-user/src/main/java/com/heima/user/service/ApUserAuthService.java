package com.heima.user.service;

import com.heima.model.admin.dto.AuthDto;
import com.heima.model.common.dtos.ResponseResult;

public interface ApUserAuthService {

    /**
     * 查询用户待审核
     * @param authDto
     * @return
     */
    public ResponseResult selectUserAuth(AuthDto authDto);

}

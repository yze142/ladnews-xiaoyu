package com.heima.model.admin.dto;

import lombok.Data;

/**
 * @program: heima-leadnews
 * @description: 审核用户信息
 * @author: hello.xaioyu
 * @create: 2022-06-17 17:57
 **/
@Data
public class AuthDto {

    Integer id;

    String msg;

    Integer page;

    Integer size;

    Integer status;


}

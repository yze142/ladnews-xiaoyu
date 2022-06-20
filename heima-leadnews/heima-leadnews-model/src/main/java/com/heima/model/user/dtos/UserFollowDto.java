package com.heima.model.user.dtos;

import com.heima.model.common.annotation.IdEncrypt;
import lombok.Data;

/**
 * @program: heima-leadnews
 * @description:
 * @author: hello.xaioyu
 * @create: 2022-06-19 15:35
 **/
@Data
public class UserFollowDto {

    @IdEncrypt
    Long articleId; // 文章id

    Integer authorId;// 作者id

    Short operation; // 0代表关注1代表取消


}

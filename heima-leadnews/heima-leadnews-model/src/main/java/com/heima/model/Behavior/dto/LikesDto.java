package com.heima.model.Behavior.dto;

import com.heima.model.common.annotation.IdEncrypt;
import lombok.Data;

/**
 * @program: heima-leadnews
 * @description:
 * @author: hello.xaioyu
 * @create: 2022-06-19 15:19
 **/
@Data
public class LikesDto {



    @IdEncrypt
    private Long articleId; //文章id
    Short type;

}

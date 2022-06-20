package com.heima.model.Behavior.dto;

import com.heima.model.common.annotation.IdEncrypt;
import lombok.Data;

/**
 * @program: heima-leadnews
 * @description: 点赞请求dto
 * @author: hello.xaioyu
 * @create: 2022-06-18 23:06
 **/
@Data
public class BehaviorDto {

    @IdEncrypt
    private Long articleId; //文章id

    private short operation; //0 点赞   1 取消点赞

    private short type;//类型 0文章  1动态   2评论


}

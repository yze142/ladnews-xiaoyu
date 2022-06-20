package com.heima.model.mess;

import lombok.Data;

/**
 * @program: heima-leadnews
 * @description:
 * @author: hello.xaioyu
 * @create: 2022-06-20 10:39
 **/
@Data
public class UpdateArticleMess {

    private UpdateArticleType type; //修改文章的字段类型

    private Long articleId; //文章id

    private Integer add;// 修改数据的增量 可为正负

    public enum UpdateArticleType{
        COLLECTION,COMMENT,LIKES,VIEWS;
    }



}

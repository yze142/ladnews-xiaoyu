package com.heima.apis.article.feign;

import com.heima.apis.article.IArticleClient;
import com.heima.model.article.dto.ArticleDto;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: heima-leadnews
 * @description:
 * @author: hello.xaioyu
 * @create: 2022-06-08 10:24
 * 实现feign远程调用的方法
 **/
@RestController
public class ArticleClient implements IArticleClient {



    /**
     * 保存文章到app端的方法
     * @param dto
     * @return
     */
    @Override
    @PostMapping("/api/v1/artile/save")
    public ResponseResult saveArticle(ArticleDto dto){
        return null;
    }
}

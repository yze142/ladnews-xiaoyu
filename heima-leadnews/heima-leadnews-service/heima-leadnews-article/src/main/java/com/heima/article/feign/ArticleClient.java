package com.heima.article.feign;

import com.heima.apis.article.IArticleClient;
import com.heima.article.servce.ArticleService;
import com.heima.model.article.dto.ArticleDto;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArticleClient implements IArticleClient {

  @Autowired
  private ArticleService articleService;

    /**
     * 保存文章到app端的方法
     * @param dto
     * @return
     */
    @Override
    @PostMapping("/api/v1/article/save")
    public ResponseResult saveArticle(ArticleDto dto) {
        return articleService.saveArticle(dto);
    }
}
package com.heima.apis.article;


import com.heima.model.article.dto.ArticleDto;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

//feign远程调用
//参数二是表示的是 服务熔断降级后指向的一个兜底回响
@FeignClient(value = "leadnews-article")
public interface IArticleClient {


    @PostMapping("/api/v1/article/save")
    public ResponseResult saveArticle(@RequestBody ArticleDto dto);

}

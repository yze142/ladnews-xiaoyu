package com.heima.article.controller;


import com.heima.article.servce.ArticleService;
import com.heima.common.contanst.ArticleConstants;
import com.heima.model.article.dto.ArticleHomeDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.user.dtos.UserFollowDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/article")
public class ArticleHomeController {

    @Autowired
    private ArticleService articleService;

    /**
     * 查询首页
     * @param dto
     * @return
     */
    @PostMapping("/load")
    public ResponseResult load(@RequestBody ArticleHomeDto dto) {

        return articleService.loda(dto,ArticleConstants.LOADTYPE_LOAD_MORE);
    }

    /**
     * 查询历史
     * @param dto
     * @return
     */
    @PostMapping("/loadmore")
    public ResponseResult loadMore(@RequestBody ArticleHomeDto dto) {
        return articleService.loda(dto,ArticleConstants.LOADTYPE_LOAD_MORE);
    }

    /**
     * 这不就是刷新么
     * @param dto
     * @return
     */
    @PostMapping("/loadnew")
    public ResponseResult loadNew(@RequestBody ArticleHomeDto dto) {
        return articleService.loda(dto,ArticleConstants.LOADTYPE_LOAD_NEW);
    }

    /**
     * 回显文章
     * load_article_behavior
     */
    @PostMapping("/load_article_behavior")
    public ResponseResult loadArticle(@RequestBody UserFollowDto userFollowDto){
        return articleService.loadArticle(userFollowDto);
    }



}
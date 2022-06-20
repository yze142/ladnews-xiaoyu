package com.heima.article.servce;

import com.heima.model.article.dto.ArticleDto;
import com.heima.model.article.dto.ArticleHomeDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.user.dtos.UserFollowDto;

public interface ArticleService {

    /**
     * 加载更多，加载更新的文章
     * @param dto
     * @param loadtypeLoadMore
     * @return
     */
    ResponseResult loda(ArticleHomeDto dto, Short loadtypeLoadMore);

    /**
     * 在文章审核成功以后需要在app的article库中新增文章数据
     */
    public ResponseResult saveArticle(ArticleDto dto);

    /**
     * 回显此文章
     * @param userFollowDto
     * @return
     */
    ResponseResult loadArticle(UserFollowDto userFollowDto);
}

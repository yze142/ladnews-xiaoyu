package com.heima.search.service;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.search.dto.UserSearchDto;

public interface ArticleSearchService {

    /**
     * 全文检索文章
     * @param dto
     * @return
     */
    ResponseResult search(UserSearchDto dto);

}

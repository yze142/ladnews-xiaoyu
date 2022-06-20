package com.heima.wemedia.service;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmNewsDto;
import com.heima.model.wemedia.dtos.WmNewsPageReqDto;

//自媒体文章列表
public interface WmNewsService {
    /**
     * 按条件查询自媒体文章
     * @param dto
     * @return
     */
    ResponseResult findAllNews(WmNewsPageReqDto dto);

    /**
     *  发布文章或保存草稿
     * @param dto
     * @return
     */
    public ResponseResult submitNews(WmNewsDto dto);

    /**
     * 查看文章详情
     * @param id 文章id
     * @return 返回文章信息或者抛出异常
     */
    ResponseResult findOneNews(Integer id);

    /**
     * 删除文章
     * @param id 文章id
     * @return 返回删除成功或者抛出异常
     */
    ResponseResult delNews(Integer id);

    /**
     * 文章上架或者下架
     * @param wmNewsDto  enable 0上架 1下架
     * @return
     */
    ResponseResult downOrUp(WmNewsDto wmNewsDto);
}

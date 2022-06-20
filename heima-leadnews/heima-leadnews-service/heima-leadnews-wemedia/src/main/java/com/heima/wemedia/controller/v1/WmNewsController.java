package com.heima.wemedia.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmNewsDto;
import com.heima.model.wemedia.dtos.WmNewsPageReqDto;
import com.heima.wemedia.service.WmNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/news")
public class WmNewsController {

    @Autowired
    private WmNewsService wmNewsService;

    /**
     *  按条件查询自媒体文章
     * @param dto
     * @return
     */
    @PostMapping("/list")
    public ResponseResult findAllNews(@RequestBody WmNewsPageReqDto dto) {
        return wmNewsService.findAllNews(dto);
    }

    /**
     * 发布文章
     * @param dto
     * @return
     */
    @PostMapping("/submit")
    public ResponseResult submitNews(@RequestBody WmNewsDto dto){
        return wmNewsService.submitNews(dto);
    }

    /** 查询文章详情
     * wemedia/api/v1/news/one/{id}
     *
     * id文章id
     */
    @GetMapping("/one/{id}")
    public ResponseResult findOneNews(@PathVariable("id") Integer id){
      return   wmNewsService.findOneNews(id);

    }
    /** 删除文章
     * id 文章id
     * /api/v1/news/del_news/{id}
     */
    @GetMapping("/del_news/{id}")
    public ResponseResult delNews(@PathVariable("id") Integer id){
        return   wmNewsService.delNews(id);

    }

    /**
     * 文章上架下架
     * /api/v1/news/down_or_up
     */
    @PostMapping("/down_or_up")
    public ResponseResult downOrUp(@RequestBody WmNewsDto wmNewsDto){

        return wmNewsService.downOrUp(wmNewsDto);
    }




}
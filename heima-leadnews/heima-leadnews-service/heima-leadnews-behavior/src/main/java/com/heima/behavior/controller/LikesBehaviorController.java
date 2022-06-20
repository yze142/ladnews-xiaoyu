package com.heima.behavior.controller;

import com.heima.behavior.service.LikesBehaviorService;
import com.heima.model.Behavior.dto.BehaviorDto;
import com.heima.model.Behavior.dto.LikesDto;
import com.heima.model.Behavior.dto.ReadBehaviorDto;
import com.heima.model.common.annotation.IdEncrypt;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: heima-leadnews
 * @description: 用户点赞行为实现类
 * @author: hello.xaioyu
 * @create: 2022-06-18 22:53
 **/
@RestController
@RequestMapping("/api/v1")
public class LikesBehaviorController {

    @Autowired
    LikesBehaviorService likesBehaviorService;

    /**
     * 用户点赞
     * likes_behavior`
     */
    @PostMapping("/likes_behavior")
    public ResponseResult likesBehavior(@RequestBody BehaviorDto behaviorDto) {

     return likesBehaviorService.likesBehavior(behaviorDto);
    }

    /**
     * 用户阅读 记录用户阅读的次数
     * `/api/v1/read_behavior`
     */
    @PostMapping("/read_behavior")
    public ResponseResult readBehavior(@RequestBody ReadBehaviorDto readBehaviorDto){

       return likesBehaviorService.readBehavior(readBehaviorDto);

    }

    /**
     * 喜欢与不喜欢
     * `/api/v1/un_likes_behavior`
     */
    @PostMapping("/un_likes_behavior")
    public  ResponseResult LikeAndNotLikes(@RequestBody  LikesDto likesDto ){

        Long articleId = likesDto.getArticleId();
        Short type = likesDto.getType();
        return likesBehaviorService.LikeAndNotLikes(articleId,type);
    }





}

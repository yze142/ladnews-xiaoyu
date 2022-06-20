package com.heima.user.controller.v1;

import com.heima.model.Behavior.dto.LikesDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.user.dtos.UserFollowDto;
import com.heima.user.service.UserBehaviorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: heima-leadnews
 * @description: 用户关注
 * @author: hello.xaioyu
 * @create: 2022-06-19 15:29
 **/
@RestController
@RequestMapping("/api/v1/user")
public class UserFollowController {

    @Autowired
    private UserBehaviorService userBehaviorService;

    /**
     * 用户关注与取消关注
     * user_follow/
     * articleId; // 文章id
     * 	authorId;// 作者id
     * 	operation; 0 // 0代表关注1代表取消
     */
    @PostMapping("/user_follow")
     public ResponseResult  userFollow(@RequestBody UserFollowDto userFollowDto){


       return userBehaviorService.userFollow(userFollowDto);

    }



}

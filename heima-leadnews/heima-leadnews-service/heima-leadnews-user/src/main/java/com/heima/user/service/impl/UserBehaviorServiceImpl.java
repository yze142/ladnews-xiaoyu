package com.heima.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.heima.common.utils.thrad.AppThreadLocalUtil;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.user.UserFollowVo;
import com.heima.model.user.dtos.UserFollowDto;
import com.heima.user.redis.CacheService;
import com.heima.user.service.UserBehaviorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: heima-leadnews
 * @description:
 * @author: hello.xaioyu 用户关注
 * @create: 2022-06-19 15:40
 **/
@Service
public class UserBehaviorServiceImpl implements UserBehaviorService {

    @Autowired
    private CacheService cacheService;

    /**
     * 用户关注与取消关注
     *
     * @param userFollowDto 0代表关注1代表gg
     * @return
     */
    @Override
    public ResponseResult userFollow(UserFollowDto userFollowDto) {
        //把被关注用户和关注用户存入redis
        String follow = "FOLLOW" + userFollowDto.getArticleId();
        //存储关注行为和取消关注行为
        UserFollowVo userFollowVo=new UserFollowVo();
        //获取关注者的id存入vo对象然后转换成json字符串
        Integer UserId = AppThreadLocalUtil.getWmUser();

        userFollowVo.setUserid(UserId);
        userFollowVo.setType(userFollowDto.getOperation());

        String jsonUserFollow = JSON.toJSONString(userFollowVo);
        cacheService.hPut(follow,userFollowDto.getAuthorId().toString(),jsonUserFollow);
        return ResponseResult.okResult(200,"好点，下次不许点了");
    }
}

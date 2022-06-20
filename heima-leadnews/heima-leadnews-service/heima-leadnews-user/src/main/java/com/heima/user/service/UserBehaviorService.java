package com.heima.user.service;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.user.dtos.UserFollowDto;

public interface UserBehaviorService {

    /**
     * 用户关注与取消关注
     * @param userFollowDto
     * @return
     */
    ResponseResult userFollow(UserFollowDto userFollowDto);
}

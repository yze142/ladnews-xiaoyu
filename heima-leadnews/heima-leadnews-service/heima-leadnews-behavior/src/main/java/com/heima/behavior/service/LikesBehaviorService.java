package com.heima.behavior.service;

import com.heima.model.Behavior.dto.BehaviorDto;
import com.heima.model.Behavior.dto.ReadBehaviorDto;
import com.heima.model.common.dtos.ResponseResult;

public interface LikesBehaviorService {

    /**
     * 点赞动态
     * @param behaviorDto
     * @return
     */
    ResponseResult likesBehavior(BehaviorDto behaviorDto);


    /**
     * 阅读行为，记录阅读数量
     * @param readBehaviorDto
     * @return
     */
    ResponseResult readBehavior(ReadBehaviorDto readBehaviorDto);

    /**
     * 喜欢与不喜欢
     * @param articleId
     * @param type
     * @return
     */
    ResponseResult LikeAndNotLikes(Long articleId, Short type);
}

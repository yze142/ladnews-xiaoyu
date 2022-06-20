package com.heima.behavior.service.Impl;

import com.alibaba.fastjson.JSON;
import com.heima.behavior.redis.CacheService;
import com.heima.behavior.service.LikesBehaviorService;
import com.heima.common.utils.thrad.AppThreadLocalUtil;
import com.heima.model.Behavior.dto.BehaviorDto;
import com.heima.model.Behavior.dto.ReadBehaviorDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.mess.UpdateArticleMess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * @program: heima-leadnews
 * @description: 用户行为之点赞
 * @author: hello.xaioyu
 * @create: 2022-06-18 23:23
 **/
@Service
public class LikesBehaviorSeriveImpl implements LikesBehaviorService {

    private String FANS="LIKES_FANS"; //粉丝

      private String LIKES_BLOGGER= "LIKES_BLOGGER";//博主

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private KafkaTemplate kafkaTemplate;


    /**
     * 点赞动态
     *
     * @param behaviorDto
     * @return
     */
    @Override
    public ResponseResult likesBehavior(BehaviorDto behaviorDto) {
        //记录要发送的类型
        UpdateArticleMess updateArticleMess=new UpdateArticleMess();
         updateArticleMess.setType(UpdateArticleMess.UpdateArticleType.LIKES);
         updateArticleMess.setArticleId(behaviorDto.getArticleId());
   //如果是点赞行为就加一如果是取消点赞行为就减一
           if (behaviorDto.getOperation()==0){
               updateArticleMess.setAdd(1);
           }else {updateArticleMess.setAdd(-1);}

        Integer wmUserId = AppThreadLocalUtil.getWmUser();

        String blogger =LIKES_BLOGGER+wmUserId+behaviorDto.getArticleId();//博主文章id

      cacheService.set(blogger,String.valueOf(behaviorDto.getOperation()));
         //发送数据
        kafkaTemplate.send("HOT_ARTICLW_SCORAE_TOPIC", JSON.toJSONString(updateArticleMess));

        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }


    /**
     *  记录阅读次数
     * @param readBehaviorDto
     * @return
     */
    @Override
    public ResponseResult readBehavior(ReadBehaviorDto readBehaviorDto) {
        //记录要发送的类型
        UpdateArticleMess updateArticleMess=new UpdateArticleMess();
        updateArticleMess.setType(UpdateArticleMess.UpdateArticleType.VIEWS);
        updateArticleMess.setArticleId(readBehaviorDto.getArticleId());
        updateArticleMess.setAdd(1);

        Integer wmUserId = AppThreadLocalUtil.getWmUser();

        String blogger ="READ"+wmUserId+readBehaviorDto.getArticleId();//博主文章id

        //判断键是否存在
        if (!cacheService.exists(blogger)){
           //不存在创建键和值
            cacheService.hPut(blogger,String.valueOf(wmUserId),String.valueOf(readBehaviorDto.getCount()));
        }else {
            //存在获取键数据并且加一
            String o = (String) cacheService.hGet(blogger, wmUserId.toString());
            Integer RadCount = Integer.valueOf(o);
            int count = RadCount+readBehaviorDto.getCount();
            cacheService.hPut(blogger,String.valueOf(wmUserId),String.valueOf(count));
        }


           //发送消息
        kafkaTemplate.send("HOT_ARTICLW_SCORAE_TOPIC",JSON.toJSONString(updateArticleMess));

        return ResponseResult.okResult(200,"阅读成功金币+1");
    }

    /**
     * 喜欢与不喜欢
     * @param articleId
     * @param type
     * @return
     */
    @Override
    public ResponseResult LikeAndNotLikes(Long articleId, Short type) {
          //把数据存入redis
        Integer appId = AppThreadLocalUtil.getWmUser();
        String key = "LIKES_ARTICLE" + articleId + appId;
        cacheService.hPut(key,appId.toString(),type.toString());

        return ResponseResult.okResult(200,"求求给个喜欢");
    }
}

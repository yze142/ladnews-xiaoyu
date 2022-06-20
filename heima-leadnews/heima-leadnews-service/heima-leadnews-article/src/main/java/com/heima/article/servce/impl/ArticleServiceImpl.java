package com.heima.article.servce.impl;

import com.alibaba.fastjson.JSON;
import com.heima.article.config.CacheService;
import com.heima.article.mapper.ApArticleConfigMapper;
import com.heima.article.mapper.ApArticleMapper;
import com.heima.article.mapper.ArticleMapper;
import com.heima.article.servce.ArticleFreemarkerService;
import com.heima.article.servce.ArticleService;
import com.heima.common.utils.thrad.AppThreadLocalUtil;
import com.heima.model.Behavior.vo.LoadArticleVo;
import com.heima.model.article.dto.ArticleDto;
import com.heima.model.article.dto.ArticleHomeDto;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.article.pojos.ApArticleConfig;
import com.heima.model.article.pojos.ApArticleContent;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.user.UserFollowVo;
import com.heima.model.user.dtos.UserFollowDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @program: heima-leadnews
 * @description: 查询文章列表实现
 * @author: hello.xaioyu
 * @create: 2022-06-04 15:24
 **/
@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ApArticleConfigMapper apArticleConfigMapper;

    @Autowired
    private ApArticleMapper apArticleMapper;

    @Autowired
    private ArticleFreemarkerService articleFreemarkerService;

    /**
     * 查看文章列表方法
     *
     * @param dto
     * @param loadtypeLoadMore
     * @return
     */
    @Override
    public ResponseResult loda(ArticleHomeDto dto, Short loadtypeLoadMore) {
        if (dto.getMaxBehotTime() == null) {
            dto.setMaxBehotTime(new Date());
        }
        if (dto.getMinBehotTime() == null) {
            dto.setMinBehotTime(new Date());
        }
        if (dto.getSize() == null) {
            dto.setTag(Integer.toString(50));
        }
        if (loadtypeLoadMore == null) {
            loadtypeLoadMore = 1;
        }

        List<ApArticle> apArticles = articleMapper.loadArticleList(dto, loadtypeLoadMore);

        return ResponseResult.okResult(apArticles);
    }

    /**
     * 自媒体端审核通过了之后就可以把文章保存到article
     *
     * @param dto
     * @return
     */
    @Override
    public ResponseResult saveArticle(ArticleDto dto) {
        if (dto == null) {
            //抛异常
            return ResponseResult.errorResult(501, "参数失效");

        }
        //没id就是保存操作
        ApArticle apArticle = new ApArticle();
        BeanUtils.copyProperties(dto, apArticle);

        if (dto.getId() == null) {
            //保存文章基础属性
            articleMapper.insert(apArticle);
            //保存文章内容
            ApArticleContent apArticleContent = new ApArticleContent();
            apArticleContent.setArticleId(apArticle.getId());
            apArticleContent.setContent(dto.getContent());
            apArticleMapper.insert(apArticleContent);

            //保存文章配置表
            ApArticleConfig apArticleConfig = new ApArticleConfig(apArticle.getId());


            apArticleConfigMapper.insert(apArticleConfig);

            // int a=1/0;

        } else {

            //修改操作
            //修改文章表
            articleMapper.updateById(apArticle);
            //修改文章内容表
            Long id = dto.getId();
            ApArticleContent apArticleContent = new ApArticleContent();
            apArticleContent.setContent(dto.getContent());
            apArticleContent.setArticleId(id);
            apArticleMapper.updateById(apArticleContent);


        }
        //生成文章的静态模板
        articleFreemarkerService.buildArticleTominIo(apArticle, dto.getContent());

        //3.结果返回  文章的id
        return ResponseResult.okResult(apArticle.getId());
    }

    @Autowired
    private CacheService cacheService;

    @Autowired
    private KafkaTemplate kafkaTemplate;
    /**
     * 回显文章数据
     *
     * @param userFollowDto islike;  //点赞
     *                      isunlike;  //喜欢与不喜欢
     *                      iscollection //文章是否收藏
     *                      isfollow //是否关注作者
     * @return
     */
    @Override
    public ResponseResult loadArticle(UserFollowDto userFollowDto) {
        Integer UserId = AppThreadLocalUtil.getWmUser();
        LoadArticleVo loadArticleVo = new LoadArticleVo();


        //查询是否点赞
        String LikeKey ="LIKES_BLOGGER"+UserId+userFollowDto.getArticleId();
        String likes1 = cacheService.get(LikeKey);
        Integer likes1Type = Integer.valueOf(likes1);
        loadArticleVo.setIslike(likes1Type==0?true:false);
        //查询是否关注
        //1.1用户关注key
        String follow = "FOLLOW" + userFollowDto.getArticleId();
        String value = (String) cacheService.hGet(follow, userFollowDto.getAuthorId().toString());
        UserFollowVo userFollowVo = JSON.parseObject(value, UserFollowVo.class);

        Integer userid = null;
        Short type = null;
        if (userFollowVo != null) {

            userid = userFollowVo.getUserid();
            type = userFollowVo.getType();


        }
           loadArticleVo.setIsfollow(type==0?true:false);

        //查询是否不喜欢
        String key = "LIKES_ARTICLE" + userFollowDto.getArticleId() + UserId;

        String unLikes = (String) cacheService.hGet(key, UserId.toString());
        Integer likesTrpy = Integer.valueOf(unLikes);
         loadArticleVo.setIslike(likesTrpy==0?true:false);

        return ResponseResult.okResult(loadArticleVo);
    }
}

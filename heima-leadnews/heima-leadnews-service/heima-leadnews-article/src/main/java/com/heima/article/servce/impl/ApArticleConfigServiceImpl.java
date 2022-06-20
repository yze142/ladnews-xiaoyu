package com.heima.article.servce.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.heima.article.mapper.ApArticleConfigMapper;
import com.heima.article.mapper.ArticleMapper;
import com.heima.article.servce.ApArticleConfigService;
import com.heima.model.article.pojos.ApArticleConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.nntp.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @program: heima-leadnews
 * @description: 发送延迟消息
 * @author: hello.xaioyu
 * @create: 2022-06-14 13:39
 **/
@Service
@Slf4j
public class ApArticleConfigServiceImpl implements ApArticleConfigService {

    @Autowired
    private ApArticleConfigMapper apArticleConfigMapper;

    /**
     * 修改文章上下线
     *
     * @param map
     */
    @Override
    public void updateByMap(Map map) {
        Long id = (Long) map.get("id");
        //0 下架 1 上架
        Boolean enable = (Boolean) map.get("enable");
        boolean isDown = true;
        if(enable.equals(1)){
            isDown = false;
        }

//        UpdateWrapper<ApArticleConfig> dw=new UpdateWrapper();
//        dw.lambda().eq(ApArticleConfig::getArticleId,id).set(ApArticleConfig::getIsDown,enable);
        ApArticleConfig config=new ApArticleConfig();
         config.setIsDown(enable);
        apArticleConfigMapper.updateById(config);


    }
}

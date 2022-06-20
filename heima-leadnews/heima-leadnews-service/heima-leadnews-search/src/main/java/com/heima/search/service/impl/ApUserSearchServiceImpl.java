package com.heima.search.service.impl;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.search.pojos.ApUserSearch;
import com.heima.search.service.ApUserSearchService;
import org.elasticsearch.geometry.Circle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Queue;

/**
 * @program: heima-leadnews
 * @description: 用户搜索
 * @author: hello.xaioyu
 * @create: 2022-06-15 17:43
 **/
@Service
public class ApUserSearchServiceImpl implements ApUserSearchService {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 保存用户搜索记录
     * @param keyword 搜索关键词
     * @param userId  用户Id
     */
    @Override
    @Async
    public void insert(String keyword, Integer userId) {
        //1.判断是否存在
        Query query = Query.query(Criteria.where("userId").is(userId).and("keyword").is(keyword));
        ApUserSearch apUserSearch = mongoTemplate.findOne(query, ApUserSearch.class);
        //存在就更新搜索时间捏
         if (apUserSearch!=null){
             apUserSearch.setCreatedTime(new Date());
             mongoTemplate.save(apUserSearch);
             return;
         }
        ApUserSearch userSearch=new ApUserSearch();
         userSearch.setCreatedTime(new Date());
         userSearch.setUserId(userId);
         userSearch.setKeyword(keyword);


        //2.判断搜索记录的条数是否大于10条 如果大于10条就删除最老的那条
        Query query1 = Query.query(Criteria.where("userId").is(userId));
         query1.with(Sort.by(Sort.Direction.DESC,"createdTime"));//根据时间降序排序

        List<ApUserSearch> apUserSearches = mongoTemplate.find(query1, ApUserSearch.class);
        if (apUserSearches.size()<10){
         //不满10条那就保存
            mongoTemplate.save(userSearch);

        }else {
            //删除最老的那一条
            ApUserSearch userSearch1 = apUserSearches.get(apUserSearches.size()-1);
            Query query2 = Query.query(Criteria.where("userId").is(userSearch1.getUserId()).and("keyword").is(userSearch1.getKeyword()));
            mongoTemplate.findAndReplace(query2,apUserSearch);

        }

    }

    /**
     * 查看历史搜索记录
     * @return
     */
    @Override
    public ResponseResult findUserSearch() {
        Query userId = Query.query(Criteria.where("userId").is(4));
       userId.with(Sort.by(Sort.Direction.DESC,"createdTime"));
        List<ApUserSearch> apUserSearches = mongoTemplate.find(userId, ApUserSearch.class);

        return ResponseResult.okResult(apUserSearches);
    }
}

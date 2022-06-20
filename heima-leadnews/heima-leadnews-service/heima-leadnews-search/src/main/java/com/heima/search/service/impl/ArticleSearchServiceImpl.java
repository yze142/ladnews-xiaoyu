package com.heima.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.heima.common.utils.thrad.AppThreadLocalUtil;
import com.heima.common.utils.thrad.WmThreadLocalUtil;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.search.dto.UserSearchDto;
import com.heima.search.service.ApUserSearchService;
import com.heima.search.service.ArticleSearchService;
import com.heima.utils.common.AppJwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @program: heima-leadnews
 * @description: 分词查询
 * @author: hello.xaioyu
 * @create: 2022-06-14 20:29
 **/
@Service
@Slf4j
public class ArticleSearchServiceImpl implements ArticleSearchService {


    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private ApUserSearchService apUserSearchService;

    /**
     * 全文检索查询文章
     *
     * @param dto
     * @return
     */
    @Override
    public ResponseResult search(UserSearchDto dto) {
        //1.检查参数
        if (dto == null || dto.getSearchWords() == null) {

            return ResponseResult.errorResult(AppHttpCodeEnum
                    .PARAM_INVALID);
        }

        //异步调用保存搜索记录


            apUserSearchService.insert(dto.getSearchWords(), 4);



        //2.设置查询条件
        SearchRequest request = new SearchRequest("app_info_article");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //3、布尔查询
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        //关键词的分词之后再查询
        QueryStringQueryBuilder queryStringQueryBuilder = QueryBuilders.
                queryStringQuery(dto.getSearchWords()).field("title")
                .field("content").defaultOperator(Operator.OR);
        boolQuery.must(queryStringQueryBuilder);

        //查询小于mindate的数据
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("publishTime").lt(dto.getMinBehotTime().getTime());
        boolQuery.filter(rangeQueryBuilder);
        //分页查询
        request.source().from(0).size(dto.getPageSize());


        //按照发布时间
        request.source().sort("publishTime", SortOrder.DESC); //根据时间倒序查询
        //设置高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("title");
        highlightBuilder.preTags("<font style='color: red; font-size: inherit;'>");
        highlightBuilder.postTags("</font>");
        request.source().highlighter(highlightBuilder);

        SearchResponse search = null;
        request.source().query(boolQuery);
        try {
            search = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("根据分词查询全文失败");
        }

        //3.结果封装返回

        List<Map> list = new ArrayList<>();

        SearchHit[] hits = search.getHits().getHits();
        for (SearchHit hit : hits) {
            String json = hit.getSourceAsString();
            Map map = JSON.parseObject(json, Map.class);
            //处理高亮
            if(hit.getHighlightFields() != null && hit.getHighlightFields().size() > 0){
                Text[] titles = hit.getHighlightFields().get("title").getFragments();
                String title = StringUtils.join(titles);
                //高亮标题
                map.put("h_title",title);
            }else {
                //原始标题
                map.put("h_title",map.get("title"));
            }
            list.add(map);
        }

        return ResponseResult.okResult(list);
    }
}

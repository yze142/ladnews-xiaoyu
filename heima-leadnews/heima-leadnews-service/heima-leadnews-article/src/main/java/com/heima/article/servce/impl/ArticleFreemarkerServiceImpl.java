package com.heima.article.servce.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.heima.article.mapper.ArticleMapper;
import com.heima.article.servce.ArticleFreemarkerService;
import com.heima.article.servce.ArticleService;
import com.heima.file.service.impl.MinIOFileStorageService;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.schedule.vos.SearchArticleVo;
import com.heima.model.search.dto.UserSearchDto;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: heima-leadnews
 * @description: 生成文章静态资源
 * @author: hello.xaioyu
 * @create: 2022-06-09 22:47
 **/
@Service
@Slf4j
@Transactional
public class ArticleFreemarkerServiceImpl implements ArticleFreemarkerService {

    /**
     * 生成文章静态
     *
     * @param apArticle 文章各种属性
     * @param content 文章内容
     */
    @Autowired
    private MinIOFileStorageService minIOFileStorageService;
    @Autowired
    private Configuration configuration;

    @Autowired
    private ArticleMapper articleMapper2;


    @Override
    @Async
    public void buildArticleTominIo(ApArticle apArticle, String content) {


        try {
            //先标注静态模板
            Template template = configuration.getTemplate("article.ftl");
            StringWriter out = new StringWriter();
            //生成数据模板
            Map<String, Object> contentMap = new HashMap<>();
            contentMap.put("content", JSONArray.parseArray(content));
            template.process(contentMap, out);

            InputStream is = new ByteArrayInputStream(out.toString().getBytes());
            //拼接文件名字上传
            String s = minIOFileStorageService.uploadHtmlFile("",
                    apArticle.getId().toString() + ".html", is);

            apArticle.setStaticUrl(s);

            articleMapper2.updateById(apArticle);

            //发送消息，创建索引
            createArticleESIndex(apArticle, content, s);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Autowired
    private KafkaTemplate kafkaTemplate;

    /**
     * 送消息，创建索引
     *
     * @param apArticle
     * @param content
     * @param path
     */
    private void createArticleESIndex(ApArticle apArticle, String content, String path) {
          //把文章数据封Dto对象中发送出去
        SearchArticleVo searchArticleVo=new SearchArticleVo();
        BeanUtils.copyProperties(apArticle,searchArticleVo);
        searchArticleVo.setContent(content);
        kafkaTemplate.send("article.es.sync.topic",JSON.toJSONString(searchArticleVo));
    }
}

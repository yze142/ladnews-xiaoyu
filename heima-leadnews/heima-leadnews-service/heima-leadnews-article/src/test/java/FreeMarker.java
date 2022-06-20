import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.heima.article.ArticleApplication;

import com.heima.article.mapper.ApArticleMapper;
import com.heima.article.mapper.ArticleMapper;
import com.heima.file.config.MinIOConfig;
import com.heima.file.config.MinIOConfigProperties;
import com.heima.file.service.FileStorageService;
import com.heima.file.service.impl.MinIOFileStorageService;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.article.pojos.ApArticleContent;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;


/**
 * @program: heima-leadnews
 * @description:
 * @author: hello.xaioyu
 * @create: 2022-06-05 14:25
 **/
@SpringBootTest(classes = ArticleApplication.class)
@RunWith(SpringRunner.class)
public class FreeMarker {

    @Autowired
    private ApArticleMapper articleMapper;

    @Autowired
    private ArticleMapper articleMapper2;


    @Autowired
    private Configuration configuration;



    @Autowired
    private MinIOFileStorageService minIOFileStorageService;

    @Test
    public void  createSraruc() throws Exception {
        QueryWrapper<ApArticleContent> qw = new QueryWrapper<>();
        qw.lambda().eq(ApArticleContent::getArticleId,"1302977558807060482");
        ApArticleContent apArticle = articleMapper.selectOne(qw);

        System.out.println(apArticle);
    //文章to陪你过freemarker生成htnml文件
        Template template = configuration.getTemplate("article.ftl");
        StringWriter out = new StringWriter();
        //添加数据模型
        Map<String, Object> content=new HashMap<>();

        content.put("content",JSONArray.parseArray(apArticle.getContent()));
        template.process(content, out);
        InputStream is = new ByteArrayInputStream(out.toString().getBytes());

        //3.把html文件上传到minio中
        String path = minIOFileStorageService.uploadHtmlFile("", apArticle.getArticleId() + ".html", is);

        //4.修改ap_article表，保存static_url字段
        ApArticle article = new ApArticle();
        article.setId(apArticle.getArticleId());
        article.setStaticUrl(path);
        articleMapper2.updateById(article);
    }

}

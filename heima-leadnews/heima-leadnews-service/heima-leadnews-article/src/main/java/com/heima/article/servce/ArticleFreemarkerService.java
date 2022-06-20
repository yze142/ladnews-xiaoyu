package com.heima.article.servce;

import com.heima.model.article.pojos.ApArticle;

//保存文章到静态模板
public interface ArticleFreemarkerService {

    /**
     * 把文章转换成静态资源
     */
    public void buildArticleTominIo(ApArticle apArticle,String content);

}

package com.heima.model.Behavior.vo;

import lombok.Data;

/**
 * @program: heima-leadnews
 * @description:
 * @author: hello.xaioyu
 * @create: 2022-06-19 18:08
 **/
@Data
public class LoadArticleVo {


    Boolean islike;//点赞
    Boolean  isunlike;//喜欢
    Boolean   iscollection;//是否收藏
    Boolean    isfollow;//是否关注

}

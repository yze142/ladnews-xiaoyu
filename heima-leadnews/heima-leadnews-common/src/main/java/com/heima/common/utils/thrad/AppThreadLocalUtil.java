package com.heima.common.utils.thrad;

import com.heima.model.article.pojos.ApArticle;
import com.heima.model.user.pojos.ApUser;
import com.heima.model.wemedia.pojos.WmUser;

/**
 * @program: heima-leadnews
 * @description:
 * @author: hello.xaioyu
 * @create: 2022-06-15 19:38
 **/

public class AppThreadLocalUtil {

    public static ThreadLocal<ApUser> WM_USER_THREAD=new ThreadLocal<>();


    //存入线程中
    public static void setUser(ApUser user){
        WM_USER_THREAD.set(user);

    }
    //获取用户id
    public  static Integer getWmUser(){
        ApUser apUser = WM_USER_THREAD.get();
        return  4;

    }
    //清理线程
    public static void rmWmUser(){
        WM_USER_THREAD.remove();
    }

}

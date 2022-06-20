package com.heima.common.utils.thrad;

import com.heima.model.wemedia.pojos.WmUser;

/**
 * @program: heima-leadnews
 * @description:
 * @author: hello.xaioyu
 * @create: 2022-06-05 22:22
 **/

public class WmThreadLocalUtil {

    public static ThreadLocal<WmUser> WM_USER_THREAD_LOCAL=new ThreadLocal<>();


//存入线程中
    public static void setWmUser(WmUser user){
     WM_USER_THREAD_LOCAL.set(user);

    }
    //获取用户id
    public  static Integer getWmUser(){
        WmUser wmUser = WM_USER_THREAD_LOCAL.get();
      return  wmUser.getId();

    }
    //清理线程
    public static void rmWmUser(){
        WM_USER_THREAD_LOCAL.remove();
    }



}

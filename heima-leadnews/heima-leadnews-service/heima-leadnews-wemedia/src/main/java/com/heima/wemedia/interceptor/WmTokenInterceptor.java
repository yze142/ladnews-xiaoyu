package com.heima.wemedia.interceptor;

import com.heima.common.utils.thrad.WmThreadLocalUtil;
import com.heima.model.wemedia.pojos.WmUser;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Properties;

/**
 * @program: heima-leadnews
 * @description: 拦截器
 * @author: hello.xaioyu
 * @create: 2022-06-05 22:16
 **/

public class WmTokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
       //获取请求头中的参数
        String userId = request.getHeader("userId");
        if (userId!=null){
            //存入线程
            WmUser wmUser = new WmUser();
            wmUser.setId(Integer.valueOf(userId));
            WmThreadLocalUtil.setWmUser(wmUser);

        }

        return true;
    }

    /**
     * 拦截之后走的方法，就是清理线程
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
      WmThreadLocalUtil.rmWmUser();
    }
}

package com.lrm.blog.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录过滤器
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {

    /**
     * preHandle：在方法被调用前执行。在该方法中可以做类似校验的功能。如果返回true，
     * 则继续调用下一个拦截器。如果返回false，
     * 则中断执行，也就是说我们想调用的方法 不会被执行，
     * 但是你可以修改response为你想要的响应。
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        if (request.getSession().getAttribute("user")==null){
            response.sendRedirect("/admin");
            return false;
        }else {
            return true;
        }

    }
}

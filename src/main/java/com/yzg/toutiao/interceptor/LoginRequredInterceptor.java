package com.yzg.toutiao.interceptor;

import com.yzg.toutiao.aspect.LogAspect;
import com.yzg.toutiao.dao.LoginTicketMapper;
import com.yzg.toutiao.dao.UserMapper;
import com.yzg.toutiao.model.HostHolder;
import com.yzg.toutiao.model.LoginTicket;
import com.yzg.toutiao.model.User;
import com.yzg.toutiao.model.example.LoginTicketExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * @author yzg
 * @create 2019/7/6
 */

@Component
public class LoginRequredInterceptor implements HandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogAspect.class);


    @Autowired
    HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        LOGGER.warn("当前登录用户:" + hostHolder.getUser());

        if (hostHolder.getUser() == null) {
            LOGGER.warn("未登录跳转++++++++++++++++++++++++++++++++++");
            response.sendRedirect("/login?next=" + request.getRequestURL());
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}

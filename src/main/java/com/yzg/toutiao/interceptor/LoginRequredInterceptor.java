package com.yzg.toutiao.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.yzg.toutiao.annotation.LoginRequired;
import com.yzg.toutiao.aspect.LogAspect;
import com.yzg.toutiao.dao.LoginTicketMapper;
import com.yzg.toutiao.dao.UserMapper;
import com.yzg.toutiao.model.HostHolder;
import com.yzg.toutiao.model.LoginTicket;
import com.yzg.toutiao.model.Result;
import com.yzg.toutiao.model.User;
import com.yzg.toutiao.model.example.LoginTicketExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
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
        // 如果不是映射到方法直接通过
        if (!(o instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) o;
        Method method = handlerMethod.getMethod();
        // 判断接口是否需要登录
        LoginRequired methodAnnotation = method.getAnnotation(LoginRequired.class);
        // 有 @LoginRequired 注解，需要认证
        if (methodAnnotation != null){
            if (hostHolder.getUser() == null) {
                //未登录
                ObjectMapper mapper= new ObjectMapper();
                String json = mapper.writeValueAsString(new Result().code(403).message("未登录"));
                writeJson(response, json);
                return false;
            }else {
                //已登录
                return true;
            }
        }
        return true;
    }
    private void writeJson(HttpServletResponse resp, String json){
        resp.setContentType("application/json;charset=UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            //设定类容为json的格式
            //写到客户端
            out.write(json);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}

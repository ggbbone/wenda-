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
import javax.servlet.http.HttpSession;
import java.security.interfaces.RSAKey;
import java.util.Date;
import java.util.List;

/**
 * @author yzg
 * @create 2019/7/6
 */

@Component
public class PassportInterceptor implements HandlerInterceptor {

    private static final Logger LOGGER=LoggerFactory.getLogger(LogAspect.class);

    @Autowired
    LoginTicketMapper loginTicketMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object o) throws Exception {
        User user = (User) request.getSession().getAttribute("user");
        //已登录直接从session中获取用户
        if (user != null){
            hostHolder.setUser(user);
            return true;
        }
        //从cookie获取ticket
        String ticket = getValueByCookie("ticket",request);
        if (ticket != null){
            LoginTicketExample loginTicketExample = new LoginTicketExample();
            loginTicketExample.createCriteria().andTicketEqualTo(ticket);
            List<LoginTicket> loginTickets = loginTicketMapper.selectByExample(loginTicketExample);
            if (loginTickets.size()==0){
                //ticket不存在
                deleteValueByCookie("ticket",request,response);
                return true;
            }else if (loginTickets.get(0).getExpired().before(new Date())){
                //登陆过期
                deleteValueByCookie("ticket",request,response);
                return true;
            }else if (loginTickets.get(0).getStatus() != 0){
                //ticket状态无效
                deleteValueByCookie("ticket",request,response);
                return true;
            }else {
                //ticket正确且有效
                user = userMapper.selectByPrimaryKey(loginTickets.get(0).getUserId());
                hostHolder.setUser(user);
                request.getSession().setAttribute("user",user);
                return true;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object o, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null){
            modelAndView.addObject("user",hostHolder.getUser());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        hostHolder.clear();
    }



    private String getValueByCookie(String key, HttpServletRequest request){
        String value = null;
        if (request.getCookies()!=null){
            for (Cookie cookie:request.getCookies()){
                if (key.equals(cookie.getName())){
                    value = cookie.getValue();
                    break;
                }
            }
        }
        return value;
    }

    private void deleteValueByCookie(String key, HttpServletRequest request, HttpServletResponse response){
        if (request.getCookies()!=null){
            for (Cookie cookie:request.getCookies()){
                if (key.equals(cookie.getName())){
                    //删除cookie
                    Cookie cookie1 = new Cookie(cookie.getName(), null);
                    cookie1.setMaxAge(0);
                    response.addCookie(cookie1);
                    break;
                }
            }
        }

    }
}

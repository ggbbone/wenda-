package com.yzg.toutiao.controller;

import com.yzg.toutiao.aspect.LogAspect;
import com.yzg.toutiao.model.Result;
import com.yzg.toutiao.model.User;
import com.yzg.toutiao.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author yzg
 * @create 2019/6/27
 * 登陆注册相关
 */
@Controller
public class LoginController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogAspect.class);

    @Autowired
    UserService userService;

    /**
     * 注册请求
     *
     * @return
     */
    @RequestMapping(value = "register", method = RequestMethod.POST)
    @ResponseBody
    public Result reg(@RequestBody User user) {
        return userService.register(user.getName(), user.getPassword());
    }

    /**
     * 登陆请求
     *
     * @param username   用户名
     * @param password   密码
     * @param rememberMe 是否记住登陆
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public Result login(@RequestParam(value = "username") String username,
                        @RequestParam(value = "password") String password,
                        @RequestParam(value = "remember_me", defaultValue = "false") boolean rememberMe,
                        HttpServletResponse response) {
        User user = userService.getUserByName(username);
        if (user == null) {
            //用户不存在
            return new Result().code(1).message("用户不存在");
        } else {
            Result result = userService.login(user, password, rememberMe);
            if ((int) result.get("status") == 200) {
                //登陆成功
                Cookie cookie = new Cookie("ticket", (String) result.get("data"));
                cookie.setPath("/");
                cookie.setMaxAge(3600*24*14);
                response.addCookie(cookie);
            }
            return result;
        }

    }

    /**
     * 访问登陆页面
     *
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String toRegLogin() {

        return "login";
    }

    /**
     * 访问注册页面
     *
     * @return
     */
    @RequestMapping(value = "register", method = RequestMethod.GET)
    public String toLogin() {
        return "register";
    }

    /**
     * 退出登录
     * @param ticket
     * @return
     */
    @RequestMapping(value = "logout",method = RequestMethod.GET)
    public String logout(@CookieValue("ticket") String ticket, HttpSession session){
        userService.logout(ticket,session);
        return "redirect:/";
    }
}

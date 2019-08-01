package com.yzg.toutiao.controller;

import com.yzg.toutiao.aspect.LogAspect;
import com.yzg.toutiao.async.EventModel;
import com.yzg.toutiao.async.EventProducer;
import com.yzg.toutiao.async.EventType;
import com.yzg.toutiao.model.EntityType;
import com.yzg.toutiao.model.HostHolder;
import com.yzg.toutiao.model.Result;
import com.yzg.toutiao.model.User;
import com.yzg.toutiao.service.JedisAdapter;
import com.yzg.toutiao.service.UserService;
import com.yzg.toutiao.utils.RedisKeyUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    UserService userService;
    @Autowired
    EventProducer eventProducer;
    @Autowired
    JedisAdapter jedisAdapter;


    /**
     * 注册请求
     *
     * @return
     */
    @RequestMapping(value = "register", method = RequestMethod.POST)
    @ResponseBody
    public Result reg(@RequestBody User user) {
        LOGGER.info("注册用户信息：" + user);
        //数据校验
        if (StringUtils.isBlank(user.getName())) {
            return new Result().code(1).message("用户名不正确");
        }
        if (StringUtils.isBlank(user.getPassword())) {
            return new Result().code(2).message("密码不正确");
        }
        if (userService.getUserByName(user.getName()) != null){
            return new Result().code(3).message("用户名已存在");
        }
        if (userService.getUserByEmail(user.getEmail()) != null){
            return new Result().code(4).message("邮箱已被使用");
        }
        String key = RedisKeyUtils.getRegisterCodeByUserId(user.getEmail());
        String code = jedisAdapter.get(key);
        if (code == null || !code.equals(user.getCode())){
            return new Result().code(5).message("验证码不正确");
        }
        userService.register(user);
        return new Result().success();
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
            //根据邮箱查询
            /*user = userService.getUserByEmail(username);
            LOGGER.info("根据邮箱查询：" + user);*/
            //用户不存在
            return new Result().code(1).message("用户不存在");

        }
        Result result = userService.login(user, password, rememberMe);
        if ((int) result.get("status") == 200) {
            //登陆成功
            Cookie cookie = new Cookie("ticket", (String) result.get("data"));
            cookie.setPath("/");
            cookie.setMaxAge(3600 * 24 * 14);
            response.addCookie(cookie);
            //登陆事件
            eventProducer.fireEvent(new EventModel(EventType.LOGIN));
        }
        return result;
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
     *
     * @param ticket
     * @return
     */
    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public String logout(@CookieValue("ticket") String ticket, HttpSession session) {
        userService.logout(ticket, session);
        return "redirect:/";
    }

    /**
     * 获取验证码发送到邮箱
     * @param email
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/code/email",method = RequestMethod.POST)
    public Result getCode(@RequestParam String email){
        if (userService.getUserByEmail(email) != null){
            return new Result().code(4).message("邮箱已被使用");
        }
        //随机生成验证码
        String code="123321";
        //发送事件到队列
        eventProducer.fireEvent(new EventModel(EventType.REGISTER)
                .setExt("code",code).setExt("email",email));

        return new Result().success();
    }


}

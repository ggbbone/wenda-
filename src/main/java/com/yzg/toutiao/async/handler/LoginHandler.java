package com.yzg.toutiao.async.handler;

import com.yzg.toutiao.async.EventHandler;
import com.yzg.toutiao.async.EventModel;
import com.yzg.toutiao.async.EventType;
import com.yzg.toutiao.controller.LoginController;
import com.yzg.toutiao.utils.MailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yzg
 * @create 2019/7/22
 */
@Component
public class LoginHandler implements EventHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginHandler.class);

    @Autowired
    MailSender mailSender;

    @Override
    public void doHandle(EventModel model) {
        LOGGER.info("登陆事件处理");
        /*Map<String, Object> map = new HashMap<>();
        map.put("username",model.getExt("username"));
        mailSender.sendWithHTMLTemplate(model.getExt("email"),
                "登陆ip异常","mails/login_exception.html",map);*/
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Collections.singletonList(EventType.LOGIN);
    }
}

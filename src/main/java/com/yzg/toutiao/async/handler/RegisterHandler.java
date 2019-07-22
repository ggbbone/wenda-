package com.yzg.toutiao.async.handler;

import com.yzg.toutiao.async.EventHandler;
import com.yzg.toutiao.async.EventModel;
import com.yzg.toutiao.async.EventType;
import com.yzg.toutiao.service.JedisAdapter;
import com.yzg.toutiao.utils.MailSender;
import com.yzg.toutiao.utils.RedisKeyUtils;
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
 * <p>
 * 注册事件处理器
 */
@Component
public class RegisterHandler implements EventHandler {
    @Autowired
    MailSender mailSender;
    @Autowired
    JedisAdapter jedisAdapter;

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterHandler.class);

    @Override
    public void doHandle(EventModel model) {
        LOGGER.info("注册事件处理");
        Map<String, Object> map = new HashMap<>();
        map.put("username", model.getExt("username"));
        map.put("link", model.getExt("link"));
        mailSender.sendWithHTMLTemplate(model.getExt("email"),
                "欢迎注册问答网", "mails/register_check.html", map);
        //code存入redis,有效期24小时
        jedisAdapter.set(RedisKeyUtils.getRegisterCodeByUserId(model.getActorId()),
                model.getExt("code"), 3600*24);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Collections.singletonList(EventType.REGISTER);
    }
}

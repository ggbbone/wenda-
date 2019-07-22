package com.yzg.toutiao.async;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yzg.toutiao.aspect.LogAspect;
import com.yzg.toutiao.service.JedisAdapter;
import com.yzg.toutiao.utils.RedisKeyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author yzg
 * @create 2019/7/21
 */
@Service
public class EventProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventProducer.class);

    @Autowired
    JedisAdapter jedisAdapter;

    public boolean fireEvent(EventModel eventModel){
        try {
            String json = JSONObject.toJSONString(eventModel);
            String key = RedisKeyUtils.getEventQueue();
            LOGGER.info("添加事件到redis队列:" + key +" VALUE:" + json);
            jedisAdapter.lpush(key,json);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}

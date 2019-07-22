package com.yzg.toutiao.async;

import com.alibaba.fastjson.JSON;
import com.yzg.toutiao.aspect.LogAspect;
import com.yzg.toutiao.service.JedisAdapter;
import com.yzg.toutiao.utils.RedisKeyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yzg
 * @create 2019/7/21
 * 处理队列中的事件并与各个handler沟通
 * InitializingBean接口的作用在spring 初始化后，执行完所有属性设置方法(即setXxx)将
 * 自动调用 afterPropertiesSet(), 在配置文件中无须特别的配置
 */
@Service
public class EventConsumer implements InitializingBean, ApplicationContextAware {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventConsumer.class);

    @Autowired
    JedisAdapter jedisAdapter;

    private Map<EventType, List<EventHandler>> config = new HashMap<>();

    private ApplicationContext applicationContext;

    //这个方法将在所有的属性被初始化后调用
    @Override
    public void afterPropertiesSet() throws Exception {
        //获取现在有多少个eventHandler初始化了
        Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
        if (beans != null){
            for (Map.Entry<String, EventHandler> entry : beans.entrySet()){
                //找到那些handler对当前的事件感兴趣
                List<EventType> eventTypes = entry.getValue().getSupportEventTypes();
                for (EventType type : eventTypes){
                    if (!config.containsKey(type)){
                        //有可能是第一次注册这个事件，所以就可能初始的时候是null
                        //把handler放到config中
                        //把event注册到config中
                        config.put(type, new ArrayList<EventHandler>());
                    }
                    //把对这些event感兴趣的handler添加到config
                    config.get(type).add(entry.getValue());
                }
            }
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                LOGGER.info("线程开始读取事件");
                while (true){
                    String key = RedisKeyUtils.getEventQueue();
                    List<String> events = jedisAdapter.brpop(0, key);
                    for (String message : events){
                        LOGGER.info("读取事件:"+ message);
                        if (message.equals(key)){
                            continue;
                        }
                        EventModel eventModel = JSON.parseObject(message, EventModel.class);
                        if (!config.containsKey(eventModel.getType())){
                            LOGGER.error("不能识别的事件类型");
                            continue;
                        }
                        for (EventHandler handler : config.get(eventModel.getType())){
                            LOGGER.info("识别到事件：");
                            handler.doHandle(eventModel);
                        }
                    }
                }
            }
        });

        thread.start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}

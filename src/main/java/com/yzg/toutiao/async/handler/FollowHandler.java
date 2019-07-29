package com.yzg.toutiao.async.handler;

import com.yzg.toutiao.async.EventHandler;
import com.yzg.toutiao.async.EventModel;
import com.yzg.toutiao.async.EventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @author yzg
 * @create 2019/7/23
 */
@Component
public class FollowHandler implements EventHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(FollowHandler.class);

    @Override
    public void doHandle(EventModel model) {

    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Collections.singletonList(EventType.FOLLOW);
    }
}

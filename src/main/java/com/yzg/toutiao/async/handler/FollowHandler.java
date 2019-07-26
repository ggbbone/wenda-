package com.yzg.toutiao.async.handler;

import com.yzg.toutiao.async.EventHandler;
import com.yzg.toutiao.async.EventModel;
import com.yzg.toutiao.async.EventType;

import java.util.Collections;
import java.util.List;

/**
 * @author yzg
 * @create 2019/7/23
 */
public class FollowHandler implements EventHandler {

    @Override
    public void doHandle(EventModel model) {

    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Collections.singletonList(EventType.FOLLOW);
    }
}

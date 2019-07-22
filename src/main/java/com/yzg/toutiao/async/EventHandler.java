package com.yzg.toutiao.async;

import java.util.List;

/**
 * @author yzg
 * @create 2019/7/21
 */
public interface EventHandler {

    /**
     * 处理的事件
     * @param model
     */
    void doHandle(EventModel model);

    /**
     * 关注的事件类型
     * @return
     */
    List<EventType> getSupportEventTypes();
}

package com.yzg.toutiao.async;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yzg
 * @create 2019/7/21
 */
public class EventModel {

    /**
     * 事件类型
     */
    private EventType type;
    /**
     * 触发用户id
     */
    private int actorId;
    /**
     * 触发目标类型
     */
    private int entityType;

    /**
     * 触发目标id
     */
    private int entityId;

    /**
     * 触发目标用户id
     */
    private int entityOwnerId;

    public EventModel() {
    }

    public EventModel(EventType type) {
        this.type = type;
    }

    private Map<String, String> exts = new HashMap<>();

    public EventModel setExt(String key, String value){
        exts.put(key,value);
        return this;
    }

    public String getExt(String key){
        return exts.get(key);
    }

    public EventType getType() {
        return type;
    }

    public EventModel setType(EventType type) {
        this.type = type;
        return this;
    }

    public int getActorId() {
        return actorId;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityOwnerId() {
        return entityOwnerId;
    }

    public EventModel setEntityOwnerId(int entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
        return this;
    }

    public Map<String, String> getExts() {
        return exts;
    }

    public EventModel setExts(Map<String, String> exts) {
        this.exts = exts;
        return this;
    }
}

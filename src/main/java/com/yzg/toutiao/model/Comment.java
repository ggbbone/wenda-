package com.yzg.toutiao.model;

import java.util.Date;

public class Comment {
    private Integer id;

    private Integer userId;

    private Date creadtedDate;

    private Integer entityId;

    private Integer entityType;

    private String content;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getCreadtedDate() {
        return creadtedDate;
    }

    public void setCreadtedDate(Date creadtedDate) {
        this.creadtedDate = creadtedDate;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public Integer getEntityType() {
        return entityType;
    }

    public void setEntityType(Integer entityType) {
        this.entityType = entityType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}
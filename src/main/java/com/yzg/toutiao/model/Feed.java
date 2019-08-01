package com.yzg.toutiao.model;

import com.alibaba.fastjson.JSONObject;

import java.util.Date;

public class Feed {
    private Integer id;

    private Byte type;

    private Date createdDate;

    private Integer userId;

    //JSON
    private String data;


    private Byte state;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data == null ? null : data.trim();
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Feed{" +
                "id=" + id +
                ", type=" + type +
                ", createdDate=" + createdDate +
                ", userId=" + userId +
                ", data='" + data + '\'' +
                ", state=" + state +
                '}';
    }
}
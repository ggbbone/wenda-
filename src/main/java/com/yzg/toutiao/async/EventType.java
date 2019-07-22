package com.yzg.toutiao.async;

/**
 * @author yzg
 * @create 2019/7/21
 */
public enum  EventType {

    LIKE(0),
    COMMENT(1),
    LOGIN(2),
    REGISTER(3),
    EMAIL(4);

    private int value;
    EventType(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }
}

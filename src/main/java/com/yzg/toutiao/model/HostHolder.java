package com.yzg.toutiao.model;

import org.springframework.stereotype.Component;

/**
 * @author yzg
 * @create 2019/7/6
 */
@Component
public class HostHolder {
    private static  ThreadLocal<User> users = new ThreadLocal<User>();

    public User getUser() {
        return users.get();
    }

    public void setUser(User user) {
        users.set(user);
    }

    public void clear(){
        users.remove();
    }
}

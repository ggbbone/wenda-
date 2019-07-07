package com.yzg.toutiao.model;

import java.util.LinkedHashMap;

/**
 * @author yzg
 * @create 2019/5/22
 *
 * 通用的json数据返回
 */
public class Result extends LinkedHashMap<String, Object> {
    private static final long serialVersionUID = -364546270975223015L;

    public Result result(String key, Object value) {
        this.put(key, value);
        return this;
    }

    public Result success(){
        return this.success("success");
    }
    public Result success(Object message){
        this.put("status",200);
        this.put("message", message);
        return this;
    }

    public Result fail(){
        return this.fail("fail");
    }

    public Result fail(Object message) {
        this.put("status",400);
        this.put("message", message);
        return this;
    }

    public Result redirect(String message) {
        this.put("status",300);
        this.put("message", message);
        return this;
    }

    public Result unauthorized() {
        return this.unauthorized("the current user is unauthorized");
    }
    public Result unauthorized(Object message){
        this.put("status",401);
        this.put("message", message);
        return this;
    }

    public Result forbidden(Object message){
        this.put("status",403);
        this.put("message", message);
        return this;
    }

    public Result code(int code) {
        return result("status",code);
    }

    public Result message(String message) {
        return result("message", message);
    }

    public Result data(Object data) {
        return result("data", data);
    }

}

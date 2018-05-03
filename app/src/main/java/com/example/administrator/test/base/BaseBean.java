package com.example.administrator.test.base;

/**
 * Created by 梁遂 on 2017/9/28.
 */

public class BaseBean {

    /**
     * status : 200     状态码
     * msg : OK
     */

    private int status;
    private String msg;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}

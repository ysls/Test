package com.example.administrator.test.login;

/**
 * Created by 梁遂 on 2017/9/27.
 * 获取验证码
 */

public class CodeBean {

    /**
     * flag : 1
     * msg : 验证码下发成功!
     */

    private int flag;
    private String msg;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

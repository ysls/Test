package com.example.administrator.test.login;


public class LoginBean {

    /**
     * flag : 0
     * msg : 登录失败,可能是已经登录或者验证码错误!
     * token : null
     */

    private int flag;
    private String msg;
    private String token;

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

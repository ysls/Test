package com.example.administrator.test.backup;

public class DownBean {

    /**
     * flag : 1
     * mlist : [{"name":"aa0","phone":"18428326467"},{"name":"aa1","phone":"18428326467"},{"name":"aa2","phone":"18428326467"},{"name":"aa3","phone":"18428326467"},{"name":"aa4","phone":"18428326467"}]
     * msg : 恢复通讯录成功
     */

    private int flag;
    private String mlist;
    private String msg;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getMlist() {
        return mlist;
    }

    public void setMlist(String mlist) {
        this.mlist = mlist;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

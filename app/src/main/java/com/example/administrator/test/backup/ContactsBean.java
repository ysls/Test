package com.example.administrator.test.backup;

public class ContactsBean {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ContactsBean(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public ContactsBean() {
    }

    private String name;
    private String phone;
}

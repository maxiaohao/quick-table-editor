package com.ct.ks.bsc.qte.model;

public class User {

    long user_id;
    String user_name;
    String pwd_md5; // not in db model
    String salt;
    String salted_md5; // salted_md5 = md5(user_name + pwd_md5 + salt)
    boolean admin;
    boolean disabled;


    public long getUser_id() {
        return user_id;
    }


    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }


    public String getUser_name() {
        return user_name;
    }


    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }


    public String getPwd_md5() {
        return pwd_md5;
    }


    public void setPwd_md5(String pwd_md5) {
        this.pwd_md5 = pwd_md5;
    }


    public String getSalt() {
        return salt;
    }


    public void setSalt(String salt) {
        this.salt = salt;
    }


    public String getSalted_md5() {
        return salted_md5;
    }


    public void setSalted_md5(String salted_md5) {
        this.salted_md5 = salted_md5;
    }


    public boolean isAdmin() {
        return admin;
    }


    public void setAdmin(boolean admin) {
        this.admin = admin;
    }


    public boolean isDisabled() {
        return disabled;
    }


    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

}

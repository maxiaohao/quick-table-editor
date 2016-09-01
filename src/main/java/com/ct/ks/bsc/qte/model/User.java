package com.ct.ks.bsc.qte.model;

public class User {

    long user_id;
    String user_name;
    String pwd_md5;
    boolean is_admin;
    boolean is_disabled;


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


    public boolean isIs_admin() {
        return is_admin;
    }


    public void setIs_admin(boolean is_admin) {
        this.is_admin = is_admin;
    }


    public boolean isIs_disabled() {
        return is_disabled;
    }


    public void setIs_disabled(boolean is_disabled) {
        this.is_disabled = is_disabled;
    }

}

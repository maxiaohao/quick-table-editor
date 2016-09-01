package com.ct.ks.bsc.qte.model;

public class Ds {

    long ds_id;
    String ds_name;
    String jdbc_driver;
    String jdbc_url;
    String username;
    String password;
    boolean is_disabled;


    public long getDs_id() {
        return ds_id;
    }


    public void setDs_id(long ds_id) {
        this.ds_id = ds_id;
    }


    public String getDs_name() {
        return ds_name;
    }


    public void setDs_name(String ds_name) {
        this.ds_name = ds_name;
    }


    public String getJdbc_driver() {
        return jdbc_driver;
    }


    public void setJdbc_driver(String jdbc_driver) {
        this.jdbc_driver = jdbc_driver;
    }


    public String getJdbc_url() {
        return jdbc_url;
    }


    public void setJdbc_url(String jdbc_url) {
        this.jdbc_url = jdbc_url;
    }


    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public boolean isIs_disabled() {
        return is_disabled;
    }


    public void setIs_disabled(boolean is_disabled) {
        this.is_disabled = is_disabled;
    }

}

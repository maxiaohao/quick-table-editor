package com.ct.ks.bsc.qte.model;

public class QteDataSource {

    long ds_id;
    String ds_name;
    String jdbc_class;
    String jdbc_url;
    String username = "";
    String password = "";
    boolean disabled;


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


    public String getJdbc_class() {
        return jdbc_class;
    }


    public void setJdbc_class(String jdbc_class) {
        this.jdbc_class = jdbc_class;
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


    public boolean isDisabled() {
        return disabled;
    }


    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

}

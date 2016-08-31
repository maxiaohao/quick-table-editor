package com.ct.ks.bsc.qte.model;

public class User {

    long USER_ID;
    String USER_NAME;
    String PWD_MD5;
    boolean IS_ADMIN;
    boolean IS_DISABLED;


    public long getUSER_ID() {
        return USER_ID;
    }


    public void setUSER_ID(long uSER_ID) {
        USER_ID = uSER_ID;
    }


    public String getUSER_NAME() {
        return USER_NAME;
    }


    public void setUSER_NAME(String uSER_NAME) {
        USER_NAME = uSER_NAME;
    }


    public String getPWD_MD5() {
        return PWD_MD5;
    }


    public void setPWD_MD5(String pWD_MD5) {
        PWD_MD5 = pWD_MD5;
    }


    public boolean isIS_ADMIN() {
        return IS_ADMIN;
    }


    public void setIS_ADMIN(boolean iS_ADMIN) {
        IS_ADMIN = iS_ADMIN;
    }


    public boolean isIS_DISABLED() {
        return IS_DISABLED;
    }


    public void setIS_DISABLED(boolean iS_DISABLED) {
        IS_DISABLED = iS_DISABLED;
    }

}

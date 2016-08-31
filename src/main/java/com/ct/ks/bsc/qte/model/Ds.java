package com.ct.ks.bsc.qte.model;

public class Ds {

    long DS_ID;
    String DS_NAME;
    String JDBC_DRIVER;
    String JDBC_URL;
    String USERNAME;
    String PASSWORD;
    boolean IS_DISABLED;


    public long getDS_ID() {
        return DS_ID;
    }


    public void setDS_ID(long dS_ID) {
        DS_ID = dS_ID;
    }


    public String getDS_NAME() {
        return DS_NAME;
    }


    public void setDS_NAME(String dS_NAME) {
        DS_NAME = dS_NAME;
    }


    public String getJDBC_DRIVER() {
        return JDBC_DRIVER;
    }


    public void setJDBC_DRIVER(String jDBC_DRIVER) {
        JDBC_DRIVER = jDBC_DRIVER;
    }


    public String getJDBC_URL() {
        return JDBC_URL;
    }


    public void setJDBC_URL(String jDBC_URL) {
        JDBC_URL = jDBC_URL;
    }


    public String getUSERNAME() {
        return USERNAME;
    }


    public void setUSERNAME(String uSERNAME) {
        USERNAME = uSERNAME;
    }


    public String getPASSWORD() {
        return PASSWORD;
    }


    public void setPASSWORD(String pASSWORD) {
        PASSWORD = pASSWORD;
    }


    public boolean isIS_DISABLED() {
        return IS_DISABLED;
    }


    public void setIS_DISABLED(boolean iS_DISABLED) {
        IS_DISABLED = iS_DISABLED;
    }
}

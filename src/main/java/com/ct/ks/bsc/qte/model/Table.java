package com.ct.ks.bsc.qte.model;

public class Table {

    long TABLE_ID;
    long DS_ID;
    String TABLE_SCHEMA;
    String TABLE_NAME;
    String TABLE_TITLE;
    String TABLE_DESC;
    boolean IS_DISABLED;


    public long getTABLE_ID() {
        return TABLE_ID;
    }


    public void setTABLE_ID(long tABLE_ID) {
        TABLE_ID = tABLE_ID;
    }


    public long getDS_ID() {
        return DS_ID;
    }


    public void setDS_ID(long dS_ID) {
        DS_ID = dS_ID;
    }


    public String getTABLE_SCHEMA() {
        return TABLE_SCHEMA;
    }


    public void setTABLE_SCHEMA(String tABLE_SCHEMA) {
        TABLE_SCHEMA = tABLE_SCHEMA;
    }


    public String getTABLE_NAME() {
        return TABLE_NAME;
    }


    public void setTABLE_NAME(String tABLE_NAME) {
        TABLE_NAME = tABLE_NAME;
    }


    public String getTABLE_TITLE() {
        return TABLE_TITLE;
    }


    public void setTABLE_TITLE(String tABLE_TITLE) {
        TABLE_TITLE = tABLE_TITLE;
    }


    public String getTABLE_DESC() {
        return TABLE_DESC;
    }


    public void setTABLE_DESC(String tABLE_DESC) {
        TABLE_DESC = tABLE_DESC;
    }


    public boolean isIS_DISABLED() {
        return IS_DISABLED;
    }


    public void setIS_DISABLED(boolean iS_DISABLED) {
        IS_DISABLED = iS_DISABLED;
    }

}

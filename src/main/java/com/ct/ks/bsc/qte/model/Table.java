package com.ct.ks.bsc.qte.model;

public class Table {

    long table_id;
    long ds_id;
    String table_schema;
    String table_name;
    String table_title;
    String table_desc;
    boolean is_disabled;


    public long getTable_id() {
        return table_id;
    }


    public void setTable_id(long table_id) {
        this.table_id = table_id;
    }


    public long getDs_id() {
        return ds_id;
    }


    public void setDs_id(long ds_id) {
        this.ds_id = ds_id;
    }


    public String getTable_schema() {
        return table_schema;
    }


    public void setTable_schema(String table_schema) {
        this.table_schema = table_schema;
    }


    public String getTable_name() {
        return table_name;
    }


    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }


    public String getTable_title() {
        return table_title;
    }


    public void setTable_title(String table_title) {
        this.table_title = table_title;
    }


    public String getTable_desc() {
        return table_desc;
    }


    public void setTable_desc(String table_desc) {
        this.table_desc = table_desc;
    }


    public boolean isIs_disabled() {
        return is_disabled;
    }


    public void setIs_disabled(boolean is_disabled) {
        this.is_disabled = is_disabled;
    }

}

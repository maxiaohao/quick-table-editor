package com.ct.ks.bsc.qte.util;

/**
 * Wrap of a result for CRUD (Create, Read, Update, Delete) operations.
 *
 * @author xma11 <maxiaohao@gmail.com>
 *
 */
public class CrudResult {

    public static final int RET_CODE_OK = 0;
    public static final int RET_CODE_INVALID_PARAM = -1;
    public static final int RET_CODE_INTERNAL_ERROR = -2;
    public static final int RET_CODE_AUTHENTICATIOIN_FAILURE = -3;

    boolean success = true;
    Object data;
    String msg;
    int retcode = RET_CODE_OK;
    long total;// unique member for paging


    public CrudResult(boolean success) {
        super();
        this.success = success;
    }


    public CrudResult(boolean success, String msg) {
        super();
        this.success = success;
        this.msg = msg;
    }


    public CrudResult(boolean success, String msg, int retcode) {
        super();
        this.success = success;
        this.msg = msg;
        this.retcode = retcode;
    }


    public CrudResult(boolean success, Object data) {
        super();
        this.success = success;
        this.data = data;
    }


    public CrudResult(boolean success, Object data, String msg) {
        super();
        this.success = success;
        this.data = data;
        this.msg = msg;
    }


    public CrudResult(boolean success, Object data, String msg, int retcode) {
        super();
        this.success = success;
        this.retcode = retcode;
        this.data = data;
        this.msg = msg;
    }


    public boolean isSuccess() {
        return success;
    }


    public Object getData() {
        return data;
    }


    public String getMsg() {
        return msg;
    }


    public int getRetcode() {
        return retcode;
    }


    public long getTotal() {
        return total;
    }


    public void setTotal(long total) {
        this.total = total;
    }

}

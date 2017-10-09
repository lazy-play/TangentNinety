package com.pudding.tangentninety.module.http.excepiton;

/**
 * Created by Error on 2017/6/22 0022.
 */

public class ApiException extends Exception{

    private int code;

    public ApiException(String msg) {
        super(msg);
    }

    public ApiException(String msg, int code) {
        super(msg);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
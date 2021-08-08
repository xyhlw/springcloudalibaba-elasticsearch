package com.xy.cloud.search.result;

import com.google.common.base.MoreObjects;



public class Result<T> {


    /**
     * 返回码
     */

    private int code;

    /**
     * 返回结果描述
     */

    private String message;

    /**
     * 返回内容
     */

    private T data;


    public Result() {}

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Result(int code, String message) {
        this.code = code;
        this.message = message;
        this.data = null;
    }

    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }


    public Result(CommonResultStatus status) {
        this.code = status.getCode();
        this.message = status.getMessage();
        this.data = null;
    }

    public Result(CommonResultStatus status, T data) {
        this.code = status.getCode();
        this.message = status.getMessage();
        this.data = data;

    }

//    public boolean isSuccessful() {
//        return this.code == 0;
//    }

    public static<T> Result ok(T data) {
        return new Result(CommonResultStatus.SUCCESS, data);
    }


    public static Result ok() {
        return new Result(CommonResultStatus.SUCCESS);
    }

    public static Result processing() {
        return new Result(CommonResultStatus.PROCESSING);
    }

    public static Result error(CommonResultStatus error) {
        return new Result(error);
    }

    public static Result error(int code ,  String message) {
        return new Result(code , message);
    }

    public static<T> Result fail(String msg) {
        return new Result(CommonResultStatus.FAILURE.getCode(),msg,null);
    }
    public static<T> Result fail(ResultStatus resultStatus){
        return new Result(resultStatus.getCode(),resultStatus.getMessage(),null);
    }
    public static<T> Result fail(int code,String msg) {
        return new Result(code,msg,null);
    }
    public static<T> Result fail() {
        return new Result(CommonResultStatus.FAILURE);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("code", code)
                .add("message", message)
                .add("data", data)
                .toString();
    }
}

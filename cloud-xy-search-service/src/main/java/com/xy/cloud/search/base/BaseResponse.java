package com.xy.cloud.search.base;

public class BaseResponse<T> {

	/**
	 * 返回码
	 */
	private Integer code;
	/**
	 * 消息
	 */
	private String msg;
	
	/**
	 * 返回
	 */
	private T data;
	// 分页

	public BaseResponse() {

	}

	public BaseResponse(Integer code, String msg, T data) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}

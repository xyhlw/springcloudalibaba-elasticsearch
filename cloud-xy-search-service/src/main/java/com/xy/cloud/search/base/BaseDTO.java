package com.xy.cloud.search.base;

import java.util.Date;



/**
 * 检索查询基类
 *
 * @author yangchang
 */
public class BaseDTO {
	/**
	 * 页码
	 */
	private Integer pageNum = 1;

	/**
	 * 每页条数
	 */
	private Integer pageSize = 10;

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
}

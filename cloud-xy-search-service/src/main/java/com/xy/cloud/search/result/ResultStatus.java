package com.xy.cloud.search.result;

/**
 * @author 杨畅 on 2020/6/30
 */
public interface ResultStatus {
    /**
     * 错误信息
     * @return
     */
    String getMessage();

    /**
     * 错误码
     * @return
     */
    int getCode();
}

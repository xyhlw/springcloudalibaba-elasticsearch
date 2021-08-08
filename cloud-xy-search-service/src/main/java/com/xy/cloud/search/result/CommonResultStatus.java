package com.xy.cloud.search.result;


/**
 * 自定义请求状态码
 */
public enum CommonResultStatus implements ResultStatus {
    SUCCESS(200, "成功"),
    FAILURE(-1, "服务器内部错误"),
    PROCESSING(-2, "正在执行中"),

    SENTINEL_BLOCK(9001, "操作太频繁"),
    //用户相关，1000-1999
    SC_ERROR_PASSWORD_WRONG(1001, "账号或密码错误"),
    SC_ERROR_DUPLICATE_ACCOUNTNAME(1002, "账号重复"),
    SC_ERROR_ACCOUNT_NOT_FOUND(1003, "账号不存在"),
    SC_ERROR_DUPLICATE_EMAIL(1004, "邮箱重复"),
    SC_ERROR_DUPLICATE_MOBILE(1005, "手机号码重复"),
    SC_ERROR_DUPLICATE_RULE(1006, "等级规则信息重复"),
    SC_ERROR_DUPLICATE_REPAIR(1006, "该标签已存在！"),
    TOKEN_EXPIRED(105,"token已过期，请重新登录!"),
    /**
     * 账号被锁定禁止登录
     */
    LOCKED_ACCOUNT_ERROR(102,"账号被锁定禁止登录"),
    // 通用2000-2999
    COMMON_ERROR_UPLOAD_INVALIDTYPE(2003, "上传文件格式非法，请选择正确的格式"),
    COMMON_ERROR_UPLOAD_SIZE_EXCEED(2005, "上传文件大小超出限制!"),
    COMMON_ERROR_PARAM_NOTNULL(2001, "参数不能为空"),
    COMMON_ERROR_DATA_NOTFOUND(2002, "数据找不到"),

    //业务定义3000 -- 4999

    //sys
    SYS_ERROR_SUPER_ROLE_DELETE(5005,"超级管理员角色不能删除"),
    SYS_ERROR_USER_GROUP_NAME_LENGTH(5007,"数据权限名称输入长度超过限制"),
    SYS_ERROR_USER_GROUP_REMARK_LENGTH(5008,"数据权限描述输入长度超过限制"),
    SYS_ERROR_ROLE_NAME_LENGTH(5009,"角色名称输入长度超过限制"),
    SYS_ERROR_ROLE_REMARK_LENGTH(5010,"角色描述输入长度超过限制"),
    SYS_ERROR_DICTIONARY_NAME_LENGTH(5011,"数据字典名称输入长度超过限制"),
    SYS_ERROR_DICTIONARY_VALUE_LENGTH(5011,"数据字典值输入长度超过限制"),
    SYS_ERROR_DICTIONARY_SEQ_LENGTH(5012,"数据字典序号输入长度超过限制"),
    SYS_ERROR_ADMIN_ACCOUNT_CHANGE(5013,"不能修改admin账号的权限"),
    SYS_ERROR_ROLE_NAME_DUPLICATE(5014,"角色名称不能重复"),
    SYS_ERROR_ROLE_DELETE_EXISTS_USER(5015,"角色有配置用户，不能删除"),


    USER_ERROR_TOKEN_EXPIRED_STATUS(401,"您的认证已失效,请重新登录"),
    USER_ERROR_TOKEN_OTHER_LOGIN_STATUS(403,"您的账号已在其他设备登录,如不是您本人操作,建议您尽快修改密码"),

    /**
     * 用户已存在，请勿重复注册!
     */
    USER_ALREADY_EXIST(104,"用户已存在，请勿重复注册!"),
    OVER_LAND_PARAMETER_ERROR(60400,"参数错误"),
    OVER_LAND_DOES_NOT_EXIST(60401,"资源不存在"),
    OVER_LAND_OTHER_OPT(60402,"数据已被他人操作,请刷新后重试"),

    ;

    /**
     * 返回码
     */
    private int code;

    /**
     * 返回结果描述
     */
    private String message;


    CommonResultStatus(int code, String message) {
        this.setCode(code);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }


    public void setCode(int code) {
        this.code = code;
    }

}
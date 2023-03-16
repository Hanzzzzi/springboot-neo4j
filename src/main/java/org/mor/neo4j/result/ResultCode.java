package org.omaha.neo4j.result;

/**
 * @Author: mor
 * @Date: 2020/12/24 16:55
 */
public enum ResultCode {

    // 成功
    SUCCESS(200, "success"),

    //3 开头 token token异常
    TOKEN_PAST(301, "token过期"), TOKEN_ERROR(302, "token异常"),
    // 登录异常
    LOGIN_ERROR(303, "登录异常"), REMOTE_ERROR(304, "异地登录"),

    // 添加删除更新，4开头
    SAVE_FAIL(403, "添加失败"),
    RELATIONSHIP_CONFLICT(403, "关系冲突，添加失败"),
    UPDATE_FAIL(404, "更新失败"),
    DELETE_FAIL(405, "删除失败"),


    // 用户异常，5开头
    LECTURER_REQUISITION_REGISTERED(501, "用户或密码错误"),
    LECTURER_REQUISITION_WAIT(502, "该用户没有权限"),

    // 错误
    SYS_ERROR(997, "系统异常了....."),
    INTERFACE_ERROR(998, "出错了请稍后尝试....."),
    ERROR(999, "服务器出错了请稍后尝试....."),

    INVALID_PARAMETERS(1001, "参数异常"),

    //给前台返回提示信息 给用户展示···
    TIPS_INFO(1111,"没有找到符合条件的数据");


    private Integer code; //状态码

    private String message; //描述信息

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

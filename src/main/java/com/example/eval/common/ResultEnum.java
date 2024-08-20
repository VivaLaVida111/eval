package com.example.eval.common;



/**
 * 统一返回格式ResponseDTO中的状态码和状态信息
 * by 张渝
 * @since 2022-09-29
 */
public enum ResultEnum {
    SUCCESS(2000, "请求成功"),
    ERROR(5000, "服务器内部错误"),
    FAILED(4004, "请求失败"),
    FORBIDDEN(4003, "没有相关权限"),
    VALIDATE_FAILED(4002, "参数检验失败"),
    UNAUTHORIZED(4001, "暂未登录或token已经过期");

    private Integer code;
    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
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
}

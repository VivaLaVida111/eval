package com.example.eval.common;


/**
 * 统一返回格式预封装
 * by 张渝
 * @since 2022-09-30
 */
public class ResponseDataUtil {
    /**
     * @param data 数据实体
     * @param <T> 实体类型
     */
    public static <T> ResponseData<T> Success(T data) {
        return new ResponseData<T>(ResultEnum.SUCCESS, data);
    }

    public static <T> ResponseData<T> Success() {
        return new ResponseData<T>(ResultEnum.SUCCESS);
    }

    public static <T> ResponseData<T> Success(String msg) {
        return new ResponseData<T>(msg);
    }

    public static <T> ResponseData<T> Success(Integer code, String msg) {
        return new ResponseData<T>(code, msg);
    }

    public static <T> ResponseData<T> Success(Integer code, String msg, T data) {
        return new ResponseData<T>(code, msg, data);
    }

    public static <T> ResponseData<T> Success(ResultEnum resultEnum) {
        return new ResponseData<T>(resultEnum);
    }

    public static <T> ResponseData<T> Error(T data) {
        return new ResponseData<T>(ResultEnum.FAILED, data);
    }

    public static <T> ResponseData<T> Error() {
        return new ResponseData<T>(ResultEnum.FAILED);
    }

    public static <T> ResponseData<T> Error(String msg) {
        return new ResponseData<T>(ResultEnum.FAILED.getCode(), msg);
    }

    public static <T> ResponseData<T> Error(Integer code, String msg) {
        return new ResponseData<T>(code, msg);
    }

    public static <T> ResponseData<T> Error(Integer code, String msg, T data) {
        return new ResponseData<T>(code, msg, data);
    }

    public static <T> ResponseData<T> Error(ResultEnum resultEnum) {
        return new ResponseData<T>(resultEnum);
    }

    public static <T> ResponseData<T> Forbidden(T data) {
        return new ResponseData<T>(ResultEnum.FORBIDDEN, data);
    }

    public static <T> ResponseData<T> Unauthorized(T data) {
        return new ResponseData<T>(ResultEnum.UNAUTHORIZED, data);
    }

    public static <T> ResponseData<T> ValidateFailed() {
        return new ResponseData<T>(ResultEnum.VALIDATE_FAILED);
    }

    public static <T> ResponseData<T> ValidateFailed(String msg) {
        return new ResponseData<T>(ResultEnum.VALIDATE_FAILED.getCode(), msg);
    }
}

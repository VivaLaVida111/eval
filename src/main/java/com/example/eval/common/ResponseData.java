package com.example.eval.common;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * mp代码生成器
 * by luo
 * @since 2022-10-20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseData<T> implements Serializable {
    /**
     * @param code 状态码
     * @param msg 状态描述信息
     * @param data 数据实体
     */
    private Integer code;
    private String msg;
    private T data;

    public ResponseData(ResultEnum resultEnums) {
        this.code = resultEnums.getCode();
        this.msg = resultEnums.getMsg();
    }

    public ResponseData(ResultEnum resultEnums, T data) {
        this.code = resultEnums.getCode();
        this.msg = resultEnums.getMsg();
        this.data = data;
    }

    public ResponseData(String msg) {
        this.msg = msg;
    }

    public ResponseData(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}

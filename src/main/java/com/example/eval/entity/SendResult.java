package com.example.eval.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class SendResult implements Serializable {
    private static final long serialVersionUID = 1L;

    private Boolean status;
    private String msg;

    public SendResult(Boolean status, String msg) {
        this.status = status;
        this.msg = msg;
    }
}

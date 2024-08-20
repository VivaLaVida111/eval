package com.example.eval.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class SMSChangePwdResult implements Serializable {
    private static final long serialVersionUID = 1L;

    private Boolean isChanged;
    private String msg;

    public SMSChangePwdResult(Boolean isChanged, String msg) {
        this.isChanged = isChanged;
        this.msg = msg;
    }
}

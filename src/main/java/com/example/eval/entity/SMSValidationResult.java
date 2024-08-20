package com.example.eval.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class SMSValidationResult implements Serializable {
    private static final long serialVersionUID = 1L;

    private Boolean isValid;
    private String msg;
    private String token;

    public SMSValidationResult(Boolean isValid, String msg, String token) {
        this.isValid = isValid;
        this.msg = msg;
        this.token = token;
    }
}

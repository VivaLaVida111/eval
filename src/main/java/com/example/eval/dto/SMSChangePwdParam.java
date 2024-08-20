package com.example.eval.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class SMSChangePwdParam implements Serializable {
    private static final long serialVersionUID = 1L;

    private String phone;
    private String token;
    private String password;

    public SMSChangePwdParam(String phone, String token, String password) {
        this.phone = phone;
        this.token = token;
        this.password = password;
    }
}

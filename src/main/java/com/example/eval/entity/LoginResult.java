package com.example.eval.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class LoginResult implements Serializable  {

    private static final long serialVersionUID = 1L;

    private String token;
    private String tokenHead;
    private Boolean isValidPassword;
    private String msg;
}

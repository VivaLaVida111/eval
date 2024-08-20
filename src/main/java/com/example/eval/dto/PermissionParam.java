package com.example.eval.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class PermissionParam implements Serializable {
    private static final long serialVersionUID = 1L;
    private String roleSystem;
    private String roleName;
    private String userName;
    private String operateType;
    private String operator;
}


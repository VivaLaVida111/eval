package com.example.eval.dto;

import com.example.eval.entity.Role;
import com.example.eval.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class UserPermission implements Serializable  {
    private static final long serialVersionUID = 1L;
    private String telephone;
    private String realName;
    private List<Role> roleList;

    public UserPermission(User user, List<Role> roleList) {
        this.realName = user.getRealName();
        this.telephone = user.getTelephone();
        this.roleList = roleList;
    }
}


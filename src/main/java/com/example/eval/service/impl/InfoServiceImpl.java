package com.example.eval.service.impl;

import com.example.eval.dto.UserDetailsImpl;
import com.example.eval.entity.Role;
import com.example.eval.entity.User;
import com.example.eval.service.InfoService;
import com.example.eval.service.AuthService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InfoServiceImpl implements InfoService {
    @Resource
    private AuthService authService;
    @Override
    public Map<String, String> getInfo() {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();
        Map<String, String> map = new HashMap<>();
        map.put("error_message", "success");
        map.put("id", user.getId().toString());
        map.put("name", user.getName());
        map.put("telephone", user.getTelephone());
        map.put("real_name", user.getRealName());
        List<Role> roleList = authService.getRoleList(user.getId());
        StringBuilder roles = new StringBuilder();
        if (roleList != null) {
            for (int i = 0; i < roleList.size(); i++) {
                Role role = roleList.get(i);
                roles.append(role.getName());
                if (i < roleList.size() - 1) {
                    roles.append(",");
                }
            }
        }
        map.put("roles", roles.toString());
        return map;
    }
}


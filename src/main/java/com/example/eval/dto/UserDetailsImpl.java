package com.example.eval.dto;

import com.example.eval.entity.Role;
import com.example.eval.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SpringSecurity需要的用户详情
 */
public class UserDetailsImpl implements UserDetails {
    private User user;
    private List<Role> roleList;
    public UserDetailsImpl(User user, List<Role> roleList) {
        this.user = user;
        this.roleList = roleList;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (roleList == null) {
            return null;
        }

        //返回当前用户的权限
        return roleList.stream()
                .filter(role -> role.getName()!=null)
                .map(role ->new SimpleGrantedAuthority("考评系统" + ":" + role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public User getUser() {
        return user;
    }
}

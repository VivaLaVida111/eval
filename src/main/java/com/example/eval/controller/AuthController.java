package com.example.eval.controller;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson2.JSONObject;
import com.example.eval.common.ResponseDataUtil;
import com.example.eval.common.ResponseData;
import com.example.eval.dto.*;
import com.example.eval.entity.LoginResult;
//import com.example.eval.entity.PermissionApplication;
import com.example.eval.entity.SendResult;
import com.example.eval.entity.User;
import com.example.eval.service.AuthService;
//import com.example.jinniuprog.service.InfoService;
import com.example.eval.service.InfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.net.HttpCookie;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("//auth")
public class AuthController {
    @Resource
    private AuthService authService;
    @Resource
    private InfoService infoService;
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @PostMapping("/register")
    public Boolean register(@RequestBody User userParam) {
        String username = userParam.getName();
        String password = userParam.getPassword();
        String realName = userParam.getRealName();
        if (username == null || password == null || realName == null) {
            return false;
        }
        User user = authService.register(userParam);
        if (user == null) {
            return false;
        }
        return true;
    }

    @GetMapping("/delete_user/{username}")
    public Boolean deleteUser(@PathVariable String username) {
        Map<String, String> infoMap = infoService.getInfo();
        String operator = infoMap.get("name");
        return authService.deleteUser(operator, username);
    }

    @PostMapping("/login")
    public ResponseData<Map<String, String>> login(@RequestBody UserLoginParam userLoginParam) {
        SimpleDateFormat format_time = new SimpleDateFormat();
        logger.warn("AuthController requestTime:" + format_time.format(new Date(System.currentTimeMillis())));
        String username = userLoginParam.getName();
        System.out.println("登陆人员账号："+username);
        String password = userLoginParam.getPassword();
        Map<String, String> tokenMap = new HashMap<>();
        if (username == null || password == null) {
            tokenMap.put("error_message", "username or password is null");
            return ResponseDataUtil.Error(tokenMap);
        }
        LoginResult res = authService.login(username, password);
        String token = res.getToken();
        Boolean isValidPassword = res.getIsValidPassword();
        if (token == null || isValidPassword == null) {
            String msg = res.getMsg();
            tokenMap.put("error_message", msg != null ? msg : "username or password is not correct");
            return ResponseDataUtil.Error(tokenMap);
        }
        tokenMap = infoService.getInfo();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        tokenMap.put("isValidPassword", isValidPassword.toString());
        
        return ResponseDataUtil.Success(tokenMap);
    }

    @PostMapping("/getInfo")
    public ResponseData<Map<String, String>> getInfo(@RequestBody String token) {
        Map<String, String> tokenMap = infoService.getInfo();

        return ResponseDataUtil.Success(tokenMap);
    }



    @PostMapping("/change_password")
    public Boolean changePassword(@RequestBody String password) {
        if (password == null) {
            return false;
        }
        Map<String, String> infoMap = infoService.getInfo();
        String name = infoMap.get("name");
        return authService.changePassword(name, password);
    }

    @PostMapping("/admin_change_password")
    public Boolean adminChangePassword(@RequestBody User user) {
        if (user == null || user.getName() == null || user.getPassword() == null) {
            return false;
        }
        Map<String, String> infoMap = infoService.getInfo();
        String admin = infoMap.get("name");
        return authService.adminChangePassword(admin, user.getName(), user.getPassword());
    }

    @GetMapping("/all_permission")
    @PreAuthorize("hasAuthority('all:superAdmin')")
    public List<UserPermission> getAllPermissionList() {
        return authService.getAllPermissionList();
    }

    @GetMapping("/non_super_admin_list")
    public List<UserPermission> getNonSuperAdminList() {
        Map<String, String> infoMap = infoService.getInfo();
        String name = infoMap.get("name");
        return authService.getNonSuperAdminList(name);
    }

    @GetMapping("/self_role_list")
    public List<UserPermission> getSelfRoleList() {
        Map<String, String> infoMap = infoService.getInfo();
        String name = infoMap.get("name");
        return authService.getSelfRoleList(name);
    }

    @GetMapping("/super_admin_list")
    public List<UserPermission> getSuperAdminList() {
        Map<String, String> infoMap = infoService.getInfo();
        String name = infoMap.get("name");
        return authService.getSuperAdminList(name);
    }



    // 获取该用户拥有管理员身份的系统内的所有人员权限，用户为超级管理员返回结果与“all_permission”接口一致，若该人员无管理员身份则返回空
    @GetMapping("/permission_list")
    public List<UserPermission> getPermissionList() {
        Map<String, String> infoMap = infoService.getInfo();
        String name = infoMap.get("name");
        return authService.getPermissionList(name);
    }

    // 管理权限时用此接口，仅超级管理员有效
//    @PostMapping("/apply_for_permission")
//    public Boolean applyForPermission(@RequestBody PermissionParam permissionParam) {
//        return authService.applyForPermission(permissionParam);
//    }
//
//    @GetMapping("/get_permission_applications")
//    public List<PermissionApplication> getPermissionApplications() {
//        Map<String, String> infoMap = infoService.getInfo();
//        String name = infoMap.get("name");
//        return authService.getPermissionApplications(name);
//    }
//
//    @GetMapping("/get_self_permission_applications")
//    public List<PermissionApplication> getSelfPermissionApplications() {
//        Map<String, String> infoMap = infoService.getInfo();
//        String name = infoMap.get("name");
//        return authService.getSelfPermissionApplications(name);
//    }
//
//    @GetMapping("/approve_application/{id}")
//    public Boolean approveApplication(@PathVariable Integer id) {
//        return authService.approveApplication(id);
//    }
//
//    @GetMapping("/refuse_application/{id}")
//    public Boolean refuseApplication(@PathVariable Integer id) {
//        return authService.refuseApplication(id);
//    }

    @PostMapping("/add_permission")
    public Boolean addPermission(@RequestBody PermissionParam permission) {
        return authService.addPermission(permission);
    }

    @PostMapping("/delete_permission")
    public Boolean deletePermission(@RequestBody PermissionParam permission) {
        return authService.deletePermission(permission);
    }


//    @PostMapping("/change_permission")
//    public Boolean changePermission(@RequestBody ChangePermissionParam param) {
//        return authService.changePermission(param.getOldPermission(), param.getNewPermission());
//    }


}

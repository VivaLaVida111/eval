package com.example.eval.service;

import com.example.eval.dto.PermissionParam;
import com.example.eval.dto.UserDetailsImpl;
import com.example.eval.dto.UserPermission;
import com.example.eval.entity.*;

import java.util.List;

public interface AuthService {
    /**
     * 根据用户名获取用户信息
     */
    User getUserByName(String name);

    /**
     * 注册
     */
    User register(User userParam);

    /**
     * 删除账号
     */
    Boolean deleteUser(String operator, String username);

    /**
     * 登录
     * @return 生成的JWT的token
     */
    LoginResult login(String username, String password);

    SendResult sendCode(String phone);
    SMSValidationResult validateCode(String phone, String code);
    SMSChangePwdResult changePasswordBySMS(String phone, String token, String password);

    /**
     * 免密登录
     * @author sunny
     * @return 生成的JWT的token
     * @date 2023.10.20
     */
    LoginResult pswFreeLogin(String username);
    /**
     * 检查密码是否符合规范
     */
    Boolean isValidPassword(String password);

    /**
     * 修改密码
     */
    Boolean changePassword(String name, String password);
    Boolean adminChangePassword(String admin, String username, String password);

    /**
     * 查询该用户拥有的全部角色
     */
    List<Role> getRoleList(Integer id);

    /**
     * 查询系统全部人员权限
     */
    List<UserPermission> getAllPermissionList();

    List<UserPermission> getNonSuperAdminList(String name);

    List<UserPermission> getSelfRoleList(String name);

    List<UserPermission> getSuperAdminList(String name);

    /**
     * 只返回用户有管理员权限的系统的人员权限情况
     */
    List<UserPermission> getPermissionList(String name);

    Boolean addPermission(PermissionParam permission);

    Boolean deletePermission(PermissionParam permission);

    Boolean changePermission(PermissionParam oldPermission, PermissionParam newPermission);

//    Boolean applyForPermission(PermissionParam permissionParam);

//    List<PermissionApplication> getPermissionApplications(String name);
//
//    List<PermissionApplication> getSelfPermissionApplications(String name);

//    Boolean approveApplication(Integer applicationId);
//
//    Boolean refuseApplication(Integer applicationId);
}


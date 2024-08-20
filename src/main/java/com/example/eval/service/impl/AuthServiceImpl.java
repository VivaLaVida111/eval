package com.example.eval.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson2.JSONObject;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.eval.utils.RandomUtil;
import com.example.eval.component.JwtTokenUtil;
import com.example.eval.dto.PermissionParam;
import com.example.eval.dto.UserPermission;
import com.example.eval.entity.*;
import com.example.eval.mapper.RoleMapper;
import com.example.eval.mapper.UserMapper;
import com.example.eval.mapper.UserRoleRelationMapper;
import com.example.eval.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class AuthServiceImpl implements AuthService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class);
    @Resource
    private UserDetailsService userDetailsService;
    @Resource
    private JwtTokenUtil jwtTokenUtil;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private UserMapper userMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private UserRoleRelationMapper userRoleRelationMapper;


    @Override
    public User getUserByName(String username) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("name", username);
        List<User> users = userMapper.selectList(wrapper);
        if (users != null && users.size() > 0) {
            return users.get(0);
        }
        return null;
    }

    @Override
    public User register(User userParam) {
        User user = new User();
        BeanUtils.copyProperties(userParam, user);
        user.setCreateTime(LocalDateTime.now());
        //查询是否有相同用户名的用户
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("name", user.getName());
        List<User> users = userMapper.selectList(wrapper);
        if (users != null && users.size() > 0) {
            return null;
        }
        //将密码进行加密操作
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        userMapper.insert(user);
        return user;
    }

    @Override
    public SendResult sendCode(String mobile) {
        try {
            User user = getUserByName(mobile);
            if (user == null) {
                return new SendResult(false, "该用户不存在");
            }

            String appid = "bfoew4nVKGr13QVPNbCY9DED1KAWxrlx";
            String appKey = "HOZZl0oe1YJRVoVIYXQ98xGg51zLDgRF";
            String content = RandomUtil.getFourBitRandom();
            String msg = "【金牛区综合执法局】验证码为" + content + "，请于5分钟内完成验证。";
            //String msg = URLEncoder.encode(content, "utf-8");
            String all = appid + mobile + msg + appKey;
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(all.getBytes());
            byte[] messageDigest = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String h = Integer.toHexString(0xFF & b);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            //System.out.println(hexString);
            String sign = hexString.toString();
            String url = "http://47.96.186.52:9998/http/send";
            //JSONObject params = new JSONObject();
            Map<String, Object> params = new HashMap<>();
            params.put("appid", appid);
            params.put("mobile", mobile);
            params.put("msg", msg);
            params.put("sign", sign);
            //System.out.println(params);

            HttpResponse response = HttpRequest.post(url)
                    .form(params)
                    .execute();
            //String body = HttpRequest.get(url).form(params).execute().body();
            System.out.println(response.body());
            if (JSONObject.parseObject(response.body()).get("resultCode").equals(0)) {
                user.setValidationCode(content);
                user.setValidationCodeTime(LocalDateTime.now());
                userMapper.updateById(user);
                return new SendResult(true, "发送成功");
            } else {
                return new SendResult(false, "发送失败");
            }
        } catch (Exception e) {
            LOGGER.error("发送短信验证码失败", e);
        }
        return null;
    }

    @Override
    public SMSValidationResult validateCode(String mobile, String code) {
        if (!Pattern.matches("^1[3-9]\\d{9}$", mobile)) {
            return new SMSValidationResult(false, "手机号格式错误", null);
        }
        User user = getUserByName(mobile);
        if (user == null) {
            return new SMSValidationResult(false, "该用户不存在", null);
        }
        if (user.getValidationCodeTime().plus(Duration.ofMinutes(5)).isBefore(LocalDateTime.now())) {
            return new SMSValidationResult(false, "验证码已过期", null);
        }
        Boolean isValid = user.getValidationCode().equals(code);
        if (isValid) {
            LocalDateTime time = user.getValidationCodeTime();
            String token = generateSMSToken(time);
            return new SMSValidationResult(true, "验证成功", token);
        } else {
            return new SMSValidationResult(false, "验证码错误", null);
        }
    }

    public SMSChangePwdResult changePasswordBySMS(String phone, String token, String password) {
        User user = getUserByName(phone);
        if (user == null) {
            return new SMSChangePwdResult(false, "该用户不存在");
        }
        if (user.getValidationCodeTime().plus(Duration.ofMinutes(5)).isBefore(LocalDateTime.now())) {
            return new SMSChangePwdResult(false, "验证码已过期");
        }
        String token2 = generateSMSToken(user.getValidationCodeTime());
        if (!token.equals(token2)) {
            return new SMSChangePwdResult(false, "token错误");
        }
        if (!isValidPassword(password)) {
            return new SMSChangePwdResult(false, "密码不符合规范");
        }
        String encodePassword = passwordEncoder.encode(password);
        user.setPassword(encodePassword);
        userMapper.updateById(user);
        return new SMSChangePwdResult(true, "修改成功");
    }

    private String generateSMSToken(LocalDateTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dateTimeString = time.format(formatter);
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(dateTimeString.getBytes());
            byte[] hashedBytes = messageDigest.digest();

            // Convert the byte array into a hex string
            String token = byteArrayToHexString(hashedBytes);
            return token;
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("SHA-256算法不存在", e);
        }
        return null;
    }

    private String byteArrayToHexString(byte[] bytes) {
        Formatter formatter = new Formatter();
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    @Override
    public Boolean deleteUser(String operator, String username) {
        if (!isSuperAdmin(operator)) {
            return false;
        }

        User user = getUserByName(username);
        if (user == null) {
            return false;
        }

        QueryWrapper<UserRoleRelation> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", user.getId());
        List<UserRoleRelation> relations = userRoleRelationMapper.selectList(wrapper);
        if (relations != null) {
            for (UserRoleRelation relation : relations) {
                userRoleRelationMapper.deleteById(relation);
            }
        }

        userMapper.deleteById(user);
        return true;
    }

    @Override
    public Boolean isValidPassword(String password) {
        // 检查密码长度是否至少为8位
        if (password.length() < 8) {
            return false;
        }

        // 使用正则表达式检查密码是否包含至少一个字母、一个数字和一个符号
        String regex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{6,}$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(password).matches();
    }

    @Override
    public LoginResult login(String username, String password) {
        LoginResult res = new LoginResult();
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            System.out.println("用户信息："+userDetails);
            User user = getUserByName(username);
            Integer loginStatus = user.getLoginStatus();
            Integer loginErrCount = user.getLoginErrCount();
            LocalDateTime lastLoginErrTime = user.getLastLoginErrTime();
            //用户锁定后，时间到了解锁，时间未到抛出异常信息
            if (loginStatus != null && loginStatus == 1) {
                //获取当前时间以及用户的LastLoginErrTime, 并计算是否超过30分钟
                LocalDateTime now = LocalDateTime.now();
                if (user.getLastLoginErrTime().plusMinutes(30).isAfter(now)) {
                    //throw new BadCredentialsException("当前用户登录锁定中");
                    long duration = user.getLastLoginErrTime().plusMinutes(30).until(now, ChronoUnit.MINUTES);
                    res.setMsg("当前用户登录锁定中，还需" + Math.abs(duration) + "分钟解锁");
                    return res;
                }
            }

            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                if (loginErrCount == null || loginErrCount == 0) {
                    user.setLoginErrCount(1);
                    user.setLastLoginErrTime(LocalDateTime.now());
                } else {
                    LocalDateTime now = LocalDateTime.now();
                    //两次输入错误时间间隔不超过30分钟，才对LoginErrCount进行加1
                    if (user.getLastLoginErrTime().plusMinutes(30).isAfter(now)) {
                        user.setLoginErrCount(loginErrCount + 1);
                        user.setLastLoginErrTime(now);
                        //超过6次，锁定30分钟
                        if (user.getLoginErrCount() == 6) {
                            user.setLoginStatus(1);
                        }
                        // 否则，不进行LoginErrCount加1，但更新LastLoginErrTime
                    } else {
                        user.setLastLoginErrTime(now);
                    }
                }
                userMapper.updateById(user);
                //throw new BadCredentialsException("密码不正确");
                Integer remain = 6 - user.getLoginErrCount();
                String msg = remain == 0 ? "密码错误次数已达上限，请30分钟后再试" : "用户名或密码错误，剩余" + remain + "次机会，次数用完锁定30分钟";
                res.setMsg(msg);
                return res;
            }
            //登录成功，刷新状态
            user.setLoginErrCount(0);
            user.setLoginStatus(0);
            user.setLastLoginErrTime(null);
            userMapper.updateById(user);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtTokenUtil.generateToken(userDetails);
            res.setToken(token);
            res.setIsValidPassword(isValidPassword(password));
        } catch (AuthenticationException e) {
            LOGGER.warn("登录异常:{}", e.getMessage());
        }
        return res;
    }

    @Override
    public LoginResult pswFreeLogin(String username){
        LoginResult res = new LoginResult();
        try{
            System.out.println("开始查信息");
            //检查数据库中是否存在该用户
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            System.out.println("用户信息："+userDetails);
            if(userDetails != null){
                //存在该用户则创建认证令牌并自动登录用户
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                String token = jwtTokenUtil.generateToken(userDetails);
                res.setToken(token);
                res.setIsValidPassword(true);
                System.out.println("是否验证："+res.getIsValidPassword());
            }else{
                //用户不在数据库中
                throw new BadCredentialsException("您无权访问该应用！");
            }
        }catch (AuthenticationException e) {
            LOGGER.warn("登录异常:{}", e.getMessage());
        }

        return res;
    }

    @Override
    public Boolean changePassword(String name, String password) {
        User user = getUserByName(name);
        if (isValidPassword(password)) {
            String encodePassword = passwordEncoder.encode(password);
            user.setPassword(encodePassword);
            userMapper.updateById(user);
            return true;
        }
        return false;
    }

    @Override
    public Boolean adminChangePassword(String admin, String username, String password) {
        if (isSuperAdmin(admin)) {
            User user = getUserByName(username);
            String encodePassword = passwordEncoder.encode(password);
            user.setPassword(encodePassword);
            userMapper.updateById(user);
            return true;
        }

        return false;
    }

    @Override
    public List<Role> getRoleList(Integer id) {
        QueryWrapper<UserRoleRelation> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", id);
        List<UserRoleRelation> relations = userRoleRelationMapper.selectList(wrapper);
        if (relations != null && relations.size() > 0) {
            List<Role> roleList = new ArrayList<>();
            for (UserRoleRelation relation : relations) {
                Integer roleId = relation.getRoleId();
                Role role = roleMapper.selectById(roleId);
                roleList.add(role);
            }
            return roleList;
        }
        return null;
    }

    private Boolean isSuperAdmin(String name) {
        User user = getUserByName(name);
        if (user == null) {
            return false;
        }
        List<Role> roleList = getRoleList(user.getId());
        if (roleList != null) {
            for (Role role : roleList) {
                if ("superAdmin".equals(role.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<UserPermission> getAllPermissionList() {
        List<UserPermission> res = new ArrayList<>();
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.isNotNull("id");
        List<User> users = userMapper.selectList(wrapper);
        if (users != null) {
            for (User user : users) {
                UserPermission userPermission = new UserPermission(user, getRoleList(user.getId()));
                res.add(userPermission);
            }
        }
        return res;
    }

    @Override
    public List<UserPermission> getNonSuperAdminList(String name) {
        if (!isSuperAdmin(name)) {
            return null;
        }
        List<UserPermission> res = new ArrayList<>();
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.isNotNull("id");
        List<User> users = userMapper.selectList(wrapper);
        if (users != null) {
            for (User user : users) {
                List<Role> roleList = getRoleList(user.getId());
                UserPermission userPermission = new UserPermission(user, roleList);
                res.add(userPermission);
                if (roleList != null) {
                    for (Role role : roleList) {
                        if ("superAdmin".equals(role.getName())) {
                            res.remove(res.size() - 1);
                        }
                    }
                }
            }
        }
        return res;
    }
    @Override
    public List<UserPermission> getSelfRoleList(String name){
        List<UserPermission> res = new ArrayList<>();
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("name",name);
        List<User> users = userMapper.selectList(wrapper);
        if (users != null) {
            for (User user : users) {
                List<Role> roleList = getRoleList(user.getId());
                UserPermission userPermission = new UserPermission(user, roleList);
                res.add(userPermission);
            }
        }
        return res;
    }


    @Override
    public List<UserPermission> getSuperAdminList(String name) {
        if (!isSuperAdmin(name)) {
            return null;
        }
        List<UserPermission> res = new ArrayList<>();
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.isNotNull("id");
        List<User> users = userMapper.selectList(wrapper);
        if (users != null) {
            for (User user : users) {
                List<Role> roleList = getRoleList(user.getId());

                if (roleList != null) {
                    for (Role role : roleList) {
                        if ("superAdmin".equals(role.getName())) {
                            UserPermission userPermission = new UserPermission(user, roleList);
                            res.add(userPermission);
                        }
                    }
                }
            }
        }
        return res;
    }

    @Override
    public List<UserPermission> getPermissionList(String username) {
        List<UserPermission> res = new ArrayList<>();
        User user = getUserByName(username);
        List<Role> roleList = getRoleList(user.getId());
        for (Role role : roleList) {
            if ("superAdmin".equals(role.getName())) {
                return getAllPermissionList();
            } else if ("admin".equals(role.getName())) {
                res.addAll(getPermissionBySys(role.getSystem()));
            }
        }
        return res;
    }

    private List<UserPermission> getPermissionBySys(String system) {
        List<UserPermission> res = new ArrayList<>();
        QueryWrapper<Role> roleWrapper = new QueryWrapper<>();
        roleWrapper.eq("`system`", system);
        List<Role> roleList = roleMapper.selectList(roleWrapper);
        if (roleList != null) {
            for (Role role : roleList) {
                QueryWrapper<UserRoleRelation> urrWrapper = new QueryWrapper<>();
                urrWrapper.eq("role_id", role.getId());
                List<UserRoleRelation> relations = userRoleRelationMapper.selectList(urrWrapper);
                if (relations != null) {
                    for (UserRoleRelation relation : relations) {
                        User user = userMapper.selectById(relation.getUserId());
                        List<Role> userRole = new ArrayList<>();
                        userRole.add(role);
                        UserPermission permission = new UserPermission(user, userRole);
                        res.add(permission);
                    }
                }
            }
        }
        return  res;
    }

    @Override
    public Boolean addPermission(PermissionParam permission) {
        User user = getUserByName(permission.getUserName());
        Role role = getRole(permission.getRoleSystem(), permission.getRoleName());
        if (user == null || role == null) {
            return false;
        }
        QueryWrapper<UserRoleRelation> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", user.getId())
                .eq("role_id", role.getId());
        UserRoleRelation relation = userRoleRelationMapper.selectOne(wrapper);
        if (relation != null) {
            return false;
        }
        UserRoleRelation urr = new UserRoleRelation();
        urr.setUserId(user.getId());
        urr.setRoleId(role.getId());
        userRoleRelationMapper.insert(urr);
        return true;
    }

    @Override
    public Boolean deletePermission(PermissionParam permission) {
        User user = getUserByName(permission.getUserName());
        Role role = getRole(permission.getRoleSystem(), permission.getRoleName());
        if (user == null || role == null) {
            return false;
        }
        QueryWrapper<UserRoleRelation> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", user.getId())
                .eq("role_id", role.getId());
        UserRoleRelation relation = userRoleRelationMapper.selectOne(wrapper);
        if (relation == null) {
            return false;
        }
        userRoleRelationMapper.deleteById(relation);
        return true;
    }

    @Override
    public Boolean changePermission(PermissionParam oldPermission, PermissionParam newPermission) {
        if (deletePermission(oldPermission)) {
            return addPermission(newPermission);
        }
        return false;
    }

    private Role getRole(String system, String name) {
        QueryWrapper<Role> wrapper = new QueryWrapper<>();
        wrapper.eq("`system`", system)
                .eq("name", name);
        return roleMapper.selectOne(wrapper);
    }

//    @Override
//    public Boolean applyForPermission(PermissionParam permissionParam) {
//        if (!isSuperAdmin(permissionParam.getOperator())) {
//            return false;
//        }
//        User user = getUserByName(permissionParam.getUserName());
//        PermissionApplication permissionApplication = new PermissionApplication();
//        permissionApplication.setReviewed(false);
//        permissionApplication.setOperator(permissionParam.getOperator());
//        permissionApplication.setTelephone(user.getTelephone());
//        permissionApplication.setRealName(user.getRealName());
//        permissionApplication.setOperateType(permissionParam.getOperateType());
//        permissionApplication.setRoleSystem(permissionParam.getRoleSystem());
//        permissionApplication.setRoleName(permissionParam.getRoleName());
//
//        int result = permissionApplicationMapper.insert(permissionApplication);
//        return result==1 ;
//    }
//
//    @Override
//    public List<PermissionApplication> getPermissionApplications(String name) {
//        if (!isSuperAdmin(name)) {
//            return null;
//        }
//        List<PermissionApplication> list = new ArrayList<>();
//        QueryWrapper<PermissionApplication> wrapper = new QueryWrapper<>();
//        wrapper.eq("reviewed", false)
//                .ne("operator", name);
//        return permissionApplicationMapper.selectList(wrapper);
//    }
//
//    @Override
//    public List<PermissionApplication> getSelfPermissionApplications(String name) {
//        if (!isSuperAdmin(name)) {
//            return null;
//        }
//        List<PermissionApplication> list = new ArrayList<>();
//        QueryWrapper<PermissionApplication> wrapper = new QueryWrapper<>();
//        wrapper.eq("reviewed", false)
//                .eq("operator", name);
//        return permissionApplicationMapper.selectList(wrapper);
//    }
//
//    @Override
//    public Boolean approveApplication(Integer applicationId) {
//        PermissionApplication application = permissionApplicationMapper.selectById(applicationId);
//        if (application == null) {
//            return false;
//        }
//        if (!application.getReviewed()) {
//            PermissionParam param = new PermissionParam();
//            param.setRoleName(application.getRoleName());
//            param.setRoleSystem(application.getRoleSystem());
//            param.setUserName(application.getTelephone());
//
//            String type = application.getOperateType();
//            if ("add".equals(type)) {
//                application.setReviewed(true);
//                application.setApproved(true);
//                permissionApplicationMapper.updateById(application);
//                addPermission(param);
//                return true;
//            } else if ("delete".equals(type)) {
//                application.setReviewed(true);
//                application.setApproved(true);
//                permissionApplicationMapper.updateById(application);
//                deletePermission(param);
//                return true;
//            }
//        }
//        return false;
//    }
//
//    @Override
//    public Boolean refuseApplication(Integer applicationId) {
//        PermissionApplication application = permissionApplicationMapper.selectById(applicationId);
//        if (application == null) {
//            return false;
//        }
//
//        if (!application.getReviewed()) {
//            application.setReviewed(true);
//            application.setApproved(false);
//            permissionApplicationMapper.updateById(application);
//            return true;
//        }
//        return false;
//    }

}

package com.example.eval.service.impl;

import com.example.eval.entity.User;
import com.example.eval.mapper.UserMapper;
import com.example.eval.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author luo
 * @since 2024-08-08
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}

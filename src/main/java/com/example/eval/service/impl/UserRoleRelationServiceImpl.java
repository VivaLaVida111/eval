package com.example.eval.service.impl;

import com.example.eval.entity.UserRoleRelation;
import com.example.eval.mapper.UserRoleRelationMapper;
import com.example.eval.service.IUserRoleRelationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户和角色关系表，多对多关系； 服务实现类
 * </p>
 *
 * @author luo
 * @since 2024-08-08
 */
@Service
public class UserRoleRelationServiceImpl extends ServiceImpl<UserRoleRelationMapper, UserRoleRelation> implements IUserRoleRelationService {

}

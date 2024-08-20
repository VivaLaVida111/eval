package com.example.eval.mapper;

import com.example.eval.entity.UserRoleRelation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户和角色关系表，多对多关系； Mapper 接口
 * </p>
 *
 * @author luo
 * @since 2024-08-08
 */
@Mapper
public interface UserRoleRelationMapper extends BaseMapper<UserRoleRelation> {

}

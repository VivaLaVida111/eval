package com.example.eval.service;

import com.example.eval.entity.BigRules;
import com.example.eval.entity.SmallRules;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 考评系统小项评分规则 服务类
 * </p>
 *
 * @author luo
 * @since 2024-11-04
 */
public interface ISmallRulesService extends IService<SmallRules> {
    Boolean update(SmallRules smallRules);

    Boolean add(SmallRules smallRules);

    Boolean delete(SmallRules smallRules);
}

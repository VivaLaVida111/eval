package com.example.eval.service;

import com.example.eval.entity.BigRules;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.eval.entity.DetailRules;

import java.util.List;

/**
 * <p>
 * 考评系统大项评分规则 服务类
 * </p>
 *
 * @author luo
 * @since 2024-06-14
 */
public interface IBigRulesService extends IService<BigRules> {
    List<DetailRules> getDetailRules();
}

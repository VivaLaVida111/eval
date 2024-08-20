package com.example.eval.service.impl;

import com.example.eval.entity.BigRules;
import com.example.eval.entity.DetailRules;
import com.example.eval.entity.SmallRules;
import com.example.eval.mapper.BigRulesMapper;
import com.example.eval.mapper.SmallRulesMapper;
import com.example.eval.service.IBigRulesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 考评系统大项评分规则 服务实现类
 * </p>
 *
 * @author luo
 * @since 2024-06-14
 */
@Service
public class BigRulesServiceImpl extends ServiceImpl<BigRulesMapper, BigRules> implements IBigRulesService {
    @Resource
    private BigRulesMapper bigRulesMapper;
    @Resource
    private SmallRulesMapper smallRulesMapper;

    @Override
    public List<DetailRules> getDetailRules() {
        List<BigRules> bigRulesList = bigRulesMapper.selectList(null);
        List<SmallRules> smallRulesList = smallRulesMapper.selectList(null);
        List<DetailRules> detailRulesList = new ArrayList<>();
        for (BigRules bigRules : bigRulesList) {
            DetailRules detailRules = new DetailRules();
            detailRules.setBigRules(bigRules);
            List<SmallRules> smallRules = new ArrayList<>();
            for (SmallRules smallRule : smallRulesList) {
                if (smallRule.getParentId().equals(bigRules.getId())) {
                    smallRules.add(smallRule);
                }
            }
            detailRules.setSmallRules(smallRules);
            detailRulesList.add(detailRules);
        }
        return detailRulesList;
    }
}

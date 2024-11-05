package com.example.eval.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.eval.entity.BigRules;
import com.example.eval.entity.DetailRules;
import com.example.eval.entity.SmallRules;
import com.example.eval.mapper.BigRulesMapper;
import com.example.eval.mapper.SmallRulesMapper;
import com.example.eval.service.IBigRulesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.eval.service.InfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    @Resource
    private InfoService infoService;

    private Boolean checkPermission(BigRules bigRules) {
        Map<String, String> infoMap = infoService.getInfo();
        String roles = infoMap.get("roles");
        if(bigRules.getId() == null) {
            return roles.contains("管理者");
        }
        BigRules oldBigRule;
        QueryWrapper<BigRules> bigRulesQueryWrapper = new QueryWrapper<>();
        bigRulesQueryWrapper.eq("id", bigRules.getId());
        oldBigRule = bigRulesMapper.selectOne(bigRulesQueryWrapper);
        if(oldBigRule == null) {
            return roles.contains("管理者");
        }
        return roles.contains("管理者") || roles.contains(oldBigRule.getItem());
    }

    @Override
    public Boolean add(BigRules bigRules) {
        if (checkPermission(bigRules)) {
            return bigRulesMapper.insert(bigRules) > 0;
        } else {
            return false;
        }
    }

    @Override
    public Boolean delete(BigRules bigRules) {
        if (bigRules.getId() == null) {
            return false;
        }
        if (checkPermission(bigRules)) {
            BigRules newRules = new BigRules();
            newRules.setId(bigRules.getId());
            newRules.setDeleted(true);
            return bigRulesMapper.updateById(newRules) > 0;
        } else {
            return false;
        }
    }

    @Override
    public Boolean update(BigRules bigRules) {
        if (bigRules.getId() == null) {
            return false;
        }
        if (checkPermission(bigRules)) {
            return bigRulesMapper.updateById(bigRules) > 0;
        } else {
            return false;
        }
    }

    @Override
    public List<DetailRules> getDetailRules() {
        QueryWrapper<BigRules> bigRulesQueryWrapper = new QueryWrapper<>();
        bigRulesQueryWrapper.ne("deleted", true);
        QueryWrapper<SmallRules> smallRulesQueryWrapper = new QueryWrapper<>();
        smallRulesQueryWrapper.ne("deleted", true);
        List<BigRules> bigRulesList = bigRulesMapper.selectList(bigRulesQueryWrapper);
        List<SmallRules> smallRulesList = smallRulesMapper.selectList(smallRulesQueryWrapper);
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

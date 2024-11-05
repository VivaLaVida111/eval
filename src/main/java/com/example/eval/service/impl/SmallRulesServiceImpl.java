package com.example.eval.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.eval.entity.BigRules;
import com.example.eval.entity.SmallRules;
import com.example.eval.mapper.BigRulesMapper;
import com.example.eval.mapper.SmallRulesMapper;
import com.example.eval.service.ISmallRulesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.eval.service.InfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 * 考评系统小项评分规则 服务实现类
 * </p>
 *
 * @author luo
 * @since 2024-11-04
 */
@Service
public class SmallRulesServiceImpl extends ServiceImpl<SmallRulesMapper, SmallRules> implements ISmallRulesService {
    @Resource
    private BigRulesMapper bigRulesMapper;
    @Resource
    private SmallRulesMapper smallRulesMapper;
    @Resource
    private InfoService infoService;


    private Boolean checkPermission(SmallRules smallRules) {
        if(smallRules.getParentId() == null) {
            return false;
        }
        Map<String, String> infoMap = infoService.getInfo();
        String roles = infoMap.get("roles");
//        if(smallRules.getId() == null) {
//            return roles.contains("管理者");
//        }
        BigRules oldBigRule;
        QueryWrapper<BigRules> bigRulesQueryWrapper = new QueryWrapper<>();
        bigRulesQueryWrapper.eq("id", smallRules.getParentId());
        oldBigRule = bigRulesMapper.selectOne(bigRulesQueryWrapper);
        if(oldBigRule == null) {
            return false;
        }
        return roles.contains("管理者") || roles.contains(oldBigRule.getItem());
    }

    @Override
    public Boolean add(SmallRules smallRules) {
        if (checkPermission(smallRules)) {
            return smallRulesMapper.insert(smallRules) > 0;
        } else {
            return false;
        }
    }

    @Override
    public Boolean delete(SmallRules smallRules) {
        if (smallRules.getId() == null) {
            return false;
        }
        if (checkPermission(smallRules)) {
            SmallRules newRules = new SmallRules();
            newRules.setId(smallRules.getId());
            newRules.setDeleted(true);
            return smallRulesMapper.updateById(newRules) > 0;
        } else {
            return false;
        }
    }

    @Override
    public Boolean update(SmallRules smallRules) {
        if (smallRules.getId() == null) {
            return false;
        }
        if (checkPermission(smallRules)) {
            return smallRulesMapper.updateById(smallRules) > 0;
        } else {
            return false;
        }
    }
}

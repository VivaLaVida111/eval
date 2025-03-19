package com.example.eval.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.eval.dto.UserPermission;
import com.example.eval.entity.*;
import com.example.eval.mapper.BigRulesMapper;
import com.example.eval.mapper.DetailsMapper;
import com.example.eval.mapper.SmallRulesMapper;
import com.example.eval.service.AuthService;
import com.example.eval.service.IDetailsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.eval.service.InfoService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 考评成绩明细表 服务实现类
 * </p>
 *
 * @author luo
 * @since 2024-06-14
 */
@Service
public class DetailsServiceImpl extends ServiceImpl<DetailsMapper, Details> implements IDetailsService {
    @Resource
    private DetailsMapper detailsMapper;
    @Resource
    private BigRulesMapper bigRulesMapper;
    @Resource
    private SmallRulesMapper smallRulesMapper;
    @Resource
    private AuthService authService;
    @Resource
    private InfoService infoService;

    private Boolean checkPermission(Details detail) {
        Map<String, String> infoMap = infoService.getInfo();
        String name = infoMap.get("name");
        List<UserPermission> permissions = authService.getSelfRoleList(name);
        QueryWrapper<BigRules> bigRulesQueryWrapper = new QueryWrapper<>();
        bigRulesQueryWrapper.eq("id", detail.getBigRulesId());
        BigRules bigRules = bigRulesMapper.selectOne(bigRulesQueryWrapper);
        for(UserPermission permission : permissions) {
            List<Role> roles = permission.getRoleList();
            for (Role role : roles) {
                String roleName = role.getName();
                if (roleName.equals(bigRules.getItem()) || roleName.equals("管理者")) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Boolean add(Details detail) {
        if(checkPermission(detail)) {
            QueryWrapper<BigRules> bigRulesQueryWrapper = new QueryWrapper<>();
            bigRulesQueryWrapper.eq("id", detail.getBigRulesId());
            BigRules bigRules = bigRulesMapper.selectOne(bigRulesQueryWrapper);
            detail.setSubtotal(detail.getInput() * bigRules.getPercentage() / 100);
            return detailsMapper.insert(detail) > 0;
        }
        return false;
    }

    @Override
    public Boolean delete(Details detail) {
        if(checkPermission(detail)) {
            return detailsMapper.deleteById(detail.getId()) > 0;
        }
        return false;
    }

    @Override
    public Boolean update(Details detail) {
        if(checkPermission(detail)) {
            QueryWrapper<BigRules> bigRulesQueryWrapper = new QueryWrapper<>();
            bigRulesQueryWrapper.eq("id", detail.getBigRulesId());
            BigRules bigRules = bigRulesMapper.selectOne(bigRulesQueryWrapper);
            detail.setSubtotal(detail.getInput() * bigRules.getPercentage() / 100);
            return detailsMapper.updateById(detail) > 0;
        }
        return false;
    }

    @Override
    public List<BigRulesStatistics> getBigRulesStatistics(String start, String end) {
        return detailsMapper.getBigRulesStatistics(start, end);
    }

    @Override
    public List<StreetStatistics> getStreetStatistics(String start, String end, Integer bigRuleId) {
        List<String> streetNames = new ArrayList<>(Arrays.asList(
                "金泉街道", "天回镇街道", "五块石街道", "抚琴街道",
                "西华街道", "营门口街道", "凤凰山街道", "荷花池街道",
                "九里堤街道", "沙河源街道", "驷马桥街道", "西安路街道", "茶店子街道"
        ));

        List<StreetStatistics> res;
        res = detailsMapper.getStreetStatistics(start, end, bigRuleId);
        if (res != null && res.size() == streetNames.size()) {
            return res;
        } else {
            if (res == null) {
                res = new ArrayList<>();
            }
            for (StreetStatistics s : res) {
                streetNames.remove(s.getStreet());
            }

            QueryWrapper<BigRules> bigRulesQueryWrapper = new QueryWrapper<>();
            bigRulesQueryWrapper.eq("id", bigRuleId);
            BigRules bigRules = bigRulesMapper.selectOne(bigRulesQueryWrapper);
            for (String street : streetNames) {
                StreetStatistics resItem = new StreetStatistics();
                resItem.setStreet(street);
                resItem.setItem(bigRules.getItem());
                resItem.setId(bigRuleId);
                resItem.setScore(100.0);
                res.add(resItem);
            }
        }
        return res;
    }

    /**
     * 根据街道名称查找详情
     * @param street 街道名称
     * @return 符合条件的Details列表
     */
    @Override
    public List<DetailsFront> findByStreet(String street) {
        QueryWrapper<Details> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("street", street);
        return parse2Front(detailsMapper.selectList(queryWrapper));
    }

    private DetailsFront countTotalByCondition(String start, String end, String street, List<Integer> ids) {
        // 创建查询条件构造器
        QueryWrapper<Details> queryWrapper = new QueryWrapper<>();

        // 添加时间范围条件
        queryWrapper.between("time", start, end);

        // 添加街道条件
        if (street != null && !street.isEmpty()) {
            queryWrapper.eq("street", street);
        }

        // 添加 big_rules_id IN 条件
        if (ids != null && !ids.isEmpty()) {
            queryWrapper.in("big_rules_id", ids);
        }

        // 执行查询
        List<Details> list = detailsMapper.selectList(queryWrapper);
        Double total = 0.0;
        for (Details details : list) {
            total += details.getSubtotal();
        }
        DetailsFront res = new DetailsFront();
        res.setSubtotal(total);
        res.setStreet(street);
        return res;
    }

    /**
     * 根据时间查找详情, street,roles为可选项
     * @param `time` 时间戳
     * @return 符合条件的Details列表
     */
    @Override
    public List<Details4Display> findByCondition(String start, String end, String street, String roles) {
        // 创建查询条件
        QueryWrapper<Details> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("`time`", start);
        queryWrapper.le("`time`", end);
        if (street != null && !street.equals("")) {
            queryWrapper.eq("street", street);
        }

        // 解析roles字符串
        List<Integer> bigRulesIds = new ArrayList<>();
        if (roles != null && !roles.equals("")) {
            List<String> rolesList = Arrays.asList(roles.split(","));
            // 查询BigRules数据库
            QueryWrapper<BigRules> bigRulesQueryWrapper = new QueryWrapper<>();
            bigRulesQueryWrapper.in("item", rolesList);
            List<BigRules> bigRulesList = bigRulesMapper.selectList(bigRulesQueryWrapper);
            // 获取BigRules中的id
            bigRulesIds = bigRulesList.stream().map(BigRules::getId).collect(Collectors.toList());
            // 将id添加到Details的查询条件中
            if (!bigRulesIds.isEmpty()) {
                queryWrapper.in("big_rules_id", bigRulesIds);
            }
        }
        // 先查询总计结果
//        String ids = bigRulesIds.isEmpty()? "" : bigRulesIds.stream().map(String::valueOf).collect(Collectors.joining(","));
//        DetailsFront count;
//        if (!bigRulesIds.isEmpty()) {
//            List<String> idList = Arrays.asList(ids.split(","));
//            count = detailsMapper.countTotalByCondition(start, end, street, idList);
//        }
//        count = detailsMapper.countTotalByCondition(start, end, street, null);
        DetailsFront count = countTotalByCondition(start, end, street, bigRulesIds);
        Details4Display total = new Details4Display();
        total.setStreet("总计");
        total.setSubtotal(count.getSubtotal());
        List<Details> result = detailsMapper.selectList(queryWrapper);
        List<Details4Display> details4DisplayList = parse2Display(result);
        details4DisplayList.add(total);
        return details4DisplayList;
    }

    /**
     * 根据时间查找详情， street,roles为可选项，分页查询
     * @param `time` 时间戳
     * @return 符合条件的Details列表
     */
    @Override
    public Page<DetailsFront> findPageByCondition(String start, String end, String street, String roles, int pageNum, int pageSize) {
        // 创建查询条件
        QueryWrapper<Details> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("`time`", start);
        queryWrapper.le("`time`", end);
        if (street != null && !street.equals("")) {
            queryWrapper.eq("street", street);
        }

        // 解析roles字符串
        List<Integer> bigRulesIds = new ArrayList<>();
        if (roles != null && !roles.equals("")) {
            List<String> rolesList = Arrays.asList(roles.split(","));
            // 查询BigRules数据库
            QueryWrapper<BigRules> bigRulesQueryWrapper = new QueryWrapper<>();
            bigRulesQueryWrapper.in("item", rolesList);
            List<BigRules> bigRulesList = bigRulesMapper.selectList(bigRulesQueryWrapper);
            // 获取BigRules中的id
            bigRulesIds = bigRulesList.stream().map(BigRules::getId).collect(Collectors.toList());
            // 将id添加到Details的查询条件中
            if (!bigRulesIds.isEmpty()) {
                queryWrapper.in("big_rules_id", bigRulesIds);
            }
        }
        // 先查询总计结果
//        String ids = bigRulesIds.isEmpty()? "" : bigRulesIds.stream().map(String::valueOf).collect(Collectors.joining(","));
//        DetailsFront count;
//        if (bigRulesIds != null && !bigRulesIds.isEmpty()) {
//            // 将逗号分隔的字符串转换为List
//            List<String> idList = Arrays.asList(ids.split(","));
//            count = detailsMapper.countTotalByCondition(start, end, street, idList);
//        }
//        count = detailsMapper.countTotalByCondition(start, end, street, null);
        DetailsFront count = countTotalByCondition(start, end, street, bigRulesIds);

        // 创建分页对象
        Page<Details> page = new Page<>(pageNum, pageSize);

        // 执行分页查询
        Page<Details> resultPage = detailsMapper.selectPage(page, queryWrapper);

        // 解析查询结果
        List<DetailsFront> detailsFrontList = parse2Front(resultPage.getRecords());

        if (pageNum == 1) {
            detailsFrontList.add(0, count);
        }

        // 将解析后的列表封装到新的 Page 对象中
        Page<DetailsFront> resultFrontPage = new Page<>(pageNum, pageSize, resultPage.isSearchCount());
        resultFrontPage.setTotal(resultPage.getTotal());
        resultFrontPage.setRecords(detailsFrontList);

        return resultFrontPage;
    }

    @Override
    public List<DetailsFront> findAll() {
        return parse2Front(detailsMapper.selectList(null));
    }

    @Override
    public List<EvalResult> countScore(String start, String end) {
        // 13个街道依次是：金泉街道，天回镇街道，五块石街道，抚琴街道，西华街道，营门口街道，凤凰山街道，荷花池街道，九里堤街道，沙河源街道，驷马桥街道，西安路街道，茶店子街道。
        List<String> streetNames = Arrays.asList(
                "金泉街道", "天回镇街道", "五块石街道", "抚琴街道",
                "西华街道", "营门口街道", "凤凰山街道", "荷花池街道",
                "九里堤街道", "沙河源街道", "驷马桥街道", "西安路街道", "茶店子街道"
        );

        Map<String, Integer> streets = new HashMap<>();
        for (String streetName : streetNames) {
            streets.put(streetName, 0);
        }

        List<EvalResult> evalResults = detailsMapper.countScore(start, end, null);
        for (EvalResult evalResult : evalResults) {
            streets.remove(evalResult.getStreet());
        }
        for (String street : streets.keySet()) {
            EvalResult evalResult = new EvalResult();
            evalResult.setStreet(street);
            evalResult.setScore(100.0);
            // 创建 DateTimeFormatter 对象，匹配给定的日期时间格式
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            // 将 String 转换为 LocalDateTime
            LocalDateTime startTime = LocalDateTime.parse(start + " 00:00:00", formatter);
            LocalDateTime endTime = LocalDateTime.parse(end + " 00:00:00", formatter);
            evalResult.setStartTime(startTime);
            evalResult.setEndTime(endTime);
            evalResults.add(evalResult);
        }
        return evalResults;
    }

    private List<DetailsFront> parse2Front(List<Details> detailsList) {
        List<DetailsFront> detailsFrontList = new ArrayList<>();
        for (Details details : detailsList) {
            DetailsFront detailsFront = new DetailsFront();
            //detailsFront.setId(details.getId());
            //detailsFront.setCount(details.getCount());
            detailsFront.setInput(details.getInput());
            detailsFront.setStreet(details.getStreet());
            detailsFront.setRemark(details.getRemark());
            detailsFront.setSubtotal(details.getSubtotal());
            detailsFront.setTime(details.getTime());
            QueryWrapper<BigRules> bigRulesQueryWrapper = new QueryWrapper<>();
            bigRulesQueryWrapper.eq("id", details.getBigRulesId());
            BigRules bigRules = bigRulesMapper.selectOne(bigRulesQueryWrapper);
            detailsFront.setBigRules(bigRules);
            QueryWrapper<SmallRules> smallRulesQueryWrapper = new QueryWrapper<>();
            smallRulesQueryWrapper.eq("id", details.getSmallRulesId());
            SmallRules smallRules = smallRulesMapper.selectOne(smallRulesQueryWrapper);
            detailsFront.setSmallRules(smallRules);
            detailsFrontList.add(detailsFront);
        }
        return detailsFrontList;
    }

    private List<Details4Display> parse2Display(List<Details> detailsList) {
        List<Details4Display> details4DisplayList = new ArrayList<>();
        for (Details details : detailsList) {
            Details4Display details4Display = new Details4Display();
            details4Display.setInput(details.getInput());
            details4Display.setStreet(details.getStreet());
            details4Display.setRemark(details.getRemark());
            details4Display.setSubtotal(details.getSubtotal());
            details4Display.setTime(details.getTime());
            QueryWrapper<BigRules>bigRulesQueryWrapper = new QueryWrapper<>();
            bigRulesQueryWrapper.eq("id", details.getBigRulesId());
            BigRules bigRules = bigRulesMapper.selectOne(bigRulesQueryWrapper);
            details4Display.setBigRules(bigRules.getItem());
            details4Display.setBigPercentage(bigRules.getPercentage());
            QueryWrapper<SmallRules> smallRulesQueryWrapper = new QueryWrapper<>();
            smallRulesQueryWrapper.eq("id", details.getSmallRulesId());
            SmallRules smallRules = smallRulesMapper.selectOne(smallRulesQueryWrapper);
            details4Display.setSmallRules(smallRules.getItem());
            details4DisplayList.add(details4Display);
        }
        return details4DisplayList;
    }
}

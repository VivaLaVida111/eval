package com.example.eval.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.eval.entity.*;
import com.example.eval.mapper.BigRulesMapper;
import com.example.eval.mapper.DetailsMapper;
import com.example.eval.mapper.SmallRulesMapper;
import com.example.eval.service.IDetailsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
    /**
     * 根据街道名称查找详情
     * @param street 街道名称
     * @return 符合条件的Details列表
     */
    public List<DetailsFront> findByStreet(String street) {
        QueryWrapper<Details> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("street", street);
        return parse(detailsMapper.selectList(queryWrapper));
    }

    /**
     * 根据时间查找详情
     * @param `time` 时间戳
     * @return 符合条件的Details列表
     */
    public List<DetailsFront> findByTime(String start, String end, String street) {
        QueryWrapper<Details> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("`time`", start);
        queryWrapper.le("`time`", end);
        if (street != null && !street.equals("")) {
            queryWrapper.eq("street", street);
        }
        List<DetailsFront> detailsFrontList = parse(detailsMapper.selectList(queryWrapper));
        DetailsFront total = new DetailsFront();
        total.setStreet(street);
        total.setRemark("总计");
        // subtotal为负数
        total.setSubtotal(100.0 + detailsFrontList.stream().mapToDouble(DetailsFront::getSubtotal).sum());
        detailsFrontList.add(total);

        return detailsFrontList;
    }

    public List<DetailsFront> findAll() {
        return parse(detailsMapper.selectList(null));
    }

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

    private List<DetailsFront> parse(List<Details> detailsList) {
        List<DetailsFront> detailsFrontList = new ArrayList<>();
        for (Details details : detailsList) {
            DetailsFront detailsFront = new DetailsFront();
            detailsFront.setId(details.getId());
            detailsFront.setCount(details.getCount());
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
}

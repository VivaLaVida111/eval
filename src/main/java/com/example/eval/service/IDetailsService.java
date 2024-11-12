package com.example.eval.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.eval.entity.*;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 考评成绩明细表 服务类
 * </p>
 *
 * @author luo
 * @since 2024-06-14
 */
public interface IDetailsService extends IService<Details> {
    List<DetailsFront> findAll();
    List<DetailsFront> findByStreet(String street);
    Page<DetailsFront> findByTime(String start, String end, String street, int pageNum, int pageSize);
    List<EvalResult> countScore(String start, String end);

    Boolean add(Details detail);

    Boolean delete(Details detail);

    Boolean update(Details detail);

    List<BigRulesStatistics> getBigRulesStatistics(String start, String end);

    List<StreetStatistics> getStreetStatistics(String start, String end, Integer bigRuleId);
}

package com.example.eval.mapper;

import com.example.eval.entity.*;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 考评成绩明细表 Mapper 接口
 * </p>
 *
 * @author luo
 * @since 2024-06-14
 */
@Mapper
public interface DetailsMapper extends BaseMapper<Details> {
    List<EvalResult> countScore(@Param("start") String start, @Param("end") String end, @Param("street") String street);

    List<BigRulesStatistics> getBigRulesStatistics(@Param("start") String start, @Param("end") String end);

    List<StreetStatistics> getStreetStatistics(@Param("start") String start, @Param("end") String end, @Param("id") Integer bigRuleId);


    DetailsFront countTotalByCondition(@Param("start") String start, @Param("end") String end, @Param("street") String street, @Param("ids") String bigRuleIds);

    List<SmallRuleStatistics> getSmallRuleStatistics(@Param("id") Integer bigRuleId);
//  List<SmallRuleStatistics> getSmallRuleStatistics(@Param("start")String start, @Param("end")String end, @Param("id")Integer bigRuleId);
    DetailsFront countTotalByCondition(@Param("start") String start, @Param("end") String end, @Param("street") String street, @Param("ids") List<String> ids);

}

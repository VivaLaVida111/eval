package com.example.eval.mapper;

import com.example.eval.entity.Details;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.eval.entity.EvalResult;
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
}

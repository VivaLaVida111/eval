package com.example.eval.mapper;

import com.example.eval.entity.EvalResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 评分结果记录表 Mapper 接口
 * </p>
 *
 * @author luo
 * @since 2024-06-14
 */
@Mapper
public interface EvalResultMapper extends BaseMapper<EvalResult> {

}

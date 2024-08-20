package com.example.eval.service.impl;

import com.example.eval.entity.EvalResult;
import com.example.eval.mapper.EvalResultMapper;
import com.example.eval.service.IEvalResultService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 评分结果记录表 服务实现类
 * </p>
 *
 * @author luo
 * @since 2024-06-14
 */
@Service
public class EvalResultServiceImpl extends ServiceImpl<EvalResultMapper, EvalResult> implements IEvalResultService {

}

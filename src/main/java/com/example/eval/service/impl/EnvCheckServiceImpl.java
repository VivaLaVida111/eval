package com.example.eval.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.eval.entity.EnvCheck;
import com.example.eval.mapper.EnvCheckMapper;
import com.example.eval.service.IEnvCheckService;
import org.springframework.stereotype.Service;

@Service
public class EnvCheckServiceImpl
        extends ServiceImpl<EnvCheckMapper, EnvCheck>
        implements IEnvCheckService {
}

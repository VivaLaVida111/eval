package com.example.eval.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.eval.entity.DemolitionRecord;
import com.example.eval.mapper.DemolitionRecordMapper;
import com.example.eval.service.DemolitionRecordService;
import org.springframework.stereotype.Service;

// Service 实现类
@Service
public class DemolitionRecordServiceImpl extends ServiceImpl<DemolitionRecordMapper, DemolitionRecord>
        implements DemolitionRecordService { }


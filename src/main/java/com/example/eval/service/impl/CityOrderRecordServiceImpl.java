package com.example.eval.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.eval.entity.CityOrderRecord;
import com.example.eval.mapper.CityOrderRecordMapper;
import com.example.eval.service.ICityOrderRecordService;
import org.springframework.stereotype.Service;

@Service
public class CityOrderRecordServiceImpl
        extends ServiceImpl<CityOrderRecordMapper, CityOrderRecord>
        implements ICityOrderRecordService {
}

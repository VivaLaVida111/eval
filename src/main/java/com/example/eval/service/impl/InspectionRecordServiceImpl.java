package com.example.eval.service.impl;

import com.example.eval.dto.AttributeStatDTO;
import com.example.eval.dto.ResourceStatDTO;
import com.example.eval.dto.StreetCheckStatDTO;
import com.example.eval.dto.StreetPatrolStatDTO;
import com.example.eval.entity.InspectionRecord;
import com.example.eval.mapper.InspectionRecordMapper;
import com.example.eval.service.IInspectionRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 巡查登记表 服务实现类
 * </p>
 *
 * @author luo
 * @since 2025-04-18
 */
@Service
public class InspectionRecordServiceImpl extends ServiceImpl<InspectionRecordMapper, InspectionRecord> implements IInspectionRecordService {

    /**
     * 已巡查状态的常量值
     * 用于在数据库查询中筛选已完成巡查的记录
     */
    private static final String PATROLLED_STATUS = "已巡查";
    
    /**
     * 已整改状态的常量值
     * 用于在数据库查询中筛选已完成整改的记录
     */
    private static final String REFORMED_STATUS = "已整改";

    /**
     * 统计问题来源数量
     * 
     * @param startTime 查询开始时间
     * @param endTime 查询结束时间
     * @return 按问题来源分组的统计结果DTO列表
     */
    @Override
    public List<ResourceStatDTO> countByResource(LocalDateTime startTime, LocalDateTime endTime) {
        // ServiceImpl中的baseMapper是MyBatis-Plus提供的基础Mapper，在父类中注入
        // 这里调用自定义的mapper方法，直接返回DTO对象列表
        return baseMapper.countResourceList(startTime, endTime);
    }
    
    /**
     * 统计问题属性数量
     * 
     * @param startTime 查询开始时间
     * @param endTime 查询结束时间
     * @return 按问题属性分组的统计结果DTO列表
     */
    @Override
    public List<AttributeStatDTO> countByAttribute(LocalDateTime startTime, LocalDateTime endTime) {
        // 直接调用mapper方法返回DTO对象列表
        return baseMapper.countAttributeList(startTime, endTime);
    }
    
    /**
     * 按街道分组统计巡查状态
     * 统计各街道的记录总数和已巡查的记录数
     * 
     * @param startTime 查询开始时间
     * @param endTime 查询结束时间
     * @return 按街道分组的巡查状态统计结果DTO列表
     */
    @Override
    public List<StreetPatrolStatDTO> countPatrolStatusByStreet(LocalDateTime startTime, LocalDateTime endTime) {
        // 调用mapper方法，传入时间范围和已巡查状态的常量值
        // 使用DTO对象接收结果，避免手动处理Map转换
        return baseMapper.countPatrolStatusByStreet(startTime, endTime, PATROLLED_STATUS);
    }
    
    /**
     * 按街道分组统计核查状态
     * 统计各街道的记录总数和已整改的记录数
     * 
     * @param startTime 查询开始时间
     * @param endTime 查询结束时间
     * @return 按街道分组的核查状态统计结果DTO列表
     */
    @Override
    public List<StreetCheckStatDTO> countCheckStatusByStreet(LocalDateTime startTime, LocalDateTime endTime) {
        // 调用mapper方法，传入时间范围和已整改状态的常量值
        // 使用DTO对象接收结果，避免手动处理Map转换
        return baseMapper.countCheckStatusByStreet(startTime, endTime, REFORMED_STATUS);
    }
}

package com.example.eval.service;

import com.example.eval.dto.AttributeStatDTO;
import com.example.eval.dto.ResourceStatDTO;
import com.example.eval.dto.StreetCheckStatDTO;
import com.example.eval.dto.StreetPatrolStatDTO;
import com.example.eval.entity.InspectionRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 巡查登记表 服务类
 * </p>
 *
 * @author luo
 * @since 2025-04-18
 */
public interface IInspectionRecordService extends IService<InspectionRecord> {
    /**
     * 统计问题来源数量
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 各来源的数量统计
     */
    List<ResourceStatDTO> countByResource(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 统计问题属性数量
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 各问题属性的数量统计
     */
    List<AttributeStatDTO> countByAttribute(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 按街道统计巡查状态
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 各街道的巡查统计结果
     */
    List<StreetPatrolStatDTO> countPatrolStatusByStreet(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 按街道统计核查状态
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 各街道的核查统计结果
     */
    List<StreetCheckStatDTO> countCheckStatusByStreet(LocalDateTime startTime, LocalDateTime endTime);
}

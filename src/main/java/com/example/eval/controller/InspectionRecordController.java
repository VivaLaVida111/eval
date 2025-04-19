package com.example.eval.controller;


import com.example.eval.dto.AttributeStatDTO;
import com.example.eval.dto.ResourceStatDTO;
import com.example.eval.dto.StreetCheckStatDTO;
import com.example.eval.dto.StreetPatrolStatDTO;
import com.example.eval.entity.InspectionRecord;
import com.example.eval.service.IInspectionRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 巡查登记表 前端控制器
 * </p>
 *
 * @author luo
 * @since 2025-04-18
 */
@RestController
@RequestMapping("/inspection-record")
@RequiredArgsConstructor
public class InspectionRecordController {
    /**
     * 巡查记录服务接口
     * 通过构造器注入（@RequiredArgsConstructor + final）
     */
    private final IInspectionRecordService inspectionRecordService;

    // 新增
    @PostMapping
    public Boolean create(@RequestBody InspectionRecord InspectionRecord) {
        return inspectionRecordService.save(InspectionRecord);
    }

    // 删除
    @DeleteMapping("/{id}")
    public Boolean delete(@PathVariable Long id) {
        return inspectionRecordService.removeById(id);
    }

    // 查询单个
    @GetMapping("/{id}")
    public InspectionRecord getById(@PathVariable int id) {
        return inspectionRecordService.getById(id);
    }

    // 查询全部
    @GetMapping
    public List<InspectionRecord> getAll() {
        return inspectionRecordService.list();
    }

    // 修改
    @PatchMapping
    public Boolean update(@RequestBody InspectionRecord InspectionRecord) {
        return inspectionRecordService.updateById(InspectionRecord);
    }
    
    /**
     * 统计问题来源数量
     *
     * @param startTime 开始时间，ISO格式(yyyy-MM-ddTHH:mm:ss)
     * @param endTime   结束时间，ISO格式(yyyy-MM-ddTHH:mm:ss)
     * @return 各来源的数量统计DTO对象列表
     */
    @GetMapping("/stats/resource")
    public List<ResourceStatDTO> countByResource(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime endTime) {
        return inspectionRecordService.countByResource(startTime, endTime);
    }
    
    /**
     * 统计问题属性数量
     *
     * @param startTime 开始时间，ISO格式(yyyy-MM-ddTHH:mm:ss)
     * @param endTime   结束时间，ISO格式(yyyy-MM-ddTHH:mm:ss)
     * @return 各问题属性的数量统计DTO对象列表
     */
    @GetMapping("/stats/attribute")
    public List<AttributeStatDTO> countByAttribute(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime endTime) {
        return inspectionRecordService.countByAttribute(startTime, endTime);
    }
    
    /**
     * 按街道统计巡查状态
     * 返回各街道的记录总数和已巡查记录数
     *
     * @param startTime 开始时间，ISO格式(yyyy-MM-ddTHH:mm:ss)
     * @param endTime   结束时间，ISO格式(yyyy-MM-ddTHH:mm:ss)
     * @return 各街道的巡查统计结果DTO对象列表
     */
    @GetMapping("/stats/patrol-by-street")
    public List<StreetPatrolStatDTO> countPatrolStatusByStreet(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime endTime) {
        return inspectionRecordService.countPatrolStatusByStreet(startTime, endTime);
    }
    
    /**
     * 按街道统计核查状态
     * 返回各街道的记录总数和已整改记录数
     *
     * @param startTime 开始时间，ISO格式(yyyy-MM-ddTHH:mm:ss)
     * @param endTime   结束时间，ISO格式(yyyy-MM-ddTHH:mm:ss)
     * @return 各街道的核查统计结果DTO对象列表
     */
    @GetMapping("/stats/check-by-street")
    public List<StreetCheckStatDTO> countCheckStatusByStreet(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime endTime) {
        return inspectionRecordService.countCheckStatusByStreet(startTime, endTime);
    }
}


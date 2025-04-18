package com.example.eval.controller;


import com.example.eval.entity.InspectionRecord;
import com.example.eval.service.IInspectionRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("//inspection-record")
@RequiredArgsConstructor
public class InspectionRecordController {
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
}


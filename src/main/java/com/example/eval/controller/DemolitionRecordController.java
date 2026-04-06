package com.example.eval.controller;
import com.example.eval.entity.DemolitionRecord;
import com.example.eval.service.DemolitionRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/demolition-record")
public class DemolitionRecordController {


    private final DemolitionRecordService demolitionRecordService;

    public DemolitionRecordController(DemolitionRecordService demolitionRecordService) {
        this.demolitionRecordService = demolitionRecordService;
    }

    // 新增（前端Excel上传时逐条调用）
    @PostMapping
    public boolean addDemolitionRecord(@RequestBody DemolitionRecord record) {
        return demolitionRecordService.save(record);
    }

    // 查询全部
    @GetMapping
    public List<DemolitionRecord> getAll() {
        return demolitionRecordService.list();
    }

    // 按街道查询（前端可选）
    @GetMapping("/street/{street}")
    public List<DemolitionRecord> getByStreet(@PathVariable String street) {
        return demolitionRecordService.lambdaQuery()
                .eq(DemolitionRecord::getStreet, street)
                .list();
    }
}
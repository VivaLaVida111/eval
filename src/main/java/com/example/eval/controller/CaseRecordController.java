package com.example.eval.controller;


import com.example.eval.entity.CaseRecord;
import com.example.eval.service.ICaseRecordService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * <p>
 * 案件记录表 前端控制器
 * </p>
 *
 * @author luo
 * @since 2025-04-14
 */
@RestController
@RequestMapping("//case-record")
@RequiredArgsConstructor
public class CaseRecordController {
    private final ICaseRecordService caseRecordService;
    // 新增
    @PostMapping
    public Boolean create(@RequestBody CaseRecord caseRecord) {
//        System.out.println("Street:"+caseRecord.getStreet()+ ",CaseNumber:"+caseRecord.getCaseNumber()+
//                ",Remark:"+caseRecord.getRemark()+ ",Reason:"+caseRecord.getReason());
        return caseRecordService.save(caseRecord);
    }

    // 删除
    @DeleteMapping("/{id}")
    public Boolean delete(@PathVariable Long id) {
        return caseRecordService.removeById(id);
    }

    // 查询单个
    @GetMapping("/{id}")
    public CaseRecord getById(@PathVariable int id) {
        return caseRecordService.getById(id);
    }

    // 查询全部
    @GetMapping
    public List<CaseRecord> getAll() {
        return caseRecordService.list();
    }

    // 修改
    @PatchMapping
    public Boolean update(@RequestBody CaseRecord caseRecord) {
        return caseRecordService.updateById(caseRecord);
    }
}


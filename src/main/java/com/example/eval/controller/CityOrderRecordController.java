package com.example.eval.controller;

import com.example.eval.entity.CityOrderRecord;
import com.example.eval.service.ICityOrderRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 市容秩序记录 前端控制器
 * </p>
 *
 * @author luo
 * @since 2025-04-18
 */
@RestController
@RequestMapping("/city-order-record")
@RequiredArgsConstructor
public class CityOrderRecordController {

    private final ICityOrderRecordService cityOrderRecordService;

    // 新增
    @PostMapping
    public Boolean create(@RequestBody CityOrderRecord record) {
        return cityOrderRecordService.save(record);
    }

    // 删除
    @DeleteMapping("/{id}")
    public Boolean delete(@PathVariable Long id) {
        return cityOrderRecordService.removeById(id);
    }

    // 查询单个
    @GetMapping("/{id}")
    public CityOrderRecord getById(@PathVariable Long id) {
        return cityOrderRecordService.getById(id);
    }

    // 查询全部
    @GetMapping
    public List<CityOrderRecord> getAll() {
        return cityOrderRecordService.list();
    }

    // 修改
    @PatchMapping
    public Boolean update(@RequestBody CityOrderRecord record) {
        return cityOrderRecordService.updateById(record);
    }
}

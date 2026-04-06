package com.example.eval.controller;

import com.example.eval.entity.EnvCheck;
import com.example.eval.service.IEnvCheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 环境卫生——实地检查 前端控制器
 * </p>
 *
 * @author luo
 * @since 2025-04-18
 */
@RestController
@RequestMapping("/env-check")
@RequiredArgsConstructor
public class EnvCheckController {

    private final IEnvCheckService envCheckService;

    // 新增
    @PostMapping
    public Boolean create(@RequestBody EnvCheck record) {
        return envCheckService.save(record);
    }

    // 删除
    @DeleteMapping("/{id}")
    public Boolean delete(@PathVariable Long id) {
        return envCheckService.removeById(id);
    }

    // 查询单个
    @GetMapping("/{id}")
    public EnvCheck getById(@PathVariable Long id) {
        return envCheckService.getById(id);
    }

    // 查询全部
    @GetMapping
    public List<EnvCheck> getAll() {
        return envCheckService.list();
    }

    // 修改
    @PatchMapping
    public Boolean update(@RequestBody EnvCheck record) {
        return envCheckService.updateById(record);
    }
}

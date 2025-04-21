package com.example.eval.controller;


import com.example.eval.entity.BigRules;
import com.example.eval.entity.SmallRules;
import com.example.eval.service.IBigRulesService;
import com.example.eval.service.ISmallRulesService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 考评系统小项评分规则 前端控制器
 * </p>
 *
 * @author luo
 * @since 2024-11-04
 */
@RestController
@RequestMapping("//small-rules")
public class SmallRulesController {
    @Resource
    private ISmallRulesService smallRulesService;

    @PostMapping("/add")
    public Boolean add(@RequestBody SmallRules smallRules) {
        return smallRulesService.add(smallRules);
    }

    @PostMapping("/update")
    public Boolean update(@RequestBody SmallRules smallRules) {
        return smallRulesService.update(smallRules);
    }

    @PostMapping("/delete")
    public Boolean delete(@RequestBody SmallRules smallRules) {
        return smallRulesService.delete(smallRules);
    }



}


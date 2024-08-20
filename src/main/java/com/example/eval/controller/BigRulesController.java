package com.example.eval.controller;


import com.example.eval.entity.DetailRules;
import com.example.eval.service.IBigRulesService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 考评系统大项评分规则 前端控制器
 * </p>
 *
 * @author luo
 * @since 2024-06-14
 */
@RestController
@RequestMapping("//big-rules")
public class BigRulesController {
    @Resource
    private IBigRulesService bigRulesService;

    @GetMapping("/getDetailRules")
    public List<DetailRules> getDetailRules() {
        return bigRulesService.getDetailRules();
    }

}


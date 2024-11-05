package com.example.eval.controller;


import com.example.eval.entity.BigRules;
import com.example.eval.entity.DetailRules;
import com.example.eval.entity.Details;
import com.example.eval.service.IBigRulesService;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/add")
    public Boolean add(@RequestBody BigRules bigRules) {
        return bigRulesService.add(bigRules);
    }

    @PostMapping("/update")
    public Boolean update(@RequestBody BigRules bigRules) {
        return bigRulesService.update(bigRules);
    }

    @PostMapping("/delete")
    public Boolean delete(@RequestBody BigRules bigRules) {
        return bigRulesService.delete(bigRules);
    }

}


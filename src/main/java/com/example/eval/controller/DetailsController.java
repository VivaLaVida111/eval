package com.example.eval.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.eval.entity.*;
import com.example.eval.service.IDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 考评成绩明细表 前端控制器
 * </p>
 *
 * @author luo
 * @since 2024-06-14
 */
@RestController
@RequestMapping("//details")
public class DetailsController {
    @Resource
    private IDetailsService detailsService;
    @GetMapping("")
    public List<DetailsFront> findAll() {
        return detailsService.findAll();
    }
    @GetMapping("/street/{street}")
    public List<DetailsFront> findByStreet(@PathVariable String street) {
        return detailsService.findByStreet(street);
    }
    @GetMapping("/period/{start}/{end}/{pageNum}/{pageSize}")
    public Page<DetailsFront> findByTime(@PathVariable String start, @PathVariable String end, @PathVariable Integer pageNum, @PathVariable Integer pageSize, @RequestParam(required = false, defaultValue = "") String street) {
        return detailsService.findByTime(start, end, street, pageNum, pageSize);
    }

    @GetMapping("/score/{start}/{end}")
    public List<EvalResult> countScore(@PathVariable String start, @PathVariable String end) {
        return detailsService.countScore(start, end);
    }

    @GetMapping("/bigRulesStatistics/{start}/{end}")
    public List<BigRulesStatistics> getBigRulesStatistics(@PathVariable String start, @PathVariable String end) {
        return detailsService.getBigRulesStatistics(start, end);
    }

    @GetMapping("/streetStatistics/{start}/{end}/{bigRuleId}")
    public List<StreetStatistics> getStreetStatistics(@PathVariable String start, @PathVariable String end, @PathVariable Integer bigRuleId) {
        return detailsService.getStreetStatistics(start, end, bigRuleId);
    }

    @PostMapping("/add")
    public Boolean add(@RequestBody Details detail) {
        return detailsService.add(detail);
    }

    @PostMapping("/delete")
    public Boolean delete(@RequestBody Details detail) {
        return detailsService.delete(detail);
    }

    @PostMapping("/update")
    public Boolean update(@RequestBody Details detail) {
        return detailsService.update(detail);
    }
}


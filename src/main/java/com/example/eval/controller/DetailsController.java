package com.example.eval.controller;


import com.example.eval.entity.Details;
import com.example.eval.entity.DetailsFront;
import com.example.eval.entity.EvalResult;
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
    @GetMapping("/period/{start}/{end}")
    public List<DetailsFront> findByTime(@PathVariable String start, @PathVariable String end, @RequestParam(required = false, defaultValue = "") String street) {
        return detailsService.findByTime(start, end, street);
    }

    @GetMapping("/score/{start}/{end}")
    public List<EvalResult> countScore(@PathVariable String start, @PathVariable String end) {
        return detailsService.countScore(start, end);
    }
}


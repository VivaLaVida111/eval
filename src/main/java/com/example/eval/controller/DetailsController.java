package com.example.eval.controller;


import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.URLUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.eval.entity.*;
import com.example.eval.service.IDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

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
    public Page<DetailsFront> findPageByCondition(@PathVariable String start, @PathVariable String end,
                                         @PathVariable Integer pageNum, @PathVariable Integer pageSize,
                                         @RequestParam(required = false, defaultValue = "") String street,
                                         @RequestParam(required = false, defaultValue = "") String roles) {
        return detailsService.findPageByCondition(start, end, street, roles, pageNum, pageSize);
    }

    @GetMapping("/period/excel/{start}/{end}")
    public void findByCondition(HttpServletResponse response,
                                                  @PathVariable String start, @PathVariable String end,
                                                  @RequestParam(required = false, defaultValue = "") String street,
                                                  @RequestParam(required = false, defaultValue = "") String roles) throws IOException {
        List<Details4Display> details = detailsService.findByCondition(start, end, street, roles);
        // 设置文本内省
        response.setContentType("application/vnd.ms-excel");
        // 设置字符编码
        response.setCharacterEncoding("utf-8");
        // 替换 roles 中的逗号为 "和"
        String safeRoles = roles.contains(",") ? roles.replace(",", "和") : roles;
        String name = (!"".equals(start) && start.length() > 10 ? start.substring(0, 10) : start)
                    + "至"
                    + (!"".equals(end) && end.length() > 10 ? end.substring(0, 10) : end)
                    + (!"".equals(street) ? street : "全区")
                    + (!"".equals(safeRoles) && !"viewer".equals(safeRoles) && !safeRoles.contains("管理者")  ? safeRoles : "")
                    + "体征事件.xlsx";
        String encodedFileName = URLUtil.encode(name, CharsetUtil.CHARSET_UTF_8);
        response.setHeader("content-disposition",  "attachment;filename="+encodedFileName);
        EasyExcel.write(response.getOutputStream(), Details4Display.class).sheet(name).doWrite(details);
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

    //按大规则id查询其全部小规则评分
    @GetMapping("/smallRuleStatistics/{start}/{end}/{bigRuleId}")
    public List<SmallRuleStatistics> getSmallRuleStatistics(@PathVariable String start, @PathVariable String end, @PathVariable Integer bigRuleId) {
        return detailsService.getSmallRuleStatistics(start, end, bigRuleId);
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


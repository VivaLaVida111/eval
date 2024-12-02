package com.example.eval.scheduled;

import com.example.eval.entity.BigRules;
import com.example.eval.entity.Details;
import com.example.eval.entity.SmallRules;
import com.example.eval.service.IBigRulesService;
import com.example.eval.service.IDetailsService;
import com.example.eval.service.IEvalResultService;
import com.example.eval.service.ISmallRulesService;
import org.springframework.scheduling.annotation.Scheduled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class Generator {
    @Resource
    private IDetailsService detailsService;
    @Resource
    private IBigRulesService bigRulesService;
    @Resource
    private ISmallRulesService smallRulesService;

    private static final Logger logger = LoggerFactory.getLogger(Generator.class);

    @Scheduled(fixedRate = 1000 * 60 * 60 * 24)
    public void generate() {
        logger.info("开始生成每日评分细则");
        // 13个街道依次是：金泉街道，天回镇街道，五块石街道，抚琴街道，西华街道，营门口街道，凤凰山街道，荷花池街道，九里堤街道，沙河源街道，驷马桥街道，西安路街道，茶店子街道。
        List<String> streets = new ArrayList<>(Arrays.asList("金泉街道", "天回镇街道", "五块石街道", "抚琴街道", "西华街道", "营门口街道", "凤凰山街道", "荷花池街道", "九里堤街道", "沙河源街道", "驷马桥街道", "西安路街道", "茶店子街道"));
        //List<String> streets = new ArrayList<>(Arrays.asList("街道1", "街道2", "街道3", "街道4", "街道5", "街道6", "街道7", "街道8", "街道9", "街道10", "街道11", "街道12", "街道13"));
        try{
            for (String street : streets) {
                Double randomValue = Math.random();
                if (randomValue.compareTo(0.2) < 0) {
                    Details details = new Details();
                    details.setStreet(street);
                    details.setBigRulesId(2);
                    details.setSmallRulesId(1);
                    BigRules bigRules = bigRulesService.getById(2);
                    SmallRules smallRules = smallRulesService.getById(1);
                    Double input = -2.0;
                    details.setInput(input);
                    Double subtotal = bigRules.getPercentage()  * input / 100 ;
                    details.setSubtotal(subtotal);

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDateTime now = LocalDateTime.now(); // 获取当前日期时间
                    String formattedDateTime = now.format(formatter);
                    details.setTime(now);
                    detailsService.save(details);
                }
            }
        } catch (Exception e) {
            logger.error("生成每日评分细则失败", e);
        }
    }
}

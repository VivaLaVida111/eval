package com.example.eval.scheduled;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.example.eval.entity.Details;
import com.example.eval.entity.InspectionRecord;
import com.example.eval.mapper.DetailsMapper;
import com.example.eval.mapper.InspectionRecordMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
@RequiredArgsConstructor
public class InspectionScheduler {

    private final InspectionRecordMapper inspectionRecordMapper;
    private final DetailsMapper detailsMapper;

    private String getToken(){
        String loginUrl = "https://zhzf.tfryb.com/jinniu/web-api/auth/loginNoEncryption";
        JSONObject queryParam = new JSONObject();
        queryParam.put("username","foreignuser");
        queryParam.put("password","foreign@Cd2025");
        HttpResponse loginRes = HttpRequest.post(loginUrl).body(queryParam.toJSONString()).execute();
        String token = JSONObject.parseObject(loginRes.body()).getString("token");
        return token;
    }
    //@Scheduled(cron = "*/30 * * * * ?")
    @Scheduled(cron = "0 0 6 * * ?")
    public void fetchAndStorePatrolData() {
        String token = getToken();
        System.out.println(token);
        String queryUrl = "https://zhzf.tfryb.com/jinniu/web-api/auth/resource/getSiteData";
        HttpResponse queryRes = HttpRequest.get(queryUrl)
                .header(Header.CONTENT_TYPE,"application/json")
                .header(Header.AUTHORIZATION, token)
                .execute();
        System.out.println(queryRes);
        JSONArray jsonArray = JSONArray.parseArray(queryRes.body());
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject json = jsonArray.getJSONObject(i);
            InspectionRecord record = new InspectionRecord();

            // 字段映射
            record.setStreet(json.getString("streetName"));
            record.setInspectionNumber(String.valueOf(json.getLong("serialNumber")));
            record.setDetailSite(json.getString("address"));
            record.setSiteName(json.getString("siteName"));
            record.setPatrolStatus(json.getString("patrolStatus"));
            record.setPatroller(json.getString("patrolPerson"));
            record.setResource("街道自查");
            // 时间戳转 LocalDateTime
            Long patrolTimestamp = json.getLong("patrolTime");
            if (patrolTimestamp != null) {
                record.setPatrolTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(patrolTimestamp), ZoneId.systemDefault()));
            }

            // 多张图片连接
            JSONArray patrolPics = json.getJSONArray("patrolPictures");
            if (patrolPics != null) {
                record.setPatrolPic(String.join(",", patrolPics.toJavaList(String.class)));
            }

            // 整改照片（取第一个问题的第一张整改图）
            JSONArray questions = json.getJSONArray("questions");
            if (questions != null && !questions.isEmpty()) {
                JSONObject firstQ = questions.getJSONObject(0);
                JSONArray rectifyPics = firstQ.getJSONArray("rectifyResultPictures");
                if (rectifyPics != null && !rectifyPics.isEmpty()) {
                    record.setReformPic(rectifyPics.getString(0));
                }
                record.setCheckStatus(firstQ.getString("questionStatus")); // 如“已整改”
            }
            inspectionRecordMapper.insert(record);
            System.out.println(json.getString("patrolStatus"));
            // === 插入 Details 考评记录 ===
            if ("超期".equals(json.getString("patrolStatus"))){
                System.out.println("进入循环");
                Details detail = new Details();
                detail.setStreet(json.getString("streetName"));
                detail.setResultId(record.getId());  // 使用插入后自动生成的主键ID
                detail.setBigRulesId(8);
                detail.setSmallRulesId(113);
                detail.setInput(-0.1);
                detail.setSubtotal(-0.1 * 0.05); // subtotal = input × 5%
                detail.setTime(LocalDateTime.now());
                detail.setRemark("自动生成，巡查记录已超期");
                detailsMapper.insert(detail);


            }
        }
    }

}

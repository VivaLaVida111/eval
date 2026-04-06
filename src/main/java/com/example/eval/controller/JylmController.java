package com.example.eval.controller;

import com.example.eval.common.ResponseData;
import com.example.eval.common.ResponseDataUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/jylm")
public class JylmController {

    // 暂存在内存中
    private static List<Map<String, Object>> complaintDataStore = new ArrayList<>();

    // 上传接口：适配你的 ResponseData 封装类
    @PostMapping("/complaint/upload")
    public ResponseData<String> uploadComplaint(@RequestBody List<Map<String, Object>> data) {
        complaintDataStore = data;
        return ResponseDataUtil.Success("数据已暂存于后端内存");
    }

    // 获取接口：用于页面初始化回显
    @GetMapping("/complaint/list")
    public ResponseData<List<Map<String, Object>>> getComplaintList() {
        return ResponseDataUtil.Success(complaintDataStore);
    }
}

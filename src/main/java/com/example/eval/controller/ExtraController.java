package com.example.eval.controller;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.example.eval.common.ResponseData;
import com.example.eval.common.ResponseDataUtil;
import com.example.eval.scheduled.Generator;
import com.example.eval.utils.SM4Util;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
@RestController
@RequestMapping("/extra")
@RequiredArgsConstructor
public class ExtraController {
    private static final Logger logger = LoggerFactory.getLogger(ExtraController.class);
    // 核心配置常量
    private static final String BASE_URL = "http://119.4.191.13:8880"; // 第三方系统基础地址
    private static final int HTTP_TIMEOUT = 5000; // HTTP请求超时时间（5秒）
    private static final int LOGIN_RETRY_COUNT = 1; // 单次手机号登录重试次数
    private static final String DEFAULT_PHONE = "18380195019"; // 兜底手机号

    // 街道名称与手机号的映射表（保持顺序）
    private static final Map<String, String> STREET_PHONE_MAP = new LinkedHashMap<>();
    static {
        // 初始化街道-手机号映射
        STREET_PHONE_MAP.put("抚琴街道", "13438396968");
        STREET_PHONE_MAP.put("西安路街道", "15397620795");
        STREET_PHONE_MAP.put("驷马桥街道", "18328732219");
        STREET_PHONE_MAP.put("荷花池街道", "18681271958");
        STREET_PHONE_MAP.put("五块石街道", "13541265170");
        STREET_PHONE_MAP.put("九里堤街道", "18108063373");
        STREET_PHONE_MAP.put("营门口街道", "13550302880");
        STREET_PHONE_MAP.put("茶店子街道", "13678113762");
        STREET_PHONE_MAP.put("金泉街道", "13551024402");
        STREET_PHONE_MAP.put("沙河源街道", "15208211434");
        STREET_PHONE_MAP.put("天回镇街道", "15388166797");
        STREET_PHONE_MAP.put("西华街道", "13658034061");
        STREET_PHONE_MAP.put("凤凰山街道", "18008061270");
    }
    /**
     * 网络理政 - 街道体征统计代理接口
     *
     * 原始接口：
     * https://119.4.191.13:9580/prod-api/extra/streetDailyStatics
     *
     * 返回格式：
     * {
     *   "code": "RES.SUCCESS",
     *   "message": "ok",
     *   "data": [ ...数组... ]
     * }
     */
    @GetMapping("/streetDailyStatics")
    public ResponseData<JSONArray> getStreetDailyStatics(
            @RequestParam(required = false) String street
    ) {

        // 外部接口 URL
        String url = "https://119.4.191.13:9580/prod-api/extra/streetDailyStatics";
        if (street != null && !street.isEmpty()) {
            url += "?street=" + street;
        }

        try {
            // 发送 GET 请求
            HttpResponse resp = HttpRequest.get(url)
                    .timeout(8000)   // 超时 8 秒
                    .execute();

            // 输出原始响应日志
            System.out.println("🌐 外部接口响应状态：" + resp.getStatus());
            System.out.println("🌐 外部接口原始返回：" + resp.body());

            // 解析 JSON
            JSONObject jsonObj = JSON.parseObject(resp.body());

            // 必须取 JSONArray，因为 data 是数组
            JSONArray dataArr = jsonObj.getJSONArray("data");

            return ResponseDataUtil.Success(dataArr);

        } catch (Exception e) {
            System.err.println("❌ 调用外部接口失败：" + e.getMessage());
            return ResponseDataUtil.Error("外部接口调用失败：" + e.getMessage());
        }
    }

    /**
     * 临街店铺管家 —— 招牌审批量
     * 代理请求：
     *   GET https://119.4.191.13:6580/sign/GetDoorData?token=jinniuqu&month=yyyy-MM
     *
     * @param month 月份（格式：yyyy-MM，如 2025-10）
     */
    @GetMapping("/signDoorData")
    public ResponseData<List<JSONObject>> getSignDoorData(
            @RequestParam String month
    ) {
        String queryUrl = "https://119.4.191.13:6580/sign/GetDoorData"
                + "?token=jinniuqu&month=" + month;
        System.out.println("➡ 请求临街店铺管家：{}"+ queryUrl);

        HttpResponse res = HttpRequest.get(queryUrl).execute();
        System.out.println("⬅ 外部接口状态：{}"+res.getStatus());
        System.out.println("⬅ 外部接口返回原文：{}"+ res.body());

        JSONObject root = JSON.parseObject(res.body());
        // 文档：code=2000 成功，数据在 list 字段
        if (root == null || !"2000".equals(root.getString("code"))) {
            return ResponseDataUtil.Error("外部接口返回异常: " + (root != null ? root.getString("code") : "null"));
        }

        JSONArray listArr = root.getJSONArray("list");
        List<JSONObject> list = new ArrayList<>();
        if (listArr != null) {
            list = listArr.toJavaList(JSONObject.class);
        }
        return ResponseDataUtil.Success(list);
    }

    /**
     * 市容秩序 - 外部巡查数据代理接口
     *
     * 第一步：登录获取 token
     *   POST https://zhzf.tfryb.com/jinniu/web-api/auth/loginNoEncryption
     *   Body: {"username": "foreignuser", "password": "foreign@Cd2025"}
     *
     * 第二步：带 token 请求数据
     *   GET  https://zhzf.tfryb.com/jinniu/web-api/api/develop/check/checkData
     *
     * 统一返回外部接口的 JSON（整个对象），前端自行拆分 signboard / shop 等字段
     */
    @GetMapping("/cityOrderCheckData")
    public ResponseData<JSONObject> getCityOrderCheckData() {
        try {
            // ================== 1. 登录获取 token ==================
            String loginUrl = "https://zhzf.tfryb.com/jinniu/web-api/auth/loginNoEncryption";

            JSONObject loginBody = new JSONObject();
            loginBody.put("username", "foreignuser");
            loginBody.put("password", "foreign@Cd2025");

            HttpResponse loginResp = HttpRequest.post(loginUrl)
                    .body(loginBody.toJSONString())
                    .header("Content-Type", "application/json")
                    .timeout(8000)
                    .execute();

            System.out.println("🌐 [市容秩序] 登录响应状态：" + loginResp.getStatus());
            System.out.println("🌐 [市容秩序] 登录返回原文：" + loginResp.body());

            if (loginResp.getStatus() != 200) {
                return ResponseDataUtil.Error("登录外部市容秩序系统失败，HTTP 状态码：" + loginResp.getStatus());
            }

            // 注意：接口示例里看着像是字符串形式的 JSON，这里统一按 JSON 解析
            JSONObject loginJson = JSON.parseObject(loginResp.body());
            String token = loginJson.getString("token");
            if (token == null || token.isEmpty()) {
                return ResponseDataUtil.Error("登录外部系统失败，未获取到 token");
            }

            // ================== 2. 带 token 请求数据 ==================
            String dataUrl = "https://zhzf.tfryb.com/jinniu/web-api/api/develop/check/checkData";

            HttpResponse dataResp = HttpRequest.get(dataUrl)
                    .header("Authorization", token)
                    .execute();

            System.out.println("🌐 [市容秩序] 数据响应状态：" + dataResp.getStatus());
            System.out.println("🌐 [市容秩序] 数据返回原文：" + dataResp.body());

            if (dataResp.getStatus() != 200) {
                return ResponseDataUtil.Error("获取市容秩序数据失败，HTTP 状态码：" + dataResp.getStatus());
            }

            JSONObject dataJson = JSON.parseObject(dataResp.body());
            // 外部示例：
            // {
            //   "code": 200,
            //   "data": { "signboard": [...], "shop": [...] },
            //   "message": "查询成功"
            // }
            // 这里直接把整个对象透传给前端
            return ResponseDataUtil.Success(dataJson);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDataUtil.Error("调用市容秩序外部接口异常：" + e.getMessage());
        }
    }
    @GetMapping("/sootTzData")
    public ResponseData<List<JSONObject>> getSootTzData(
            @RequestParam String month   // 例如 "2025-10"
    ) {
        // 外部接口地址
        String queryUrl = "https://119.4.191.13:4585/external/GetSootTzData"
                + "?token=jinniuqu&month=" + month;

        System.out.println("➡ 请求餐饮油烟体征接口: " + queryUrl);

        try {
            HttpResponse res = HttpRequest.get(queryUrl)
                    .timeout(16000)
                    .execute();

            System.out.println("⬅ 外部接口状态: " + res.getStatus());
            System.out.println("⬅ 外部接口返回原文: " + res.body());

            JSONObject root = JSON.parseObject(res.body());
            if (root == null || root.getIntValue("code") != 2000) {
                return ResponseDataUtil.Error(
                        "外部接口返回异常: " + (root != null ? root.getString("code") : "null")
                );
            }

            JSONArray listArr = root.getJSONArray("list");
            List<JSONObject> list = new ArrayList<>();
            if (listArr != null) {
                list = listArr.toJavaList(JSONObject.class);
            }

            return ResponseDataUtil.Success(list);
        } catch (Exception e) {
            System.err.println("❌ 调用餐饮油烟体征接口失败: " + e.getMessage());
            return ResponseDataUtil.Error("外部接口调用失败：" + e.getMessage());
        }
    }
    /**
     * 共享单车 - 街道体征数据代理接口
     *
     * 外部接口：
     *   GET https://119.4.191.13:9510/external/GetBikeTzData?token=jinniuqu&day=yyyy-MM-dd
     *
     * 外部返回：
     *   { "code": 2000, "list": [ { area_name, a_1, a_2, b_1, b_2, c_1, c_2, d_1, d_2, e_1, e_2, all }, ... ] }
     *
     * 对前端统一返回：
     *   ResponseData<List<JSONObject>>  // data 字段里是 list 数组
     */
    @GetMapping("/bikeTzData")
    public ResponseData<List<JSONObject>> getBikeTzData(
            @RequestParam String day   // 例如 "2025-11-17"
    ) {
        String queryUrl = "https://119.4.191.13:9510/external/GetBikeTzData"
                + "?token=jinniuqu&day=" + day;

        System.out.println("➡ 请求共享单车体征接口: " + queryUrl);

        try {
            HttpResponse res = HttpRequest.get(queryUrl)
                    .timeout(16000)
                    .execute();

            System.out.println("⬅ 外部接口状态: " + res.getStatus());
            System.out.println("⬅ 外部接口返回原文: " + res.body());

            JSONObject root = JSON.parseObject(res.body());
            // ✅ 外部：code == 2000 且数据在 list 字段
            if (root == null || root.getIntValue("code") != 2000) {
                return ResponseDataUtil.Error(
                        "外部接口返回异常: " + (root != null ? root.getString("code") : "null")
                );
            }

            JSONArray listArr = root.getJSONArray("list");
            List<JSONObject> list = new ArrayList<>();
            if (listArr != null) {
                list = listArr.toJavaList(JSONObject.class);
            }

            // ✅ 对前端：统一 ResponseData 格式，数据在 data 字段
            return ResponseDataUtil.Success(list);

        } catch (Exception e) {
            System.err.println("❌ 调用共享单车体征接口失败: " + e.getMessage());
            return ResponseDataUtil.Error("外部接口调用失败：" + e.getMessage());
        }
    }

    private String getHwzyToken() {
        HashMap<String, Object> loginParam = new HashMap<>();
        loginParam.put("username", "18113020713");
        loginParam.put("password", "Think@007");

        String loginUrl = "https://119.4.191.13:3581/home/users/get_access_token";
        HttpResponse loginRes = HttpRequest.post(loginUrl)
                .header(Header.USER_AGENT, "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.0.0 Safari/537.36")
                .header(Header.ACCEPT, "application/json, text/javascript")
                .header(Header.CONTENT_TYPE, "application/x-www-form-urlencoded; charset=UTF-8")
                .header(Header.CONTENT_LENGTH, "225")
                .form(loginParam)
                .execute();

        String hwzyToken = "";
        if (loginRes != null && loginRes.body() != null) {
            com.alibaba.fastjson2.JSONObject jsonObject = com.alibaba.fastjson2.JSONObject.parseObject(loginRes.body());
            if (jsonObject != null && jsonObject.getJSONObject("data") != null) {
                hwzyToken = jsonObject.getJSONObject("data").getString("access_token");
            }
        }

        return hwzyToken;
    }


    /**
     * 核心接口：批量获取所有街道的钉钉综合信息（失败自动降级兜底手机号）
     * 无需传参，自动遍历所有街道，单街道失败则用兜底手机号重试
     * @return 标准化响应（key=街道名，value=该街道的完整数据字典）
     */
    @GetMapping("/mainInfo")
    public ResponseData<JSONObject> getMainInfo() {
        // 最终返回结果：key=街道名，value=该街道的完整数据字典
        JSONObject finalResult = new JSONObject();
        logger.info("开始批量获取所有街道的钉钉综合信息，共{}个街道，兜底手机号：{}",
                STREET_PHONE_MAP.size(), DEFAULT_PHONE);

        // 遍历所有街道-手机号映射，逐个处理
        for (Map.Entry<String, String> entry : STREET_PHONE_MAP.entrySet()) {
            String streetName = entry.getKey(); // 街道名称
            String originalPhone = entry.getValue(); // 街道原始手机号
            logger.info("开始处理街道：{}，原始手机号：{}", streetName, originalPhone);

            // 初始化街道数据字典（默认失败状态）
            JSONObject streetData = initStreetData(streetName);

            try {
                // 步骤1：先用原始手机号尝试获取数据
                boolean isOriginalSuccess = fetchStreetData(streetName, originalPhone, streetData);
                if (isOriginalSuccess) {
                    logger.info("街道{}（原始手机号{}）数据获取成功", streetName, originalPhone);
                    finalResult.put(streetName, streetData);
                    continue;
                }

                // 步骤2：原始手机号失败，使用兜底手机号重试
                logger.warn("街道{}（原始手机号{}）获取失败，尝试兜底手机号{}重试",
                        streetName, originalPhone, DEFAULT_PHONE);
                boolean isDefaultSuccess = fetchStreetData(streetName, DEFAULT_PHONE, streetData);
                if (isDefaultSuccess) {
                    streetData.put("status", "兜底成功"); // 标记兜底成功
                    streetData.put("usedPhone", DEFAULT_PHONE); // 记录实际使用的手机号
                    logger.info("街道{}（兜底手机号{}）数据获取成功", streetName, DEFAULT_PHONE);
                } else {
                    streetData.put("status", "最终失败"); // 标记最终失败
                    logger.error("街道{}原始手机号+兜底手机号均获取失败", streetName);
                }

            } catch (Exception e) {
                logger.error("街道{}处理异常", streetName, e);
                streetData.put("status", "处理异常");
            }

            // 将当前街道数据存入最终结果
            finalResult.put(streetName, streetData);
        }

        logger.info("所有街道数据处理完成，共{}个街道", finalResult.size());
        return ResponseDataUtil.Success(finalResult);
    }

    /**
     * 初始化街道数据字典（默认值）
     * @param streetName 街道名称
     * @return 初始化后的JSON对象
     */
    private JSONObject initStreetData(String streetName) {
        JSONObject streetData = new JSONObject();
        streetData.put("streetName", streetName);       // 街道名称（核心标识）
        streetData.put("originalPhone", STREET_PHONE_MAP.get(streetName)); // 原始手机号
        streetData.put("usedPhone", "");               // 实际使用的手机号（原始/兜底）
        streetData.put("status", "初始状态");           // 状态：成功/兜底成功/最终失败/处理异常
        streetData.put("今日打卡率", "暂无数据");        // 打卡率
        streetData.put("今日在岗人数", 0);              // 在岗人数
        streetData.put("今日定位异常人数", 0);          // 定位异常人数
        streetData.put("本月事件办理数", 0);            // 事件办理数
        streetData.put("严管街达标数量", 0);            // 严管街达标数
        return streetData;
    }

    /**
     * 通用方法：根据手机号获取街道数据（登录+接口调用）
     * @param streetName 街道名称（仅日志用）
     * @param phone 要使用的手机号（原始/兜底）
     * @param streetData 要填充的街道数据字典
     * @return true=获取成功，false=获取失败
     */
    private boolean fetchStreetData(String streetName, String phone, JSONObject streetData) {
        // 步骤1：登录获取Token
        String authToken = loginAndGetToken(streetName, phone);
        if (authToken == null) {
            logger.warn("街道{}手机号{}登录失败", streetName, phone);
            return false;
        }

        // 步骤2：调用第三方接口获取业务数据
        JSONObject statResponse = executePostRequest(BASE_URL + "/door/stat", null, authToken);
        if (statResponse == null) {
            logger.warn("街道{}手机号{}调用/door/stat接口失败", streetName, phone);
            return false;
        }

        // 步骤3：解析并填充数据
        JSONObject statData = statResponse.getJSONObject("data");
        if (statData == null) {
            logger.warn("街道{}手机号{}接口响应无data字段", streetName, phone);
            return false;
        }

        // 安全解析各字段（空值/类型异常兜底）
        float checkRate = statData.containsKey("checkRate") && statData.get("checkRate") instanceof Number
                ? statData.getFloatValue("checkRate") : 0.0f;
        int onWork = statData.getInteger("onWork") == null ? 0 : statData.getInteger("onWork");
        int abnormal = statData.getInteger("abnormal") == null ? 0 : statData.getInteger("abnormal");
        int problemNum = statData.getInteger("problemNum") == null ? 0 : statData.getInteger("problemNum");
        int qualifiedRegionNum = statData.getInteger("qualifiedRegionNum") == null ? 0 : statData.getInteger("qualifiedRegionNum");

        // 填充数据字典
        streetData.put("status", "成功");
        streetData.put("usedPhone", phone);
        streetData.put("今日打卡率", (int) (checkRate * 100) + " %");
        streetData.put("今日在岗人数", onWork);
        streetData.put("今日定位异常人数", abnormal);
        streetData.put("本月事件办理数", problemNum);
        streetData.put("严管街达标数量", qualifiedRegionNum);
        return true;
    }

    /**
     * 登录第三方系统并获取Token（支持重试）
     * @param streetName 街道名称（仅日志用）
     * @param phone 登录手机号
     * @return 认证Token（失败返回null）
     */
    private String loginAndGetToken(String streetName, String phone) {
        String loginUrl = BASE_URL + "/auth/login_csgj";
        JSONObject loginParam = new JSONObject();
        loginParam.put("phone", phone);

        // 重试机制：失败后重试指定次数
        for (int i = 0; i < LOGIN_RETRY_COUNT; i++) {
            try (HttpResponse loginResponse = HttpRequest.post(loginUrl)
                    .body(loginParam.toJSONString())
                    .timeout(HTTP_TIMEOUT)
                    .execute()) {

                // 检查响应状态和Token有效性
                if (loginResponse.isOk() && loginResponse.body() != null) {
                    JSONObject responseBody = JSON.parseObject(loginResponse.body());
                    JSONObject dataObj = responseBody.getJSONObject("data");
                    if (dataObj != null && dataObj.getString("token") != null) {
                        logger.info("街道{}手机号{}登录成功（重试{}次）", streetName, phone, i);
                        return dataObj.getString("token");
                    }
                }
                logger.warn("街道{}手机号{}登录重试{}次失败，状态码：{}",
                        streetName, phone, i + 1, loginResponse.getStatus());
            } catch (Exception e) {
                logger.error("街道{}手机号{}登录重试{}次异常", streetName, phone, i + 1, e);
            }
        }
        return null;
    }

    /**
     * 执行POST请求（统一封装HTTP调用逻辑）
     * @param url 请求地址
     * @param requestBody 请求体（可为null）
     * @param token 认证Token
     * @return 响应JSON对象（失败返回null）
     */
    private JSONObject executePostRequest(String url, JSONObject requestBody, String token) {
        try (HttpResponse response = HttpRequest.post(url)
                .header(Header.AUTHORIZATION, "Bearer " + token) // 正确拼接Bearer+空格
                .header(Header.CONTENT_TYPE, "application/json")
                .body(requestBody != null ? requestBody.toJSONString() : "")
                .timeout(HTTP_TIMEOUT)
                .execute()) {

            if (response.isOk() && response.body() != null) {
                return JSON.parseObject(response.body());
            } else {
                logger.warn("POST请求失败，URL：{}，状态码：{}", url, response.getStatus());
            }
        } catch (Exception e) {
            logger.error("POST请求执行异常，URL：{}", url, e);
        }
        return null;
    }


    // 内存缓存：key = "YYYYMMdd"（如 "20260124"），value = 分数结果 JSONObject
    private static final ConcurrentHashMap<String, JSONObject> CACHE = new ConcurrentHashMap<>();
    private static volatile String lastCacheDate = "";

    @GetMapping("/sjtj")
    public ResponseData<JSONObject> getScGw24SjtjData() {
        String today = LocalDate.now(ZoneId.of("Asia/Shanghai"))
                .format(DateTimeFormatter.BASIC_ISO_DATE); // e.g. "20260124"

        // 自动清理非今天的缓存（线程安全）
        if (!today.equals(lastCacheDate)) {
            synchronized (CACHE) {
                if (!today.equals(lastCacheDate)) {
                    CACHE.clear();
                    lastCacheDate = today;
                }
            }
        }

        // 尝试命中缓存
        JSONObject cached = CACHE.get(today);
        if (cached != null) {
            System.out.println("✅ 命中本地缓存: " + today);
            return ResponseDataUtil.Success(cached);
        }

        // 获取新鲜数据
        JSONObject freshData = fetchFreshSjtjData();
        if (freshData == null) {
            return ResponseDataUtil.Error("获取或处理城管事件统计数据失败");
        }

        CACHE.put(today, freshData);
        System.out.println("💾 已缓存今日数据: " + today);
        return ResponseDataUtil.Success(freshData);
    }

    // 抽离原始业务逻辑，返回 JSONObject 或 null
    private JSONObject fetchFreshSjtjData() {
        String targetUrl = "https://api.cdmbc.cn:4433/gateway/api/1/scgw/24/sjtj".trim();
        System.out.println("➡ 请求城管24事件统计基础表接口: " + targetUrl);

        String decryptKey = "4d28460097cd11c18da53d85ed4788a8";

        try {
            Map<String, Object> queryParams = new HashMap<>();
            queryParams.put("authUserName", "ythpt");
            queryParams.put("authUserKey", "3a13ab1753a343039124204fb1f3ec40");

            String fullUrl = targetUrl + "?" +
                    queryParams.entrySet().stream()
                            .map(e -> {
                                try {
                                    return e.getKey() + "=" + java.net.URLEncoder.encode(String.valueOf(e.getValue()), "UTF-8");
                                } catch (UnsupportedEncodingException ex) {
                                    throw new RuntimeException(ex);
                                }
                            })
                            .collect(java.util.stream.Collectors.joining("&"));

            HttpResponse res = HttpRequest.post(fullUrl)
                    .header("Appkey", "962350900469301248")
                    .header("districtKey", "8e8cc8f7cec0d501263c36a1851b5f29")
                    .header("Content-Type", "application/json")
                    .body("{}")
                    .timeout(16000)
                    .execute();

            System.out.println("⬅ 外部接口状态: " + res.getStatus());
            String cipherText = res.body();
            System.out.println("⬅ 外部接口返回密文长度: " + (cipherText != null ? cipherText.length() : 0));

            if (cipherText == null || cipherText.trim().isEmpty()) {
                System.err.println("❌ 外部接口返回密文为空");
                return null;
            }

            String plainText;
            try {
                plainText = SM4Util.decryptEcb(decryptKey, cipherText);
                System.out.println("⬅ SM4解密后明文长度: " + plainText.length());
                System.out.println("📄 SM4解密后明文内容: " + plainText);
            } catch (Exception decryptEx) {
                System.err.println("❌ SM4解密失败: " + decryptEx.getMessage());
                return null;
            }

            JSONObject root = JSON.parseObject(plainText);
            if (root == null || !"200".equals(root.getString("code")) || !"调用成功".equals(root.getString("message"))) {
                System.err.println("❌ 外部接口返回业务异常: " + root.toJSONString());
                return null;
            }

            JSONObject dataObj = root.getJSONObject("data");
            if (dataObj == null) {
                System.err.println("❌ data 字段缺失");
                return null;
            }

            JSONArray recordsArr = dataObj.getJSONArray("records");
            if (recordsArr == null || recordsArr.isEmpty()) {
                System.out.println("⚠ records 为空，返回空街道分数");
                return new JSONObject();
            }

            ZoneId chinaZone = ZoneId.of("Asia/Shanghai");
            LocalDate today = LocalDate.now(chinaZone);
            Map<String, StreetStats> streetStatsMap = new HashMap<>();

            // 遍历外层 records
            for (int i = 0; i < recordsArr.size(); i++) {
                JSONObject record = recordsArr.getJSONObject(i);
                if (record == null) continue;

                // 获取内层 list 数组
                JSONArray listArr = record.getJSONArray("list");
                if (listArr == null || listArr.isEmpty()) {
                    // 调试：看看是不是这里断掉了
                    System.out.println("第 " + i + " 个 record 的 list 为空");
                    continue;
                }

                // 遍历内层 list 中的实际记录
                for (int j = 0; j < listArr.size(); j++) {
                    JSONObject item = listArr.getJSONObject(j);
                    if (item == null) continue;

                    // 【移除时间过滤逻辑】

                    // 从内层 item 取街道名
                    String streetName = item.getString("streetName");
                    if (streetName == null || streetName.trim().isEmpty()) {
                        streetName = "未知街道";
                    }

                    // 累加统计数据
                    StreetStats stats = streetStatsMap.computeIfAbsent(streetName, k -> new StreetStats());
                    stats.totalDisposalCompleted += item.getLongValue("disposalCompleted");
                    stats.totalDisposalRequired += item.getLongValue("disposalRequired");
                    stats.totalTimelyDisposal += item.getLongValue("timelyDisposal");
                    stats.totalRework += item.getLongValue("rework");
                    stats.totalCaseClosedRequired += item.getLongValue("caseCloseRequired");
                    stats.totalOverdueDisposal += item.getLongValue("overdueDisposal");
                    stats.totalDeferred += item.getLongValue("deferred");
                }
            }

            // ====== 计算每个街道分数 ======
            JSONObject result = new JSONObject();
            for (Map.Entry<String, StreetStats> entry : streetStatsMap.entrySet()) {
                String streetName = entry.getKey();
                StreetStats s = entry.getValue();

                double dailyDisposalScore = s.totalDisposalRequired > 0 ?
                        (double) s.totalDisposalCompleted / s.totalDisposalRequired * 30 : 0.0;

                double onTimeDisposalScore = s.totalDisposalRequired > 0 ?
                        (double) s.totalTimelyDisposal / s.totalDisposalRequired * 20 : 0.0;

                double reworkScore = s.totalCaseClosedRequired > 0 ?
                        Math.max(0.0, (1 - (double) s.totalRework / s.totalCaseClosedRequired) * 20) : 0.0;

                double overdueDisposalScore = s.totalDisposalRequired > 0 ?
                        Math.max(0.0, (1 - (double) s.totalOverdueDisposal / s.totalDisposalRequired) * 20) : 0.0;

                double deferredScore = s.totalDisposalRequired > 0 ?
                        Math.max(0.0, (1 - (double) s.totalDeferred / s.totalDisposalRequired) * 10) : 0.0;

                JSONObject scoreObj = new JSONObject();
                scoreObj.put("日常处置率得分", String.format("%.2f", dailyDisposalScore));
                scoreObj.put("按期处置率得分", String.format("%.2f", onTimeDisposalScore));
                scoreObj.put("返工率得分", String.format("%.2f", reworkScore));
                scoreObj.put("超期待处置率得分", String.format("%.2f", overdueDisposalScore));
                scoreObj.put("延期率得分", String.format("%.2f", deferredScore));

                result.put(streetName, scoreObj);
            }

            return result;

        } catch (Exception e) {
            System.err.println("❌ 调用城管24事件统计接口失败: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // 内部辅助类
    private static class StreetStats {
        long totalDisposalCompleted = 0;
        long totalDisposalRequired = 0;
        long totalTimelyDisposal = 0;
        long totalRework = 0;
        long totalCaseClosedRequired = 0;
        long totalOverdueDisposal = 0;
        long totalDeferred = 0;
    }
}

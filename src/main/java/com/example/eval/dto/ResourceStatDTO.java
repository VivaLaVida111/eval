package com.example.eval.dto;

import lombok.Data;

/**
 * 问题来源统计数据传输对象
 */
@Data
public class ResourceStatDTO {
    /**
     * 问题来源
     */
    private String resource;
    
    /**
     * 该来源的记录数量
     */
    private Long count;
} 
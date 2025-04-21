package com.example.eval.dto;

import lombok.Data;

/**
 * 街道核查状态统计数据传输对象
 */
@Data
public class StreetCheckStatDTO {
    /**
     * 街道名称
     */
    private String street;
    
    /**
     * 该街道的记录总数
     */
    private Long total;
    
    /**
     * 该街道中已整改的记录数
     */
    private Long reformedCount;
} 
package com.example.eval.dto;

import lombok.Data;

/**
 * 问题属性统计数据传输对象
 */
@Data
public class AttributeStatDTO {
    /**
     * 问题属性
     */
    private String attribute;
    
    /**
     * 该属性的记录数量
     */
    private Long count;
} 
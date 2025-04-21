package com.example.eval.dto;

import lombok.Data;

/**
 * 街道巡查状态统计数据传输对象
 */
@Data
public class StreetPatrolStatDTO {
    /**
     * 街道名称
     */
    private String street;
    
    /**
     * 该街道的记录总数
     */
    private Long total;
    
    /**
     * 该街道中已巡查的记录数
     */
    private Long patrolledCount;
} 
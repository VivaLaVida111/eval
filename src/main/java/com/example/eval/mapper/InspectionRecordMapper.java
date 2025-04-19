package com.example.eval.mapper;

import com.example.eval.dto.AttributeStatDTO;
import com.example.eval.dto.ResourceStatDTO;
import com.example.eval.dto.StreetCheckStatDTO;
import com.example.eval.dto.StreetPatrolStatDTO;
import com.example.eval.entity.InspectionRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.ResultMap;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 巡查登记表 Mapper 接口
 * </p>
 *
 * @author luo
 * @since 2025-04-18
 */
@Mapper
public interface InspectionRecordMapper extends BaseMapper<InspectionRecord> {
    
    /**
     * 按问题来源分组统计数量
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 各来源的数量统计，返回DTO对象列表
     */
    @Select("<script>" +
            "SELECT resource, COUNT(*) as count FROM inspection_record " +
            "WHERE 1=1 " +
            "<if test='startTime != null'> AND patrol_time &gt;= #{startTime} </if>" +
            "<if test='endTime != null'> AND patrol_time &lt;= #{endTime} </if>" +
            "GROUP BY resource" +
            "</script>")
    List<ResourceStatDTO> countResourceList(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
    
    /**
     * 按问题属性分组统计数量
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 各问题属性的数量统计，返回DTO对象列表
     */
    @Select("<script>" +
            "SELECT attribute, COUNT(*) as count FROM inspection_record " +
            "WHERE 1=1 " +
            "<if test='startTime != null'> AND patrol_time &gt;= #{startTime} </if>" +
            "<if test='endTime != null'> AND patrol_time &lt;= #{endTime} </if>" +
            "GROUP BY attribute" +
            "</script>")
    List<AttributeStatDTO> countAttributeList(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
    
    /**
     * 按街道分组统计巡查状态
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param patrolledStatus 已巡查状态的值
     * @return 各街道的统计结果，返回DTO对象列表
     */
    @Select("<script>" +
            "SELECT " +
            "  street, " +
            "  COUNT(*) as total, " +
            "  SUM(CASE WHEN patrol_status = #{patrolledStatus} THEN 1 ELSE 0 END) as patrolledCount " +
            "FROM inspection_record " +
            "WHERE 1=1 " +
            "<if test='startTime != null'> AND patrol_time &gt;= #{startTime} </if>" +
            "<if test='endTime != null'> AND patrol_time &lt;= #{endTime} </if>" +
            "GROUP BY street" +
            "</script>")
    List<StreetPatrolStatDTO> countPatrolStatusByStreet(
            @Param("startTime") LocalDateTime startTime, 
            @Param("endTime") LocalDateTime endTime,
            @Param("patrolledStatus") String patrolledStatus);
            
    /**
     * 按街道分组统计核查状态
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param reformedStatus 已整改状态的值
     * @return 各街道的核查统计结果，返回DTO对象列表
     */
    @Select("<script>" +
            "SELECT " +
            "  street, " +
            "  COUNT(*) as total, " +
            "  SUM(CASE WHEN check_status = #{reformedStatus} THEN 1 ELSE 0 END) as reformedCount " +
            "FROM inspection_record " +
            "WHERE 1=1 " +
            "<if test='startTime != null'> AND patrol_time &gt;= #{startTime} </if>" +
            "<if test='endTime != null'> AND patrol_time &lt;= #{endTime} </if>" +
            "GROUP BY street" +
            "</script>")
    List<StreetCheckStatDTO> countCheckStatusByStreet(
            @Param("startTime") LocalDateTime startTime, 
            @Param("endTime") LocalDateTime endTime,
            @Param("reformedStatus") String reformedStatus);
}

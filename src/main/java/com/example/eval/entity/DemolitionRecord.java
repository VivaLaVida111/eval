package com.example.eval.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("demolition_record")
@ApiModel(value = "DemolitionRecord对象", description = "违建拆除记录表")
public class DemolitionRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("填报单位")
    private String reportUnit;

    @ApiModelProperty("填报时间 (yyyy-MM-dd)")
    private LocalDate reportDate;

    @ApiModelProperty("所属街办")
    private String street;

    @ApiModelProperty("所属社区")
    private String community;

    @ApiModelProperty("违建名称")
    private String illegalName;

    @ApiModelProperty("违建地址")
    private String illegalAddress;

    @ApiModelProperty("公共区域/住宅小区")
    private String publicAreaOrCommunity;

    @ApiModelProperty("违建位置")
    private String illegalLocation;

    @ApiModelProperty("违建用途")
    private String illegalUsage;

    @ApiModelProperty("拆除时间 (yyyy-MM-dd)")
    private LocalDate demolitionDate;

    @ApiModelProperty("拆除面积（平方米）")
    private BigDecimal demolitionArea;

    @ApiModelProperty("新增/存量")
    private String stockType;

    @ApiModelProperty("是否涉及征地拆迁")
    private String involvesLandDemolition;

    @ApiModelProperty("涉及专项整治")
    private String specialRectification;

    @ApiModelProperty("安全隐患")
    private String safetyHazard;

    @ApiModelProperty("备注（自建房）")
    private String remarkSelfBuilt;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

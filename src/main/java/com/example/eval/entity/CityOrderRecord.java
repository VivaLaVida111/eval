package com.example.eval.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;/**
 * <p>
 * 市容秩序导入/登记记录
 * </p>
 */
@Getter
@Setter
@TableName("city_order_record")
@ApiModel(value = "CityOrderRecord对象", description = "市容秩序记录表")
public class CityOrderRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    // ====== 基本信息 ======
    @ApiModelProperty("问题来源（市级巡查/区级巡查/街道自查/群众举报等）")
    private String resource;

    @ApiModelProperty("巡查编号")
    private String checkNumber;

    @ApiModelProperty("检查时间（字符串，建议格式：YYYY-MM-DDTHH:mm:ss）")
    private LocalDateTime checkTime;   // 注意：表结构为 VARCHAR，故这里用 String

    @ApiModelProperty("所属街道")
    private String street;

    @ApiModelProperty("详细地址")
    private String detailAddress;

    // ====== 现场问题 ======
    @ApiModelProperty("问题描述")
    private String problemDesc;

    @ApiModelProperty("问题图片（URL/文件名）")
    private String problemPic;

    @ApiModelProperty("是否下达通知（是/否）")
    private String noticeIssued;

    @ApiModelProperty("核查状态（已整改/未整改/无问题等）")
    private String checkStatus;

    // ====== 备注 ======
    @ApiModelProperty("备注")
    private String note;
}

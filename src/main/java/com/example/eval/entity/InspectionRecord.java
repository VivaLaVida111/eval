package com.example.eval.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 巡查登记表
 * </p>
 *
 * @author luo
 * @since 2025-04-18
 */
@Getter
@Setter
  @TableName("inspection_record")
@ApiModel(value = "InspectionRecord对象", description = "巡查登记表")
public class InspectionRecord implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      @ApiModelProperty("问题来源")
      private String resource;

      @ApiModelProperty("巡查编号")
      private String inspectionNumber;

      @ApiModelProperty("工地名称")
      private String siteName;

      @ApiModelProperty("详细地址")
      private String detailSite;

      @ApiModelProperty("街道")
      private String street;

      @ApiModelProperty("问题属性")
      private String attribute;

      @ApiModelProperty("巡查状态")
      private String patrolStatus;

      @ApiModelProperty("巡查时间")
      private LocalDateTime patrolTime;

      @ApiModelProperty("巡查人")
      private String patroller;

      @ApiModelProperty("巡查照片")
      private String patrolPic;

      @ApiModelProperty("核查状态")
      private String checkStatus;

      @ApiModelProperty("整改照片")
      private String reformPic;


}

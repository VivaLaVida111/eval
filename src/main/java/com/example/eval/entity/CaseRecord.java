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
import lombok.Setter;

/**
 * <p>
 * 案件记录表
 * </p>
 *
 * @author luo
 * @since 2025-04-14
 */
@Getter
@Setter
@TableName("case_record")
@ApiModel(value = "CaseRecord对象", description = "案件记录表")
public class CaseRecord implements Serializable {

  private static final long serialVersionUID = 1L;

  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  // ====== 基本信息 ======
  @ApiModelProperty("案件类型")
  private String caseType;

  @ApiModelProperty("执法程序（一般、简易、行政强制）")
  @TableField("procedure_type")
  private String procedure;

  @ApiModelProperty("案件名称/案由")
  private String caseTitle;

  // ====== 相对人信息 ======
  @ApiModelProperty("违法人姓名或违法企业名称")
  private String illegalName;

  @ApiModelProperty("联系方式")
  private String contact;

  @ApiModelProperty("违法人类别（自然人、个体工商户、企业的填法人姓名）")
  private String illegalCategory;

  @ApiModelProperty("法人姓名")
  private String legalPerson;

  @ApiModelProperty("统一社会信用代码")
  private String creditCode;

  @ApiModelProperty("身份证号码")
  private String idNumber;

  // ====== 线索/现场 ======
  @ApiModelProperty("线索来源（日常巡查、上级交办、部门移交、举报投诉）")
  private String clueSource;

  @ApiModelProperty("现场检查时间")
  private LocalDateTime checkTime;

  @ApiModelProperty("违法地点")
  private String illegalAddress;

  // ====== 法律要素 ======
  @ApiModelProperty("违法行为")
  private String illegalBehavior;

  @ApiModelProperty("违法依据")
  private String illegalBasis;

  @ApiModelProperty("处罚依据")
  private String punishBasis;

  // ====== 时间节点 ======
  @ApiModelProperty("立案日期（yyyy-MM-dd）")
  private LocalDate registerDate;

  @ApiModelProperty("处罚决定书日期（yyyy-MM-dd）")
  private LocalDate decisionDate;

  @ApiModelProperty("结案日期（yyyy-MM-dd）")
  private LocalDate closeDate;

  @ApiModelProperty("上交月份（yyyy-MM）")
  private String submitMonth;

  // ====== 文书/单位 ======
  @ApiModelProperty("处罚决定书编号")
  private String decisionNo;

  @ApiModelProperty("承办单位")
  private String undertakeDept;

  // ====== 处理裁量与流程 ======
  @ApiModelProperty("自由裁量权幅度（一般、减轻、从轻、从重、不予处罚、免于处罚）")
  private String discretionLevel;

  @ApiModelProperty("是否公示（是/否）")
  private String isPublic;

  @ApiModelProperty("是否听证（是/否）")
  private String isHearing;

  @ApiModelProperty("是否召开案审会（是/否）")
  private String isReviewMeeting;

  @ApiModelProperty("是否重大行政执法案件（是/否）")
  private String isMajorCase;

  // ====== 处罚结果 ======
  @ApiModelProperty("处罚类型（罚款、予以警告、拆除、未罚款、其他）")
  private String punishType;

  @ApiModelProperty("罚款金额")
  private BigDecimal fineAmount;

  @ApiModelProperty("罚没收据编号")
  private String receiptNo;

  // ====== 办案信息 ======
  @ApiModelProperty("办案人员")
  private String handlers;

  @ApiModelProperty("是否有全过程记录（是/否）")
  private String hasFullRecord;

  // ====== 备注 ======
  @ApiModelProperty("备注")
  private String remark;
}

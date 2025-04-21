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
      private Integer id;

      @ApiModelProperty("所属街道")
      private String street;

      @ApiModelProperty("案件编号")
      private String caseNumber;

      @ApiModelProperty("处罚事由")
      private String reason;

      @ApiModelProperty("备注")
      private String remark;

      @ApiModelProperty("立案时间")
      private LocalDateTime filingTime;

      @ApiModelProperty("办结时间")
      private LocalDateTime closeTime;


}

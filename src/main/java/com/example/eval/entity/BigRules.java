package com.example.eval.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 考评系统大项评分规则
 * </p>
 *
 * @author luo
 * @since 2024-11-04
 */
@Getter
@Setter
  @TableName("big_rules")
@ApiModel(value = "BigRules对象", description = "考评系统大项评分规则")
public class BigRules implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      @ApiModelProperty("评分项")
      private String item;

      @ApiModelProperty("百分比")
      private Double percentage;

      @ApiModelProperty("删除表示位")
      private Boolean deleted;


}

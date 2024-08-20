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
 * 考评系统小项评分规则
 * </p>
 *
 * @author luo
 * @since 2024-06-14
 */
@Getter
@Setter
  @TableName("small_rules")
@ApiModel(value = "SmallRules对象", description = "考评系统小项评分规则")
public class SmallRules implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      @ApiModelProperty("评分项")
      private String item;

      @ApiModelProperty("百分比")
      private Double percentage;

      @ApiModelProperty("所属大项的id")
      private Integer parentId;


}

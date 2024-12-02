package com.example.eval.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 考评成绩明细表
 * </p>
 *
 * @author luo
 * @since 2024-12-01
 */
@Getter
@Setter
  @ApiModel(value = "Details对象", description = "考评成绩明细表")
public class Details implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      @ApiModelProperty("街道")
      private String street;

      @ApiModelProperty("对应考评结果表的id")
      private Integer resultId;

    private Integer bigRulesId;

    private Integer smallRulesId;

      @ApiModelProperty("备注")
      private String remark;

      @ApiModelProperty("前端管理员录入的数据")
      private Double input;

    private LocalDateTime time;

      @ApiModelProperty("input * 大项占比")
      private Double subtotal;


}

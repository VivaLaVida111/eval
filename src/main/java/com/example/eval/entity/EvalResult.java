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
 * 评分结果记录表
 * </p>
 *
 * @author luo
 * @since 2024-06-14
 */
@Getter
@Setter
  @TableName("eval_result")
@ApiModel(value = "EvalResult对象", description = "评分结果记录表")
public class EvalResult implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      @ApiModelProperty("街道名称")
      private String street;

      @ApiModelProperty("考评结果")
      private Double score;

      @ApiModelProperty("考评范围：周，月，年")
      private String timeRange;

      @ApiModelProperty("开始时间")
      private LocalDateTime startTime;

      @ApiModelProperty("结束时间")
      private LocalDateTime endTime;


}

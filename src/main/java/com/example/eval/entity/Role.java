package com.example.eval.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author luo
 * @since 2024-08-08
 */
@Getter
@Setter
  @ApiModel(value = "Role对象", description = "角色表")
@TableName("`role`")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      @ApiModelProperty("权限名称")
      private String name;

      @ApiModelProperty("描述")
      private String description;

      @ApiModelProperty("所属系统")
      @TableField("`system`")
      private String system;


}

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
 * 用户和角色关系表，多对多关系；
 * </p>
 *
 * @author luo
 * @since 2024-08-08
 */
@Getter
@Setter
  @TableName("user_role_relation")
@ApiModel(value = "UserRoleRelation对象", description = "用户和角色关系表，多对多关系；")
public class UserRoleRelation implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      @ApiModelProperty("用户id")
      private Integer userId;

      @ApiModelProperty("权限id")
      private Integer roleId;


}

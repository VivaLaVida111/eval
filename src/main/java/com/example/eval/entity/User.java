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
 * 
 * </p>
 *
 * @author luo
 * @since 2024-08-08
 */
@Getter
@Setter
  @ApiModel(value = "User对象", description = "")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

      @ApiModelProperty("系统用户id")
        @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      @ApiModelProperty("系统用户名")
      private String name;

      @ApiModelProperty("用户密码")
      private String password;

      @ApiModelProperty("该用户的电话号码")
      private String telephone;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

      @ApiModelProperty("真实名称")
      private String realName;

    private Integer loginStatus;

    private LocalDateTime lastLoginErrTime;

    private Integer loginErrCount;

    private String validationCode;

    private LocalDateTime validationCodeTime;


}

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
 * 环境卫生——实地检查记录
 * </p>
 */
@Getter
@Setter
@TableName("env_check")
@ApiModel(value = "EnvCheck对象", description = "环境卫生实地检查记录表")
public class EnvCheck implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    // ====== 基本信息 ======
    @ApiModelProperty("管理主体（环卫所/车队）")
    private String manageSubject;

    @ApiModelProperty("检查时间（DATETIME）")
    private LocalDateTime checkTime;

    @ApiModelProperty("所属街道")
    private String street;

    @ApiModelProperty("详细地址")
    private String detailAddress;

    // ====== 问题信息 ======
    @ApiModelProperty("问题类别（环境卫生/垃圾分类）")
    private String problemCategory;

    @ApiModelProperty("存在问题")
    private String problemDesc;

    @ApiModelProperty("问题照片（URL/文件名）")
    private String problemPic;

    // ====== 其他 ======
    @ApiModelProperty("检查人")
    private String inspector;

    @ApiModelProperty("备注")
    private String note;
}

package com.example.eval.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
// 前端展示用，把规则名称添加进来
public class DetailsFront implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String street;

    private BigRules bigRules;

    private SmallRules smallRules;

    private Double input;

    private String remark;

    private Double subtotal;

    private LocalDateTime time;


}

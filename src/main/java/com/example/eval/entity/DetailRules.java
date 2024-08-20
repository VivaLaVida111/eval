package com.example.eval.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
// 大项规则及其所属的小项规则
public class DetailRules implements Serializable {

        private static final long serialVersionUID = 1L;

        private BigRules bigRules;

        private List<SmallRules> smallRules;

}

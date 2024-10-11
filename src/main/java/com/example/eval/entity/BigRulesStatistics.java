package com.example.eval.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class BigRulesStatistics implements Serializable  {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private String item;
    private Double score;
}
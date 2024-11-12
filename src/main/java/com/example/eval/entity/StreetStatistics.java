package com.example.eval.entity;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
@Getter
@Setter
public class StreetStatistics implements Serializable {

    private static final long serialVersionUID = 1L;
    private String street;
    private Double score;
    private String item;
    private Integer id;
}

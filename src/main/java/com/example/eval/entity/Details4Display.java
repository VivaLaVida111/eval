package com.example.eval.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Details4Display implements Serializable {
    private static final long serialVersionUID = 1L;

    //private Integer id;
    @ExcelProperty("街道")
    @ColumnWidth(20)
    private String street;

    @ExcelProperty("大项规则")
    @ColumnWidth(20)
    private String bigRules;

    @ExcelProperty("大项占比")
    @ColumnWidth(20)
    private Double bigPercentage;

    @ExcelProperty("小项规则")
    @ColumnWidth(20)
    private String smallRules;

    @ExcelProperty("小项扣分")
    @ColumnWidth(20)
    private Double input;

    @ExcelProperty("备注")
    @ColumnWidth(20)
    private String remark;

    @ExcelProperty("小计")
    @ColumnWidth(20)
    private Double subtotal;

    @ExcelProperty("时间")
    @ColumnWidth(20)
    private LocalDateTime time;

}

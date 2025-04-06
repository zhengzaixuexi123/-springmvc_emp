package com.example.modle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    private Integer id;
    private String last;
    private String first;
    private String userId;
    private Date start;
    private String comments;
    private Integer manager;
    private String title;
    private Integer deptId;
    private BigDecimal salary;
    private BigDecimal commissionPct;
}

package com.z.module.acct.web.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LineDataVO {
    private String name;
    private BigDecimal line1;
    private BigDecimal line2;
}

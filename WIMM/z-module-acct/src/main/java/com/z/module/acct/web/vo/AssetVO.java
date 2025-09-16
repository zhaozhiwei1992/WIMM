package com.z.module.acct.web.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AssetVO {
    private BigDecimal totalAssets;
    private BigDecimal totalLiabilities;
    private BigDecimal netWorth;
}

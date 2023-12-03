package com.cbnu.shop.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Cart {
    private Long cartId;
    private Long memberId;
    private BigDecimal totalPrice;
}

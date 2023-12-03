package com.cbnu.shop.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItem {
    private Long orderId;
    private Long itemId;
    private BigDecimal itemPrice;
    private Long quantity;
}

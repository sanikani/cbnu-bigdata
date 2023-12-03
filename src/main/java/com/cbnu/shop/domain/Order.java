package com.cbnu.shop.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class Order {
    private Long orderId;
    private Long memberId;
    private BigDecimal totalPrice;
    private Timestamp orderDate;
    private PaymentStatus paymentStatus;
    private ShippingStatus shippingStatus;
}

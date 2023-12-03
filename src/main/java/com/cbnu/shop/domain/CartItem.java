package com.cbnu.shop.domain;

import lombok.Data;

@Data
public class CartItem {
    private Long itemId;
    private Long cartId;
    private Long quantity;
}

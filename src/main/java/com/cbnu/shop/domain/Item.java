package com.cbnu.shop.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Date;

@Data
public class Item {
    private Long itemId;
    private Long categoryId;
    private String itemName;
    private BigDecimal price;
    private Long quantity;
    private String brand;
    private Date releaseDate;
}

package com.cbnu.shop.domain;

import lombok.Data;

@Data
public class Category {
    private Long categoryId;
    private Long parentCategoryId;
    private String name;
}

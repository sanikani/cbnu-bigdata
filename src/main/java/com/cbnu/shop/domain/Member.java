package com.cbnu.shop.domain;

import lombok.Data;

import java.sql.Date;

@Data
public class Member {
    private Long memberId;
    private String name;
    private String phone;
    private String email;
    private Long age;
    private String password;
    private Date registrationDate;
}

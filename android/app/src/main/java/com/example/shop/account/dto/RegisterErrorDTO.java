package com.example.shop.account.dto;

import lombok.Data;

@Data
public class RegisterErrorDTO {
    private String[] email;
    private String[] firstName;
}

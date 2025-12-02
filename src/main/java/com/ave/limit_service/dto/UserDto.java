package com.ave.limit_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private String userId;
    private double currentLimit;
    private double reservedAmount;
}

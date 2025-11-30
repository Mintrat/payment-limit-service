package com.ave.limit_service.dto;

public record UserDto(String userId, double currentLimit, double reservedAmount) {
}

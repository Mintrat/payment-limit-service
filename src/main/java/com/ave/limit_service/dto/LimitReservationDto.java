package com.ave.limit_service.dto;

import com.ave.limit_service.enums.LimitReservationStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class LimitReservationDto {
    private long id;
    private String userId;
    private LocalDateTime expiresAt;
    private double amount;
    private LimitReservationStatus status;
}

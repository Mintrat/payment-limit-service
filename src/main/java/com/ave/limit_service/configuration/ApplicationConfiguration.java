package com.ave.limit_service.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class ApplicationConfiguration {

    @Value("${limit-service.default-limit}")
    private double defaultLimit;

    @Value("${limit-service.reservation-timeout-minutes}")
    private int reservationTimeoutMinutes;
}

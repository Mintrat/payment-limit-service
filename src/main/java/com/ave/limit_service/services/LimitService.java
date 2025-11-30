package com.ave.limit_service.services;

import com.ave.limit_service.configuration.ApplicationConfiguration;
import com.ave.limit_service.dto.LimitReservationDto;
import com.ave.limit_service.dto.ReservationDto;
import com.ave.limit_service.dto.WithdrawDto;
import com.ave.limit_service.dto.UserDto;
import com.ave.limit_service.enums.LimitReservationStatus;
import com.ave.limit_service.exception.LimitExceededException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class LimitService {

    final private UserLimitService userLimitService;
    final private LimitReservationsService limitReservationsService;
    final private ApplicationConfiguration applicationConfiguration;

    public LimitService(
            UserLimitService userLimitService,
            LimitReservationsService limitReservationsService,
            ApplicationConfiguration applicationConfiguration
    ) {
        this.userLimitService = userLimitService;
        this.limitReservationsService = limitReservationsService;
        this.applicationConfiguration = applicationConfiguration;
    }

    public UserDto findUser(String userId) {
        return userLimitService.findUser(userId);
    }

    public void withdraw(WithdrawDto withdrawDto) {
        userLimitService.withdraw(withdrawDto);
    }

    @Transactional
    public LimitReservationDto reservation(ReservationDto request) {
        UserDto userDto = findUser(request.userId());
        double totalReservation = userDto.reservedAmount() + request.amount();

        if (totalReservation > userDto.currentLimit()) {
            throw new LimitExceededException();
        }
        userLimitService.save(new UserDto(userDto.userId(), userDto.currentLimit(), totalReservation));

        LimitReservationDto limitReservationDto = new LimitReservationDto();
        LocalDateTime expiresAt = LocalDateTime
                .now()
                .plusMinutes(applicationConfiguration.getReservationTimeoutMinutes());

        limitReservationDto.setUserId(request.userId());
        limitReservationDto.setExpiresAt(expiresAt);
        limitReservationDto.setAmount(request.amount());
        limitReservationDto.setStatus(LimitReservationStatus.RESERVED);

        return limitReservationsService.create(limitReservationDto);
    }
}

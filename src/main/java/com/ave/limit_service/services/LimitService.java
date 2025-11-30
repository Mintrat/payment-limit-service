package com.ave.limit_service.services;

import com.ave.limit_service.dto.LimitReservationDto;
import com.ave.limit_service.dto.ReservationDto;
import com.ave.limit_service.dto.WithdrawDto;
import com.ave.limit_service.dto.UserDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LimitService {

    final private UserLimitService userLimitService;
    final private LimitReservationsService limitReservationsService;

    public LimitService(
            UserLimitService userLimitService,
            LimitReservationsService limitReservationsService
    ) {
        this.userLimitService = userLimitService;
        this.limitReservationsService = limitReservationsService;
    }

    public UserDto findUser(String userId) {
        return userLimitService.findUser(userId);
    }

    public void withdraw(WithdrawDto withdrawDto) {
        userLimitService.withdraw(withdrawDto);
    }

    @Transactional
    public LimitReservationDto reservation(ReservationDto request) {
        userLimitService.reservation(request);
        return limitReservationsService.reservation(request);
    }

    @Transactional
    public void confirmReservation(Long reservationId) {
        LimitReservationDto limitReservationDto = limitReservationsService.confirmAndGetReservation(reservationId);
        userLimitService.confirmReservation(limitReservationDto);
    }

    @Transactional
    public void cancelReservation(Long reservationId) {
        LimitReservationDto limitReservationDto = limitReservationsService.cancelAndGetReservation(reservationId);
        userLimitService.cancelReservation(limitReservationDto);
    }
}

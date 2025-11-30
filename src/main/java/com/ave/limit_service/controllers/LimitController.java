package com.ave.limit_service.controllers;

import com.ave.limit_service.dto.LimitReservationDto;
import com.ave.limit_service.dto.ReservationDto;
import com.ave.limit_service.dto.WithdrawDto;
import com.ave.limit_service.dto.UserDto;
import com.ave.limit_service.services.LimitService;
import org.springframework.web.bind.annotation.*;

@RestController
public class LimitController {
    private final LimitService limitService;

    public LimitController(LimitService limitService) {
        this.limitService = limitService;
    }

    @GetMapping("/limits/{userId}")
    public UserDto findUserById(@PathVariable String userId) {
        return limitService.findUser(userId);
    }

    @PostMapping("/limit/withdraw")
    public void withdraw(@RequestBody WithdrawDto request) {
        limitService.withdraw(request);
    }

    @PostMapping("/reservation")
    public LimitReservationDto reservation(@RequestBody ReservationDto request) {
        return limitService.reservation(request);
    }
}

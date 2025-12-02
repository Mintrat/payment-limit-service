package com.ave.limit_service.services;

import com.ave.limit_service.configuration.ApplicationConfiguration;
import com.ave.limit_service.dto.LimitReservationDto;
import com.ave.limit_service.dto.ReservationDto;
import com.ave.limit_service.dto.WithdrawDto;
import com.ave.limit_service.entities.UserLimitsEntity;
import com.ave.limit_service.exception.LimitExceededException;
import com.ave.limit_service.mappers.UserLimitMapper;
import com.ave.limit_service.repositories.UserRepository;
import com.ave.limit_service.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserLimitService {

    private final UserRepository userRepository;

    private final ApplicationConfiguration applicationConfiguration;

    private final UserLimitMapper userLimitMapper;

    public UserLimitService(
            UserRepository userRepository,
            ApplicationConfiguration applicationConfiguration,
            UserLimitMapper userLimitMapper
    ) {
        this.userRepository = userRepository;
        this.applicationConfiguration = applicationConfiguration;
        this.userLimitMapper = userLimitMapper;
    }

    public UserDto findUser(String userId) {
        return userRepository
                .findById(userId)
                .map(userLimitMapper::mapUserLimitsEntityToUserDto)
                .orElseGet(() -> createUser(userId));
    }

    public UserDto createUser(String userID) {
        UserLimitsEntity userLimitsEntity = new UserLimitsEntity();
        userLimitsEntity.setUserId(userID);
        userLimitsEntity.setCurrentLimit(applicationConfiguration.getDefaultLimit());
        userLimitsEntity.setReservedAmount(0.00);

        userRepository.save(userLimitsEntity);

        return userLimitMapper.mapUserLimitsEntityToUserDto(userLimitsEntity);
    }

    public void withdraw(WithdrawDto withdrawDto) {
        UserDto userDto = findUser(withdrawDto.userId());

        if (withdrawDto.amount() > (userDto.getCurrentLimit() - userDto.getReservedAmount())) {
            throw new LimitExceededException("Limit exceeded");
        }

        UserLimitsEntity userLimitsEntity = new UserLimitsEntity();

        userLimitsEntity.setUserId(withdrawDto.userId());
        userLimitsEntity.setCurrentLimit(userDto.getCurrentLimit() - withdrawDto.amount());
        userLimitsEntity.setReservedAmount(userDto.getReservedAmount());

        userRepository.save(userLimitsEntity);
    }

    public void save(UserDto userDto) {
        UserLimitsEntity userLimitsEntity = new UserLimitsEntity();

        userLimitsEntity.setUserId(userDto.getUserId());
        userLimitsEntity.setCurrentLimit(userDto.getCurrentLimit());
        userLimitsEntity.setReservedAmount(userDto.getReservedAmount());

        userRepository.save(userLimitsEntity);
    }

    public void confirmReservation(LimitReservationDto limitReservationDto) {
        UserDto userDto = findUser(limitReservationDto.getUserId());

        if (userDto.getCurrentLimit() < limitReservationDto.getAmount()) {
            throw new LimitExceededException("Limit exceeded");
        }

        double currentLimit = userDto.getCurrentLimit() - limitReservationDto.getAmount();
        double reservedAmount = userDto.getReservedAmount() < limitReservationDto.getAmount()
                ? 0 : userDto.getReservedAmount() - limitReservationDto.getAmount();

        userDto.setCurrentLimit(currentLimit);
        userDto.setReservedAmount(reservedAmount);

        save(userDto);
    }

    public void cancelReservation(LimitReservationDto limitReservationDto) {
        UserDto userDto = findUser(limitReservationDto.getUserId());
        double reservedAmount = userDto.getReservedAmount() < limitReservationDto.getAmount()
                ? 0 : userDto.getReservedAmount() - limitReservationDto.getAmount();

        userDto.setReservedAmount(reservedAmount);

        save(userDto);
    }

    public void reservation(ReservationDto reservationDto) {
        UserDto userDto = findUser(reservationDto.userId());
        double totalReservation = userDto.getReservedAmount() + reservationDto.amount();

        if (totalReservation > userDto.getCurrentLimit()) {
            throw new LimitExceededException("Limit exceeded");
        }

        userDto.setReservedAmount(totalReservation);

        save(userDto);
    }

    public void rollBackReservations(List<LimitReservationDto> reservations) {
        Map<String, Double> userAmounts = reservations.stream().collect(Collectors.groupingBy(
                LimitReservationDto::getUserId,
                Collectors.summingDouble(LimitReservationDto::getAmount)
        ));

        List<UserLimitsEntity> userLimits = userRepository
                .findByUserIdIn(userAmounts.keySet().stream().toList())
                .stream()
                .map(entity -> {
                    Double amount = userAmounts.get(entity.getUserId());
                    Double currentAmount = entity.getReservedAmount();
                    double newAmount = currentAmount > amount ? currentAmount - amount : 0;

                    entity.setReservedAmount(newAmount);
                    return entity;
                }).toList();

        userRepository.saveAll(userLimits);
    }

    public void restoreLimits() {
        userRepository.updateAllCurrentLimits(applicationConfiguration.getDefaultLimit());
    }
}

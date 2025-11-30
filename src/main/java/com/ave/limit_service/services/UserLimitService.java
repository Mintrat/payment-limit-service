package com.ave.limit_service.services;

import com.ave.limit_service.configuration.ApplicationConfiguration;
import com.ave.limit_service.dto.WithdrawDto;
import com.ave.limit_service.entities.UserLimitsEntity;
import com.ave.limit_service.exception.LimitExceededException;
import com.ave.limit_service.repositories.UserRepository;
import com.ave.limit_service.dto.UserDto;
import org.springframework.stereotype.Service;

@Service
public class UserLimitService {

    private final UserRepository userRepository;

    private final ApplicationConfiguration applicationConfiguration;

    public UserLimitService(UserRepository userRepository, ApplicationConfiguration applicationConfiguration) {
        this.userRepository = userRepository;
        this.applicationConfiguration = applicationConfiguration;
    }

    public UserDto findUser(String userId) {
        return userRepository
                .findById(userId)
                .map(user -> new UserDto(
                        user.getUserId(),
                        user.getCurrentLimit(),
                        user.getReservedAmount()
                )).orElseGet(() -> createUser(userId));
    }

    public UserDto createUser(String userID) {
        UserLimitsEntity userLimitsEntity = new UserLimitsEntity();
        userLimitsEntity.setUserId(userID);
        userLimitsEntity.setCurrentLimit(applicationConfiguration.getDefaultLimit());
        userLimitsEntity.setReservedAmount(0.00);

        userRepository.save(userLimitsEntity);

        return new UserDto(
                userLimitsEntity.getUserId(),
                userLimitsEntity.getCurrentLimit(),
                userLimitsEntity.getReservedAmount()
        );
    }

    public void withdraw(WithdrawDto withdrawDto) {
        UserDto userDto = findUser(withdrawDto.userId());

        if (withdrawDto.amount() > (userDto.currentLimit() - userDto.reservedAmount())) {
            throw new LimitExceededException();
        }

        UserLimitsEntity userLimitsEntity = new UserLimitsEntity();

        userLimitsEntity.setUserId(withdrawDto.userId());
        userLimitsEntity.setCurrentLimit(userDto.currentLimit() - withdrawDto.amount());
        userLimitsEntity.setReservedAmount(userDto.reservedAmount());

        userRepository.save(userLimitsEntity);
    }

    public void save(UserDto userDto) {
        UserLimitsEntity userLimitsEntity = new UserLimitsEntity();

        userLimitsEntity.setUserId(userDto.userId());
        userLimitsEntity.setCurrentLimit(userDto.currentLimit());
        userLimitsEntity.setReservedAmount(userDto.reservedAmount());

        userRepository.save(userLimitsEntity);
    }
}

package com.ave.limit_service.mappers;

import com.ave.limit_service.dto.UserDto;
import com.ave.limit_service.entities.UserLimitsEntity;
import org.springframework.stereotype.Component;

@Component
public class UserLimitMapper {
    public UserDto mapUserLimitsEntityToUserDto(UserLimitsEntity userLimitsEntity) {
        UserDto userDto = new UserDto();

        userDto.setUserId(userLimitsEntity.getUserId());
        userDto.setReservedAmount(userLimitsEntity.getReservedAmount());
        userDto.setCurrentLimit(userLimitsEntity.getCurrentLimit());

        return userDto;
    }
}

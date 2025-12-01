package com.ave.limit_service.mappers;

import com.ave.limit_service.dto.LimitReservationDto;
import com.ave.limit_service.entities.LimitReservationEntity;
import org.springframework.stereotype.Component;

@Component
public class LimitReservationMapper {

    public LimitReservationDto mapLimitReservationEntityToLimitReservationDto(LimitReservationEntity limitReservationEntity) {
        LimitReservationDto limitReservationDto = new LimitReservationDto();

        limitReservationDto.setId(limitReservationEntity.getId());
        limitReservationDto.setUserId(limitReservationEntity.getUserId());
        limitReservationDto.setStatus(limitReservationEntity.getStatus());
        limitReservationDto.setAmount(limitReservationEntity.getAmount());
        limitReservationDto.setExpiresAt(limitReservationEntity.getExpiresAt());

        return limitReservationDto;
    }

    public LimitReservationEntity matLimitReservationDtoToLimitReservationEntity(LimitReservationDto limitReservationDto) {
        LimitReservationEntity limitReservationEntity = new LimitReservationEntity();

        if (limitReservationDto.getId() != 0L) {
            limitReservationEntity.setId(limitReservationDto.getId());
        }

        limitReservationEntity.setUserId(limitReservationDto.getUserId());
        limitReservationEntity.setStatus(limitReservationDto.getStatus());
        limitReservationEntity.setAmount(limitReservationDto.getAmount());
        limitReservationEntity.setExpiresAt(limitReservationDto.getExpiresAt());

        return limitReservationEntity;
    }
}

package com.ave.limit_service.services;

import com.ave.limit_service.dto.LimitReservationDto;
import com.ave.limit_service.entities.LimitReservationEntity;
import com.ave.limit_service.repositories.LimitReservationsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LimitReservationsService {

    private final LimitReservationsRepository limitReservationsRepository;

    public LimitReservationsService(LimitReservationsRepository limitReservationsRepository) {
        this.limitReservationsRepository = limitReservationsRepository;
    }

    public LimitReservationDto find(int id) {
        return this.limitReservationsRepository
                .findById(id)
                .map(limitReservationEntity -> {
                    LimitReservationDto limitReservationDto = new LimitReservationDto();

                    limitReservationDto.setId(limitReservationEntity.getId());
                    limitReservationDto.setUserId(limitReservationEntity.getUserId());
                    limitReservationDto.setAmount(limitReservationEntity.getAmount());
                    limitReservationDto.setStatus(limitReservationEntity.getStatus());
                    limitReservationDto.setExpiresAt(limitReservationEntity.getExpiresAt());

                    return limitReservationDto;
                })
                .orElseThrow(EntityNotFoundException::new);
    }

    public LimitReservationDto create(LimitReservationDto limitReservationDto) {
        LimitReservationEntity limitReservationEntity = new LimitReservationEntity();

        limitReservationEntity.setUserId(limitReservationDto.getUserId());
        limitReservationEntity.setStatus(limitReservationDto.getStatus());
        limitReservationEntity.setAmount(limitReservationDto.getAmount());
        limitReservationEntity.setExpiresAt(limitReservationDto.getExpiresAt());

        limitReservationsRepository.save(limitReservationEntity);

        LimitReservationDto result = new LimitReservationDto();

        result.setId(limitReservationEntity.getId());
        result.setUserId(limitReservationEntity.getUserId());
        result.setStatus(limitReservationEntity.getStatus());
        result.setAmount(limitReservationEntity.getAmount());
        result.setExpiresAt(limitReservationEntity.getExpiresAt());

        return result;
    }
}

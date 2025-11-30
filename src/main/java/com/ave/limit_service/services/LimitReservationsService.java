package com.ave.limit_service.services;

import com.ave.limit_service.configuration.ApplicationConfiguration;
import com.ave.limit_service.dto.LimitReservationDto;
import com.ave.limit_service.dto.ReservationDto;
import com.ave.limit_service.entities.LimitReservationEntity;
import com.ave.limit_service.enums.LimitReservationStatus;
import com.ave.limit_service.exception.LimitErrorStatusException;
import com.ave.limit_service.repositories.LimitReservationsRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class LimitReservationsService {

    private final LimitReservationsRepository limitReservationsRepository;
    private final ApplicationConfiguration applicationConfiguration;

    public LimitReservationsService(
            LimitReservationsRepository limitReservationsRepository,
            ApplicationConfiguration applicationConfiguration
    ) {
        this.limitReservationsRepository = limitReservationsRepository;
        this.applicationConfiguration = applicationConfiguration;
    }

    public LimitReservationDto find(Long id) {
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

    public LimitReservationDto save(LimitReservationDto limitReservationDto) {
        LimitReservationEntity limitReservationEntity = getLimitReservationEntity(limitReservationDto);

        limitReservationsRepository.save(limitReservationEntity);

        LimitReservationDto result = new LimitReservationDto();

        result.setId(limitReservationEntity.getId());
        result.setUserId(limitReservationEntity.getUserId());
        result.setStatus(limitReservationEntity.getStatus());
        result.setAmount(limitReservationEntity.getAmount());
        result.setExpiresAt(limitReservationEntity.getExpiresAt());

        return result;
    }

    public LimitReservationDto confirmAndGetReservation(Long reservationId) {
        LimitReservationDto limitReservationDto = this.find(reservationId);

        if (!LimitReservationStatus.RESERVED.equals(limitReservationDto.getStatus())) {
            throw new LimitErrorStatusException("The limit has expired or been cancelled or has already been confirmed");
        }

        limitReservationDto.setStatus(LimitReservationStatus.CONFIRMED);

        return save(limitReservationDto);
    }

    public LimitReservationDto cancelAndGetReservation(Long reservationId) {
        LimitReservationDto limitReservationDto = this.find(reservationId);

        if (!LimitReservationStatus.RESERVED.equals(limitReservationDto.getStatus())) {
            throw new LimitErrorStatusException("The limit has expired or been cancelled or has already been confirmed");
        }

        limitReservationDto.setStatus(LimitReservationStatus.CANCELED);

        return save(limitReservationDto);
    }

    public LimitReservationDto reservation(ReservationDto request) {
        LimitReservationDto limitReservationDto = new LimitReservationDto();
        LocalDateTime expiresAt = LocalDateTime
                .now()
                .plusMinutes(applicationConfiguration.getReservationTimeoutMinutes());

        limitReservationDto.setUserId(request.userId());
        limitReservationDto.setExpiresAt(expiresAt);
        limitReservationDto.setAmount(request.amount());
        limitReservationDto.setStatus(LimitReservationStatus.RESERVED);

        return save(limitReservationDto);
    }

    public List<LimitReservationDto> toExpire() {
        List<LimitReservationEntity> reservationEntities = limitReservationsRepository.findByStatusAndExpiresAtBefore(
                LimitReservationStatus.RESERVED,
                LocalDateTime.now()
        );
        log.info(reservationEntities.toString());
        List<LimitReservationEntity> updatedReservationEntities = reservationEntities
                .stream()
                .peek(entity -> entity.setStatus(LimitReservationStatus.EXPIRED))
                .toList();

        limitReservationsRepository.saveAll(updatedReservationEntities);

        return updatedReservationEntities
                .stream()
                .map(LimitReservationsService::getLimitReservationEntity)
                .toList();
    }

    private static LimitReservationEntity getLimitReservationEntity(LimitReservationDto limitReservationDto) {
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

    private static LimitReservationDto getLimitReservationEntity(LimitReservationEntity limitReservationEntity) {
        LimitReservationDto limitReservationDto = new LimitReservationDto();

        limitReservationDto.setId(limitReservationEntity.getId());
        limitReservationDto.setUserId(limitReservationEntity.getUserId());
        limitReservationDto.setStatus(limitReservationEntity.getStatus());
        limitReservationDto.setAmount(limitReservationEntity.getAmount());
        limitReservationDto.setExpiresAt(limitReservationEntity.getExpiresAt());

        return limitReservationDto;
    }
}

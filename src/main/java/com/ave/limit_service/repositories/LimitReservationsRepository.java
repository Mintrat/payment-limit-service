package com.ave.limit_service.repositories;

import com.ave.limit_service.entities.LimitReservationEntity;
import com.ave.limit_service.enums.LimitReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LimitReservationsRepository extends JpaRepository<LimitReservationEntity, Long> {
    List<LimitReservationEntity> findByStatusAndExpiresAtBefore(LimitReservationStatus status, LocalDateTime expiresAt);
}

package com.ave.limit_service.repositories;

import com.ave.limit_service.entities.LimitReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LimitReservationsRepository extends JpaRepository<LimitReservationEntity, Integer> {
}

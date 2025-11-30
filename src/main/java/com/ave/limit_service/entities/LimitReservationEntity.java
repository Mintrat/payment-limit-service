package com.ave.limit_service.entities;

import com.ave.limit_service.enums.LimitReservationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "limit_reservations")
public class LimitReservationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "amount", nullable = false)
    private double amount;

    @Column(name = "status", nullable = false)
    private LimitReservationStatus status;
}

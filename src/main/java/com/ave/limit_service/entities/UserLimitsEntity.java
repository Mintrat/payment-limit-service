package com.ave.limit_service.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_limits")
public class UserLimitsEntity {
    @Id
    @Column(name = "user_id", unique = true, nullable = false)
    private String userId;

    @Column(name = "current_limit", nullable = false)
    private double currentLimit;

    @Column(name = "reserved_amount", nullable = false)
    private double reservedAmount;
}

package com.ave.limit_service.repositories;

import com.ave.limit_service.entities.UserLimitsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserLimitsEntity, String> {
}

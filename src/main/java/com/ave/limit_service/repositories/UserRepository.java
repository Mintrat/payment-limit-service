package com.ave.limit_service.repositories;

import com.ave.limit_service.entities.UserLimitsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserLimitsEntity, String> {
    List<UserLimitsEntity> findByUserIdIn(List<String> userIds);

    @Modifying
    @Query("UPDATE UserLimitsEntity u SET u.currentLimit = :newLimit")
    int updateAllCurrentLimits(@Param("newLimit") double newLimit);
}

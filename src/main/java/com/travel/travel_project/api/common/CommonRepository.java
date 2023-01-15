package com.travel.travel_project.api.common;

import com.travel.travel_project.domain.common.CommonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommonRepository extends JpaRepository<CommonEntity, Long> {
    Optional<CommonEntity> findByCommonCode(Integer commonCode);
}

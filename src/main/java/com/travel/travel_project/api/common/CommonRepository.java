package com.travel.travel_project.api.common;

import com.travel.travel_project.domain.common.CommonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface CommonRepository extends JpaRepository<CommonEntity, Long> {
    Optional<CommonEntity> findByCommonCode(Integer commonCode);
}

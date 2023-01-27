package com.travel.api.common.domain.repository;

import com.travel.api.common.domain.CommonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface CommonRepository extends JpaRepository<CommonEntity, Long> {
    Optional<CommonEntity> findByCommonCode(Integer commonCode);
}

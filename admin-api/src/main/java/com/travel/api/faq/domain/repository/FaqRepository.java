package com.travel.api.faq.domain.repository;

import com.travel.api.faq.domain.FaqEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface FaqRepository extends JpaRepository<FaqEntity, Long> {
    @Query("select f from FaqEntity f join fetch f.newFaqCode where f.idx = :idx")
    Optional<FaqEntity> findByIdx(@Param("idx") Long idx);
}

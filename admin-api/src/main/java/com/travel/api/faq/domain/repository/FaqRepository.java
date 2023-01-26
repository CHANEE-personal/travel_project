package com.travel.api.faq.domain.repository;

import com.travel.api.faq.domain.FaqEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FaqRepository extends JpaRepository<FaqEntity, Long> {
    @Query("select f from FaqEntity f join fetch f.newFaqCode where f.idx = ?1")
    Optional<FaqEntity> findByIdx(Long idx);
}

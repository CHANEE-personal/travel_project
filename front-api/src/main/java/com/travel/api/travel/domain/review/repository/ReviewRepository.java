package com.travel.api.travel.domain.review.repository;

import com.travel.api.travel.domain.review.TravelReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface ReviewRepository extends JpaRepository<TravelReviewEntity, Long> {
    List<TravelReviewEntity> findByNewTravelEntityIdx(Long idx);
}

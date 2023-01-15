package com.travel.travel_project.api.travel.review;

import com.travel.travel_project.domain.travel.review.TravelReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface ReviewRepository extends JpaRepository<TravelReviewEntity, Long> {
    List<TravelReviewEntity> findByNewTravelEntityIdx(Long idx);
}

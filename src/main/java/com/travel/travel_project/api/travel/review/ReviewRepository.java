package com.travel.travel_project.api.travel.review;

import com.travel.travel_project.domain.travel.review.TravelReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<TravelReviewEntity, Long> {
    List<TravelReviewEntity> findByNewTravelEntityIdx(Long idx);
}

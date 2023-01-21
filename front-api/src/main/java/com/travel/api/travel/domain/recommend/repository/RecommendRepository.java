package com.travel.api.travel.domain.recommend.repository;

import com.travel.api.travel.domain.recommend.TravelRecommendEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendRepository extends JpaRepository<TravelRecommendEntity, Long> {
}

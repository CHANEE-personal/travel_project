package com.travel.travel_project.api.travel.recommend;

import com.travel.travel_project.domain.travel.recommend.TravelRecommendDTO;
import com.travel.travel_project.domain.travel.recommend.TravelRecommendEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface RecommendRepository extends JpaRepository<TravelRecommendEntity, Long> {
}

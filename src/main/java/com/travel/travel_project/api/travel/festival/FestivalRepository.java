package com.travel.travel_project.api.travel.festival;

import com.travel.travel_project.domain.travel.festival.TravelFestivalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FestivalRepository extends JpaRepository<TravelFestivalEntity, Long> {
}

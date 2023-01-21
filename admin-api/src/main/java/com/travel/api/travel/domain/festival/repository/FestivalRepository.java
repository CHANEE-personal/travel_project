package com.travel.api.travel.domain.festival.repository;

import com.travel.api.travel.domain.festival.TravelFestivalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FestivalRepository extends JpaRepository<TravelFestivalEntity, Long> {
}

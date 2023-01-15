package com.travel.travel_project.api.travel;

import com.travel.travel_project.domain.travel.TravelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TravelRepository extends JpaRepository<TravelEntity, Long> {
}

package com.travel.travel_project.api.travel;

import com.travel.travel_project.domain.travel.TravelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface TravelRepository extends JpaRepository<TravelEntity, Long> {
}

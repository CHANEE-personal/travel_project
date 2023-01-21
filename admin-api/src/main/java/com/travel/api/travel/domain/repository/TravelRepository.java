package com.travel.api.travel.domain.repository;

import com.travel.api.travel.domain.TravelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TravelRepository extends JpaRepository<TravelEntity, Long> {
}

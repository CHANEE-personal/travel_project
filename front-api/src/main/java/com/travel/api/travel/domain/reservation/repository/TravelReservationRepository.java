package com.travel.api.travel.domain.reservation.repository;

import com.travel.api.travel.domain.reservation.TravelReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TravelReservationRepository extends JpaRepository<TravelReservationEntity, Long> {
}

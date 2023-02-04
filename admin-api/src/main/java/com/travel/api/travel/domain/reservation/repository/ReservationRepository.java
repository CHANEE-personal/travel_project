package com.travel.api.travel.domain.reservation.repository;

import com.travel.api.travel.domain.reservation.TravelReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface ReservationRepository extends JpaRepository<TravelReservationEntity, Long> {
    @Query("select r from TravelReservationEntity r join fetch r.commonEntity join fetch r.travelImageEntityList where r.idx = :idx")
    Optional<TravelReservationEntity> findByIdx(@Param("idx") Long idx);
}

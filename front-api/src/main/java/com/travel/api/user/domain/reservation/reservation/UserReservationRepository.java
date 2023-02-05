package com.travel.api.user.domain.reservation.reservation;

import com.travel.api.travel.domain.schedule.TravelScheduleEntity;
import com.travel.api.user.domain.reservation.UserReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface UserReservationRepository extends JpaRepository<UserReservationEntity, Long> {
    @Query("select r from UserReservationEntity r join fetch r.newUserEntity where r.newUserEntity.idx = :userIdx")
    List<UserReservationEntity> findUserReservationList(@Param("userIdx") Long userIdx);
}

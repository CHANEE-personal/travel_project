package com.travel.api.travel.domain.schedule.repository;

import com.travel.api.travel.domain.schedule.TravelScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface ScheduleRepository extends JpaRepository<TravelScheduleEntity, Long> {

    @Query("select s from TravelScheduleEntity s join fetch s.userEntity where s.userEntity.idx = :userIdx")
    List<TravelScheduleEntity> findUserSchedule(@Param("userIdx") Long userIdx);

    @Query("select s from TravelScheduleEntity s join fetch s.userEntity where s.userEntity.idx = :userIdx and s.idx = :scheduleIdx")
    Optional<TravelScheduleEntity> findOneUserSchedule(@Param("userIdx") Long userIdx, @Param("scheduleIdx") Long scheduleIdx);
}

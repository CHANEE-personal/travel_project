package com.travel.api.travel.domain.schedule.repository;

import com.travel.api.travel.domain.schedule.TravelScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<TravelScheduleEntity, Long> {

    @Query("select s from TravelScheduleEntity s join fetch s.userEntity where s.userEntity.idx = ?1")
    List<TravelScheduleEntity> findUserSchedule(Long userIdx);

    @Query("select s from TravelScheduleEntity s join fetch s.userEntity where s.userEntity.idx = ?1 and s.idx = ?2")
    Optional<TravelScheduleEntity> findOneUserSchedule(Long userIdx, Long scheduleIdx);
}

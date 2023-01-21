package com.travel.api.travel.domain.schedule.repository;

import com.travel.api.travel.domain.schedule.TravelScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<TravelScheduleEntity, Long> {
}

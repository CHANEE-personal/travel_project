package com.travel.travel_project.api.travel.schedule;

import com.travel.travel_project.domain.travel.schedule.TravelScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ScheduleRepository extends JpaRepository<TravelScheduleEntity, Long> {
}

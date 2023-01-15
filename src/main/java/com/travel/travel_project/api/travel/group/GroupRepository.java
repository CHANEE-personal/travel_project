package com.travel.travel_project.api.travel.group;

import com.travel.travel_project.domain.travel.group.TravelGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface GroupRepository extends JpaRepository<TravelGroupEntity, Long> {
}

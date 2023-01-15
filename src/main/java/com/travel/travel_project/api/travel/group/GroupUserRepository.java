package com.travel.travel_project.api.travel.group;

import com.travel.travel_project.domain.travel.group.TravelGroupUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface GroupUserRepository extends JpaRepository<TravelGroupUserEntity, Long> {
}

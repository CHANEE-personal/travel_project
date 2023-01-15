package com.travel.travel_project.api.travel.group;

import com.travel.travel_project.domain.travel.group.TravelGroupUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupUserRepository extends JpaRepository<TravelGroupUserEntity, Long> {
}

package com.travel.api.travel.domain.group.repository;

import com.travel.api.travel.domain.group.TravelGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<TravelGroupEntity, Long> {
}

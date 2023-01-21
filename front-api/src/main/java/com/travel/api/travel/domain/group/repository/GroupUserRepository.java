package com.travel.api.travel.domain.group.repository;

import com.travel.api.travel.domain.group.TravelGroupUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupUserRepository extends JpaRepository<TravelGroupUserEntity, Long> {
}

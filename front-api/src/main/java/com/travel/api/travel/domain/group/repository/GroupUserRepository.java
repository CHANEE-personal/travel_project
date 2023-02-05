package com.travel.api.travel.domain.group.repository;

import com.travel.api.travel.domain.group.TravelGroupEntity;
import com.travel.api.travel.domain.group.TravelGroupUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Repository
@Transactional
public interface GroupUserRepository extends JpaRepository<TravelGroupUserEntity, Long> {

    @Query("select g from TravelGroupUserEntity g where g.userEntity.idx = :userIdx and g.travelGroupEntity.idx = :groupIdx")
    Optional<TravelGroupUserEntity> findByUserGroup(@Param("userIdx") Long userIdx, @Param("groupIdx") Long groupIdx);
}

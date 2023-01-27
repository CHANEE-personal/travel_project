package com.travel.api.travel;

import com.travel.api.travel.domain.TravelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface TravelRepository extends JpaRepository<TravelEntity, Long> {

    @Query("select t from TravelEntity t join fetch t.newTravelCode join fetch t.travelImageEntityList where t.idx = :idx")
    Optional<TravelEntity> findByIdx(@Param("idx") Long idx);

    @Query("select t from TravelEntity t join fetch t.newTravelCode where t.idx < :idx and t.visible = 'Y' order by t.idx desc")
    Optional<TravelEntity> findPrevByIdx(@Param("idx") Long idx);

    @Query("select t from TravelEntity t join fetch t.newTravelCode where t.idx > :idx and t.visible = 'Y' order by t.idx asc")
    Optional<TravelEntity> findNextByIdx(@Param("idx") Long idx);
}

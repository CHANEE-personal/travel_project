package com.travel.api.travel;

import com.travel.api.travel.domain.TravelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TravelRepository extends JpaRepository<TravelEntity, Long> {

    @Query("select t from TravelEntity t join fetch t.newTravelCode join fetch t.travelImageEntityList where t.idx = ?1")
    Optional<TravelEntity> findByIdx(Long idx);

    @Query("select t from TravelEntity t join fetch t.newTravelCode where t.idx < ?1 and t.visible = 'Y' order by t.idx desc")
    Optional<TravelEntity> findPrevByIdx(Long idx);

    @Query("select t from TravelEntity t join fetch t.newTravelCode where t.idx > ?1 and t.visible = 'Y' order by t.idx asc")
    Optional<TravelEntity> findNextByIdx(Long idx);
}

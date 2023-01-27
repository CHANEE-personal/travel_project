package com.travel.api.travel.domain.festival.repository;

import com.travel.api.travel.domain.festival.TravelFestivalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface FestivalRepository extends JpaRepository<TravelFestivalEntity, Long> {

    @Query("select f from TravelFestivalEntity f " +
            "where f.festivalMonth = :month " +
            "and f.festivalDay = :day " +
            "order by f.idx desc")
    List<TravelFestivalEntity> findFestivalList(@Param("month") Integer month, @Param("day") Integer day);
}

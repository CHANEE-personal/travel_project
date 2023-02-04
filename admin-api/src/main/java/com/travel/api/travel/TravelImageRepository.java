package com.travel.api.travel;

import com.travel.api.travel.domain.image.TravelImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TravelImageRepository extends JpaRepository<TravelImageEntity, Long> {
}

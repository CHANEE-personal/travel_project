package com.travel.travel_project.api.faq;

import com.travel.travel_project.domain.faq.FaqEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FaqRepository extends JpaRepository<FaqEntity, Long> {
}

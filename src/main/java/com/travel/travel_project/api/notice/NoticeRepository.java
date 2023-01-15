package com.travel.travel_project.api.notice;

import com.travel.travel_project.domain.notice.NoticeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<NoticeEntity, Long> {
}

package com.travel.travel_project.api.post;

import com.travel.travel_project.domain.post.reply.ReplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ReplyRepository extends JpaRepository<ReplyEntity, Long> {
}

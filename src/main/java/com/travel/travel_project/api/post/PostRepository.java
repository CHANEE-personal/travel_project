package com.travel.travel_project.api.post;

import com.travel.travel_project.domain.post.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface PostRepository extends JpaRepository<PostEntity, Long> {
}

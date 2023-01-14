package com.travel.travel_project.api.user;

import com.travel.travel_project.domain.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UserRepository extends JpaRepository<UserEntity, Long> {
}

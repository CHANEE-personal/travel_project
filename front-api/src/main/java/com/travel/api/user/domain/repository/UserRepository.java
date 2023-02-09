package com.travel.api.user.domain.repository;

import com.travel.api.user.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUserId(String id);
    Optional<UserEntity> findByUserToken(String token);
    Optional<UserEntity> findByUserRefreshToken(String refreshToken);
}

package com.travel.api.user.domain.reservation.reservation;

import com.travel.api.user.domain.reservation.UserReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UserReservationRepository extends JpaRepository<UserReservationEntity, Long> {
}

package com.travel.api.user.domain.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travel.api.travel.domain.schedule.TravelScheduleDTO;
import com.travel.api.travel.domain.schedule.TravelScheduleEntity;
import com.travel.api.user.domain.UserDTO;
import com.travel.api.user.domain.UserEntity;
import com.travel.exception.TravelException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.travel.api.travel.domain.schedule.QTravelScheduleEntity.travelScheduleEntity;
import static com.travel.exception.ApiExceptionType.NOT_FOUND_SCHEDULE;

@Repository
@RequiredArgsConstructor
public class UserQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    /**
     * <pre>
     * 1. MethodName : addFavoriteTravel
     * 2. ClassName  : UserRepository.java
     * 3. Comment    : 좋아하는 여행지 추가
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 12. 07.
     * </pre>
     */
    public UserDTO addFavoriteTravel(Long idx, Long favoriteIdx) {
        UserEntity oneUser = em.find(UserEntity.class, idx);
        List<String> favoriteTravelIdx = oneUser.getFavoriteTravelIdx();

        if (!favoriteTravelIdx.contains(String.valueOf(favoriteIdx))) {
            favoriteTravelIdx.add(String.valueOf(favoriteIdx));
        }

        em.merge(oneUser);
        return UserEntity.toDto(oneUser);
    }

    /**
     * <pre>
     * 1. MethodName : findUserSchedule
     * 2. ClassName  : UserRepository.java
     * 3. Comment    : 유저가 작성한 스케줄 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 12. 14.
     * </pre>
     */
    public List<TravelScheduleDTO> findUserSchedule(Long userIdx) {
        List<TravelScheduleEntity> userSchedule = queryFactory
                .selectFrom(travelScheduleEntity)
                .where(travelScheduleEntity.userEntity.idx.eq(userIdx))
                .fetch();

        return userSchedule != null ? TravelScheduleEntity.toDtoList(userSchedule) : Collections.emptyList();
    }

    /**
     * <pre>
     * 1. MethodName : findOneUserSchedule
     * 2. ClassName  : UserRepository.java
     * 3. Comment    : 유저가 작성한 스케줄 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 12. 14.
     * </pre>
     */
    public TravelScheduleDTO findOneUserSchedule(Long userIdx, Long scheduleIdx) {
        TravelScheduleEntity oneSchedule = Optional.ofNullable(queryFactory
                .selectFrom(travelScheduleEntity)
                .where(travelScheduleEntity.userEntity.idx.eq(userIdx).and(travelScheduleEntity.idx.eq(scheduleIdx)))
                .fetchOne()).orElseThrow(() -> new TravelException(NOT_FOUND_SCHEDULE));

        return TravelScheduleEntity.toDto(oneSchedule);
    }
}

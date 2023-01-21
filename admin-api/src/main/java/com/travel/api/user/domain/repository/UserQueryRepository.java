package com.travel.api.user.domain.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travel.api.travel.domain.schedule.TravelScheduleDTO;
import com.travel.api.travel.domain.schedule.TravelScheduleEntity;
import com.travel.api.user.domain.UserDTO;
import com.travel.api.user.domain.UserEntity;
import com.travel.exception.TravelException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.travel.api.travel.domain.schedule.QTravelScheduleEntity.travelScheduleEntity;
import static com.travel.api.user.domain.QUserEntity.userEntity;
import static com.travel.exception.ApiExceptionType.NOT_FOUND_SCHEDULE;
import static com.travel.exception.ApiExceptionType.NOT_FOUND_USER;

@Repository
@RequiredArgsConstructor
public class UserQueryRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * <pre>
     * 1. MethodName : findUsersList
     * 2. ClassName  : UserRepository.java
     * 3. Comment    : 관리자 > 유저 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 10. 9.
     * </pre>
     */
    public Page<UserDTO> findUserList(Map<String, Object> userMap, PageRequest pageRequest) {
        List<UserEntity> findUserList = queryFactory
                .selectFrom(userEntity)
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        assert findUserList != null;
        return new PageImpl<>(UserEntity.toDtoList(findUserList), pageRequest, findUserList.size());
    }

    /**
     * <pre>
     * 1. MethodName : findOneUserByToken
     * 2. ClassName  : UserRepository.java
     * 3. Comment    : 토큰을 이용한 유저 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 10. 6.
     * </pre>
     */
    public String findOneUserByToken(String token) {
        UserEntity userInfo = Optional.ofNullable(queryFactory
                .selectFrom(userEntity)
                .where(userEntity.userToken.eq(token)
                        .and(userEntity.visible.eq("Y")))
                .fetchOne()).orElseThrow(() -> new TravelException(NOT_FOUND_USER));

        return userInfo.getUserId();
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

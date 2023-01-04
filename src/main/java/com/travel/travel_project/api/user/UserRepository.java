package com.travel.travel_project.api.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travel.travel_project.common.StringUtil;
import com.travel.travel_project.domain.travel.schedule.TravelScheduleDTO;
import com.travel.travel_project.domain.travel.schedule.TravelScheduleEntity;
import com.travel.travel_project.domain.user.UserDTO;
import com.travel.travel_project.domain.user.UserEntity;
import com.travel.travel_project.exception.TravelException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.travel.travel_project.common.StringUtils.nullStrToStr;
import static com.travel.travel_project.domain.travel.schedule.QTravelScheduleEntity.*;
import static com.travel.travel_project.domain.user.QUserEntity.userEntity;
import static com.travel.travel_project.exception.ApiExceptionType.NOT_FOUND_SCHEDULE;
import static com.travel.travel_project.exception.ApiExceptionType.NOT_FOUND_USER;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public String adminLogin(UserEntity existUserEntity) {
        final String db_pw = nullStrToStr(findOneUser(existUserEntity.getIdx()).getPassword());
        String result;

        if (passwordEncoder.matches(existUserEntity.getPassword(), db_pw)) {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(existUserEntity.getUserId(), existUserEntity.getPassword())
            );
            getContext().setAuthentication(authentication);
            result = "Y";
        } else {
            result = "N";
        }
        return result;
    }

    /**
     * <pre>
     * 1. MethodName : insertUserToken
     * 2. ClassName  : UserRepository.java
     * 3. Comment    : 회원 로그인 후 토큰 등록 By EntityManager
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 10. 13.
     * </pre>
     */
    public Long insertUserToken(UserEntity userEntity) {
        em.merge(userEntity);
        return userEntity.getIdx();
    }

    /**
     * <pre>
     * 1. MethodName : findUsersCount
     * 2. ClassName  : UserRepository.java
     * 3. Comment    : 관리자 > 유저 리스트 갯수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 10. 9.
     * </pre>
     */
    public int findUsersCount(Map<String, Object> userMap) {
        return queryFactory
                .selectFrom(userEntity)
                .fetch().size();
    }

    /**
     * <pre>
     * 1. MethodName : findUsersList
     * 2. ClassName  : UserRepository.java
     * 3. Comment    : 관리자 > 유저 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 10. 9.
     * </pre>
     */
    public List<UserDTO> findUserList(Map<String, Object> userMap) {
        List<UserEntity> findUserList = queryFactory
                .selectFrom(userEntity)
                .fetch();

        return findUserList != null ? UserEntity.toDtoList(findUserList) : Collections.emptyList();
    }

    /**
     * <pre>
     * 1. MethodName : findOneUser
     * 2. ClassName  : UserRepository.java
     * 3. Comment    : 관리자 > 유저 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 10. 9.
     * </pre>
     */
    public UserDTO findOneUser(Long idx) {
        UserEntity findOneUser = Optional.ofNullable(queryFactory.selectFrom(userEntity)
                .where(userEntity.idx.eq(idx)
                        .and(userEntity.visible.eq("Y")))
                .fetchOne()).orElseThrow(() -> new TravelException(NOT_FOUND_USER, new Throwable()));

        return UserEntity.toDto(findOneUser);
    }

    /**
     * <pre>
     * 1. MethodName : findOneUserById
     * 2. ClassName  : UserRepository.java
     * 3. Comment    : 아이디를 이용한 유저 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 10. 6.
     * </pre>
     */
    public UserDTO findOneUserById(String userId) {
        UserEntity userInfo = Optional.ofNullable(queryFactory
                .selectFrom(userEntity)
                .where(userEntity.userId.eq(userId)
                        .and(userEntity.visible.eq("Y")))
                .fetchOne()).orElseThrow(() -> new TravelException(NOT_FOUND_USER, new Throwable()));

        return UserEntity.toDto(userInfo);
    }

    /**
     * <pre>
     * 1. MethodName : findOneUserByToken
     * 2. ClassName  : UserRepository.java
     * 3. Comment    : 토큰을 이용한 유저 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 10. 6.
     * </pre>
     */
    public String findOneUserByToken(String token) {
        UserEntity userInfo = Optional.ofNullable(queryFactory
                .selectFrom(userEntity)
                .where(userEntity.userToken.eq(token)
                        .and(userEntity.visible.eq("Y")))
                .fetchOne()).orElseThrow(() -> new TravelException(NOT_FOUND_USER, new Throwable()));

        return userInfo.getUserId();
    }

    /**
     * <pre>
     * 1. MethodName : insertUser
     * 2. ClassName  : UserRepository.java
     * 3. Comment    : 유저 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 10. 09.
     * </pre>
     */
    public UserDTO insertUser(UserEntity userEntity) {
        em.persist(userEntity);
        return UserEntity.toDto(userEntity);
    }

    /**
     * <pre>
     * 1. MethodName : updateUser
     * 2. ClassName  : TravelRepository.java
     * 3. Comment    : 유저 정보 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 10. 09.
     * </pre>
     */
    public UserDTO updateUser(UserEntity userEntity) {
        em.merge(userEntity);
        return UserEntity.toDto(userEntity);
    }

    /**
     * <pre>
     * 1. MethodName : deleteUser
     * 2. ClassName  : UserRepository.java
     * 3. Comment    : 유저 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 10. 09.
     * </pre>
     */
    public Long deleteUser(Long idx) {
        em.remove(em.find(UserEntity.class, idx));
        return idx;
    }

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

        if (!favoriteTravelIdx.contains(StringUtil.getString(favoriteIdx, ""))) {
            favoriteTravelIdx.add(StringUtil.getString(favoriteIdx,""));
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
                .where(travelScheduleEntity.userIdx.eq(userIdx))
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
                .where(travelScheduleEntity.userIdx.eq(userIdx).and(travelScheduleEntity.idx.eq(scheduleIdx)))
                .fetchOne()).orElseThrow(() -> new TravelException(NOT_FOUND_SCHEDULE, new Throwable()));

        return TravelScheduleEntity.toDto(oneSchedule);
    }
}

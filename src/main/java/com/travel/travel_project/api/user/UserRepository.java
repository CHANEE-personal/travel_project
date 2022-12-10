package com.travel.travel_project.api.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travel.travel_project.api.user.mapper.UserMapper;
import com.travel.travel_project.common.StringUtil;
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

import java.util.List;
import java.util.Map;

import static com.travel.travel_project.api.user.mapper.UserMapper.INSTANCE;
import static com.travel.travel_project.common.StringUtils.nullStrToStr;
import static com.travel.travel_project.domain.user.QUserEntity.userEntity;
import static com.travel.travel_project.exception.ApiExceptionType.ERROR_FAVORITE_TRAVEL;
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
        em.flush();
        em.clear();

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
    public Integer findUsersCount(Map<String, Object> userMap) {
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
    public List<UserDTO> findUsersList(Map<String, Object> userMap) {
        List<UserEntity> findUsersList = queryFactory
                .selectFrom(userEntity)
                .fetch();

        return INSTANCE.toDtoList(findUsersList);
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
        UserEntity findOneUser = queryFactory.selectFrom(userEntity)
                .where(userEntity.idx.eq(idx)
                        .and(userEntity.visible.eq("Y")))
                .fetchOne();

        return INSTANCE.toDto(findOneUser);
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
        UserEntity userInfo = queryFactory
                .selectFrom(userEntity)
                .where(userEntity.userId.eq(userId)
                        .and(userEntity.visible.eq("Y")))
                .fetchOne();

        return INSTANCE.toDto(userInfo);
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
        UserEntity userInfo = queryFactory
                .selectFrom(userEntity)
                .where(userEntity.userToken.eq(token)
                        .and(userEntity.visible.eq("Y")))
                .fetchOne();

        assert userInfo != null;
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
        return INSTANCE.toDto(userEntity);
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
        em.flush();
        em.clear();
        return INSTANCE.toDto(userEntity);
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
        em.flush();
        em.clear();
        return idx;
    }

    /**
     * <pre>
     * 1. MethodName : addFavoriteTravel
     * 2. ClassName  : UserRepository.java
     * 3. Comment    : 좋아하는 여행지 추가
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 12. 07.
     * </pre>
     */
    public UserDTO addFavoriteTravel(Long idx, Long favoriteIdx) {
        UserEntity oneUser = em.find(UserEntity.class, idx);
        List<String> favoriteTravelIdx = oneUser.getFavoriteTravelIdx();

        if (!favoriteTravelIdx.contains(StringUtil.getString(favoriteIdx, ""))) {
            favoriteTravelIdx.add(StringUtil.getString(favoriteIdx,""));
        }

        em.merge(oneUser);
        em.flush();
        em.clear();

        return INSTANCE.toDto(oneUser);
    }
}

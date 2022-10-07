package com.travel.travel_project.api.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travel.travel_project.api.user.mapper.UserMapper;
import com.travel.travel_project.domain.user.UserDTO;
import com.travel.travel_project.domain.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import static com.travel.travel_project.domain.user.QUserEntity.userEntity;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;


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

        return UserMapper.INSTANCE.toDto(userInfo);
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
}

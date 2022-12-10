package com.travel.travel_project.api.user;

import com.travel.travel_project.domain.user.UserDTO;
import com.travel.travel_project.domain.user.UserEntity;
import com.travel.travel_project.exception.TravelException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.travel.travel_project.api.user.mapper.UserMapper.INSTANCE;
import static com.travel.travel_project.exception.ApiExceptionType.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public String adminLogin(UserEntity userEntity) throws TravelException {
        try {
            return userRepository.adminLogin(userEntity);
        } catch (Exception e) {
            throw new TravelException(NOT_FOUND_USER, e);
        }
    }

    @CachePut("user")
    @Modifying(clearAutomatically = true)
    @Transactional
    public void insertToken(UserEntity paramUserEntity) throws TravelException {
        try {
            UserEntity userEntity = INSTANCE.toEntity(userRepository.findOneUser(paramUserEntity.getIdx()));
            userRepository.insertUserToken(userEntity);
        } catch (Exception e) {
            throw new TravelException(ERROR_USER, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findUsersList
     * 2. ClassName  : UserService.java
     * 3. Comment    : 유저 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 10. 11.
     * </pre>
     */
    @Transactional(readOnly = true)
    public List<UserDTO> findUsersList(Map<String, Object> userMap) throws TravelException {
        try {
            return userRepository.findUsersList(userMap);
        } catch (Exception e) {
            throw new TravelException(NOT_FOUND_USER_LIST, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findOneUser
     * 2. ClassName  : UserService.java
     * 3. Comment    : idx 이용한 유저 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 10. 11.
     * </pre>
     */
    @Transactional(readOnly = true)
    public UserDTO findOneUser(Long idx) throws TravelException {
        try {
            return userRepository.findOneUser(idx);
        } catch (Exception e) {
            throw new TravelException(NOT_FOUND_USER, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findOneUserById
     * 2. ClassName  : UserService.java
     * 3. Comment    : 아이디를 이용한 유저 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 10. 6.
     * </pre>
     */
    @Transactional(readOnly = true)
    public UserDTO findOneUserById(String userId) throws TravelException {
        try {
            return userRepository.findOneUserById(userId);
        } catch (Exception e) {
            throw new TravelException(NOT_FOUND_USER, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findOneUserByToken
     * 2. ClassName  : UserService.java
     * 3. Comment    : 아이디를 이용한 유저 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 10. 6.
     * </pre>
     */
    @Transactional(readOnly = true)
    public String findOneUserByToken(String token) throws TravelException {
        try {
            return userRepository.findOneUserByToken(token);
        } catch (Exception e) {
            throw new TravelException(NOT_FOUND_USER, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : insertUser
     * 2. ClassName  : UserService.java
     * 3. Comment    : 유저 회원가입
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 10. 11.
     * </pre>
     */
    @CachePut("user")
    @Modifying(clearAutomatically = true)
    @Transactional
    public UserDTO insertUser(UserEntity userEntity) throws TravelException {
        try {
            return userRepository.insertUser(userEntity);
        } catch (Exception e) {
            throw new TravelException(ERROR_USER, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updateUser
     * 2. ClassName  : UserService.java
     * 3. Comment    : 유저 정보 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 10. 11.
     * </pre>
     */
    @CachePut("user")
    @Modifying(clearAutomatically = true)
    @Transactional
    public UserDTO updateUser(UserEntity userEntity) throws TravelException {
        try {
            return userRepository.updateUser(userEntity);
        } catch (Exception e) {
            throw new TravelException(ERROR_UPDATE_USER, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteUser
     * 2. ClassName  : UserService.java
     * 3. Comment    : 유저 탈퇴
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 10. 11.
     * </pre>
     */
    @CacheEvict("user")
    @Modifying(clearAutomatically = true)
    @Transactional
    public Long deleteUser(Long idx) throws TravelException {
        try {
            return userRepository.deleteUser(idx);
        } catch (Exception e) {
            throw new TravelException(ERROR_DELETE_USER, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : addFavoriteTravel
     * 2. ClassName  : UserService.java
     * 3. Comment    : 좋아하는 여행지 추가
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 12. 07.
     * </pre>
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    public UserDTO addFavoriteTravel(Long idx, Long favoriteIdx) throws TravelException {
        try {
            return userRepository.addFavoriteTravel(idx, favoriteIdx);
        } catch (Exception e) {
            throw new TravelException(ERROR_FAVORITE_TRAVEL, e);
        }
    }
}

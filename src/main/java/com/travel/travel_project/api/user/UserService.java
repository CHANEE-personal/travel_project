package com.travel.travel_project.api.user;

import com.travel.travel_project.domain.travel.schedule.TravelScheduleDTO;
import com.travel.travel_project.domain.user.UserDTO;
import com.travel.travel_project.domain.user.UserEntity;
import com.travel.travel_project.exception.TravelException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.travel.travel_project.exception.ApiExceptionType.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public String adminLogin(UserEntity userEntity) {
        try {
            return userRepository.adminLogin(userEntity);
        } catch (Exception e) {
            throw new TravelException(NOT_FOUND_USER, e);
        }
    }

    @CachePut("user")
    @Modifying(clearAutomatically = true)
    @Transactional
    public void insertToken(UserEntity paramUserEntity) {
        try {
            UserEntity userEntity = UserEntity.toEntity(userRepository.findOneUser(paramUserEntity.getIdx()));
            userRepository.insertUserToken(userEntity);
        } catch (Exception e) {
            throw new TravelException(ERROR_USER, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findUserList
     * 2. ClassName  : UserService.java
     * 3. Comment    : 유저 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 10. 11.
     * </pre>
     */
    @Cacheable(value = "user", key = "#userMap")
    @Transactional(readOnly = true)
    public List<UserDTO> findUserList(Map<String, Object> userMap) {
        return userRepository.findUserList(userMap);
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
    @Cacheable(value = "user", key = "#idx")
    @Transactional(readOnly = true)
    public UserDTO findOneUser(Long idx) {
        return userRepository.findOneUser(idx);
    }

    /**
     * <pre>
     * 1. MethodName : findOneUserById
     * 2. ClassName  : UserService.java
     * 3. Comment    : 아이디를 이용한 유저 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 10. 6.
     * </pre>
     */
    @Transactional(readOnly = true)
    public UserDTO findOneUserById(String userId) {
        return userRepository.findOneUserById(userId);
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
    public String findOneUserByToken(String token) {
        return userRepository.findOneUserByToken(token);
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
    public UserDTO insertUser(UserEntity userEntity) {
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
    @CachePut(value = "user", key = "#userEntity.idx")
    @Modifying(clearAutomatically = true)
    @Transactional
    public UserDTO updateUser(UserEntity userEntity) {
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
    @CacheEvict(value = "user", key = "#idx")
    @Modifying(clearAutomatically = true)
    @Transactional
    public Long deleteUser(Long idx) {
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
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 12. 07.
     * </pre>
     */
    @CachePut(value = "user", key = "#idx")
    @Modifying(clearAutomatically = true)
    @Transactional
    public UserDTO addFavoriteTravel(Long idx, Long favoriteIdx) {
        try {
            return userRepository.addFavoriteTravel(idx, favoriteIdx);
        } catch (Exception e) {
            throw new TravelException(ERROR_FAVORITE_TRAVEL, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findUserSchedule
     * 2. ClassName  : UserService.java
     * 3. Comment    : 유저가 작성한 스케줄 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 12. 14.
     * </pre>
     */
    @Cacheable(value = "schedule", key = "#userIdx")
    @Transactional(readOnly = true)
    public List<TravelScheduleDTO> findUserSchedule(Long userIdx) {
        return userRepository.findUserSchedule(userIdx);
    }

    /**
     * <pre>
     * 1. MethodName : findOneUserSchedule
     * 2. ClassName  : UserService.java
     * 3. Comment    : 유저가 작성한 스케줄 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 12. 14.
     * </pre>
     */
    @Cacheable(value = "schedule", key = "#scheduleIdx")
    @Transactional(readOnly = true)
    public TravelScheduleDTO findOneUserSchedule(Long userIdx, Long scheduleIdx) {
        return userRepository.findOneUserSchedule(userIdx, scheduleIdx);
    }
}

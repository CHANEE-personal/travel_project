package com.travel.travel_project.api.user;

import com.travel.travel_project.api.common.CommonRepository;
import com.travel.travel_project.api.travel.group.GroupRepository;
import com.travel.travel_project.api.travel.group.GroupUserRepository;
import com.travel.travel_project.api.travel.schedule.ScheduleRepository;
import com.travel.travel_project.domain.common.CommonEntity;
import com.travel.travel_project.domain.travel.group.TravelGroupEntity;
import com.travel.travel_project.domain.travel.group.TravelGroupUserDTO;
import com.travel.travel_project.domain.travel.group.TravelGroupUserEntity;
import com.travel.travel_project.domain.travel.schedule.TravelScheduleDTO;
import com.travel.travel_project.domain.travel.schedule.TravelScheduleEntity;
import com.travel.travel_project.domain.user.UserDTO;
import com.travel.travel_project.domain.user.UserEntity;
import com.travel.travel_project.exception.TravelException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.travel.travel_project.exception.ApiExceptionType.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserQueryRepository userQueryRepository;
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;
    private final CommonRepository commonRepository;
    private final GroupRepository groupRepository;
    private final GroupUserRepository groupUserRepository;

    private UserEntity oneUser(Long idx) {
        return userRepository.findById(idx)
                .orElseThrow(() -> new TravelException(NOT_FOUND_USER));
    }

    private CommonEntity oneCommon(Integer commonCode) {
        return commonRepository.findByCommonCode(commonCode)
                .orElseThrow(() -> new TravelException(NOT_FOUND_FAQ));
    }

    private TravelScheduleEntity oneSchedule(Long idx) {
        return scheduleRepository.findById(idx)
                .orElseThrow(() -> new TravelException(NOT_FOUND_SCHEDULE));
    }

    private TravelGroupEntity oneGroup(Long idx) {
        return groupRepository.findById(idx)
                .orElseThrow(() -> new TravelException(NOT_FOUND_TRAVEL_GROUP));
    }

    @Transactional(readOnly = true)
    public String adminLogin(UserEntity userEntity) {
        try {
            return userQueryRepository.adminLogin(userEntity);
        } catch (Exception e) {
            throw new TravelException(NOT_FOUND_USER);
        }
    }

    @Transactional
    public void insertToken(UserEntity paramUserEntity) {
        try {
            UserEntity userEntity = oneUser(paramUserEntity.getIdx());
            userQueryRepository.insertUserToken(userEntity);
        } catch (Exception e) {
            throw new TravelException(ERROR_USER);
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
    @Transactional(readOnly = true)
    public Page<UserDTO> findUserList(Map<String, Object> userMap, PageRequest pageRequest) {
        return userQueryRepository.findUserList(userMap, pageRequest);
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
    public UserDTO findOneUser(Long idx) {
        return UserEntity.toDto(oneUser(idx));
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
        return userQueryRepository.findOneUserById(userId);
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
        return userQueryRepository.findOneUserByToken(token);
    }

    /**
     * <pre>
     * 1. MethodName : insertUser
     * 2. ClassName  : UserService.java
     * 3. Comment    : 유저 회원가입
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 10. 11.
     * </pre>
     */
    @Transactional
    public UserDTO insertUser(UserEntity userEntity) {
        try {
            return UserEntity.toDto(userRepository.save(userEntity));
        } catch (Exception e) {
            throw new TravelException(ERROR_USER);
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
    @Transactional
    public UserDTO updateUser(Long idx, UserEntity userEntity) {
        try {
            oneUser(idx).update(userEntity);
            return UserEntity.toDto(userEntity);
        } catch (Exception e) {
            throw new TravelException(ERROR_UPDATE_USER);
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
    @Transactional
    public Long deleteUser(Long idx) {
        try {
            userRepository.deleteById(idx);
            return idx;
        } catch (Exception e) {
            throw new TravelException(ERROR_DELETE_USER);
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
    @Transactional
    public UserDTO addFavoriteTravel(Long idx, Long favoriteIdx) {
        try {
            return userQueryRepository.addFavoriteTravel(idx, favoriteIdx);
        } catch (Exception e) {
            throw new TravelException(ERROR_FAVORITE_TRAVEL);
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
    @Transactional(readOnly = true)
    public List<TravelScheduleDTO> findUserSchedule(Long userIdx) {
        return userQueryRepository.findUserSchedule(userIdx);
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
    @Transactional(readOnly = true)
    public TravelScheduleDTO findOneUserSchedule(Long userIdx, Long scheduleIdx) {
        return userQueryRepository.findOneUserSchedule(userIdx, scheduleIdx);
    }

    /**
     * <pre>
     * 1. MethodName : insertTravelSchedule
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 유저 여행 스케줄 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 12. 13.
     * </pre>
     */
    @Transactional
    public TravelScheduleDTO insertTravelSchedule(Long idx, TravelScheduleEntity travelScheduleEntity) {
        try {
            System.out.println("===common===");
            System.out.println(travelScheduleEntity.getScheduleDescription());
            System.out.println(travelScheduleEntity.getCommonEntity());
            System.out.println(travelScheduleEntity.getCommonEntity().getCommonCode());
            System.out.println(oneCommon(travelScheduleEntity.getCommonEntity().getCommonCode()));
            oneCommon(travelScheduleEntity.getCommonEntity().getCommonCode()).addSchedule(travelScheduleEntity);
            oneUser(idx).addSchedule(travelScheduleEntity);
            return TravelScheduleEntity.toDto(scheduleRepository.save(travelScheduleEntity));
        } catch (Exception e) {
            throw new TravelException(ERROR_TRAVEL_SCHEDULE);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updateTravelSchedule
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 유저 여행 스케줄 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 12. 13.
     * </pre>
     */
    @Transactional
    public TravelScheduleDTO updateTravelSchedule(Long idx, TravelScheduleEntity travelScheduleEntity) {
        try {
            oneSchedule(idx).update(travelScheduleEntity);
            return TravelScheduleEntity.toDto(travelScheduleEntity);
        } catch (Exception e) {
            throw new TravelException(ERROR_UPDATE_TRAVEL_SCHEDULE);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteTravelSchedule
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 유저 여행 스케줄 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 12. 13.
     * </pre>
     */
    @Transactional
    public Long deleteTravelSchedule(Long idx) {
        try {
            scheduleRepository.deleteById(idx);
            return idx;
        } catch (Exception e) {
            throw new TravelException(ERROR_DELETE_TRAVEL_SCHEDULE);
        }
    }

    /**
     * <pre>
     * 1. MethodName : insertTravelGroupUser
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 유저 여행 그룹 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 27.
     * </pre>
     */
    @Transactional
    public TravelGroupUserDTO insertTravelGroupUser(Long idx, Long groupIdx, TravelGroupUserEntity travelGroupUserEntity) {
        try {
            oneGroup(groupIdx).addGroup(travelGroupUserEntity);
            oneUser(idx).addGroup(travelGroupUserEntity);
            return TravelGroupUserEntity.toDto(groupUserRepository.save(travelGroupUserEntity));
        } catch (Exception e) {
            throw new TravelException(ERROR_TRAVEL_GROUP_UESR);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteTravelGroupUser
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 유저 여행 그룹 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 27.
     * </pre>
     */
    @Transactional
    public Long deleteTravelGroupUser(Long idx) {
        try {
            groupUserRepository.findById(idx);
            return idx;
        } catch (Exception e) {
            throw new TravelException(ERROR_DELETE_TRAVEL_GROUP_USER);
        }
    }
}

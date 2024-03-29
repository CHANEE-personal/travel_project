package com.travel.api.user;

import com.travel.api.common.domain.CommonEntity;
import com.travel.api.common.domain.repository.CommonRepository;
import com.travel.api.travel.domain.group.TravelGroupEntity;
import com.travel.api.travel.domain.group.TravelGroupUserDTO;
import com.travel.api.travel.domain.group.TravelGroupUserEntity;
import com.travel.api.travel.domain.group.repository.GroupRepository;
import com.travel.api.travel.domain.group.repository.GroupUserRepository;
import com.travel.api.travel.domain.reservation.TravelReservationEntity;
import com.travel.api.travel.domain.reservation.repository.TravelReservationRepository;
import com.travel.api.travel.domain.schedule.TravelScheduleDTO;
import com.travel.api.travel.domain.schedule.TravelScheduleEntity;
import com.travel.api.travel.domain.schedule.repository.ScheduleRepository;
import com.travel.api.user.domain.*;
import com.travel.api.user.domain.repository.UserQueryRepository;
import com.travel.api.user.domain.repository.UserRepository;
import com.travel.api.user.domain.reservation.UserReservationDTO;
import com.travel.api.user.domain.reservation.UserReservationEntity;
import com.travel.api.user.domain.reservation.reservation.UserReservationRepository;
import com.travel.exception.TravelException;
import com.travel.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.travel.exception.ApiExceptionType.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserQueryRepository userQueryRepository;
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;
    private final CommonRepository commonRepository;
    private final GroupRepository groupRepository;
    private final GroupUserRepository groupUserRepository;
    private final UserReservationRepository userReservationRepository;
    private final TravelReservationRepository travelReservationRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

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

    private TravelReservationEntity oneReservation(Long idx) {
        return travelReservationRepository.findById(idx)
                .orElseThrow(() -> new TravelException(NOT_FOUND_RESERVATION));
    }

    @Transactional
    public JwtUtil.TokenInfo adminLogin(LoginRequest loginRequest) {
        // 패스워드 일치할 시
        if (passwordEncoder.matches(loginRequest.getPassword(), findOneUserById(loginRequest.getUserId()).getPassword())) {
            Authentication authentication = authenticate(loginRequest.getUserId(), loginRequest.getPassword());
            if (authentication != null) {
                Object principal = authentication.getPrincipal();
                if (principal instanceof AuthenticationRequest) {
                    AuthenticationRequest principalDetails = (AuthenticationRequest) principal;
                    UserEntity user = principalDetails.getUserEntity();
                    // accessToken
                    String accessToken = jwtUtil.doGenerateToken(principalDetails.getUsername());
                    user.updateToken(accessToken);
                    // refreshToken
                    String refreshToken = jwtUtil.doGenerateRefreshToken(principalDetails.getUsername());
                    user.updateRefreshToken(refreshToken);

                    return jwtUtil.getJwtTokens(accessToken, refreshToken);
                }
            }
        }
        return null;
    }

    private Authentication authenticate(String userId, String password) {
        try {
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userId, password));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("BadCredentialsException");
        } catch (DisabledException e) {
            throw new DisabledException("DisabledException");
        } catch (LockedException e) {
            throw new LockedException("LockedException");
        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException("UsernameNotFoundException");
        } catch (AuthenticationException e) {
            log.error(e.getMessage());
        }

        return null;
    }

    /**
     * <pre>
     * 1. MethodName : findOneUser
     * 2. ClassName  : UserService.java
     * 3. Comment    : idx 이용한 유저 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 10. 11.
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
     * 3. Comment    : 유저 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 10. 11.
     * </pre>
     */
    @Transactional(readOnly = true)
    public UserDTO findOneUserById(String id) {
        UserEntity oneUser = userRepository.findByUserId(id)
                .orElseThrow(() -> new TravelException(NOT_FOUND_USER));
        return UserEntity.toDto(oneUser);
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
            if (userRepository.findByUserId(userEntity.getUserId()).isPresent()) {
                throw new TravelException(EXIST_USER);
            }

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
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 10. 11.
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
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 10. 11.
     * </pre>
     */
    @Transactional
    public void deleteUser(UserEntity userEntity) {
        try {
            userRepository.delete(userEntity);
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
        return scheduleRepository.findUserSchedule(userIdx)
                .stream().map(TravelScheduleEntity::toDto)
                .collect(Collectors.toList());
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
        return TravelScheduleEntity.toDto(scheduleRepository.findOneUserSchedule(userIdx, scheduleIdx)
                .orElseThrow(() -> new TravelException(NOT_FOUND_SCHEDULE)));
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
     * 1. MethodName : findTravelReservation
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 유저 여행 예약 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 02. 04.
     * </pre>
     */
    @Transactional(readOnly = true)
    public List<UserReservationDTO> findTravelReservation(Long userIdx) {
        return userReservationRepository.findUserReservationList(userIdx)
                .stream().map(UserReservationEntity::toDto).collect(Collectors.toList());
    }

    /**
     * <pre>
     * 1. MethodName : travelReservation
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 유저 여행 예약
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 02. 04.
     * </pre>
     */
    @Transactional
    public UserReservationDTO travelReservation(Long userIdx, Long reservationIdx, UserReservationEntity userReservation) {
        TravelReservationEntity oneReservation = oneReservation(reservationIdx);

        // 예약 가능 상태 체크
        if (oneReservation.getStatus()) {
            // 예약 가능 인원 체크
            if (oneReservation.getPossibleCount() <= 0 || oneReservation.getPossibleCount() < userReservation.getUserCount()) {
                throw new TravelException(POSSIBLE_COUNT);
            }

            // 예약 일자 체크
            if (oneReservation.getStartDate().isAfter(userReservation.getStartDate()) &&
                    oneReservation.getEndDate().isBefore(userReservation.getEndDate())) {
                throw new TravelException(POSSIBLE_DATE);
            }

            // 여행지 예약
            oneReservation(reservationIdx).addReservation(userReservation);
            oneUser(userIdx).addUser(userReservation);
            return UserReservationEntity.toDto(userReservationRepository.save(userReservation));
        } else {
            throw new TravelException(ERROR_RESERVATION);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteTravelReservation
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 유저 여행 예약 취소
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 02. 04.
     * </pre>
     */
    @Transactional
    public Long deleteTravelReservation(Long userIdx, Long reservationIdx) {
        if (oneUser(userIdx) != null) {
            userReservationRepository.deleteById(reservationIdx);
            return reservationIdx;
        } else {
            return null;
        }
    }

    /**
     * <pre>
     * 1. MethodName : insertTravelGroup
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 유저 여행 그룹 가입
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 02. 05.
     * </pre>
     */
    @Transactional
    public TravelGroupUserDTO insertTravelGroup(Long userIdx, Long groupIdx) {
        return TravelGroupUserEntity.toDto(groupUserRepository.save(
                TravelGroupUserEntity.builder()
                        .userEntity(oneUser(userIdx))
                        .travelGroupEntity(oneGroup(groupIdx))
                        .build()));
    }

    /**
     * <pre>
     * 1. MethodName : deleteTravelGroup
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 유저 여행 그룹 탈퇴
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 02. 05.
     * </pre>
     */
    @Transactional
    public Long deleteTravelGroup(Long userIdx, Long groupIdx) {
        TravelGroupUserEntity travelGroupUserEntity = groupUserRepository.findByUserGroup(userIdx, groupIdx)
                .orElseThrow(() -> new TravelException(NOT_FOUND_TRAVEL_GROUP));

        // 여행 그룹 탈퇴
        groupUserRepository.delete(travelGroupUserEntity);
        return travelGroupUserEntity.getIdx();
    }
}

package com.travel.api.user;

import com.travel.api.common.domain.CommonEntity;
import com.travel.api.common.domain.repository.CommonRepository;
import com.travel.api.travel.domain.group.TravelGroupEntity;
import com.travel.api.travel.domain.group.TravelGroupUserDTO;
import com.travel.api.travel.domain.group.TravelGroupUserEntity;
import com.travel.api.travel.domain.group.repository.GroupRepository;
import com.travel.api.travel.domain.group.repository.GroupUserRepository;
import com.travel.api.travel.domain.schedule.TravelScheduleDTO;
import com.travel.api.user.domain.*;
import com.travel.api.user.domain.repository.UserQueryRepository;
import com.travel.api.user.domain.repository.UserRepository;
import com.travel.exception.TravelException;
import com.travel.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.travel.exception.ApiExceptionType.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserQueryRepository userQueryRepository;
    private final UserRepository userRepository;
    private final CommonRepository commonRepository;
    private final GroupRepository groupRepository;
    private final GroupUserRepository groupUserRepository;
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

    private TravelGroupEntity oneGroup(Long idx) {
        return groupRepository.findById(idx)
                .orElseThrow(() -> new TravelException(NOT_FOUND_TRAVEL_GROUP));
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
        } catch(BadCredentialsException e) {
            throw new BadCredentialsException("BadCredentialsException");
        } catch(DisabledException e) {
            throw new DisabledException("DisabledException");
        } catch(LockedException e) {
            throw new LockedException("LockedException");
        } catch(UsernameNotFoundException e) {
            throw new UsernameNotFoundException("UsernameNotFoundException");
        } catch(AuthenticationException e) {
            log.error(e.getMessage());
        }

        return null;
    }

    /**
     * <pre>
     * 1. MethodName : findUserList
     * 2. ClassName  : UserService.java
     * 3. Comment    : 유저 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 10. 11.
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
    public UserDTO insertUser(SignUpRequest signUpRequest) {
        try {
            if (userRepository.findByUserId(signUpRequest.getUserId()).isPresent()) {
                throw new TravelException(EXIST_USER);
            }

            return UserEntity.toDto(userRepository.save(UserEntity.builder()
                    .userId(signUpRequest.getUserId())
                    .password(passwordEncoder.encode(signUpRequest.getPassword()))
                    .name(signUpRequest.getName())
                    .email(signUpRequest.getEmail())
                    .role(Role.ROLE_ADMIN)
                    .visible("Y")
                    .build()));

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

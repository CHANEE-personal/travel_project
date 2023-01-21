package com.travel.api.user;

import com.travel.api.travel.domain.group.TravelGroupUserDto;
import com.travel.api.travel.domain.group.TravelGroupUserEntity;
import com.travel.api.travel.domain.schedule.TravelScheduleDto;
import com.travel.api.user.domain.*;
import com.travel.common.Paging;
import com.travel.jwt.AuthenticationResponse;
import com.travel.jwt.JwtUtil;
import com.travel.jwt.MyUserDetailsService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URI;
import java.rmi.ServerError;
import java.util.List;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@Api(tags = "유저 관련 API")
@RequestMapping("/admin/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final MyUserDetailsService userDetailsService;
    private final JwtUtil jwtTokenUtil;

    /**
     * <pre>
     * 1. MethodName : findUserList
     * 2. ClassName  : UserController.java
     * 3. Comment    : 유저 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 10. 11.
     * </pre>
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "유저 조회", notes = "유저를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "유저 조회 성공", response = List.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping
    public ResponseEntity<Page<UserDto>> findUserList(@RequestParam(required = false) Map<String, Object> paramMap, Paging paging) {
        return ResponseEntity.ok(userService.findUserList(paramMap, paging.getPageRequest(paging.getPageNum(), paging.getSize())));
    }

    /**
     * <pre>
     * 1. MethodName : login
     * 2. ClassName  : UserController.java
     * 3. Comment    : 유저 로그인 처리
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 10. 11.
     * </pre>
     */
    @ApiOperation(value = "유저 로그인 처리", notes = "유저 로그인 처리한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "유저 로그인 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping("/login")
    public ResponseEntity<JwtUtil.TokenInfo> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) throws Exception {
        JwtUtil.TokenInfo jwtResponse = userService.adminLogin(loginRequest);
        jwtTokenUtil.setHeaderAccessToken(response, jwtResponse.getAccessToken());
        jwtTokenUtil.setHeaderRefreshToken(response, jwtResponse.getRefreshToken());
        return ok().body(jwtResponse);
    }

    @ApiOperation(value = "JWT 토큰 재발급", notes = "JWT 토큰을 재발급")
    @PostMapping(value = "/refresh")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "access-token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "REFRESH-TOKEN", value = "refresh-token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<?> createAuthenticationRefreshToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        // id, password 인증
        authenticate(authenticationRequest.getUserId(), authenticationRequest.getPassword());

        // 사용자 정보 조회 후 token 생성
        String refreshToken = jwtTokenUtil.generateRefreshToken(userDetailsService.loadUserByUsername(authenticationRequest.getUserId()));

        return ok(new AuthenticationResponse(refreshToken));
    }

    /**
     * <pre>
     * 1. MethodName : authenticate
     * 2. ClassName  : UserController.java
     * 3. Comment    : 로그인 시 인증
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 10. 11.
     * </pre>
     */
    private void authenticate(String id, String password) throws Exception {
        try {
            Authentication request = new UsernamePasswordAuthenticationToken(id, password);
            if (request.getName().equals(request.getCredentials())) {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getName(), request.getCredentials()));
            }
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED");
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS");
        }
    }

    /**
     * <pre>
     * 1. MethodName : insertUser
     * 2. ClassName  : UserController.java
     * 3. Comment    : 유저 회원가입
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 10. 11.
     * </pre>
     */
    @ApiOperation(value = "유저 회원가입 처리", notes = "유저 회원가입을 처리한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "회원가입 성공", response = UserDto.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping
    public ResponseEntity<UserDto> insertUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity.created(URI.create("")).body(userService.insertUser(signUpRequest));
    }

    /**
     * <pre>
     * 1. MethodName : updateUser
     * 2. ClassName  : UserController.java
     * 3. Comment    : 유저 정보 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 10. 11.
     * </pre>
     */
    @ApiOperation(value = "유저 정보 수정 처리", notes = "유저 정보 수정 처리한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "유저 정보 수정 성공", response = UserDto.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping("/{idx}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long idx, @CurrentUser UserEntity userEntity) {
        return ResponseEntity.ok(userService.updateUser(idx, userEntity));
    }

    /**
     * <pre>
     * 1. MethodName : deleteUser
     * 2. ClassName  : UserController.java
     * 3. Comment    : 유저 탈퇴
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 10. 11.
     * </pre>
     */
    @ApiOperation(value = "유저 탈퇴 처리", notes = "유저 탈퇴 처리한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "유저 탈퇴 성공", response = Long.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@CurrentUser UserEntity userEntity) {
        userService.deleteUser(userEntity);
        return ResponseEntity.noContent().build();
    }

    /**
     * <pre>
     * 1. MethodName : findUserSchedule
     * 2. ClassName  : UserController.java
     * 3. Comment    : 유저가 작성한 여행 스케줄 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 12. 14.
     * </pre>
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "유저가 작성한 여행 스케줄 리스트 조회", notes = "유저가 작성한 여행 스케줄 리스트를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "유저가 작성한 여행 스케줄 리스트 조회", response = List.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{idx}/schedule")
    public ResponseEntity<List<TravelScheduleDto>> findUserSchedule(@PathVariable Long idx) {
        return ResponseEntity.ok(userService.findUserSchedule(idx));
    }

    /**
     * <pre>
     * 1. MethodName : findOneUserSchedule
     * 2. ClassName  : UserController.java
     * 3. Comment    : 유저가 작성한 여행 스케줄 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 12. 14.
     * </pre>
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "유저가 작성한 여행 스케줄 상세 조회", notes = "유저가 작성한 여행 스케줄을 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "유저가 작성한 여행 스케줄 상세 조회", response = TravelScheduleDto.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{idx}/schedule/{scheduleIdx}")
    public ResponseEntity<TravelScheduleDto> findOneUserSchedule(@PathVariable Long idx, @PathVariable Long scheduleIdx) {
        return ResponseEntity.ok(userService.findOneUserSchedule(idx, scheduleIdx));
    }

    /**
     * <pre>
     * 1. MethodName : insertTravelGroupUser
     * 2. ClassName  : TravelController.java
     * 3. Comment    : 유저 여행 그룹 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 27.
     * </pre>
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "유저 여행 그룹 등록", notes = "유저 여행 그룹을 등록한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "유저 여행 그룹 등록 성공", response = TravelGroupUserDto.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping("/{idx}/group_user/{groupIdx}")
    public ResponseEntity<TravelGroupUserDto> insertTravelGroupUser(@PathVariable Long idx, @PathVariable Long groupIdx, @Valid @RequestBody TravelGroupUserEntity travelGroupUserEntity) {
        return ResponseEntity.created(URI.create("")).body(userService.insertTravelGroupUser(idx, groupIdx, travelGroupUserEntity));
    }

    /**
     * <pre>
     * 1. MethodName : deleteTravelGroupUser
     * 2. ClassName  : TravelController.java
     * 3. Comment    : 유저 여행 그룹 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 27.
     * </pre>
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "유저 여행 그룹 삭제", notes = "유저 여행 그룹을 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "유저 여행 그룹 삭제 성공", response = Long.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @DeleteMapping("/group_user/{idx}")
    public ResponseEntity<Long> deleteTravelGroupUser(@PathVariable Long idx) {
        userService.deleteTravelGroupUser(idx);
        return ResponseEntity.noContent().build();
    }
}

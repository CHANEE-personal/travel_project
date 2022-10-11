package com.travel.travel_project.api.user;

import com.travel.travel_project.common.Page;
import com.travel.travel_project.common.SearchCommon;
import com.travel.travel_project.domain.user.AuthenticationRequest;
import com.travel.travel_project.domain.user.UserDTO;
import com.travel.travel_project.domain.user.UserEntity;
import com.travel.travel_project.jwt.AuthenticationResponse;
import com.travel.travel_project.jwt.JwtUtil;
import com.travel.travel_project.jwt.MyUserDetailsService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.rmi.ServerError;
import java.util.List;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@Api(tags = "유저 관련 API")
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final MyUserDetailsService userDetailsService;
    private final JwtUtil jwtTokenUtil;
    private final SearchCommon searchCommon;

    /**
     * <pre>
     * 1. MethodName : findUsersList
     * 2. ClassName  : UserController.java
     * 3. Comment    : 유저 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 10. 11.
     * </pre>
     */
    @ApiOperation(value = "유저 조회", notes = "유저를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "유저 조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping
    public List<UserDTO> findUsersList(@RequestParam(required = false) Map<String, Object> paramMap, Page page) {
        return userService.findUsersList(searchCommon.searchCommon(page, paramMap));
    }

//    /**
//     * <pre>
//     * 1. MethodName : login
//     * 2. ClassName  : UserController.java
//     * 3. Comment    : 유저 로그인 처리
//     * 4. 작성자       : CHO
//     * 5. 작성일       : 2022. 10. 11.
//     * </pre>
//     */
//    @ApiOperation(value = "유저 로그인 처리", notes = "유저 로그인 처리한다.")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "유저 로그인 성공", response = Map.class),
//            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
//            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
//            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
//            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
//    })
//    @PostMapping("/login")
//    public Map<String, Object> login(@RequestBody AuthenticationRequest authenticationRequest, HttpServletResponse response) throws Exception {
//        Map<String, Object> userMap = new HashMap<>();
//
//        UserEntity userEntity = UserEntity.builder()
//                .userId(authenticationRequest.getUserId())
//                .password(authenticationRequest.getPassword())
//                .build();
//
//        if ("Y".equals(userService.adminLogin(userEntity))) {
//            userMap.put("loginYn", "Y");
//            userMap.put("userId", userEntity.getUserId());
//            userMap.put("token", createAuthenticationToken(authenticationRequest));
//
//            // 로그인 완료 시 생성된 token 값 DB에 저장
//            UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUserId());
//            String accessToken = jwtTokenUtil.generateToken(userDetails);
//            String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);
//            userEntity.setUserToken(accessToken);
//            userEntity.setUserRefreshToken(refreshToken);
//            jwtTokenUtil.setHeaderAccessToken(response, accessToken);
//            jwtTokenUtil.setHeaderRefreshToken(response, refreshToken);
//
//            userService.insertToken(adminUserEntity);
//        }
//
//        return userMap;
//    }

    /**
     * <pre>
     * 1. MethodName : createAuthenticationToken
     * 2. ClassName  : UserController.java
     * 3. Comment    : 로그인 시 JWT 토큰 발급
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 10. 11.
     * </pre>
     */
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        // id, password 인증
        authenticate(authenticationRequest.getUserId(), authenticationRequest.getPassword());

        // 사용자 정보 조회 후 token 생성
        String token = jwtTokenUtil.generateToken(userDetailsService.loadUserByUsername(authenticationRequest.getUserId()));

        return ok(new AuthenticationResponse(token));
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
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 10. 11.
     * </pre>
     */
    private void authenticate(String id, String password) throws Exception {
        try {
            Authentication request = new UsernamePasswordAuthenticationToken(id, password);
            if (request.getName().equals(request.getCredentials())) {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getName(), request.getCredentials()));
            }
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : insertUser
     * 2. ClassName  : UserController.java
     * 3. Comment    : 유저 회원가입
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 10. 11.
     * </pre>
     */
    @ApiOperation(value = "유저 회원가입 처리", notes = "유저 회원가입을 처리한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "회원가입 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping
    public UserDTO insertUser(@Valid @RequestBody UserEntity userEntity) {
        return userService.insertUser(userEntity);
    }

    /**
     * <pre>
     * 1. MethodName : updateUser
     * 2. ClassName  : UserController.java
     * 3. Comment    : 유저 정보 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 10. 11.
     * </pre>
     */
    @ApiOperation(value = "유저 정보 수정 처리", notes = "유저 정보 수정 처리한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "유저 정보 수정 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping("/{idx}")
    public UserDTO updateUser(@Valid @RequestBody UserEntity userEntity) {
        return userService.updateUser(userEntity);
    }

    /**
     * <pre>
     * 1. MethodName : deleteUser
     * 2. ClassName  : UserController.java
     * 3. Comment    : 유저 탈퇴
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 10. 11.
     * </pre>
     */
    @ApiOperation(value = "유저 탈퇴 처리", notes = "유저 탈퇴 처리한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "유저 탈퇴 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @DeleteMapping("/{idx}")
    public Long deleteUser(@PathVariable Long idx) {
        return userService.deleteUser(idx);
    }
}

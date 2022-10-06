package com.travel.travel_project.api.user;

import com.travel.travel_project.domain.user.UserDTO;
import com.travel.travel_project.exception.TravelException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.travel.travel_project.exception.ApiExceptionType.NOT_FOUND_USER;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

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
}

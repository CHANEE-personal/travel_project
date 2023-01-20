package com.travel.travel_project.jwt;

import com.travel.travel_project.api.user.UserRepository;
import com.travel.travel_project.api.user.UserService;
import com.travel.travel_project.domain.user.AuthenticationRequest;
import com.travel.travel_project.domain.user.UserDTO;
import com.travel.travel_project.domain.user.UserEntity;
import com.travel.travel_project.exception.TravelException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.travel.travel_project.exception.ApiExceptionType.NOT_FOUND_USER;
import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {

        try {
            UserEntity userEntity = userRepository.findByUserId(id)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with id"));

            // 아이디 일치하는지 확인
            return new AuthenticationRequest(userEntity);
        } catch (Exception e) {
            throw new TravelException(NOT_FOUND_USER);
        }
    }
}

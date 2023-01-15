package com.travel.travel_project.jwt;

import com.travel.travel_project.api.user.UserService;
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

import static com.travel.travel_project.exception.ApiExceptionType.NOT_FOUND_USER;
import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {

        try {
            UserDTO userInfo = userService.findOneUserById(id);

            // 아이디 일치하는지 확인
            return new User(userInfo.getName(), userInfo.getPassword(), createAuthorityList("ROLE_ADMIN"));
        } catch (Exception e) {
            throw new TravelException(NOT_FOUND_USER);
        }
    }
}

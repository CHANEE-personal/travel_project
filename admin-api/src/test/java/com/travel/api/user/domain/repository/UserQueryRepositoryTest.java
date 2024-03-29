package com.travel.api.user.domain.repository;

import com.travel.api.user.domain.UserDto;
import com.travel.api.user.domain.UserEntity;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.TypeDef;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.travel.api.user.domain.Role.ROLE_ADMIN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@ExtendWith(MockitoExtension.class)
@DisplayName("유저 Repository Test")
@TypeDef(name = "json", typeClass = JsonStringType.class)
class UserQueryRepositoryTest {
    @Mock
    private UserQueryRepository mockUserQueryRepository;
    private final UserQueryRepository userQueryRepository;
    private final EntityManager em;

    private UserEntity userEntity;
    private UserDto userDTO;

    void createUser() {
        userEntity = UserEntity.builder()
                .userId("cksgml159")
                .email("cksgml159@naver.com")
                .role(ROLE_ADMIN)
                .visible("Y")
                .build();

        userDTO = UserEntity.toDto(userEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createUser();
    }

    @Test
    @Disabled
    @DisplayName("유저리스트조회테스트")
    void 유저리스트조회테스트() {
        // given
        Map<String, Object> userMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(0, 3);
        Page<UserDto> userList = userQueryRepository.findUserList(userMap, pageRequest);

        // then
        assertThat(userList.getTotalElements()).isGreaterThan(0);
    }

    @Test
    @DisplayName("유저 리스트 Mockito 검색 조회 테스트")
    void 유저리스트Mockito검색조회테스트() {
        // given
        Map<String, Object> userMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(0, 3);


        List<UserDto> userList = new ArrayList<>();
        userList.add(UserDto.builder().idx(1L).userId("test01")
                .name("조찬희").password("test01")
                .email("test01@test.com").visible("Y").build());

        Page<UserDto> resultPage = new PageImpl<>(userList, pageRequest, userList.size());

        // when
        when(mockUserQueryRepository.findUserList(userMap, pageRequest)).thenReturn(resultPage);
        Page<UserDto> newUserList = mockUserQueryRepository.findUserList(userMap, pageRequest);

        List<UserDto> findUserList = newUserList.stream().collect(Collectors.toList());

        // then
        assertThat(findUserList.get(0).getIdx()).isEqualTo(userList.get(0).getIdx());
        assertThat(findUserList.get(0).getUserId()).isEqualTo(userList.get(0).getUserId());
        assertThat(findUserList.get(0).getName()).isEqualTo(userList.get(0).getName());
        assertThat(findUserList.get(0).getEmail()).isEqualTo(userList.get(0).getEmail());

        // verify
        verify(mockUserQueryRepository, times(1)).findUserList(userMap, pageRequest);
        verify(mockUserQueryRepository, atLeastOnce()).findUserList(userMap, pageRequest);
        verifyNoMoreInteractions(mockUserQueryRepository);

        InOrder inOrder = inOrder(mockUserQueryRepository);
        inOrder.verify(mockUserQueryRepository).findUserList(userMap, pageRequest);
    }

    @Test
    @DisplayName("유저 리스트 BDD 검색 조회 테스트")
    void 유저리스트BDD검색조회테스트() {
        // given
        Map<String, Object> userMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(0, 3);

        List<UserDto> userList = new ArrayList<>();
        userList.add(UserDto.builder().idx(1L).userId("test01")
                .name("조찬희").password("test01")
                .email("test01@test.com").visible("Y").build());

        Page<UserDto> resultPage = new PageImpl<>(userList, pageRequest, userList.size());

        // when
        given(mockUserQueryRepository.findUserList(userMap, pageRequest)).willReturn(resultPage);
        Page<UserDto> newUserList = mockUserQueryRepository.findUserList(userMap, pageRequest);

        List<UserDto> findUserList = newUserList.stream().collect(Collectors.toList());

        // then
        assertThat(findUserList.get(0).getIdx()).isEqualTo(userList.get(0).getIdx());
        assertThat(findUserList.get(0).getUserId()).isEqualTo(userList.get(0).getUserId());
        assertThat(findUserList.get(0).getName()).isEqualTo(userList.get(0).getName());
        assertThat(findUserList.get(0).getEmail()).isEqualTo(userList.get(0).getEmail());

        // verify
        then(mockUserQueryRepository).should(times(1)).findUserList(userMap, pageRequest);
        then(mockUserQueryRepository).should(atLeastOnce()).findUserList(userMap, pageRequest);
        then(mockUserQueryRepository).shouldHaveNoMoreInteractions();
    }
}

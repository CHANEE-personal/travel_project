package com.travel.travel_project.api.user;

import com.travel.travel_project.domain.common.CommonEntity;
import com.travel.travel_project.domain.travel.schedule.TravelScheduleDTO;
import com.travel.travel_project.domain.travel.schedule.TravelScheduleEntity;
import com.travel.travel_project.domain.user.UserDTO;
import com.travel.travel_project.domain.user.UserEntity;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.TypeDef;
import org.json.JSONArray;
import org.json.JSONException;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.travel.travel_project.domain.user.Role.ROLE_ADMIN;
import static com.travel.travel_project.domain.user.Role.ROLE_TRAVEL_USER;
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
    private UserDTO userDTO;

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
        Page<UserDTO> userList = userQueryRepository.findUserList(userMap, pageRequest);

        // then
        assertThat(userList.getTotalElements()).isGreaterThan(0);
    }

    @Test
    @DisplayName("유저 리스트 Mockito 검색 조회 테스트")
    void 유저리스트Mockito검색조회테스트() {
        // given
        Map<String, Object> userMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(0, 3);


        List<UserDTO> userList = new ArrayList<>();
        userList.add(UserDTO.builder().idx(1L).userId("test01")
                        .adminName("관리자01").name("조찬희").password("test01")
                        .email("test01@test.com").visible("Y").build());

        Page<UserDTO> resultPage = new PageImpl<>(userList, pageRequest, userList.size());

        // when
        when(mockUserQueryRepository.findUserList(userMap, pageRequest)).thenReturn(resultPage);
        Page<UserDTO> newUserList = mockUserQueryRepository.findUserList(userMap, pageRequest);

        List<UserDTO> findUserList = newUserList.stream().collect(Collectors.toList());

        // then
        assertThat(findUserList.get(0).getIdx()).isEqualTo(userList.get(0).getIdx());
        assertThat(findUserList.get(0).getUserId()).isEqualTo(userList.get(0).getUserId());
        assertThat(findUserList.get(0).getAdminName()).isEqualTo(userList.get(0).getAdminName());
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
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<UserDTO> userList = new ArrayList<>();
        userList.add(UserDTO.builder().idx(1L).userId("test01")
                .adminName("관리자01").name("조찬희").password("test01")
                .email("test01@test.com").visible("Y").build());

        Page<UserDTO> resultPage = new PageImpl<>(userList, pageRequest, userList.size());

        // when
        given(mockUserQueryRepository.findUserList(userMap, pageRequest)).willReturn(resultPage);
        Page<UserDTO> newUserList = mockUserQueryRepository.findUserList(userMap, pageRequest);

        List<UserDTO> findUserList = newUserList.stream().collect(Collectors.toList());

        // then
        assertThat(findUserList.get(0).getIdx()).isEqualTo(userList.get(0).getIdx());
        assertThat(findUserList.get(0).getUserId()).isEqualTo(userList.get(0).getUserId());
        assertThat(findUserList.get(0).getAdminName()).isEqualTo(userList.get(0).getAdminName());
        assertThat(findUserList.get(0).getName()).isEqualTo(userList.get(0).getName());
        assertThat(findUserList.get(0).getEmail()).isEqualTo(userList.get(0).getEmail());

        // verify
        then(mockUserQueryRepository).should(times(1)).findUserList(userMap, pageRequest);
        then(mockUserQueryRepository).should(atLeastOnce()).findUserList(userMap, pageRequest);
        then(mockUserQueryRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("유저 좋아하는 여행지 추가")
    void 유저좋아하는여행지추가() throws JSONException {
        List<String> list = new ArrayList<>();
        list.add("1");

        userEntity = UserEntity.builder()
                .userId("cksgml159")
                .password("test159")
                .email("cksgml159@naver.com")
                .name("test")
                .role(ROLE_TRAVEL_USER)
                .favoriteTravelIdx(list)
                .visible("Y")
                .build();

        em.persist(userEntity);

        JSONArray jsonArray = new JSONArray();
        jsonArray.put(userEntity.getFavoriteTravelIdx());
        Object insertObject = jsonArray.get(0);

        assertThat(userEntity.getFavoriteTravelIdx()).isEqualTo(insertObject);

        UserDTO updateUser = userQueryRepository.addFavoriteTravel(userEntity.getIdx(), 2L);

        jsonArray.remove(0);
        jsonArray.put(updateUser.getFavoriteTravelIdx());
        Object updateObject = jsonArray.get(0);

        assertThat(updateUser.getFavoriteTravelIdx()).isEqualTo(updateObject);
    }
}
package com.travel.travel_project.api.user;

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
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
@DisplayName("?????? Repository Test")
@TypeDef(name = "json", typeClass = JsonStringType.class)
class UserRepositoryTest {
    @Mock
    private UserRepository mockUserRepository;
    private final UserRepository userRepository;
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
    @DisplayName("??????????????????????????????")
    void ??????????????????????????????() {
        // given
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("jpaStartPage", 1);
        userMap.put("size", 3);

        // then
        assertThat(userRepository.findUserList(userMap)).isNotEmpty();
    }

    @Test
    @DisplayName("?????? ????????? Mockito ?????? ?????? ?????????")
    void ???????????????Mockito?????????????????????() {
        // given
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("jpaStartPage", 1);
        userMap.put("size", 3);

        List<UserDTO> userList = new ArrayList<>();
        userList.add(UserDTO.builder().idx(1L).userId("test01")
                        .adminName("?????????01").name("?????????").password("test01")
                        .email("test01@test.com").visible("Y").build());

        // when
        when(mockUserRepository.findUserList(userMap)).thenReturn(userList);
        List<UserDTO> newUserList = mockUserRepository.findUserList(userMap);

        // then
        assertThat(newUserList.get(0).getIdx()).isEqualTo(userList.get(0).getIdx());
        assertThat(newUserList.get(0).getUserId()).isEqualTo(userList.get(0).getUserId());
        assertThat(newUserList.get(0).getAdminName()).isEqualTo(userList.get(0).getAdminName());
        assertThat(newUserList.get(0).getName()).isEqualTo(userList.get(0).getName());
        assertThat(newUserList.get(0).getEmail()).isEqualTo(userList.get(0).getEmail());

        // verify
        verify(mockUserRepository, times(1)).findUserList(userMap);
        verify(mockUserRepository, atLeastOnce()).findUserList(userMap);
        verifyNoMoreInteractions(mockUserRepository);

        InOrder inOrder = inOrder(mockUserRepository);
        inOrder.verify(mockUserRepository).findUserList(userMap);
    }

    @Test
    @DisplayName("?????? ????????? BDD ?????? ?????? ?????????")
    void ???????????????BDD?????????????????????() {
        // given
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("jpaStartPage", 1);
        userMap.put("size", 3);

        List<UserDTO> userList = new ArrayList<>();
        userList.add(UserDTO.builder().idx(1L).userId("test01")
                .adminName("?????????01").name("?????????").password("test01")
                .email("test01@test.com").visible("Y").build());

        // when
        given(mockUserRepository.findUserList(userMap)).willReturn(userList);
        List<UserDTO> newUserList = mockUserRepository.findUserList(userMap);

        // then
        assertThat(newUserList.get(0).getIdx()).isEqualTo(userList.get(0).getIdx());
        assertThat(newUserList.get(0).getUserId()).isEqualTo(userList.get(0).getUserId());
        assertThat(newUserList.get(0).getAdminName()).isEqualTo(userList.get(0).getAdminName());
        assertThat(newUserList.get(0).getName()).isEqualTo(userList.get(0).getName());
        assertThat(newUserList.get(0).getEmail()).isEqualTo(userList.get(0).getEmail());

        // verify
        then(mockUserRepository).should(times(1)).findUserList(userMap);
        then(mockUserRepository).should(atLeastOnce()).findUserList(userMap);
        then(mockUserRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("?????? ???????????? Mockito ?????????")
    void ??????????????????Mockito?????????() {
        // given
        UserEntity userEntity = UserEntity.builder()
                .idx(1L).userId("test01")
                .name("?????????").password("test01").role(ROLE_ADMIN)
                .email("test01@test.com").visible("Y").build();
        userRepository.insertUser(userEntity);
        userDTO = UserEntity.toDto(userEntity);

        // when
        when(mockUserRepository.findOneUser(userEntity.getIdx())).thenReturn(userDTO);
        UserDTO userInfo = mockUserRepository.findOneUser(userEntity.getIdx());

        // then
        assertThat(userInfo.getUserId()).isEqualTo("test01");
        assertThat(userInfo.getName()).isEqualTo("?????????");
        assertThat(userInfo.getEmail()).isEqualTo("test01@test.com");

        // verify
        verify(mockUserRepository, times(1)).findOneUser(userEntity.getIdx());
        verify(mockUserRepository, atLeastOnce()).findOneUser(userEntity.getIdx());
        verifyNoMoreInteractions(mockUserRepository);

        InOrder inOrder = inOrder(mockUserRepository);
        inOrder.verify(mockUserRepository).findOneUser(userEntity.getIdx());
    }

    @Test
    @DisplayName("?????? ???????????? BDD ?????????")
    void ??????????????????BDD?????????() {
        // given
        UserEntity userEntity = UserEntity.builder()
                .idx(1L).userId("test01")
                .name("?????????").password("test01").role(ROLE_ADMIN)
                .email("test01@test.com").visible("Y").build();
        userRepository.insertUser(userEntity);
        userDTO = UserEntity.toDto(userEntity);

        // when
        given(mockUserRepository.findOneUser(userEntity.getIdx())).willReturn(userDTO);
        UserDTO userInfo = mockUserRepository.findOneUser(userEntity.getIdx());

        // then
        assertThat(userInfo.getUserId()).isEqualTo("test01");
        assertThat(userInfo.getName()).isEqualTo("?????????");
        assertThat(userInfo.getEmail()).isEqualTo("test01@test.com");

        // verify
        then(mockUserRepository).should(times(1)).findOneUser(userEntity.getIdx());
        then(mockUserRepository).should(atLeastOnce()).findOneUser(userEntity.getIdx());
        then(mockUserRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("?????? ???????????? Mockito ?????????")
    void ??????????????????Mockito?????????() {
        // given
        UserEntity userEntity = UserEntity.builder()
                .userId("test")
                .password("test")
                .name("test")
                .email("test@test.com")
                .role(ROLE_ADMIN)
                .visible("Y")
                .build();

        Long idx = userRepository.insertUser(userEntity).getIdx();

        UserEntity newUserEntity = UserEntity.builder()
                .idx(idx)
                .userId("test1")
                .password("test1")
                .name("test1")
                .email("test1@test.com")
                .role(ROLE_ADMIN)
                .visible("Y")
                .build();

        userRepository.updateUser(newUserEntity);
        UserDTO newUserDTO = UserEntity.toDto(newUserEntity);

        // when
        when(mockUserRepository.findOneUser(newUserEntity.getIdx())).thenReturn(newUserDTO);
        UserDTO userInfo = mockUserRepository.findOneUser(newUserEntity.getIdx());

        // then
        assertThat(userInfo.getUserId()).isEqualTo("test1");
        assertThat(userInfo.getName()).isEqualTo("test1");

        // verify
        verify(mockUserRepository, times(1)).findOneUser(newUserEntity.getIdx());
        verify(mockUserRepository, atLeastOnce()).findOneUser(newUserEntity.getIdx());
        verifyNoMoreInteractions(mockUserRepository);

        InOrder inOrder = inOrder(mockUserRepository);
        inOrder.verify(mockUserRepository).findOneUser(newUserEntity.getIdx());
    }

    @Test
    @DisplayName("?????? ???????????? BDD ?????????")
    void ??????????????????BDD?????????() {
        // given
        UserEntity userEntity = UserEntity.builder()
                .userId("test")
                .password("test")
                .name("test")
                .email("test@test.com")
                .role(ROLE_ADMIN)
                .visible("Y")
                .build();

        Long idx = userRepository.insertUser(userEntity).getIdx();

        UserEntity newUserEntity = UserEntity.builder()
                .idx(idx)
                .userId("test1")
                .password("test1")
                .name("test1")
                .email("test1@test.com")
                .visible("Y")
                .build();

        userRepository.updateUser(newUserEntity);
        UserDTO newUserDTO = UserEntity.toDto(newUserEntity);

        // when
        given(mockUserRepository.findOneUser(newUserEntity.getIdx())).willReturn(newUserDTO);
        UserDTO userInfo = mockUserRepository.findOneUser(newUserEntity.getIdx());

        // then
        assertThat(userInfo.getUserId()).isEqualTo("test1");
        assertThat(userInfo.getName()).isEqualTo("test1");

        // verify
        then(mockUserRepository).should(times(1)).findOneUser(newUserEntity.getIdx());
        then(mockUserRepository).should(atLeastOnce()).findOneUser(newUserEntity.getIdx());
        then(mockUserRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("?????? ???????????? ?????????")
    void ?????????????????????() {
        assertThat(userRepository.deleteUser(userEntity.getIdx())).isEqualTo(userDTO.getIdx());
    }

    @Test
    @DisplayName("?????? ???????????? Mockito ?????????")
    void ??????????????????Mockito?????????() {
        UserDTO userDTO = userRepository.insertUser(userEntity);
        // when
        when(mockUserRepository.findOneUser(userDTO.getIdx())).thenReturn(userDTO);
        Long deleteIdx = userRepository.deleteUser(userDTO.getIdx());

        // then
        assertThat(mockUserRepository.findOneUser(userDTO.getIdx()).getIdx()).isEqualTo(deleteIdx);

        // verify
        verify(mockUserRepository, times(1)).findOneUser(userDTO.getIdx());
        verify(mockUserRepository, atLeastOnce()).findOneUser(userDTO.getIdx());
        verifyNoMoreInteractions(mockUserRepository);

        InOrder inOrder = inOrder(mockUserRepository);
        inOrder.verify(mockUserRepository).findOneUser(userDTO.getIdx());
    }

    @Test
    @DisplayName("?????? ???????????? BDD ?????????")
    void ??????????????????BDD?????????() {
        UserDTO userDTO = userRepository.insertUser(userEntity);
        // when
        given(mockUserRepository.findOneUser(userDTO.getIdx())).willReturn(userDTO);
        Long deleteIdx = userRepository.deleteUser(userDTO.getIdx());

        // then
        assertThat(mockUserRepository.findOneUser(userDTO.getIdx()).getIdx()).isEqualTo(deleteIdx);

        // verify
        then(mockUserRepository).should(times(1)).findOneUser(userDTO.getIdx());
        then(mockUserRepository).should(atLeastOnce()).findOneUser(userDTO.getIdx());
        then(mockUserRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("?????? ???????????? ????????? ??????")
    void ?????????????????????????????????() throws JSONException {
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

        UserDTO oneUser = userRepository.insertUser(userEntity);

        JSONArray jsonArray = new JSONArray();
        jsonArray.put(oneUser.getFavoriteTravelIdx());
        Object insertObject = jsonArray.get(0);

        assertThat(oneUser.getFavoriteTravelIdx()).isEqualTo(insertObject);

        UserDTO updateUser = userRepository.addFavoriteTravel(oneUser.getIdx(), 2L);

        jsonArray.remove(0);
        jsonArray.put(updateUser.getFavoriteTravelIdx());
        Object updateObject = jsonArray.get(0);

        assertThat(updateUser.getFavoriteTravelIdx()).isEqualTo(updateObject);
    }

    @Test
    @DisplayName("????????? ????????? ????????? ????????? ??????")
    void ??????????????????????????????????????????() {
        // given
        TravelScheduleDTO travelScheduleEntity = TravelScheduleDTO.builder()
                .userIdx(1L)
                .travelIdx(1L)
                .scheduleDescription("????????? ?????????")
                .scheduleTime(LocalDateTime.now())
                .build();

        List<TravelScheduleDTO> userSchedule = new ArrayList<>();
        userSchedule.add(travelScheduleEntity);

        // when
        when(mockUserRepository.findUserSchedule(1L)).thenReturn(userSchedule);
        List<TravelScheduleDTO> scheduleList = mockUserRepository.findUserSchedule(1L);

        // then
        assertThat(scheduleList.get(0).getScheduleDescription()).isEqualTo("????????? ?????????");

        // verify
        verify(mockUserRepository, times(1)).findUserSchedule(1L);
        verify(mockUserRepository, atLeastOnce()).findUserSchedule(1L);
        verifyNoMoreInteractions(mockUserRepository);

        InOrder inOrder = inOrder(mockUserRepository);
        inOrder.verify(mockUserRepository).findUserSchedule(1L);
    }

    @Test
    @DisplayName("????????? ????????? ????????? ?????? ??????")
    void ???????????????????????????????????????() {
        // given
        TravelScheduleEntity travelScheduleEntity = TravelScheduleEntity.builder()
                .userIdx(1L)
                .travelIdx(1L)
                .scheduleDescription("????????? ?????????")
                .scheduleTime(LocalDateTime.now())
                .build();

        em.persist(travelScheduleEntity);
        TravelScheduleDTO travelScheduleDTO = TravelScheduleEntity.toDto(travelScheduleEntity);

        // when
        when(mockUserRepository.findOneUserSchedule(travelScheduleDTO.getUserIdx(), travelScheduleDTO.getIdx())).thenReturn(travelScheduleDTO);
        TravelScheduleDTO oneUserSchedule = mockUserRepository.findOneUserSchedule(travelScheduleDTO.getUserIdx(), travelScheduleDTO.getIdx());

        // then
        assertThat(oneUserSchedule.getScheduleDescription()).isEqualTo("????????? ?????????");

        // verify
        verify(mockUserRepository, times(1)).findOneUserSchedule(travelScheduleDTO.getUserIdx(), travelScheduleDTO.getIdx());
        verify(mockUserRepository, atLeastOnce()).findOneUserSchedule(travelScheduleDTO.getUserIdx(), travelScheduleDTO.getIdx());
        verifyNoMoreInteractions(mockUserRepository);

        InOrder inOrder = inOrder(mockUserRepository);
        inOrder.verify(mockUserRepository).findOneUserSchedule(travelScheduleDTO.getUserIdx(), travelScheduleDTO.getIdx());
    }
}
package com.travel.travel_project.api.user;

import com.travel.travel_project.domain.travel.schedule.TravelScheduleDTO;
import com.travel.travel_project.domain.travel.schedule.TravelScheduleEntity;
import com.travel.travel_project.domain.user.UserDTO;
import com.travel.travel_project.domain.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
import static org.mockito.Mockito.atLeastOnce;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@DisplayName("?????? Service Test")
class UserServiceTest {
    @Mock
    private UserService mockUserService;
    private final UserService userService;
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
        assertThat(userService.findUserList(userMap)).isNotEmpty();
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
        when(mockUserService.findUserList(userMap)).thenReturn(userList);
        List<UserDTO> newUserList = mockUserService.findUserList(userMap);

        // then
        assertThat(newUserList.get(0).getIdx()).isEqualTo(userList.get(0).getIdx());
        assertThat(newUserList.get(0).getUserId()).isEqualTo(userList.get(0).getUserId());
        assertThat(newUserList.get(0).getAdminName()).isEqualTo(userList.get(0).getAdminName());
        assertThat(newUserList.get(0).getName()).isEqualTo(userList.get(0).getName());
        assertThat(newUserList.get(0).getEmail()).isEqualTo(userList.get(0).getEmail());

        // verify
        verify(mockUserService, times(1)).findUserList(userMap);
        verify(mockUserService, atLeastOnce()).findUserList(userMap);
        verifyNoMoreInteractions(mockUserService);

        InOrder inOrder = inOrder(mockUserService);
        inOrder.verify(mockUserService).findUserList(userMap);
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
        given(mockUserService.findUserList(userMap)).willReturn(userList);
        List<UserDTO> newUserList = mockUserService.findUserList(userMap);

        // then
        assertThat(newUserList.get(0).getIdx()).isEqualTo(userList.get(0).getIdx());
        assertThat(newUserList.get(0).getUserId()).isEqualTo(userList.get(0).getUserId());
        assertThat(newUserList.get(0).getAdminName()).isEqualTo(userList.get(0).getAdminName());
        assertThat(newUserList.get(0).getName()).isEqualTo(userList.get(0).getName());
        assertThat(newUserList.get(0).getEmail()).isEqualTo(userList.get(0).getEmail());

        // verify
        then(mockUserService).should(times(1)).findUserList(userMap);
        then(mockUserService).should(atLeastOnce()).findUserList(userMap);
        then(mockUserService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("?????? ???????????? Mockito ?????????")
    void ??????????????????Mockito?????????() {
        // given
        UserEntity userEntity = UserEntity.builder()
                .idx(1L).userId("test01")
                .name("?????????").password("test01")
                .email("test01@test.com").visible("Y").build();
        userService.insertUser(userEntity);
        userDTO = UserEntity.toDto(userEntity);

        // when
        when(mockUserService.findOneUser(userEntity.getIdx())).thenReturn(userDTO);
        UserDTO userInfo = mockUserService.findOneUser(userEntity.getIdx());

        // then
        assertThat(userInfo.getUserId()).isEqualTo("test01");
        assertThat(userInfo.getName()).isEqualTo("?????????");
        assertThat(userInfo.getEmail()).isEqualTo("test01@test.com");

        // verify
        verify(mockUserService, times(1)).findOneUser(userEntity.getIdx());
        verify(mockUserService, atLeastOnce()).findOneUser(userEntity.getIdx());
        verifyNoMoreInteractions(mockUserService);

        InOrder inOrder = inOrder(mockUserService);
        inOrder.verify(mockUserService).findOneUser(userEntity.getIdx());
    }

    @Test
    @DisplayName("?????? ???????????? BDD ?????????")
    void ??????????????????BDD?????????() {
        // given
        UserEntity userEntity = UserEntity.builder()
                .idx(1L).userId("test01")
                .name("?????????").password("test01")
                .email("test01@test.com").visible("Y").build();
        userService.insertUser(userEntity);
        userDTO = UserEntity.toDto(userEntity);

        // when
        given(mockUserService.findOneUser(userEntity.getIdx())).willReturn(userDTO);
        UserDTO userInfo = mockUserService.findOneUser(userEntity.getIdx());

        // then
        assertThat(userInfo.getUserId()).isEqualTo("test01");
        assertThat(userInfo.getName()).isEqualTo("?????????");
        assertThat(userInfo.getEmail()).isEqualTo("test01@test.com");

        // verify
        then(mockUserService).should(times(1)).findOneUser(userEntity.getIdx());
        then(mockUserService).should(atLeastOnce()).findOneUser(userEntity.getIdx());
        then(mockUserService).shouldHaveNoMoreInteractions();
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
                .visible("Y")
                .build();

        Long idx = userService.insertUser(userEntity).getIdx();

        UserEntity newUserEntity = UserEntity.builder()
                .idx(idx)
                .userId("test1")
                .password("test1")
                .name("test1")
                .email("test1@test.com")
                .visible("Y")
                .build();

        userService.updateUser(newUserEntity);
        UserDTO newUserDTO = UserEntity.toDto(newUserEntity);

        // when
        when(mockUserService.findOneUser(newUserEntity.getIdx())).thenReturn(newUserDTO);
        UserDTO userInfo = mockUserService.findOneUser(newUserEntity.getIdx());

        // then
        assertThat(userInfo.getUserId()).isEqualTo("test1");
        assertThat(userInfo.getName()).isEqualTo("test1");

        // verify
        verify(mockUserService, times(1)).findOneUser(newUserEntity.getIdx());
        verify(mockUserService, atLeastOnce()).findOneUser(newUserEntity.getIdx());
        verifyNoMoreInteractions(mockUserService);

        InOrder inOrder = inOrder(mockUserService);
        inOrder.verify(mockUserService).findOneUser(newUserEntity.getIdx());
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
                .visible("Y")
                .build();

        Long idx = userService.insertUser(userEntity).getIdx();

        UserEntity newUserEntity = UserEntity.builder()
                .idx(idx)
                .userId("test1")
                .password("test1")
                .name("test1")
                .email("test1@test.com")
                .visible("Y")
                .build();

        userService.updateUser(newUserEntity);
        UserDTO newUserDTO = UserEntity.toDto(newUserEntity);

        // when
        given(mockUserService.findOneUser(newUserEntity.getIdx())).willReturn(newUserDTO);
        UserDTO userInfo = mockUserService.findOneUser(newUserEntity.getIdx());

        // then
        assertThat(userInfo.getUserId()).isEqualTo("test1");
        assertThat(userInfo.getName()).isEqualTo("test1");

        // verify
        then(mockUserService).should(times(1)).findOneUser(newUserEntity.getIdx());
        then(mockUserService).should(atLeastOnce()).findOneUser(newUserEntity.getIdx());
        then(mockUserService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("?????? ???????????? ?????????")
    void ?????????????????????() {
        assertThat(userService.deleteUser(userEntity.getIdx())).isEqualTo(userDTO.getIdx());
    }

    @Test
    @DisplayName("?????? ???????????? Mockito ?????????")
    void ??????????????????Mockito?????????() {
        UserDTO userDTO = userService.insertUser(userEntity);
        // when
        when(mockUserService.findOneUser(userDTO.getIdx())).thenReturn(userDTO);
        Long deleteIdx = userService.deleteUser(userDTO.getIdx());

        // then
        assertThat(mockUserService.findOneUser(userDTO.getIdx()).getIdx()).isEqualTo(deleteIdx);

        // verify
        verify(mockUserService, times(1)).findOneUser(userDTO.getIdx());
        verify(mockUserService, atLeastOnce()).findOneUser(userDTO.getIdx());
        verifyNoMoreInteractions(mockUserService);

        InOrder inOrder = inOrder(mockUserService);
        inOrder.verify(mockUserService).findOneUser(userDTO.getIdx());
    }

    @Test
    @DisplayName("?????? ???????????? BDD ?????????")
    void ??????????????????BDD?????????() {
        UserDTO userDTO = userService.insertUser(userEntity);
        // when
        given(mockUserService.findOneUser(userDTO.getIdx())).willReturn(userDTO);
        Long deleteIdx = userService.deleteUser(userDTO.getIdx());

        // then
        assertThat(mockUserService.findOneUser(userDTO.getIdx()).getIdx()).isEqualTo(deleteIdx);

        // verify
        then(mockUserService).should(times(1)).findOneUser(userDTO.getIdx());
        then(mockUserService).should(atLeastOnce()).findOneUser(userDTO.getIdx());
        then(mockUserService).shouldHaveNoMoreInteractions();
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

        UserDTO oneUser = userService.insertUser(userEntity);

        JSONArray jsonArray = new JSONArray();
        jsonArray.put(oneUser.getFavoriteTravelIdx());
        Object insertObject = jsonArray.get(0);

        assertThat(oneUser.getFavoriteTravelIdx()).isEqualTo(insertObject);

        UserDTO updateUser = userService.addFavoriteTravel(oneUser.getIdx(), 2L);

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
        when(mockUserService.findUserSchedule(1L)).thenReturn(userSchedule);
        List<TravelScheduleDTO> scheduleList = mockUserService.findUserSchedule(1L);

        // then
        assertThat(scheduleList.get(0).getScheduleDescription()).isEqualTo("????????? ?????????");

        // verify
        verify(mockUserService, times(1)).findUserSchedule(1L);
        verify(mockUserService, atLeastOnce()).findUserSchedule(1L);
        verifyNoMoreInteractions(mockUserService);

        InOrder inOrder = inOrder(mockUserService);
        inOrder.verify(mockUserService).findUserSchedule(1L);
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
        when(mockUserService.findOneUserSchedule(travelScheduleDTO.getUserIdx(), travelScheduleDTO.getIdx())).thenReturn(travelScheduleDTO);
        TravelScheduleDTO oneUserSchedule = mockUserService.findOneUserSchedule(travelScheduleDTO.getUserIdx(), travelScheduleDTO.getIdx());

        // then
        assertThat(oneUserSchedule.getScheduleDescription()).isEqualTo("????????? ?????????");

        // verify
        verify(mockUserService, times(1)).findOneUserSchedule(travelScheduleDTO.getUserIdx(), travelScheduleDTO.getIdx());
        verify(mockUserService, atLeastOnce()).findOneUserSchedule(travelScheduleDTO.getUserIdx(), travelScheduleDTO.getIdx());
        verifyNoMoreInteractions(mockUserService);

        InOrder inOrder = inOrder(mockUserService);
        inOrder.verify(mockUserService).findOneUserSchedule(travelScheduleDTO.getUserIdx(), travelScheduleDTO.getIdx());
    }
}
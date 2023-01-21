package com.travel.api.user;

import com.travel.api.common.domain.CommonEntity;
import com.travel.api.travel.domain.schedule.TravelScheduleDTO;
import com.travel.api.travel.domain.schedule.TravelScheduleEntity;
import com.travel.api.user.domain.*;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
@DisplayName("유저 Service Test")
class UserServiceTest {
    @Mock
    private UserService mockUserService;
    private final UserService userService;
    private final EntityManager em;
    private final PasswordEncoder passwordEncoder;

    private UserEntity userEntity;
    private UserDTO userDTO;

    void createUser() {
        userEntity = UserEntity.builder()
                .userId("test111")
                .password("test111")
                .email("test@naver.com")
                .name("test")
                .role(Role.ROLE_TRAVEL_USER)
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
    @DisplayName("관리자 로그인 처리 테스트")
    void 관리자로그인처리테스트() {
        // given
        UserEntity adminUserEntity = UserEntity.builder()
                .userId("user05")
                .password(passwordEncoder.encode("pass1234"))
                .name("user05")
                .email("user05@admin.com")
                .visible("Y")
                .role(Role.ROLE_TRAVEL_USER)
                .build();

        em.persist(adminUserEntity);

        LoginRequest loginRequest = LoginRequest.builder().userId(adminUserEntity.getUserId())
                .password("pass1234").build();

        // then
        userService.adminLogin(loginRequest);
    }

    @Test
    @DisplayName("유저 회원가입 Mockito 테스트")
    void 유저회원가입Mockito테스트() {
        // given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .userId("test")
                .password("test")
                .name("test")
                .email("test@test.com")
                .visible("Y")
                .build();
        userService.insertUser(signUpRequest);
        userDTO = UserEntity.toDto(userEntity);

        // when
        when(mockUserService.findOneUser(userEntity.getIdx())).thenReturn(userDTO);
        UserDTO userInfo = mockUserService.findOneUser(userEntity.getIdx());

        // then
        assertThat(userInfo.getUserId()).isEqualTo("test01");
        assertThat(userInfo.getName()).isEqualTo("조찬희");
        assertThat(userInfo.getEmail()).isEqualTo("test01@test.com");

        // verify
        verify(mockUserService, times(1)).findOneUser(userEntity.getIdx());
        verify(mockUserService, atLeastOnce()).findOneUser(userEntity.getIdx());
        verifyNoMoreInteractions(mockUserService);

        InOrder inOrder = inOrder(mockUserService);
        inOrder.verify(mockUserService).findOneUser(userEntity.getIdx());
    }

    @Test
    @DisplayName("유저 회원가입 BDD 테스트")
    void 유저회원가입BDD테스트() {
        // given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .userId("test")
                .password("test")
                .name("test")
                .email("test@test.com")
                .visible("Y")
                .build();
        userService.insertUser(signUpRequest);
        userDTO = UserEntity.toDto(userEntity);

        // when
        given(mockUserService.findOneUser(userEntity.getIdx())).willReturn(userDTO);
        UserDTO userInfo = mockUserService.findOneUser(userEntity.getIdx());

        // then
        assertThat(userInfo.getUserId()).isEqualTo("test01");
        assertThat(userInfo.getName()).isEqualTo("조찬희");
        assertThat(userInfo.getEmail()).isEqualTo("test01@test.com");

        // verify
        then(mockUserService).should(times(1)).findOneUser(userEntity.getIdx());
        then(mockUserService).should(atLeastOnce()).findOneUser(userEntity.getIdx());
        then(mockUserService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("유저 회원수정 Mockito 테스트")
    void 유저회원수정Mockito테스트() {
        // given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .userId("test")
                .password("test")
                .name("test")
                .email("test@test.com")
                .visible("Y")
                .build();

        Long idx = userService.insertUser(signUpRequest).getIdx();

        UserEntity newUserEntity = UserEntity.builder()
                .idx(idx)
                .userId("test1")
                .password("test1")
                .name("test1")
                .role(Role.ROLE_TRAVEL_USER)
                .email("test1@test.com")
                .visible("Y")
                .build();

        userService.updateUser(idx, newUserEntity);
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
    @DisplayName("유저 회원수정 BDD 테스트")
    void 유저회원수정BDD테스트() {
        // given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .userId("test")
                .password("test")
                .name("test")
                .email("test@test.com")
                .visible("Y")
                .build();

        Long idx = userService.insertUser(signUpRequest).getIdx();

        UserEntity newUserEntity = UserEntity.builder()
                .idx(idx)
                .userId("test1")
                .password("test1")
                .name("test1")
                .email("test1@test.com")
                .role(Role.ROLE_TRAVEL_USER)
                .visible("Y")
                .build();

        userService.updateUser(idx, newUserEntity);
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
    @DisplayName("유저 회원탈퇴 테스트")
    void 유저탈퇴테스트() {
        em.persist(userEntity);
        userService.deleteUser(userEntity);
    }

    @Test
    @DisplayName("유저 회원탈퇴 Mockito 테스트")
    void 유저회원탈퇴Mockito테스트() {
        em.persist(userEntity);
        UserDTO userDTO = UserEntity.toDto(userEntity);
        // when
        when(mockUserService.findOneUser(userDTO.getIdx())).thenReturn(userDTO);
        userService.deleteUser(userEntity);

        // verify
        verify(mockUserService, times(1)).findOneUser(userDTO.getIdx());
        verify(mockUserService, atLeastOnce()).findOneUser(userDTO.getIdx());
        verifyNoMoreInteractions(mockUserService);

        InOrder inOrder = inOrder(mockUserService);
        inOrder.verify(mockUserService).findOneUser(userDTO.getIdx());
    }

    @Test
    @DisplayName("유저 회원탈퇴 BDD 테스트")
    void 유저회원탈퇴BDD테스트() {
        em.persist(userEntity);
        UserDTO userDTO = UserEntity.toDto(userEntity);
        // when
        given(mockUserService.findOneUser(userDTO.getIdx())).willReturn(userDTO);
        userService.deleteUser(userEntity);

        // verify
        then(mockUserService).should(times(1)).findOneUser(userDTO.getIdx());
        then(mockUserService).should(atLeastOnce()).findOneUser(userDTO.getIdx());
        then(mockUserService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("유저 좋아하는 여행지 추가")
    void 유저좋아하는여행지추가() throws JSONException {
        List<String> list = new ArrayList<>();
        list.add("1");

        SignUpRequest signUpRequest = SignUpRequest.builder()
                .userId("test")
                .password("test")
                .name("test")
                .email("test@test.com")
                .visible("Y")
                .build();

        UserDTO oneUser = userService.insertUser(signUpRequest);

        userService.addFavoriteTravel(oneUser.getIdx(), 1L);

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
    @DisplayName("유저가 작성한 스케줄 리스트 조회")
    void 유저가작성한스케줄리스트조회() {
        // given
        TravelScheduleDTO travelScheduleEntity = TravelScheduleDTO.builder()
                .scheduleDescription("스케줄 테스트")
                .scheduleTime(LocalDateTime.now())
                .build();

        List<TravelScheduleDTO> userSchedule = new ArrayList<>();
        userSchedule.add(travelScheduleEntity);

        // when
        when(mockUserService.findUserSchedule(1L)).thenReturn(userSchedule);
        List<TravelScheduleDTO> scheduleList = mockUserService.findUserSchedule(1L);

        // then
        assertThat(scheduleList.get(0).getScheduleDescription()).isEqualTo("스케줄 테스트");

        // verify
        verify(mockUserService, times(1)).findUserSchedule(1L);
        verify(mockUserService, atLeastOnce()).findUserSchedule(1L);
        verifyNoMoreInteractions(mockUserService);

        InOrder inOrder = inOrder(mockUserService);
        inOrder.verify(mockUserService).findUserSchedule(1L);
    }

    @Test
    @DisplayName("유저가 작성한 스케줄 상세 조회")
    void 유저가작성한스케줄상세조회() {
        CommonEntity commonEntity = CommonEntity.builder()
                .commonCode(999)
                .commonName("서울")
                .visible("Y")
                .build();

        em.persist(commonEntity);

        SignUpRequest signUpRequest = SignUpRequest.builder()
                .userId("test")
                .password("test")
                .name("test")
                .email("test@test.com")
                .visible("Y")
                .build();

        UserDTO oneUser = userService.insertUser(signUpRequest);
        // given
        TravelScheduleEntity travelScheduleEntity = TravelScheduleEntity.builder()
                .commonEntity(commonEntity)
                .scheduleDescription("스케줄 테스트")
                .scheduleTime(LocalDateTime.now())
                .build();

        userService.insertTravelSchedule(oneUser.getIdx(), travelScheduleEntity);
        TravelScheduleDTO travelScheduleDTO = TravelScheduleEntity.toDto(travelScheduleEntity);

        // when
        when(mockUserService.findOneUserSchedule(travelScheduleDTO.getUserIdx(), travelScheduleDTO.getIdx())).thenReturn(travelScheduleDTO);
        TravelScheduleDTO oneUserSchedule = mockUserService.findOneUserSchedule(travelScheduleDTO.getUserIdx(), travelScheduleDTO.getIdx());

        // then
        assertThat(oneUserSchedule.getScheduleDescription()).isEqualTo("스케줄 테스트");

        // verify
        verify(mockUserService, times(1)).findOneUserSchedule(travelScheduleDTO.getUserIdx(), travelScheduleDTO.getIdx());
        verify(mockUserService, atLeastOnce()).findOneUserSchedule(travelScheduleDTO.getUserIdx(), travelScheduleDTO.getIdx());
        verifyNoMoreInteractions(mockUserService);

        InOrder inOrder = inOrder(mockUserService);
        inOrder.verify(mockUserService).findOneUserSchedule(travelScheduleDTO.getUserIdx(), travelScheduleDTO.getIdx());
    }

    @Test
    @DisplayName("유저 여행 스케줄 등록 테스트")
    void 유저여행스케줄등록테스트() {
        CommonEntity commonEntity = CommonEntity.builder()
                .commonCode(999)
                .commonName("서울")
                .visible("Y")
                .build();

        em.persist(commonEntity);

        SignUpRequest signUpRequest = SignUpRequest.builder()
                .userId("test")
                .password("test")
                .name("test")
                .email("test@test.com")
                .visible("Y")
                .build();

        UserDTO oneUser = userService.insertUser(signUpRequest);

        // given
        TravelScheduleEntity travelScheduleEntity = TravelScheduleEntity.builder()
                .commonEntity(commonEntity)
                .scheduleDescription("스케줄 테스트")
                .scheduleTime(LocalDateTime.now())
                .build();

        TravelScheduleDTO oneSchedule = userService.insertTravelSchedule(oneUser.getIdx(), travelScheduleEntity);

        assertThat(oneSchedule.getUserIdx()).isEqualTo(oneUser.getIdx());
        assertThat(oneSchedule.getTravelCode()).isEqualTo(commonEntity.getCommonCode());
        assertThat(oneSchedule.getScheduleDescription()).isEqualTo("스케줄 테스트");
    }

    @Test
    @DisplayName("유저 여행 스케줄 수정 테스트")
    void 유저여행스케줄수정테스트() {
        CommonEntity commonEntity = CommonEntity.builder()
                .commonCode(999)
                .commonName("서울")
                .visible("Y")
                .build();

        em.persist(commonEntity);

        SignUpRequest signUpRequest = SignUpRequest.builder()
                .userId("test")
                .password("test")
                .name("test")
                .email("test@test.com")
                .visible("Y")
                .build();

        UserDTO oneUser = userService.insertUser(signUpRequest);

        // given
        TravelScheduleEntity travelScheduleEntity = TravelScheduleEntity.builder()
                .commonEntity(commonEntity)
                .scheduleDescription("스케줄 테스트")
                .scheduleTime(LocalDateTime.now())
                .build();

        TravelScheduleDTO oneSchedule = userService.insertTravelSchedule(oneUser.getIdx(), travelScheduleEntity);

        travelScheduleEntity = TravelScheduleEntity.builder()
                .idx(oneSchedule.getIdx())
                .userEntity(userEntity)
                .commonEntity(commonEntity)
                .scheduleDescription("스케줄 수정 테스트")
                .scheduleTime(LocalDateTime.now())
                .build();

        TravelScheduleDTO updateSchedule = userService.updateTravelSchedule(oneSchedule.getIdx(), travelScheduleEntity);

        assertThat(updateSchedule.getScheduleDescription()).isEqualTo("스케줄 수정 테스트");

        assertThat(oneSchedule.getUserIdx()).isEqualTo(oneUser.getIdx());
        assertThat(oneSchedule.getTravelCode()).isEqualTo(commonEntity.getCommonCode());
        assertThat(oneSchedule.getScheduleDescription()).isEqualTo("스케줄 테스트");
    }

    @Test
    @DisplayName("유저 여행 스케줄 삭제 테스트")
    void 유저여행스케줄삭제테스트() {
        CommonEntity commonEntity = CommonEntity.builder()
                .commonCode(999)
                .commonName("서울")
                .visible("Y")
                .build();

        em.persist(commonEntity);

        SignUpRequest signUpRequest = SignUpRequest.builder()
                .userId("test")
                .password("test")
                .name("test")
                .email("test@test.com")
                .visible("Y")
                .build();

        UserDTO oneUser = userService.insertUser(signUpRequest);

        // given
        TravelScheduleEntity travelScheduleEntity = TravelScheduleEntity.builder()
                .commonEntity(commonEntity)
                .scheduleDescription("스케줄 테스트")
                .scheduleTime(LocalDateTime.now())
                .build();

        TravelScheduleDTO oneSchedule = userService.insertTravelSchedule(oneUser.getIdx(), travelScheduleEntity);

        Long deleteIdx = userService.deleteTravelSchedule(oneSchedule.getIdx());

        assertThat(deleteIdx).isEqualTo(oneSchedule.getIdx());
    }
}

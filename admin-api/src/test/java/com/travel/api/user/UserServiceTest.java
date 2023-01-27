package com.travel.api.user;

import com.travel.api.common.domain.CommonEntity;
import com.travel.api.travel.domain.TravelEntity;
import com.travel.api.travel.domain.group.TravelGroupDto;
import com.travel.api.travel.domain.group.TravelGroupEntity;
import com.travel.api.travel.domain.group.TravelGroupUserDto;
import com.travel.api.travel.domain.group.TravelGroupUserEntity;
import com.travel.api.travel.domain.schedule.TravelScheduleDto;
import com.travel.api.travel.domain.schedule.TravelScheduleEntity;
import com.travel.api.user.domain.LoginRequest;
import com.travel.api.user.domain.SignUpRequest;
import com.travel.api.user.domain.UserDto;
import com.travel.api.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
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

import static com.travel.api.user.domain.Role.ROLE_ADMIN;
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
    private UserDto userDTO;

    void createUser() {
        userEntity = UserEntity.builder()
                .userId("test111")
                .password("test111")
                .email("test@naver.com")
                .name("test")
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
    @DisplayName("관리자 로그인 처리 테스트")
    void 관리자로그인처리테스트() {
        // given
        UserEntity adminUserEntity = UserEntity.builder()
                .userId("admin05")
                .password(passwordEncoder.encode("pass1234"))
                .name("admin05")
                .email("admin05@admin.com")
                .visible("Y")
                .role(ROLE_ADMIN)
                .build();

        em.persist(adminUserEntity);

        LoginRequest loginRequest = LoginRequest.builder().userId(adminUserEntity.getUserId())
                .password("pass1234").build();

        // then
        userService.adminLogin(loginRequest);
    }

    @Test
    @Disabled
    @DisplayName("유저리스트조회테스트")
    void 유저리스트조회테스트() {
        // given
        Map<String, Object> userMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(0, 3);
        List<UserDto> userList = userService.findUserList(userMap, pageRequest);

        // then
        assertThat(userList.size()).isGreaterThan(0);
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

//        Page<UserDto> resultPage = new PageImpl<>(userList, pageRequest, userList.size());

        // when
        when(mockUserService.findUserList(userMap, pageRequest)).thenReturn(userList);
        List<UserDto> newUserList = mockUserService.findUserList(userMap, pageRequest);

        // then
        assertThat(newUserList.get(0).getIdx()).isEqualTo(userList.get(0).getIdx());
        assertThat(newUserList.get(0).getUserId()).isEqualTo(userList.get(0).getUserId());
        assertThat(newUserList.get(0).getName()).isEqualTo(userList.get(0).getName());
        assertThat(newUserList.get(0).getEmail()).isEqualTo(userList.get(0).getEmail());

        // verify
        verify(mockUserService, times(1)).findUserList(userMap, pageRequest);
        verify(mockUserService, atLeastOnce()).findUserList(userMap, pageRequest);
        verifyNoMoreInteractions(mockUserService);

        InOrder inOrder = inOrder(mockUserService);
        inOrder.verify(mockUserService).findUserList(userMap, pageRequest);
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

//        Page<UserDto> resultPage = new PageImpl<>(userList, pageRequest, userList.size());

        // when
        given(mockUserService.findUserList(userMap, pageRequest)).willReturn(userList);
        List<UserDto> newUserList = mockUserService.findUserList(userMap, pageRequest);

        // then
        assertThat(newUserList.get(0).getIdx()).isEqualTo(userList.get(0).getIdx());
        assertThat(newUserList.get(0).getUserId()).isEqualTo(userList.get(0).getUserId());
        assertThat(newUserList.get(0).getName()).isEqualTo(userList.get(0).getName());
        assertThat(newUserList.get(0).getEmail()).isEqualTo(userList.get(0).getEmail());

        // verify
        then(mockUserService).should(times(1)).findUserList(userMap, pageRequest);
        then(mockUserService).should(atLeastOnce()).findUserList(userMap, pageRequest);
        then(mockUserService).shouldHaveNoMoreInteractions();
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
        UserDto insertUser = userService.insertUser(signUpRequest);

        // when
        when(mockUserService.findOneUser(userEntity.getIdx())).thenReturn(insertUser);
        UserDto userInfo = mockUserService.findOneUser(userEntity.getIdx());

        // then
        assertThat(userInfo.getUserId()).isEqualTo("test");
        assertThat(userInfo.getName()).isEqualTo("test");
        assertThat(userInfo.getEmail()).isEqualTo("test@test.com");

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
        UserDto insertUser = userService.insertUser(signUpRequest);

        // when
        given(mockUserService.findOneUser(userEntity.getIdx())).willReturn(insertUser);
        UserDto userInfo = mockUserService.findOneUser(userEntity.getIdx());

        // then
        assertThat(userInfo.getUserId()).isEqualTo("test");
        assertThat(userInfo.getName()).isEqualTo("test");
        assertThat(userInfo.getEmail()).isEqualTo("test@test.com");

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
                .role(ROLE_ADMIN)
                .email("test1@test.com")
                .visible("Y")
                .build();

        userService.updateUser(idx, newUserEntity);
        UserDto newUserDTO = UserEntity.toDto(newUserEntity);

        // when
        when(mockUserService.findOneUser(newUserEntity.getIdx())).thenReturn(newUserDTO);
        UserDto userInfo = mockUserService.findOneUser(newUserEntity.getIdx());

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
                .visible("Y")
                .build();

        userService.updateUser(idx, newUserEntity);
        UserDto newUserDTO = UserEntity.toDto(newUserEntity);

        // when
        given(mockUserService.findOneUser(newUserEntity.getIdx())).willReturn(newUserDTO);
        UserDto userInfo = mockUserService.findOneUser(newUserEntity.getIdx());

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
    @DisplayName("유저가 작성한 스케줄 리스트 조회")
    void 유저가작성한스케줄리스트조회() {
        // given
        TravelScheduleDto travelScheduleEntity = TravelScheduleDto.builder()
                .scheduleDescription("스케줄 테스트")
                .scheduleTime(LocalDateTime.now())
                .build();

        List<TravelScheduleDto> userSchedule = new ArrayList<>();
        userSchedule.add(travelScheduleEntity);

        userService.findUserSchedule(1L);

        // when
        when(mockUserService.findUserSchedule(1L)).thenReturn(userSchedule);
        List<TravelScheduleDto> scheduleList = mockUserService.findUserSchedule(1L);

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

        UserDto oneUser = userService.insertUser(signUpRequest);

        UserEntity insertUser = UserEntity.builder()
                .idx(oneUser.getIdx())
                .userId(oneUser.getUserId())
                .password(oneUser.getPassword())
                .name(oneUser.getName())
                .email(oneUser.getEmail())
                .role(ROLE_ADMIN)
                .userToken(oneUser.getUserToken())
                .visible("Y")
                .build();
        // given
        TravelScheduleEntity travelScheduleEntity = TravelScheduleEntity.builder()
                .commonEntity(commonEntity)
                .userEntity(insertUser)
                .scheduleDescription("스케줄 테스트")
                .scheduleTime(LocalDateTime.now())
                .build();

        em.persist(travelScheduleEntity);
        TravelScheduleDto travelScheduleDTO = TravelScheduleEntity.toDto(travelScheduleEntity);

        // when
        when(mockUserService.findOneUserSchedule(travelScheduleDTO.getUserDTO().getIdx(), travelScheduleDTO.getIdx())).thenReturn(travelScheduleDTO);
        TravelScheduleDto oneUserSchedule = mockUserService.findOneUserSchedule(travelScheduleDTO.getUserDTO().getIdx(), travelScheduleDTO.getIdx());

        // then
        assertThat(oneUserSchedule.getScheduleDescription()).isEqualTo("스케줄 테스트");

        // verify
        verify(mockUserService, times(1)).findOneUserSchedule(travelScheduleDTO.getUserDTO().getIdx(), travelScheduleDTO.getIdx());
        verify(mockUserService, atLeastOnce()).findOneUserSchedule(travelScheduleDTO.getUserDTO().getIdx(), travelScheduleDTO.getIdx());
        verifyNoMoreInteractions(mockUserService);

        InOrder inOrder = inOrder(mockUserService);
        inOrder.verify(mockUserService).findOneUserSchedule(travelScheduleDTO.getUserDTO().getIdx(), travelScheduleDTO.getIdx());
    }

    @Test
    @DisplayName("유저 여행 그룹 등록 Mockito 테스트")
    void 유저여행그룹등록Mockito테스트() {
        // given
        // 유저 등록
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .userId("test")
                .password("test")
                .name("test")
                .email("test@test.com")
                .visible("Y")
                .build();
        UserDto insertUser = userService.insertUser(signUpRequest);

        CommonEntity commonEntity = CommonEntity.builder()
                .commonCode(999)
                .commonName("서울")
                .visible("Y")
                .build();

        em.persist(commonEntity);

        TravelEntity travelEntity = TravelEntity.builder()
                .newTravelCode(commonEntity)
                .travelTitle("여행지 소개")
                .travelDescription("여행지 소개")
                .travelAddress("인천광역시 서구")
                .travelZipCode("123-456")
                .favoriteCount(1)
                .viewCount(0)
                .popular(false)
                .visible("Y")
                .build();

        em.persist(travelEntity);

        TravelGroupEntity travelGroupEntity = TravelGroupEntity.builder()
                .travelEntity(travelEntity)
                .groupName("서울모임").groupDescription("서울모임").visible("Y").build();

        em.persist(travelGroupEntity);

        TravelGroupDto travelGroupDTO = TravelGroupEntity.toDto(travelGroupEntity);

        TravelGroupUserEntity travelGroupUserEntity = TravelGroupUserEntity.builder().build();

        TravelGroupUserDto travelGroupUserInfo = userService.insertTravelGroupUser(insertUser.getIdx(), travelGroupDTO.getIdx(), travelGroupUserEntity);

        // then
        assertThat(travelGroupUserInfo.getGroupDto().getIdx()).isEqualTo(travelGroupDTO.getIdx());
        assertThat(travelGroupUserInfo.getUserDto().getIdx()).isEqualTo(insertUser.getIdx());
    }

    @Test
    @DisplayName("유저 여행 그룹 삭제 Mockito 테스트")
    void 유저여행그룹삭제Mockito테스트() {
        // given
        // 유저 등록
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .userId("test")
                .password("test")
                .name("test")
                .email("test@test.com")
                .visible("Y")
                .build();
        UserDto insertUser = userService.insertUser(signUpRequest);

        CommonEntity commonEntity = CommonEntity.builder()
                .commonCode(999)
                .commonName("서울")
                .visible("Y")
                .build();

        em.persist(commonEntity);

        TravelEntity travelEntity = TravelEntity.builder()
                .newTravelCode(commonEntity)
                .travelTitle("여행지 소개")
                .travelDescription("여행지 소개")
                .travelAddress("인천광역시 서구")
                .travelZipCode("123-456")
                .favoriteCount(1)
                .viewCount(0)
                .popular(false)
                .visible("Y")
                .build();

        em.persist(travelEntity);

        TravelGroupEntity travelGroupEntity = TravelGroupEntity.builder()
                .travelEntity(travelEntity)
                .groupName("서울모임").groupDescription("서울모임").visible("Y").build();

        em.persist(travelGroupEntity);
        TravelGroupDto travelGroupDTO = TravelGroupEntity.toDto(travelGroupEntity);

        TravelGroupUserEntity travelGroupUserEntity = TravelGroupUserEntity.builder().build();

        TravelGroupUserDto travelGroupUserInfo = userService.insertTravelGroupUser(insertUser.getIdx(), travelGroupDTO.getIdx(), travelGroupUserEntity);

        Long deleteIdx = userService.deleteTravelGroupUser(travelGroupUserInfo.getIdx());

        // then
        assertThat(deleteIdx).isEqualTo(travelGroupUserInfo.getIdx());
    }
}

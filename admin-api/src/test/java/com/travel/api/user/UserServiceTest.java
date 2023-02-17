package com.travel.api.user;

import com.travel.api.AdminCommonServiceTest;
import com.travel.api.common.domain.CommonEntity;
import com.travel.api.travel.domain.group.TravelGroupEntity;
import com.travel.api.travel.domain.group.TravelGroupUserDto;
import com.travel.api.travel.domain.group.TravelGroupUserEntity;
import com.travel.api.travel.domain.schedule.TravelScheduleDto;
import com.travel.api.travel.domain.schedule.TravelScheduleEntity;
import com.travel.api.travel.domain.schedule.repository.ScheduleRepository;
import com.travel.api.user.domain.*;
import com.travel.api.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.*;

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
class UserServiceTest extends AdminCommonServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private ScheduleRepository scheduleRepository;
    @InjectMocks private UserService mockUserService;
    private final UserService userService;
    private final EntityManager em;
    private final PasswordEncoder passwordEncoder;

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

        List<UserEntity> userList = new ArrayList<>();
        userList.add(userEntity);

        Page<UserEntity> resultPage = new PageImpl<>(userList, pageRequest, userList.size());

        // when
        when(userRepository.findAll(pageRequest)).thenReturn(resultPage);
        List<UserDto> newUserList = mockUserService.findUserList(userMap, pageRequest);

        // then
        assertThat(newUserList.get(0).getIdx()).isEqualTo(userList.get(0).getIdx());
        assertThat(newUserList.get(0).getUserId()).isEqualTo(userList.get(0).getUserId());
        assertThat(newUserList.get(0).getName()).isEqualTo(userList.get(0).getName());
        assertThat(newUserList.get(0).getEmail()).isEqualTo(userList.get(0).getEmail());

        // verify
        verify(userRepository, times(1)).findAll(pageRequest);
        verify(userRepository, atLeastOnce()).findAll(pageRequest);
        verifyNoMoreInteractions(userRepository);

        InOrder inOrder = inOrder(userRepository);
        inOrder.verify(userRepository).findAll(pageRequest);
    }

    @Test
    @DisplayName("유저 리스트 BDD 검색 조회 테스트")
    void 유저리스트BDD검색조회테스트() {
        // given
        Map<String, Object> userMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(0, 3);

        List<UserEntity> userList = new ArrayList<>();
        userList.add(userEntity);

        Page<UserEntity> resultPage = new PageImpl<>(userList, pageRequest, userList.size());

        // when
        given(userRepository.findAll(pageRequest)).willReturn(resultPage);
        List<UserDto> newUserList = mockUserService.findUserList(userMap, pageRequest);

        // then
        assertThat(newUserList.get(0).getIdx()).isEqualTo(userList.get(0).getIdx());
        assertThat(newUserList.get(0).getUserId()).isEqualTo(userList.get(0).getUserId());
        assertThat(newUserList.get(0).getName()).isEqualTo(userList.get(0).getName());
        assertThat(newUserList.get(0).getEmail()).isEqualTo(userList.get(0).getEmail());

        // verify
        then(userRepository).should(times(1)).findAll(pageRequest);
        then(userRepository).should(atLeastOnce()).findAll(pageRequest);
        then(userRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("유저 회원가입 Mockito 테스트")
    void 유저회원가입Mockito테스트() {
        // given
        UserEntity userEntity = UserEntity.builder()
                .userId("test")
                .password("test")
                .name("test")
                .email("test@test.com")
                .role(ROLE_ADMIN)
                .visible("Y")
                .build();

        // when
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        UserDto userInfo = mockUserService.insertUser(userEntity);

        // then
        assertThat(userInfo.getUserId()).isEqualTo("test");
        assertThat(userInfo.getName()).isEqualTo("test");
        assertThat(userInfo.getEmail()).isEqualTo("test@test.com");

        // verify
        verify(userRepository, times(1)).save(userEntity);
        verify(userRepository, atLeastOnce()).save(userEntity);

        InOrder inOrder = inOrder(userRepository);
        inOrder.verify(userRepository).save(userEntity);
    }

    @Test
    @DisplayName("유저 회원가입 BDD 테스트")
    void 유저회원가입BDD테스트() {
        // given
        UserEntity userEntity = UserEntity.builder()
                .userId("test")
                .password(passwordEncoder.encode("test"))
                .name("test")
                .email("test@test.com")
                .role(ROLE_ADMIN)
                .visible("Y")
                .build();

        // when
        given(userRepository.save(userEntity)).willReturn(userEntity);
        UserDto userInfo = mockUserService.insertUser(userEntity);

        // then
        assertThat(userInfo.getUserId()).isEqualTo("test");
        assertThat(userInfo.getName()).isEqualTo("test");
        assertThat(userInfo.getEmail()).isEqualTo("test@test.com");

        // verify
        then(userRepository).should(times(1)).save(userEntity);
        then(userRepository).should(atLeastOnce()).save(userEntity);
    }

    @Test
    @DisplayName("유저 회원수정 Mockito 테스트")
    void 유저회원수정Mockito테스트() {
        // given
        UserEntity userEntity = UserEntity.builder()
                .userId("test")
                .password(passwordEncoder.encode("test"))
                .name("test")
                .email("test@test.com")
                .role(ROLE_ADMIN)
                .visible("Y")
                .build();

        Long idx = userService.insertUser(userEntity).getIdx();

        UserEntity newUserEntity = UserEntity.builder()
                .idx(idx)
                .userId("test1")
                .password("test1")
                .name("test1")
                .role(ROLE_ADMIN)
                .email("test1@test.com")
                .visible("Y")
                .build();

        // when
        when(userRepository.findById(newUserEntity.getIdx())).thenReturn(Optional.of(newUserEntity));
        when(userRepository.save(newUserEntity)).thenReturn(newUserEntity);
        UserDto userInfo = mockUserService.updateUser(newUserEntity.getIdx(), newUserEntity);

        // then
        assertThat(userInfo.getUserId()).isEqualTo("test1");
        assertThat(userInfo.getName()).isEqualTo("test1");

        // verify
        verify(userRepository, times(1)).findById(newUserEntity.getIdx());
        verify(userRepository, atLeastOnce()).findById(newUserEntity.getIdx());
        verifyNoMoreInteractions(userRepository);

        InOrder inOrder = inOrder(userRepository);
        inOrder.verify(userRepository).findById(newUserEntity.getIdx());
    }

    @Test
    @DisplayName("유저 회원수정 BDD 테스트")
    void 유저회원수정BDD테스트() {
        // given
        UserEntity userEntity = UserEntity.builder()
                .userId("test")
                .password(passwordEncoder.encode("test"))
                .name("test")
                .email("test@test.com")
                .role(ROLE_ADMIN)
                .visible("Y")
                .build();

        Long idx = userService.insertUser(userEntity).getIdx();

        UserEntity newUserEntity = UserEntity.builder()
                .idx(idx)
                .userId("test1")
                .password("test1")
                .name("test1")
                .role(ROLE_ADMIN)
                .email("test1@test.com")
                .visible("Y")
                .build();

        // when
        given(userRepository.findById(newUserEntity.getIdx())).willReturn(Optional.of(newUserEntity));
        given(userRepository.save(newUserEntity)).willReturn(newUserEntity);
        UserDto userInfo = mockUserService.updateUser(newUserEntity.getIdx(), newUserEntity);

        // then
        assertThat(userInfo.getUserId()).isEqualTo("test1");
        assertThat(userInfo.getName()).isEqualTo("test1");

        // verify
        then(userRepository).should(times(1)).findById(newUserEntity.getIdx());
        then(userRepository).should(atLeastOnce()).findById(newUserEntity.getIdx());
        then(userRepository).shouldHaveNoMoreInteractions();
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
        TravelScheduleEntity travelScheduleEntity = TravelScheduleEntity.builder()
                .commonEntity(commonEntity)
                .userEntity(userEntity)
                .scheduleDescription("스케줄 테스트")
                .scheduleTime(LocalDateTime.now())
                .build();

        em.persist(travelScheduleEntity);

        List<TravelScheduleEntity> userSchedule = new ArrayList<>();
        userSchedule.add(travelScheduleEntity);

        // when
        when(scheduleRepository.findUserSchedule(userEntity.getIdx())).thenReturn(userSchedule);
        List<TravelScheduleDto> scheduleList = mockUserService.findUserSchedule(userEntity.getIdx());

        // then
        assertThat(scheduleList.get(0).getScheduleDescription()).isEqualTo("스케줄 테스트");

        // verify
        verify(scheduleRepository, times(1)).findUserSchedule(userEntity.getIdx());
        verify(scheduleRepository, atLeastOnce()).findUserSchedule(userEntity.getIdx());
        verifyNoMoreInteractions(scheduleRepository);

        InOrder inOrder = inOrder(scheduleRepository);
        inOrder.verify(scheduleRepository).findUserSchedule(userEntity.getIdx());
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

        UserEntity userEntity = UserEntity.builder()
                .userId("test")
                .password("test")
                .name("test")
                .email("test@test.com")
                .visible("Y")
                .role(ROLE_ADMIN)
                .build();

        em.persist(userEntity);

        // given
        TravelScheduleEntity travelScheduleEntity = TravelScheduleEntity.builder()
                .commonEntity(commonEntity)
                .userEntity(userEntity)
                .scheduleDescription("스케줄 테스트")
                .scheduleTime(LocalDateTime.now())
                .build();

        em.persist(travelScheduleEntity);

        // when
        when(scheduleRepository.findOneUserSchedule(travelScheduleEntity.getUserEntity().getIdx(), travelScheduleEntity.getIdx())).thenReturn(Optional.ofNullable(travelScheduleEntity));
        TravelScheduleDto oneUserSchedule = mockUserService.findOneUserSchedule(travelScheduleEntity.getUserEntity().getIdx(), travelScheduleEntity.getIdx());

        // then
        assertThat(oneUserSchedule.getScheduleDescription()).isEqualTo("스케줄 테스트");

        // verify
        verify(scheduleRepository, times(1)).findOneUserSchedule(travelScheduleEntity.getUserEntity().getIdx(), travelScheduleEntity.getIdx());
        verify(scheduleRepository, atLeastOnce()).findOneUserSchedule(travelScheduleEntity.getUserEntity().getIdx(), travelScheduleEntity.getIdx());
        verifyNoMoreInteractions(scheduleRepository);

        InOrder inOrder = inOrder(scheduleRepository);
        inOrder.verify(scheduleRepository).findOneUserSchedule(travelScheduleEntity.getUserEntity().getIdx(), travelScheduleEntity.getIdx());
    }

    @Test
    @DisplayName("유저 여행 그룹 등록 Mockito 테스트")
    void 유저여행그룹등록테스트() {
        // given
        TravelGroupEntity travelGroupEntity = TravelGroupEntity.builder()
                .travelEntity(travelEntity)
                .groupName("서울모임").groupDescription("서울모임").visible("Y").build();

        em.persist(travelGroupEntity);

        TravelGroupUserEntity travelGroupUserEntity = TravelGroupUserEntity.builder().build();

        TravelGroupUserDto travelGroupUserInfo = userService.insertTravelGroupUser(userEntity.getIdx(), travelGroupEntity.getIdx(), travelGroupUserEntity);

        // then
        assertThat(travelGroupUserInfo.getGroupName()).isEqualTo(travelGroupEntity.getGroupName());
        assertThat(travelGroupUserInfo.getUserId()).isEqualTo(userEntity.getUserId());
    }

    @Test
    @DisplayName("유저 여행 그룹 삭제 Mockito 테스트")
    void 유저여행그룹삭제Mockito테스트() {
        // given
        TravelGroupEntity travelGroupEntity = TravelGroupEntity.builder()
                .travelEntity(travelEntity)
                .groupName("서울모임").groupDescription("서울모임").visible("Y").build();

        em.persist(travelGroupEntity);

        TravelGroupUserEntity travelGroupUserEntity = TravelGroupUserEntity.builder().build();

        TravelGroupUserDto travelGroupUserInfo = userService.insertTravelGroupUser(userEntity.getIdx(), travelGroupEntity.getIdx(), travelGroupUserEntity);

        Long deleteIdx = userService.deleteTravelGroupUser(travelGroupUserInfo.getIdx());

        // then
        assertThat(deleteIdx).isEqualTo(travelGroupUserInfo.getIdx());
    }
}

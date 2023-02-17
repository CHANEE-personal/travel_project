package com.travel.api.user;

import com.travel.api.FrontCommonServiceTest;
import com.travel.api.common.domain.CommonEntity;
import com.travel.api.travel.domain.group.TravelGroupUserDTO;
import com.travel.api.travel.domain.reservation.TravelReservationEntity;
import com.travel.api.travel.domain.schedule.TravelScheduleDTO;
import com.travel.api.travel.domain.schedule.TravelScheduleEntity;
import com.travel.api.travel.domain.schedule.repository.ScheduleRepository;
import com.travel.api.user.domain.*;
import com.travel.api.user.domain.repository.UserRepository;
import com.travel.api.user.domain.reservation.UserReservationDTO;
import com.travel.api.user.domain.reservation.UserReservationEntity;
import com.travel.exception.TravelException;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
class UserServiceTest extends FrontCommonServiceTest {

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
        UserEntity userEntity = UserEntity.builder()
                .userId("test")
                .password(passwordEncoder.encode("test"))
                .name("test")
                .email("test@test.com")
                .visible("Y")
                .build();

        // when
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        UserDTO userInfo = mockUserService.insertUser(userEntity);

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
                .visible("Y")
                .build();

        // when
        given(userRepository.save(userEntity)).willReturn(userEntity);
        UserDTO userInfo = mockUserService.insertUser(userEntity);

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
                .userId("test123")
                .password("test")
                .name("test")
                .email("test@test.com")
                .role(Role.ROLE_TRAVEL_USER)
                .visible("Y")
                .build();

        Long idx = userService.insertUser(userEntity).getIdx();

        UserEntity newUserEntity = UserEntity.builder()
                .idx(idx)
                .userId("test1")
                .password("test1")
                .name("test1")
                .role(Role.ROLE_TRAVEL_USER)
                .email("test1@test.com")
                .visible("Y")
                .build();

        // when
        when(userRepository.findById(newUserEntity.getIdx())).thenReturn(Optional.of(newUserEntity));
        when(userRepository.save(newUserEntity)).thenReturn(newUserEntity);
        UserDTO userInfo = mockUserService.updateUser(newUserEntity.getIdx(), newUserEntity);

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
                .userId("test123")
                .password("test")
                .name("test")
                .email("test@test.com")
                .role(Role.ROLE_TRAVEL_USER)
                .visible("Y")
                .build();

        Long idx = userService.insertUser(userEntity).getIdx();

        UserEntity newUserEntity = UserEntity.builder()
                .idx(idx)
                .userId("test1")
                .password("test1")
                .name("test1")
                .role(Role.ROLE_TRAVEL_USER)
                .email("test1@test.com")
                .visible("Y")
                .build();

        // when
        given(userRepository.findById(newUserEntity.getIdx())).willReturn(Optional.of(newUserEntity));
        given(userRepository.save(newUserEntity)).willReturn(newUserEntity);
        UserDTO userInfo = mockUserService.updateUser(newUserEntity.getIdx(), newUserEntity);

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
    @DisplayName("유저 좋아하는 여행지 추가")
    void 유저좋아하는여행지추가() throws JSONException {
        List<String> list = new ArrayList<>();
        list.add("1");

        UserEntity userEntity = UserEntity.builder()
                .userId("test")
                .password("test")
                .name("test")
                .email("test@test.com")
                .role(Role.ROLE_TRAVEL_USER)
                .visible("Y")
                .build();

        UserDTO oneUser = userService.insertUser(userEntity);

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
        List<TravelScheduleDTO> scheduleList = mockUserService.findUserSchedule(userEntity.getIdx());

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
                .role(Role.ROLE_TRAVEL_USER)
                .build();

        UserDTO oneUser = userService.insertUser(userEntity);
        // given
        TravelScheduleEntity travelScheduleEntity = TravelScheduleEntity.builder()
                .commonEntity(commonEntity)
                .userEntity(userEntity)
                .scheduleDescription("스케줄 테스트")
                .scheduleTime(LocalDateTime.now())
                .build();

        TravelScheduleDTO travelScheduleDTO = userService.insertTravelSchedule(oneUser.getIdx(), travelScheduleEntity);

        // when
        when(scheduleRepository.findOneUserSchedule(oneUser.getIdx(), travelScheduleDTO.getIdx())).thenReturn(Optional.ofNullable(travelScheduleEntity));
        TravelScheduleDTO oneUserSchedule = mockUserService.findOneUserSchedule(oneUser.getIdx(), travelScheduleDTO.getIdx());

        // then
        assertThat(oneUserSchedule.getScheduleDescription()).isEqualTo("스케줄 테스트");

        // verify
        verify(scheduleRepository, times(1)).findOneUserSchedule(oneUser.getIdx(), travelScheduleDTO.getIdx());
        verify(scheduleRepository, atLeastOnce()).findOneUserSchedule(oneUser.getIdx(), travelScheduleDTO.getIdx());
        verifyNoMoreInteractions(scheduleRepository);

        InOrder inOrder = inOrder(scheduleRepository);
        inOrder.verify(scheduleRepository).findOneUserSchedule(oneUser.getIdx(), travelScheduleDTO.getIdx());
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

        UserEntity userEntity = UserEntity.builder()
                .userId("test")
                .password("test")
                .name("test")
                .email("test@test.com")
                .visible("Y")
                .role(Role.ROLE_TRAVEL_USER)
                .build();

        UserDTO oneUser = userService.insertUser(userEntity);

        // given
        TravelScheduleEntity travelScheduleEntity = TravelScheduleEntity.builder()
                .commonEntity(commonEntity)
                .scheduleDescription("스케줄 테스트")
                .scheduleTime(LocalDateTime.now())
                .build();

        TravelScheduleDTO oneSchedule = userService.insertTravelSchedule(oneUser.getIdx(), travelScheduleEntity);

        assertThat(oneSchedule.getUserId()).isEqualTo(oneUser.getUserId());
        assertThat(oneSchedule.getCommonCode()).isEqualTo(commonEntity.getCommonCode());
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

        // given
        TravelScheduleEntity travelScheduleEntity = TravelScheduleEntity.builder()
                .userEntity(userEntity)
                .commonEntity(commonEntity)
                .scheduleDescription("스케줄 테스트")
                .scheduleTime(LocalDateTime.now())
                .build();

        TravelScheduleDTO travelScheduleDTO = userService.insertTravelSchedule(userEntity.getIdx(), travelScheduleEntity);

        travelScheduleEntity = TravelScheduleEntity.builder()
                .idx(travelScheduleDTO.getIdx())
                .userEntity(userEntity)
                .commonEntity(commonEntity)
                .scheduleDescription("스케줄 수정 테스트")
                .scheduleTime(LocalDateTime.now())
                .build();

        TravelScheduleDTO updateSchedule = userService.updateTravelSchedule(travelScheduleDTO.getIdx(), travelScheduleEntity);

        em.flush();
        em.clear();

        assertThat(updateSchedule.getScheduleDescription()).isEqualTo("스케줄 수정 테스트");
        assertThat(updateSchedule.getUserId()).isEqualTo(userEntity.getUserId());
        assertThat(updateSchedule.getCommonCode()).isEqualTo(commonEntity.getCommonCode());
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

        UserEntity userEntity = UserEntity.builder()
                .userId("test")
                .password("test")
                .name("test")
                .email("test@test.com")
                .visible("Y")
                .role(Role.ROLE_TRAVEL_USER)
                .build();

        UserDTO oneUser = userService.insertUser(userEntity);

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

    @Test
    @DisplayName("유저 여행 예약 리스트 조회 테스트")
    void 유저여행예약리스트조회테스트() {
        List<UserReservationDTO> travelReservation = userService.findTravelReservation(userDTO.getIdx());

        assertThat(travelReservation).isNotEmpty();
        assertThat(travelReservation.get(0).getPrice()).isEqualTo(50000);
        assertThat(travelReservation.get(0).getUserCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("유저 여행 예약 등록 테스트")
    void 유저여행예약등록테스트() {
        TravelReservationEntity impossibleReservation = TravelReservationEntity.builder()
                .commonEntity(commonEntity)
                .title("예약 등록지")
                .description("예약 등록지")
                .address("서울 강남구")
                .zipCode("123-456")
                .price(50000)
                .possibleCount(0)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .status(true)
                .popular(false)
                .build();

        em.persist(impossibleReservation);

        // 여행예약불가능 케이스
        UserReservationEntity impossible = UserReservationEntity.builder()
                .newUserEntity(userEntity)
                .travelReservationEntity(travelReservationEntity)
                .price(travelReservationEntity.getPrice())
                .startDate(LocalDateTime.of(2022, 2, 1, 0, 0, 0))
                .endDate(LocalDateTime.of(2022, 2, 3, 23, 59, 59))
                .userCount(2)
                .build();

        assertThatThrownBy(() -> userService.travelReservation(userDTO.getIdx(), impossibleReservation.getIdx(), impossible))
                .isInstanceOf(TravelException.class).hasMessage("예약 가능한 수가 부족");

        // 여행예약가능 케이스
        UserReservationEntity insertReservation = UserReservationEntity.builder()
                .newUserEntity(userEntity)
                .travelReservationEntity(travelReservationEntity)
                .price(travelReservationEntity.getPrice())
                .startDate(LocalDateTime.of(2022, 2, 1, 0, 0, 0))
                .endDate(LocalDateTime.of(2022, 2, 3, 23, 59, 59))
                .userCount(2)
                .build();

        UserReservationDTO insertUserReservation = userService.travelReservation(userDTO.getIdx(), travelReservationDTO.getIdx(), insertReservation);

        assertThat(insertUserReservation.getUserName()).isEqualTo(userDTO.getUserId());
        assertThat(insertUserReservation.getTitle()).isEqualTo(travelReservationDTO.getTitle());
    }

    @Test
    @DisplayName("유저 여행 예약 취소 테스트")
    void 유저여행예약취소테스트() {
        Long deleteIdx = userService.deleteTravelReservation(userDTO.getIdx(), userReservationDTO.getIdx());
        assertThat(deleteIdx).isEqualTo(userReservationDTO.getIdx());
    }

    @Test
    @DisplayName("유저 여행 그룹 가입 테스트")
    void 유저여행그룹가입테스트() {
        // 유저 없음
        assertThatThrownBy(() -> userService.insertTravelGroup(9999L, travelGroupDTO.getIdx()))
                .isInstanceOf(TravelException.class).hasMessage("해당 유저 없음");

        // 여행 그룹 없음
        assertThatThrownBy(() -> userService.insertTravelGroup(userDTO.getIdx(), 9999L))
                .isInstanceOf(TravelException.class).hasMessage("여행 그룹 상세 없음");

        assertThat(userService.insertTravelGroup(userDTO.getIdx(), travelGroupDTO.getIdx()).getUserId()).isEqualTo(userDTO.getUserId());
        assertThat(userService.insertTravelGroup(userDTO.getIdx(), travelGroupDTO.getIdx()).getGroupName()).isEqualTo(travelGroupDTO.getGroupName());
    }

    @Test
    @DisplayName("유저 여행 그룹 탈퇴 테스트")
    void 유저여행그룹탈퇴테스트() {
        TravelGroupUserDTO insertTravelGroup = userService.insertTravelGroup(userDTO.getIdx(), travelGroupDTO.getIdx());
        assertThat(userService.deleteTravelGroup(userDTO.getIdx(), travelGroupDTO.getIdx())).isEqualTo(insertTravelGroup.getIdx());
    }
}

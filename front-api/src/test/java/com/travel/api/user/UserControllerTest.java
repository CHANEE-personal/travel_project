package com.travel.api.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.api.common.domain.CommonEntity;
import com.travel.api.travel.domain.TravelEntity;
import com.travel.api.travel.domain.group.TravelGroupEntity;
import com.travel.api.travel.domain.group.TravelGroupUserEntity;
import com.travel.api.travel.domain.reservation.TravelReservationEntity;
import com.travel.api.travel.domain.schedule.TravelScheduleEntity;
import com.travel.api.user.domain.LoginRequest;
import com.travel.api.user.domain.Role;
import com.travel.api.user.domain.SignUpRequest;
import com.travel.api.user.domain.UserEntity;
import com.travel.api.user.domain.reservation.UserReservationEntity;
import com.travel.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.event.EventListener;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.travel.api.user.domain.Role.ROLE_TRAVEL_USER;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.crypto.factory.PasswordEncoderFactories.createDelegatingPasswordEncoder;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(RestDocumentationExtension.class)
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@DisplayName("유저 Api Test")
class UserControllerTest {
    private final ObjectMapper objectMapper;
    private final WebApplicationContext wac;
    private final EntityManager em;
    private final JwtUtil jwtUtil;

    private UserEntity userEntity;
    private MockMvc mockMvc;
    protected PasswordEncoder passwordEncoder;

    Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_TRAVEL_USER"));
        return authorities;
    }

    @DisplayName("테스트 유저 생성")
    void createUser() {
        passwordEncoder = createDelegatingPasswordEncoder();

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("admin04", "pass1234", getAuthorities());
        String token = jwtUtil.doGenerateToken(authenticationToken.getName());

        userEntity = UserEntity.builder()
                .userId("user99")
                .password(passwordEncoder.encode("pass1234"))
                .name("test")
                .email("test@test.com")
                .role(Role.ROLE_TRAVEL_USER)
                .userToken(token)
                .visible("Y")
                .build();

        em.persist(userEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void setup(RestDocumentationContextProvider restDocumentationContextProvider) {
        this.mockMvc = webAppContextSetup(wac)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .apply(springSecurity())
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .alwaysDo(print())
                .build();

        createUser();
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("로그인 테스트")
    void 로그인테스트() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .userId(userEntity.getUserId())
                .password("pass1234")
                .build();

        mockMvc.perform(post("/front/user/login")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print())
                .andDo(document("LOGIN-USER",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        relaxedRequestFields(
                                fieldWithPath("userId").type(STRING).description("아이디"),
                                fieldWithPath("password").type(STRING).description("패스워드")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("userId").type(STRING).description("아이디"),
                                fieldWithPath("password").type(STRING).description("패스워드")
                        )))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(header().exists("X-ACCESS-TOKEN"))
                .andExpect(jsonPath("$.accessToken").isNotEmpty());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("유저 회원가입 테스트")
    void 회원가입테스트() throws Exception {
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .userId("test")
                .password("test1234")
                .name("test")
                .email("test@test.com")
                .role(ROLE_TRAVEL_USER)
                .visible("Y")
                .build();

        mockMvc.perform(post("/front/user")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andDo(print())
                .andDo(document("INSERT-USER",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        relaxedRequestFields(
                                fieldWithPath("userId").type(STRING).description("아이디"),
                                fieldWithPath("password").type(STRING).description("패스워드"),
                                fieldWithPath("name").type(STRING).description("이름"),
                                fieldWithPath("email").type(STRING).description("이메일")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("userId").type(STRING).description("아이디"),
                                fieldWithPath("password").type(STRING).description("패스워드"),
                                fieldWithPath("name").type(STRING).description("이름"),
                                fieldWithPath("email").type(STRING).description("이메일")
                        )))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.userId").value("test"))
                .andExpect(jsonPath("$.name").value("test"))
                .andExpect(jsonPath("$.email").value("test@test.com"))
                .andExpect(jsonPath("$.role").value("ROLE_TRAVEL_USER"));
    }

    @Test
    @Disabled
    @WithMockUser(roles = "USER")
    @DisplayName("유저 회원가입 권한 예외 테스트")
    void 회원가입권한테스트() throws Exception {
        userEntity = UserEntity.builder()
                .userId("test")
                .password("test")
                .name("test")
                .email("test@test.com")
                .visible("Y")
                .build();

        mockMvc.perform(post("/front/user")
                        .header("Authorization", "Bearer " + userEntity.getUserToken())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userEntity)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "TRAVEL_USER")
    @DisplayName("유저 회원수정 테스트")
    void 회원수정테스트() throws Exception {
        UserEntity updateAdminUserEntity = UserEntity.builder()
                .idx(userEntity.getIdx())
                .userId("admin03")
                .password("pass1234")
                .name("admin03")
                .email("admin03@tsp.com")
                .visible("Y")
                .build();

        mockMvc.perform(put("/front/user/{idx}", updateAdminUserEntity.getIdx())
                        .header("Authorization", "Bearer " + userEntity.getUserToken())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(updateAdminUserEntity)))
                .andDo(print())
                .andDo(document("UPDATE-USER",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        relaxedRequestFields(
                                fieldWithPath("userId").type(STRING).description("아이디"),
                                fieldWithPath("password").type(STRING).description("패스워드"),
                                fieldWithPath("name").type(STRING).description("이름"),
                                fieldWithPath("email").type(STRING).description("이메일")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("userId").type(STRING).description("아이디"),
                                fieldWithPath("password").type(STRING).description("패스워드"),
                                fieldWithPath("name").type(STRING).description("이름"),
                                fieldWithPath("email").type(STRING).description("이메일")
                        )))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.userId").value("admin03"))
                .andExpect(jsonPath("$.name").value("admin03"));
    }

    @Test
    @Disabled
    @WithMockUser(roles = "USER")
    @DisplayName("회원수정 권한 예외 테스트")
    void 회원수정권한테스트() throws Exception {
        UserEntity updateUserEntity = UserEntity.builder()
                .idx(userEntity.getIdx())
                .userId("admin03")
                .password("pass1234")
                .name("admin03")
                .email("admin03@tsp.com")
                .visible("Y")
                .build();

        mockMvc.perform(put("/front/user/{idx}", updateUserEntity.getIdx())
                        .header("Authorization", "Bearer " + userEntity.getUserToken())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userEntity)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "TRAVEL_USER")
    @DisplayName("유저 회원탈퇴 테스트")
    void 회원탈퇴테스트() throws Exception {
        mockMvc.perform(delete("/front/user")
                        .header("Authorization", "Bearer " + userEntity.getUserToken())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userEntity)))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @Disabled
    @WithMockUser(roles = "USER")
    @DisplayName("유저 회원탈퇴 권한 테스트")
    void 회원탈퇴권한테스트() throws Exception {
        mockMvc.perform(put("/front/user")
                        .header("Authorization", "Bearer " + userEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser("TRAVEL_USER")
    @DisplayName("유저가 좋아하는 여행지 추가 테스트")
    void 유저가좋아하는여행지추가테스트() throws Exception {
        List<String> favoriteIdxList = new ArrayList<>();
        favoriteIdxList.add("1");
        favoriteIdxList.add("2");

        mockMvc.perform(put("/front/user/{idx}/favorite-travel", userEntity.getIdx())
                        .header("Authorization", "Bearer " + userEntity.getUserToken())
                        .param("favoriteIdx", "2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("USER-TRAVEL-FAVORITE", pathParameters(
                        parameterWithName("idx").description("USER IDX")
                )))
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.favoriteTravelIdx").value(favoriteIdxList));

        favoriteIdxList.add("3");

        mockMvc.perform(put("/front/user/{idx}/favorite-travel", userEntity.getIdx())
                        .header("Authorization", "Bearer " + userEntity.getUserToken())
                        .param("favoriteIdx", "3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("USER-TRAVEL-FAVORITE", pathParameters(
                        parameterWithName("idx").description("USER IDX")
                )))
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.favoriteTravelIdx").value(favoriteIdxList));
    }

    @Test
    @WithMockUser("TRAVEL_USER")
    @DisplayName("유저가 작성한 스케줄 리스트 조회 테스트")
    void 유저가작성한스케줄리스트조회테스트() throws Exception {
        CommonEntity commonEntity = CommonEntity.builder()
                .commonCode(1000)
                .commonName("서울")
                .visible("Y")
                .build();

        em.persist(commonEntity);

        TravelScheduleEntity travelScheduleEntity = TravelScheduleEntity.builder()
                .commonEntity(commonEntity)
                .userEntity(userEntity)
                .scheduleDescription("유저 스케줄")
                .scheduleTime(LocalDateTime.now())
                .build();

        em.persist(travelScheduleEntity);

        mockMvc.perform(get("/front/user/{idx}/schedule", userEntity.getIdx())
                        .header("Authorization", "Bearer " + userEntity.getUserToken()))
                .andDo(print())
                .andDo(document("GET-USER", pathParameters(
                        parameterWithName("idx").description("USER IDX")
                )))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"));
    }

    @Test
    @WithMockUser("TRAVEL_USER")
    @DisplayName("유저가 작성한 스케줄 상세 조회 테스트")
    void 유저가작성한스케줄상세조회테스트() throws Exception {
        CommonEntity commonEntity = CommonEntity.builder()
                .commonCode(1000)
                .commonName("서울")
                .visible("Y")
                .build();

        em.persist(commonEntity);

        TravelScheduleEntity travelScheduleEntity = TravelScheduleEntity.builder()
                .commonEntity(commonEntity)
                .userEntity(userEntity)
                .scheduleDescription("유저 스케줄")
                .scheduleTime(LocalDateTime.now())
                .build();

        em.persist(travelScheduleEntity);

        mockMvc.perform(get("/front/user/{idx}/schedule/{scheduleIdx}", userEntity.getIdx(), travelScheduleEntity.getIdx())
                        .header("Authorization", "Bearer " + userEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.userDTO.idx").value(userEntity.getIdx()));
    }

    @Test
    @WithMockUser("TRAVEL_USER")
    @DisplayName("유저가 예약한 여행 리스트 조회 테스트")
    void 유저가예약한여행리스트조회테스트() throws Exception {
        CommonEntity commonEntity = CommonEntity.builder()
                .commonCode(1)
                .commonName("서울")
                .visible("Y")
                .build();

        em.persist(commonEntity);

        TravelReservationEntity travelReservationEntity = TravelReservationEntity.builder()
                .commonEntity(commonEntity)
                .title("예약 등록지")
                .description("예약 등록지")
                .address("서울 강남구")
                .zipCode("123-456")
                .price(50000)
                .possibleCount(10)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .status(true)
                .popular(false)
                .build();

        em.persist(travelReservationEntity);

        // 유저 여행 예약 등록
        UserReservationEntity userReservationEntity = UserReservationEntity.builder()
                .newUserEntity(userEntity)
                .travelReservationEntity(travelReservationEntity)
                .price(travelReservationEntity.getPrice())
                .startDate(LocalDateTime.of(2022, 2, 1, 0, 0, 0))
                .endDate(LocalDateTime.of(2022, 2, 3, 23, 59, 59))
                .userCount(2)
                .build();

        em.persist(userReservationEntity);

        mockMvc.perform(get("/front/user/{idx}/reservation", userEntity.getIdx())
                        .header("Authorization", "Bearer " + userEntity.getUserToken()))
                .andDo(print())
                .andDo(document("GET-USER", pathParameters(
                        parameterWithName("idx").description("USER IDX")
                )))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"));
    }

    @Test
    @WithMockUser("TRAVEL_USER")
    @DisplayName("유저 여행 예약 테스트")
    void 유저여행예약테스트() throws Exception {
        CommonEntity commonEntity = CommonEntity.builder()
                .commonCode(1)
                .commonName("서울")
                .visible("Y")
                .build();

        em.persist(commonEntity);

        TravelReservationEntity travelReservationEntity = TravelReservationEntity.builder()
                .commonEntity(commonEntity)
                .title("예약 등록지")
                .description("예약 등록지")
                .address("서울 강남구")
                .zipCode("123-456")
                .price(50000)
                .possibleCount(10)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .status(true)
                .popular(false)
                .build();

        em.persist(travelReservationEntity);

        // 유저 여행 예약 등록
        UserReservationEntity userReservationEntity = UserReservationEntity.builder()
                .newUserEntity(userEntity)
                .travelReservationEntity(travelReservationEntity)
                .price(travelReservationEntity.getPrice())
                .startDate(LocalDateTime.of(2022, 2, 1, 0, 0, 0))
                .endDate(LocalDateTime.of(2022, 2, 3, 23, 59, 59))
                .userCount(2)
                .build();

        mockMvc.perform(post("/front/user/{idx}/reservation/{reservationIdx}", userEntity.getIdx(), travelReservationEntity.getIdx())
                        .header("Authorization", "Bearer " + userEntity.getUserToken())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userReservationEntity)))
                .andDo(print())
                .andDo(document("INSERT-RESERVATION",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        relaxedRequestFields(
                                fieldWithPath("price").type(NUMBER).description("예약 가격"),
                                fieldWithPath("startDate").type(STRING).description("예약 시작 일자"),
                                fieldWithPath("endDate").type(STRING).description("예약 마감 일자"),
                                fieldWithPath("userCount").type(NUMBER).description("예약 인원")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("price").type(NUMBER).description("예약 가격"),
                                fieldWithPath("startDate").type(STRING).description("예약 시작 일자"),
                                fieldWithPath("endDate").type(STRING).description("예약 마감 일자"),
                                fieldWithPath("userCount").type(NUMBER).description("예약 인원")
                        )))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.price").value(50000));
    }

    @Test
    @WithMockUser("TRAVEL_USER")
    @DisplayName("유저 여행 예약 취소 테스트")
    void 유저여행예약취소테스트() throws Exception {
        CommonEntity commonEntity = CommonEntity.builder()
                .commonCode(1)
                .commonName("서울")
                .visible("Y")
                .build();

        em.persist(commonEntity);

        TravelReservationEntity travelReservationEntity = TravelReservationEntity.builder()
                .commonEntity(commonEntity)
                .title("예약 등록지")
                .description("예약 등록지")
                .address("서울 강남구")
                .zipCode("123-456")
                .price(50000)
                .possibleCount(10)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .status(true)
                .popular(false)
                .build();

        em.persist(travelReservationEntity);

        // 유저 여행 예약 등록
        UserReservationEntity userReservationEntity = UserReservationEntity.builder()
                .newUserEntity(userEntity)
                .travelReservationEntity(travelReservationEntity)
                .price(travelReservationEntity.getPrice())
                .startDate(LocalDateTime.of(2022, 2, 1, 0, 0, 0))
                .endDate(LocalDateTime.of(2022, 2, 3, 23, 59, 59))
                .userCount(2)
                .build();

        em.persist(userReservationEntity);

        mockMvc.perform(delete("/front/user/{idx}/reservation/{reservationIdx}", userEntity.getIdx(), userReservationEntity.getIdx())
                        .header("Authorization", "Bearer " + userEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser("TRAVEL_USER")
    @DisplayName("유저 여행 그룹 가입 테스트")
    void 유저여행그룹가입테스트() throws Exception {
        CommonEntity commonEntity = CommonEntity.builder()
                .commonCode(100)
                .commonName("서울")
                .visible("Y")
                .build();

        em.persist(commonEntity);

        // 여행지 등록
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

        // 여행 그룹 등록
        TravelGroupEntity travelGroupEntity = TravelGroupEntity.builder()
                .travelEntity(travelEntity)
                .groupName("서울 그룹")
                .groupDescription("서울 그룹")
                .visible("Y")
                .build();

        em.persist(travelGroupEntity);

        mockMvc.perform(post("/front/user/{idx}/group/{groupIdx}", userEntity.getIdx(), travelGroupEntity.getIdx())
                        .header("Authorization", "Bearer " + userEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userDTO.idx").value(userEntity.getIdx()));
    }

    @Test
    @WithMockUser("TRAVEL_USER")
    @DisplayName("유저 여행 그룹 탈퇴 테스트")
    void 유저여행그룹탈퇴테스트() throws Exception {
        CommonEntity commonEntity = CommonEntity.builder()
                .commonCode(100)
                .commonName("서울")
                .visible("Y")
                .build();

        em.persist(commonEntity);

        // 여행지 등록
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

        // 여행 그룹 등록
        TravelGroupEntity travelGroupEntity = TravelGroupEntity.builder()
                .travelEntity(travelEntity)
                .groupName("서울 그룹")
                .groupDescription("서울 그룹")
                .visible("Y")
                .build();

        em.persist(travelGroupEntity);

        TravelGroupUserEntity travelGroupUserEntity = TravelGroupUserEntity.builder()
                .userEntity(userEntity)
                .travelGroupEntity(travelGroupEntity)
                .build();

        em.persist(travelGroupUserEntity);

        mockMvc.perform(delete("/front/user/{idx}/group/{groupIdx}", userEntity.getIdx(), travelGroupEntity.getIdx())
                        .header("Authorization", "Bearer " + userEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}

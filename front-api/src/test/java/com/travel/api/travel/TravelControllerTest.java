package com.travel.api.travel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.api.common.domain.CommonEntity;
import com.travel.api.common.domain.EntityType;
import com.travel.api.travel.domain.TravelEntity;
import com.travel.api.travel.domain.festival.TravelFestivalEntity;
import com.travel.api.travel.domain.image.TravelImageEntity;
import com.travel.api.travel.domain.recommend.TravelRecommendEntity;
import com.travel.api.travel.domain.review.TravelReviewEntity;
import com.travel.api.travel.domain.schedule.TravelScheduleEntity;
import com.travel.api.travel.domain.search.SearchEntity;
import com.travel.api.user.domain.AuthenticationRequest;
import com.travel.api.user.domain.UserEntity;
import com.travel.jwt.JwtUtil;
import com.travel.jwt.MyUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.travel.api.user.domain.Role.ROLE_TRAVEL_USER;
import static com.travel.common.StringUtil.getString;
import static org.hamcrest.Matchers.greaterThan;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.crypto.factory.PasswordEncoderFactories.createDelegatingPasswordEncoder;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
@Transactional
@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@DisplayName("여행지 Api Test")
class TravelControllerTest {
    private MockMvc mockMvc;
    private final WebApplicationContext wac;
    private final ObjectMapper objectMapper;
    private final EntityManager em;

    private CommonEntity commonEntity;
    private UserEntity userEntity;
    private TravelEntity travelEntity;
    private TravelImageEntity travelImageEntity;

    private final JwtUtil jwtUtil;
    protected PasswordEncoder passwordEncoder;

    @MockBean
    protected MyUserDetailsService myUserDetailsService;
    protected AuthenticationRequest authenticationRequest;

    Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        return authorities;
    }

    @DisplayName("테스트 유저 생성")
    void createUser() {
        passwordEncoder = createDelegatingPasswordEncoder();

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("admin04", "pass1234", getAuthorities());
        String token = jwtUtil.doGenerateToken(authenticationToken.getName());

        userEntity = UserEntity.builder()
                .userId("admin05")
                .password(passwordEncoder.encode("pass1234"))
                .name("test")
                .email("test@test.com")
                .role(ROLE_TRAVEL_USER)
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

        authenticationRequest = new AuthenticationRequest(userEntity);
        when(myUserDetailsService.loadUserByUsername(userEntity.getUserId())).thenReturn(authenticationRequest);

        commonEntity = CommonEntity.builder()
                .commonCode(999)
                .commonName("서울")
                .visible("Y")
                .build();

        em.persist(commonEntity);

        commonEntity = CommonEntity.builder()
                .commonCode(999)
                .commonName("서울")
                .visible("Y")
                .build();

        em.persist(commonEntity);

        travelEntity = TravelEntity.builder()
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

        travelImageEntity = TravelImageEntity.builder()
                .typeIdx(travelEntity.getIdx())
                .imageType("main")
                .fileName("test.jpg")
                .fileMask("test.jpg")
                .filePath("/test/test.jpg")
                .entityType(EntityType.TRAVEL)
                .build();

        em.persist(travelImageEntity);
    }

    @Test
    @DisplayName("여행지 조회 테스트")
    void 여행지조회테스트() throws Exception {
        LinkedMultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        mockMvc.perform(get("/api/travel/lists").queryParams(paramMap).param("pageNum", "1").param("size", "3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"));
    }

    @Test
    @DisplayName("여행지 상세 조회 테스트")
    void 여행지상세조회테스트() throws Exception {
        mockMvc.perform(get("/api/travel/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.idx").value(1L));
    }

    @Test
    @WithMockUser("TRAVEL_USER")
    @DisplayName("여행지 좋아요 테스트")
    void 여행지좋아요테스트() throws Exception {
        mockMvc.perform(put("/front/travel/{idx}/favorite", travelEntity.getIdx())
                        .header("Authorization", "Bearer " + userEntity.getUserToken()))
                .andDo(print())
                .andDo(document("/front/travel/{idx}/favorite", pathParameters(
                        parameterWithName("idx").description("여행 IDX")
                )))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(content().string(getString(2)));
    }

    @Test
    @DisplayName("여행지 리뷰 등록 테스트")
    void 여행지리뷰등록테스트() throws Exception {
        TravelReviewEntity travelReviewEntity = TravelReviewEntity.builder()
                .reviewTitle("리뷰등록테스트")
                .reviewDescription("리뷰등록테스트")
                .viewCount(0)
                .favoriteCount(0)
                .popular(false)
                .visible("Y")
                .build();

        mockMvc.perform(post("/api/travel/1/review")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(travelReviewEntity)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.travelIdx").value(1L));
    }

    @Test
    @DisplayName("여행지 리뷰 수정 테스트")
    void 여행지리뷰수정테스트() throws Exception {
        TravelReviewEntity travelReviewEntity = TravelReviewEntity.builder()
                .reviewTitle("리뷰등록테스트")
                .reviewDescription("리뷰등록테스트")
                .viewCount(0)
                .favoriteCount(0)
                .popular(false)
                .visible("Y")
                .build();

        em.persist(travelReviewEntity);

        TravelReviewEntity newTravelReviewEntity = TravelReviewEntity.builder()
                .idx(travelReviewEntity.getIdx())
                .reviewTitle("리뷰수정테스트")
                .reviewDescription("리뷰수정테스트")
                .viewCount(0)
                .favoriteCount(0)
                .popular(false)
                .visible("Y")
                .build();

        mockMvc.perform(put("/api/travel/{idx}/review", travelReviewEntity.getIdx())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newTravelReviewEntity)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.reviewTitle").value("리뷰수정테스트"))
                .andExpect(jsonPath("$.reviewDescription").value("리뷰수정테스트"));
    }

    @Test
    @DisplayName("여행지 리뷰 삭제 테스트")
    void 여행지리뷰삭제테스트() throws Exception {
        TravelReviewEntity travelReviewEntity = TravelReviewEntity.builder()
                .reviewTitle("리뷰등록테스트")
                .reviewDescription("리뷰등록테스트")
                .viewCount(0)
                .favoriteCount(0)
                .popular(false)
                .visible("Y")
                .build();

        em.persist(travelReviewEntity);

        mockMvc.perform(delete("/api/travel/{idx}/review", travelReviewEntity.getIdx()))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("여행지 댓글 리스트 조회 테스트")
    void 여행지리뷰리스트조회테스트() throws Exception {
        mockMvc.perform(get("/api/travel/1/reply"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"));
    }

    @Test
    @DisplayName("유저 여행 스케줄 등록 테스트")
    void 유저여행스케줄등록테스트() throws Exception {
        TravelScheduleEntity travelScheduleEntity = TravelScheduleEntity.builder()
                .commonEntity(commonEntity)
                .userEntity(userEntity)
                .scheduleDescription("스케줄 테스트")
                .scheduleTime(LocalDateTime.now())
                .build();

        mockMvc.perform(post("/api/travel/schedule")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(travelScheduleEntity)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.userIdx").value(1L))
                .andExpect(jsonPath("$.travelIdx").value(1L))
                .andExpect(jsonPath("$.scheduleDescription").value("스케줄 테스트"));
    }

    @Test
    @DisplayName("유저 여행 스케줄 수정 테스트")
    void 유저여행스케줄수정테스트() throws Exception {
        TravelScheduleEntity travelScheduleEntity = TravelScheduleEntity.builder()
//                .travelIdx(1L)
//                .userIdx(1L)
                .scheduleDescription("스케줄 테스트")
                .scheduleTime(LocalDateTime.now())
                .build();

        em.persist(travelScheduleEntity);

        TravelScheduleEntity updateTravelScheduleEntity = TravelScheduleEntity.builder()
                .idx(travelScheduleEntity.getIdx())
                .userEntity(userEntity)
                .commonEntity(commonEntity)
                .scheduleDescription("스케줄 수정 테스트")
                .scheduleTime(LocalDateTime.now())
                .build();

        mockMvc.perform(put("/api/travel/{idx}/schedule", travelScheduleEntity.getIdx())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(updateTravelScheduleEntity)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.scheduleDescription").value("스케줄 수정 테스트"));
    }

    @Test
    @DisplayName("유저 여행 스케줄 삭제 테스트")
    void 유저여행스케줄삭제테스트() throws Exception {
        TravelScheduleEntity travelScheduleEntity = TravelScheduleEntity.builder()
//                .travelIdx(1L)
//                .userIdx(1L)
                .scheduleDescription("스케줄 테스트")
                .scheduleTime(LocalDateTime.now())
                .build();

        em.persist(travelScheduleEntity);

        mockMvc.perform(delete("/api/travel/{idx}/schedule", travelScheduleEntity.getIdx()))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(content().string(getString(travelScheduleEntity.getIdx())));
    }

    @Test
    @DisplayName("여행지 추천 검색어 조회 테스트")
    void 여행지추천검색어조회테스트() throws Exception {
        List<String> recommendList = new ArrayList<>();
        recommendList.add("서울");
        recommendList.add("인천");

        TravelRecommendEntity travelRecommendEntity = TravelRecommendEntity.builder()
                .recommendName(recommendList)
                .build();

        em.persist(travelRecommendEntity);

        mockMvc.perform(get("/api/travel/recommend").param("pageNum", "1").param("size", "100"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.content").isNotEmpty());
    }

    @Test
    @DisplayName("여행지 추천 검색어 상세 조회 테스트")
    void 여행지추천검색어상세조회테스트() throws Exception {
        List<String> recommendList = new ArrayList<>();
        recommendList.add("서울");
        recommendList.add("인천");

        TravelRecommendEntity travelRecommendEntity = TravelRecommendEntity.builder()
                .recommendName(recommendList)
                .build();

        em.persist(travelRecommendEntity);

        mockMvc.perform(get("/api/travel/{idx}/recommend", travelRecommendEntity.getIdx()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.idx").value(travelRecommendEntity.getIdx()));
    }

    @Test
    @DisplayName("검색어 랭킹 리스트 조회 테스트")
    void 검색어랭킹리스트조회테스트() throws Exception {
        em.persist(SearchEntity.builder().searchKeyword("서울").build());
        em.persist(SearchEntity.builder().searchKeyword("서울").build());
        em.persist(SearchEntity.builder().searchKeyword("인천").build());

        mockMvc.perform(get("/api/travel/rank"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.rankList.length()", greaterThan(0)));
    }

    @Test
    @DisplayName("검색어를 통한 여행지 조회 테스트")
    void 검색어를통한여행지조회테스트() throws Exception {
        mockMvc.perform(get("/api/travel/keyword").param("keyword", "서울"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.travelList.length()", greaterThan(0)));
    }

    @Test
    @DisplayName("축제 리스트 갯수 그룹 조회")
    void 축제리스트갯수그룹조회() throws Exception {
        // 등록
        LocalDateTime dateTime = LocalDateTime.now();

        TravelFestivalEntity travelFestivalEntity = TravelFestivalEntity.builder()
                .newFestivalCode(commonEntity)
                .festivalTitle("축제 제목")
                .festivalDescription("축제 내용")
                .festivalMonth(dateTime.getMonthValue())
                .festivalDay(dateTime.getDayOfMonth())
                .festivalTime(dateTime)
                .build();

        em.persist(travelFestivalEntity);
        em.flush();
        em.clear();

        mockMvc.perform(get("/api/travel/festival/list/{month}", dateTime.getMonthValue()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.festivalGroup.length()", greaterThan(0)));
    }

    @Test
    @DisplayName("축제리스트조회")
    void 축제리스트조회() throws Exception {
        // 등록
        LocalDateTime dateTime = LocalDateTime.now();

        TravelFestivalEntity travelFestivalEntity = TravelFestivalEntity.builder()
                .newFestivalCode(commonEntity)
                .festivalTitle("축제 제목")
                .festivalDescription("축제 내용")
                .festivalMonth(dateTime.getMonthValue())
                .festivalDay(dateTime.getDayOfMonth())
                .festivalTime(dateTime)
                .build();

        em.persist(travelFestivalEntity);
        em.flush();
        em.clear();

        mockMvc.perform(get("/api/travel/festival/list/{month}/{day}", dateTime.getMonthValue(), dateTime.getDayOfMonth()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.festivalList.length()", greaterThan(0)));
    }

    @Test
    @DisplayName("축제 상세 조회 테스트")
    void 축제상세조회테스트() throws Exception {
        // 등록
        LocalDateTime dateTime = LocalDateTime.now();

        TravelFestivalEntity travelFestivalEntity = TravelFestivalEntity.builder()
                .newFestivalCode(commonEntity)
                .festivalTitle("축제 제목")
                .festivalDescription("축제 내용")
                .festivalMonth(dateTime.getMonthValue())
                .festivalDay(dateTime.getDayOfMonth())
                .festivalTime(dateTime)
                .build();

        em.persist(travelFestivalEntity);

        mockMvc.perform(get("/api/travel/festival/{idx}", travelFestivalEntity.getIdx()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.idx").value(travelFestivalEntity.getIdx()));
    }
}

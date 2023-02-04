package com.travel.api.travel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.api.common.domain.CommonEntity;
import com.travel.api.common.domain.EntityType;
import com.travel.api.travel.domain.TravelEntity;
import com.travel.api.travel.domain.festival.TravelFestivalEntity;
import com.travel.api.travel.domain.group.TravelGroupEntity;
import com.travel.api.travel.domain.image.TravelImageEntity;
import com.travel.api.travel.domain.recommend.TravelRecommendEntity;
import com.travel.api.travel.domain.reservation.TravelReservationEntity;
import com.travel.api.travel.domain.review.TravelReviewEntity;
import com.travel.api.travel.domain.search.SearchEntity;
import com.travel.api.user.domain.AuthenticationRequest;
import com.travel.api.user.domain.UserEntity;
import com.travel.jwt.JwtUtil;
import com.travel.jwt.MyUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.event.EventListener;
import org.springframework.mock.web.MockMultipartFile;
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
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;

import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.google.common.collect.ImmutableList.of;
import static com.travel.api.user.domain.Role.ROLE_ADMIN;
import static com.travel.common.StringUtil.getString;
import static org.hamcrest.Matchers.greaterThan;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.security.crypto.factory.PasswordEncoderFactories.createDelegatingPasswordEncoder;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
@Transactional
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
    private TravelEntity travelEntity;
    private TravelImageEntity travelImageEntity;
    private final JwtUtil jwtUtil;

    private UserEntity adminUserEntity;
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

        adminUserEntity = UserEntity.builder()
                .userId("admin05")
                .password(passwordEncoder.encode("pass1234"))
                .name("test")
                .email("test@test.com")
                .role(ROLE_ADMIN)
                .userToken(token)
                .visible("Y")
                .build();

        em.persist(adminUserEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void setup() {
        this.mockMvc = webAppContextSetup(wac)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .apply(springSecurity())
                .alwaysDo(print())
                .build();

        createUser();

        authenticationRequest = new AuthenticationRequest(adminUserEntity);
        when(myUserDetailsService.loadUserByUsername(adminUserEntity.getUserId())).thenReturn(authenticationRequest);

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
    @WithMockUser("ADMIN")
    @DisplayName("여행지 조회 테스트")
    void 여행지조회테스트() throws Exception {
        LinkedMultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        mockMvc.perform(get("/admin/travel").queryParams(paramMap).param("pageNum", "1").param("size", "3")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"));
    }

    @Test
    @WithMockUser("ADMIN")
    @DisplayName("여행지 상세 조회 테스트")
    void 여행지상세조회테스트() throws Exception {
        mockMvc.perform(get("/admin/travel/{idx}", travelEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.idx").value(travelEntity.getIdx()));
    }

    @Test
    @WithMockUser("ADMIN")
    @DisplayName("여행지 삭제 테스트")
    void 여행지삭제테스트() throws Exception {
        mockMvc.perform(delete("/admin/travel/{idx}", travelEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser("ADMIN")
    @DisplayName("여행지 좋아요 테스트")
    void 여행지좋아요테스트() throws Exception {
        mockMvc.perform(put("/admin/travel/{idx}/favorite", travelEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(content().string(getString(travelEntity.getIdx())));
    }

    @Test
    @DisplayName("여행지 이미지 등록 테스트")
    void 여행지이미지등록테스트() throws Exception {
        List<MultipartFile> imageFiles = of(
                new MockMultipartFile("0522045010647", "0522045010647.png",
                        "image/png", new FileInputStream("src/main/resources/static/images/0522045010647.png")),
                new MockMultipartFile("0522045010772", "0522045010772.png",
                        "image/png", new FileInputStream("src/main/resources/static/images/0522045010772.png"))
        );

        mockMvc.perform(multipart("/admin/travel/1/images")
                        .file("images", imageFiles.get(0).getBytes())
                        .file("images", imageFiles.get(1).getBytes()))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser("ADMIN")
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

        mockMvc.perform(post("/admin/travel/{idx}/review", travelEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(travelReviewEntity)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=utf-8"));
    }

    @Test
    @WithMockUser("ADMIN")
    @DisplayName("여행지 리뷰 수정 테스트")
    void 여행지리뷰수정테스트() throws Exception {
        TravelReviewEntity travelReviewEntity = TravelReviewEntity.builder()
                .reviewTitle("리뷰등록테스트")
                .reviewDescription("리뷰등록테스트")
                .viewCount(0)
                .newTravelEntity(travelEntity)
                .favoriteCount(0)
                .popular(false)
                .visible("Y")
                .build();

        em.persist(travelReviewEntity);

        TravelReviewEntity newTravelReviewEntity = TravelReviewEntity.builder()
                .idx(travelReviewEntity.getIdx())
                .reviewTitle("리뷰수정테스트")
                .reviewDescription("리뷰수정테스트")
                .newTravelEntity(travelEntity)
                .viewCount(0)
                .favoriteCount(0)
                .popular(false)
                .visible("Y")
                .build();

        mockMvc.perform(put("/admin/travel/{idx}/review", travelReviewEntity.getIdx())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newTravelReviewEntity))
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.reviewTitle").value("리뷰수정테스트"))
                .andExpect(jsonPath("$.reviewDescription").value("리뷰수정테스트"));
    }

    @Test
    @WithMockUser("ADMIN")
    @DisplayName("여행지 리뷰 삭제 테스트")
    void 여행지리뷰삭제테스트() throws Exception {
        TravelReviewEntity travelReviewEntity = TravelReviewEntity.builder()
                .reviewTitle("리뷰등록테스트")
                .reviewDescription("리뷰등록테스트")
                .newTravelEntity(travelEntity)
                .viewCount(0)
                .favoriteCount(0)
                .popular(false)
                .visible("Y")
                .build();

        em.persist(travelReviewEntity);

        mockMvc.perform(delete("/admin/travel/{idx}/review", travelReviewEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser("ADMIN")
    @DisplayName("여행지 댓글 리스트 조회 테스트")
    void 여행지리뷰리스트조회테스트() throws Exception {
        mockMvc.perform(get("/admin/travel/{idx}/reply", travelEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"));
    }

    @Test
    @WithMockUser("ADMIN")
    @DisplayName("인기 여행지 선정 테스트")
    void 인기여행지선정테스트() throws Exception {
        mockMvc.perform(put("/admin/travel/{idx}/popular", travelEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(content().string(String.valueOf(true)));
    }

    @Test
    @WithMockUser("ADMIN")
    @DisplayName("여행지 그룹 조회 테스트")
    void 여행지그룹조회테스트() throws Exception {
        mockMvc.perform(get("/admin/travel/group").param("pageNum", "1").param("size", "100")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"));
    }

    @Test
    @WithMockUser("ADMIN")
    @DisplayName("여행지 그룹 상세 조회 테스트")
    void 여행지그룹상세조회테스트() throws Exception {
        mockMvc.perform(get("/admin/travel/{idx}/group", travelEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.idx").value(travelEntity.getIdx()));
    }

    @Test
    @WithMockUser("ADMIN")
    @DisplayName("여행지 그룹 등록 테스트")
    void 여행지그룹등록테스트() throws Exception {
        TravelGroupEntity travelGroupEntity = TravelGroupEntity.builder()
                .groupName("서울모임")
                .groupDescription("서울모임")
                .visible("Y")
                .build();

        mockMvc.perform(post("/admin/travel/{idx}/group", travelEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(travelGroupEntity)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.travelIdx").value(travelEntity.getIdx()))
                .andExpect(jsonPath("$.groupName").value("서울모임"))
                .andExpect(jsonPath("$.groupDescription").value("서울모임"));
    }

    @Test
    @WithMockUser("ADMIN")
    @DisplayName("여행지 그룹 수정 테스트")
    void 여행지그룹수정테스트() throws Exception {
        TravelGroupEntity travelGroupEntity = TravelGroupEntity.builder()
                .travelEntity(travelEntity)
                .groupName("서울모임")
                .groupDescription("서울모임")
                .visible("Y")
                .build();

        em.persist(travelGroupEntity);

        TravelGroupEntity newTravelGroupEntity = TravelGroupEntity.builder()
                .idx(travelGroupEntity.getIdx())
                .travelEntity(travelEntity)
                .groupName("인천모임")
                .groupDescription("인천모임")
                .visible("Y")
                .build();

        mockMvc.perform(put("/admin/travel/{idx}/group/{groupIdx}", travelEntity.getIdx(), travelGroupEntity.getIdx())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newTravelGroupEntity))
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.groupName").value("인천모임"))
                .andExpect(jsonPath("$.groupDescription").value("인천모임"));
    }

    @Test
    @WithMockUser("ADMIN")
    @DisplayName("여행지 그룹 삭제 테스트")
    void 여행지그룹삭제테스트() throws Exception {
        TravelGroupEntity travelGroupEntity = TravelGroupEntity.builder()
                .travelEntity(travelEntity)
                .groupName("서울모임")
                .groupDescription("서울모임")
                .visible("Y")
                .build();

        em.persist(travelGroupEntity);

        mockMvc.perform(delete("/admin/travel/group/{groupIdx}", travelGroupEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser("ADMIN")
    @DisplayName("여행지 추천 검색어 조회 테스트")
    void 여행지추천검색어조회테스트() throws Exception {
        List<String> recommendList = new ArrayList<>();
        recommendList.add("서울");
        recommendList.add("인천");

        TravelRecommendEntity travelRecommendEntity = TravelRecommendEntity.builder()
                .recommendName(recommendList)
                .build();

        em.persist(travelRecommendEntity);

        mockMvc.perform(get("/admin/travel/recommend").param("pageNum", "1").param("size", "100")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"));
    }

    @Test
    @WithMockUser("ADMIN")
    @DisplayName("여행지 추천 검색어 상세 조회 테스트")
    void 여행지추천검색어상세조회테스트() throws Exception {
        List<String> recommendList = new ArrayList<>();
        recommendList.add("서울");
        recommendList.add("인천");

        TravelRecommendEntity travelRecommendEntity = TravelRecommendEntity.builder()
                .recommendName(recommendList)
                .build();

        em.persist(travelRecommendEntity);

        mockMvc.perform(get("/admin/travel/{idx}/recommend", travelRecommendEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.idx").value(travelRecommendEntity.getIdx()));
    }

    @Test
    @WithMockUser("ADMIN")
    @DisplayName("여행지 추천 검색어 등록 테스트")
    void 여행지추천검색어등록테스트() throws Exception {
        List<String> recommendList = new ArrayList<>();
        recommendList.add("서울");
        recommendList.add("인천");

        TravelRecommendEntity travelRecommendEntity = TravelRecommendEntity.builder()
                .recommendName(recommendList)
                .build();

        mockMvc.perform(post("/admin/travel/recommend")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(travelRecommendEntity))
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.recommendName").value(recommendList));
    }

    @Test
    @WithMockUser("ADMIN")
    @DisplayName("여행지 추천 검색어 수정 테스트")
    void 여행지추천검색어수정테스트() throws Exception {
        List<String> recommendList = new ArrayList<>();
        recommendList.add("서울");
        recommendList.add("인천");

        TravelRecommendEntity travelRecommendEntity = TravelRecommendEntity.builder()
                .recommendName(recommendList)
                .build();

        em.persist(travelRecommendEntity);

        recommendList.add("대구");
        TravelRecommendEntity updateTravelRecommendEntity = TravelRecommendEntity.builder()
                .idx(travelRecommendEntity.getIdx())
                .recommendName(recommendList)
                .build();

        mockMvc.perform(put("/admin/travel/{idx}/recommend", travelRecommendEntity.getIdx())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(updateTravelRecommendEntity))
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.recommendName").value(recommendList));
    }

    @Test
    @WithMockUser("ADMIN")
    @DisplayName("여행지 추천 검색어 삭제 테스트")
    void 여행지추천검색어삭제테스트() throws Exception {
        List<String> recommendList = new ArrayList<>();
        recommendList.add("서울");
        recommendList.add("인천");

        TravelRecommendEntity travelRecommendEntity = TravelRecommendEntity.builder()
                .recommendName(recommendList)
                .build();

        em.persist(travelRecommendEntity);

        mockMvc.perform(delete("/admin/travel/{idx}/recommend", travelRecommendEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser("ADMIN")
    @DisplayName("검색어 랭킹 리스트 조회 테스트")
    void 검색어랭킹리스트조회테스트() throws Exception {
        em.persist(SearchEntity.builder().searchKeyword("서울").build());
        em.persist(SearchEntity.builder().searchKeyword("서울").build());
        em.persist(SearchEntity.builder().searchKeyword("인천").build());

        mockMvc.perform(get("/admin/travel/rank")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.rankList.length()", greaterThan(0)));
    }

    @Test
    @WithMockUser("ADMIN")
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

        mockMvc.perform(get("/admin/travel/festival/list/{month}", dateTime.getMonthValue())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.festivalGroup.length()", greaterThan(0)));
    }

    @Test
    @WithMockUser("ADMIN")
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

        mockMvc.perform(get("/admin/travel/festival/list/{month}/{day}", dateTime.getMonthValue(), dateTime.getDayOfMonth())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.festivalList.length()", greaterThan(0)));
    }

    @Test
    @WithMockUser("ADMIN")
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

        mockMvc.perform(get("/admin/travel/festival/{idx}", travelFestivalEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.idx").value(travelFestivalEntity.getIdx()));
    }

    @Test
    @WithMockUser("ADMIN")
    @DisplayName("축제 등록 테스트")
    void 축제등록테스트() throws Exception {
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

        mockMvc.perform(post("/admin/travel/festival")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(travelFestivalEntity))
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.festivalTitle").value("축제 제목"));
    }

    @Test
    @WithMockUser("ADMIN")
    @DisplayName("축제 수정 테스트")
    void 축제수정테스트() throws Exception {
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

        travelFestivalEntity = TravelFestivalEntity.builder()
                .idx(travelFestivalEntity.getIdx())
                .newFestivalCode(commonEntity)
                .festivalTitle("축제 수정 제목")
                .festivalDescription("축제 수정 내용")
                .festivalMonth(dateTime.getMonthValue())
                .festivalDay(dateTime.getDayOfMonth())
                .festivalTime(dateTime)
                .build();

        mockMvc.perform(put("/admin/travel/festival/{idx}", travelFestivalEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(travelFestivalEntity)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.festivalTitle").value("축제 수정 제목"));
    }

    @Test
    @WithMockUser("ADMIN")
    @DisplayName("축제 삭제 테스트")
    void 축제삭제테스트() throws Exception {
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

        mockMvc.perform(delete("/admin/travel/festival/{idx}", travelFestivalEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser("ADMIN")
    @DisplayName("여행 예약지 리스트 조회")
    void 여행예약지리스트조회() throws Exception {
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
        em.flush();
        em.clear();

        mockMvc.perform(get("/admin/travel/reservation")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"));
    }

    @Test
    @WithMockUser("ADMIN")
    @DisplayName("여행 예약지 상세 조회")
    void 여행예약지상세조회() throws Exception {
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
        em.flush();
        em.clear();

        mockMvc.perform(get("/admin/travel/reservation", travelReservationEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"));
    }

    @Test
    @WithMockUser("ADMIN")
    @DisplayName("여행 예약지 등록")
    void 여행예약지등록() throws Exception {
        TravelReservationEntity travelReservationEntity = TravelReservationEntity.builder()
                .commonEntity(commonEntity)
                .title("예약 등록 테스트")
                .description("예약 등록 테스트")
                .address("인천광역시")
                .zipCode("123-456")
                .price(50000)
                .possibleCount(10)
                .startDate(LocalDateTime.of(2022, 2, 1, 0, 0, 0))
                .endDate(LocalDateTime.of(2022, 2, 28, 23, 59, 59))
                .status(true)
                .popular(false)
                .build();

        mockMvc.perform(post("/admin/travel/reservation")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(travelReservationEntity))
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.title").value(travelReservationEntity.getTitle()));
    }

    @Test
    @WithMockUser("ADMIN")
    @DisplayName("여행 예약지 수정")
    void 여행예약지수정테스트() throws Exception {
        TravelReservationEntity travelReservationEntity = TravelReservationEntity.builder()
                .commonEntity(commonEntity)
                .title("예약 등록 테스트")
                .description("예약 등록 테스트")
                .address("인천광역시")
                .zipCode("123-456")
                .price(50000)
                .possibleCount(10)
                .startDate(LocalDateTime.of(2022, 2, 1, 0, 0, 0))
                .endDate(LocalDateTime.of(2022, 2, 28, 23, 59, 59))
                .status(true)
                .popular(false)
                .build();

        em.persist(travelReservationEntity);

        TravelReservationEntity updateReservation = TravelReservationEntity.builder()
                .idx(travelReservationEntity.getIdx())
                .title("예약 수정 테스트")
                .description("예약 수정 테스트")
                .address("인천광역시")
                .zipCode("123-456")
                .price(50000)
                .possibleCount(10)
                .startDate(LocalDateTime.of(2022, 2, 1, 0, 0, 0))
                .endDate(LocalDateTime.of(2022, 2, 28, 23, 59, 59))
                .status(true)
                .popular(false)
                .build();

        mockMvc.perform(put("/admin/travel/reservation/{idx}", updateReservation.getIdx())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(updateReservation))
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.title").value(updateReservation.getTitle()));
    }

    @Test
    @WithMockUser("ADMIN")
    @DisplayName("여행 예약지 삭제")
    void 여행예약지삭제() throws Exception {
        TravelReservationEntity travelReservationEntity = TravelReservationEntity.builder()
                .commonEntity(commonEntity)
                .title("예약 등록 테스트")
                .description("예약 등록 테스트")
                .address("인천광역시")
                .zipCode("123-456")
                .price(50000)
                .possibleCount(10)
                .startDate(LocalDateTime.of(2022, 2, 1, 0, 0, 0))
                .endDate(LocalDateTime.of(2022, 2, 28, 23, 59, 59))
                .status(true)
                .popular(false)
                .build();

        em.persist(travelReservationEntity);

        mockMvc.perform(delete("/admin/travel/reservation/{idx}", travelReservationEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}

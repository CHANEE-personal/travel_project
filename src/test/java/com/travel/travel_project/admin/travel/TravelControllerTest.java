package com.travel.travel_project.admin.travel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.travel_project.domain.travel.festival.TravelFestivalEntity;
import com.travel.travel_project.domain.travel.group.TravelGroupEntity;
import com.travel.travel_project.domain.travel.group.TravelGroupUserEntity;
import com.travel.travel_project.domain.travel.recommend.TravelRecommendEntity;
import com.travel.travel_project.domain.travel.review.TravelReviewEntity;
import com.travel.travel_project.domain.travel.schedule.TravelScheduleEntity;
import com.travel.travel_project.domain.travel.search.SearchEntity;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.event.EventListener;
import org.springframework.mock.web.MockMultipartFile;
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
import java.util.List;

import static com.travel.travel_project.common.StringUtil.getString;
import static java.util.List.of;
import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
@DisplayName("????????? Api Test")
class TravelControllerTest {
    private MockMvc mockMvc;
    private final WebApplicationContext wac;
    private final ObjectMapper objectMapper;
    private final EntityManager em;

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void setup() {
        this.mockMvc = webAppContextSetup(wac)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @Test
    @DisplayName("????????? ?????? ?????????")
    void ????????????????????????() throws Exception {
        LinkedMultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("jpaStartPage", "1");
        paramMap.add("size", "3");
        mockMvc.perform(get("/api/travel/lists").queryParams(paramMap))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"));
    }

    @Test
    @DisplayName("????????? ?????? ?????? ?????????")
    void ??????????????????????????????() throws Exception {
        mockMvc.perform(get("/api/travel/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.idx").value(1L));
    }

    @Test
    @DisplayName("????????? ?????? ?????????")
    void ????????????????????????() throws Exception {
        mockMvc.perform(delete("/api/travel/{idx}", 1))
                .andDo(print())
                .andExpect(status().isNoContent());

        mockMvc.perform(delete("/api/travel/{idx}", 2))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("????????? ????????? ?????????")
    void ???????????????????????????() throws Exception {
        mockMvc.perform(put("/api/travel/1/favorite"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(content().string(getString(2)));
    }

    @Test
    @DisplayName("????????? ????????? ?????? ?????????")
    void ?????????????????????????????????() throws Exception {
        List<MultipartFile> imageFiles = of(
                new MockMultipartFile("0522045010647", "0522045010647.png",
                        "image/png", new FileInputStream("src/main/resources/static/images/0522045010647.png")),
                new MockMultipartFile("0522045010772", "0522045010772.png",
                        "image/png", new FileInputStream("src/main/resources/static/images/0522045010772.png"))
        );

        mockMvc.perform(multipart("/api/travel/1/images")
                        .file("images", imageFiles.get(0).getBytes())
                        .file("images", imageFiles.get(1).getBytes()))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("????????? ?????? ?????? ?????????")
    void ??????????????????????????????() throws Exception {
        TravelReviewEntity travelReviewEntity = TravelReviewEntity.builder()
                .travelIdx(1L)
                .reviewTitle("?????????????????????")
                .reviewDescription("?????????????????????")
                .viewCount(0)
                .favoriteCount(0)
                .popular(false)
                .visible("Y")
                .build();

        mockMvc.perform(post("/api/travel/1/reply")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(travelReviewEntity)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.travelIdx").value(1L));
    }

    @Test
    @DisplayName("????????? ?????? ?????? ?????????")
    void ??????????????????????????????() throws Exception {
        TravelReviewEntity travelReviewEntity = TravelReviewEntity.builder()
                .travelIdx(1L)
                .reviewTitle("?????????????????????")
                .reviewDescription("?????????????????????")
                .viewCount(0)
                .favoriteCount(0)
                .popular(false)
                .visible("Y")
                .build();

        em.persist(travelReviewEntity);

        TravelReviewEntity newTravelReviewEntity = TravelReviewEntity.builder()
                .idx(travelReviewEntity.getIdx())
                .travelIdx(1L)
                .reviewTitle("?????????????????????")
                .reviewDescription("?????????????????????")
                .viewCount(0)
                .favoriteCount(0)
                .popular(false)
                .visible("Y")
                .build();

        mockMvc.perform(put("/api/travel/{idx}/reply", travelReviewEntity.getIdx())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newTravelReviewEntity)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.reviewTitle").value("?????????????????????"))
                .andExpect(jsonPath("$.reviewDescription").value("?????????????????????"));
    }

    @Test
    @DisplayName("????????? ?????? ?????? ?????????")
    void ??????????????????????????????() throws Exception {
        TravelReviewEntity travelReviewEntity = TravelReviewEntity.builder()
                .travelIdx(1L)
                .reviewTitle("?????????????????????")
                .reviewDescription("?????????????????????")
                .viewCount(0)
                .favoriteCount(0)
                .popular(false)
                .visible("Y")
                .build();

        em.persist(travelReviewEntity);

        mockMvc.perform(delete("/api/travel/{idx}/reply", travelReviewEntity.getIdx()))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(content().string(getString(travelReviewEntity.getIdx())));
    }

    @Test
    @DisplayName("????????? ?????? ????????? ?????? ?????????")
    void ???????????????????????????????????????() throws Exception {
        mockMvc.perform(get("/api/travel/1/reply"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"));
    }

    @Test
    @DisplayName("?????? ????????? ?????? ?????????")
    void ??????????????????????????????() throws Exception {
        mockMvc.perform(put("/api/travel/{idx}/popular", 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(content().string(String.valueOf(false)));
    }

    @Test
    @DisplayName("????????? ?????? ?????? ?????????")
    void ??????????????????????????????() throws Exception {
        mockMvc.perform(get("/api/travel/lists/group").param("page", "1").param("size", "100"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.travelGroupList.length()", greaterThan(0)));
    }

    @Test
    @DisplayName("????????? ?????? ?????? ?????? ?????????")
    void ????????????????????????????????????() throws Exception {
        mockMvc.perform(get("/api/travel/1/group"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.idx").value(1L));
    }

    @Test
    @DisplayName("????????? ?????? ?????? ?????????")
    void ??????????????????????????????() throws Exception {
        TravelGroupEntity travelGroupEntity = TravelGroupEntity.builder()
                .travelIdx(1L)
                .groupName("????????????")
                .groupDescription("????????????")
                .visible("Y")
                .build();

        mockMvc.perform(post("/api/travel/group")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(travelGroupEntity)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.travelIdx").value(1L))
                .andExpect(jsonPath("$.groupName").value("????????????"))
                .andExpect(jsonPath("$.groupDescription").value("????????????"));
    }

    @Test
    @DisplayName("????????? ?????? ?????? ?????????")
    void ??????????????????????????????() throws Exception {
        TravelGroupEntity travelGroupEntity = TravelGroupEntity.builder()
                .travelIdx(1L)
                .groupName("????????????")
                .groupDescription("????????????")
                .visible("Y")
                .build();

        em.persist(travelGroupEntity);

        TravelGroupEntity newTravelGroupEntity = TravelGroupEntity.builder()
                .idx(travelGroupEntity.getIdx())
                .travelIdx(1L)
                .groupName("????????????")
                .groupDescription("????????????")
                .visible("Y")
                .build();

        mockMvc.perform(put("/api/travel/{idx}/group", travelGroupEntity.getIdx())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newTravelGroupEntity)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.groupName").value("????????????"))
                .andExpect(jsonPath("$.groupDescription").value("????????????"));
    }

    @Test
    @DisplayName("????????? ?????? ?????? ?????????")
    void ??????????????????????????????() throws Exception {
        TravelGroupEntity travelGroupEntity = TravelGroupEntity.builder()
                .travelIdx(1L)
                .groupName("????????????")
                .groupDescription("????????????")
                .visible("Y")
                .build();

        em.persist(travelGroupEntity);

        mockMvc.perform(delete("/api/travel/{idx}/group", travelGroupEntity.getIdx()))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(content().string(getString(travelGroupEntity.getIdx())));
    }

    @Test
    @DisplayName("?????? ?????? ?????? ?????? ?????????")
    void ?????????????????????????????????() throws Exception {
        TravelGroupUserEntity travelGroupUserEntity = TravelGroupUserEntity.builder()
                .userIdx(1L)
                .groupIdx(1L)
                .build();

        mockMvc.perform(post("/api/travel/group_user")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(travelGroupUserEntity)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.userIdx").value(1L))
                .andExpect(jsonPath("$.groupIdx").value(1L));
    }

    @Test
    @DisplayName("?????? ?????? ?????? ?????? ?????????")
    void ?????????????????????????????????() throws Exception {
        TravelGroupUserEntity travelGroupUserEntity = TravelGroupUserEntity.builder()
                .userIdx(1L)
                .groupIdx(1L)
                .build();

        em.persist(travelGroupUserEntity);
        mockMvc.perform(delete("/api/travel/{idx}/group_user", travelGroupUserEntity.getIdx()))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(content().string(getString(travelGroupUserEntity.getIdx())));
    }

    @Test
    @DisplayName("?????? ?????? ????????? ?????? ?????????")
    void ????????????????????????????????????() throws Exception {
        TravelScheduleEntity travelScheduleEntity = TravelScheduleEntity.builder()
                .travelIdx(1L)
                .userIdx(1L)
                .scheduleDescription("????????? ?????????")
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
                .andExpect(jsonPath("$.scheduleDescription").value("????????? ?????????"));
    }

    @Test
    @DisplayName("?????? ?????? ????????? ?????? ?????????")
    void ????????????????????????????????????() throws Exception {
        TravelScheduleEntity travelScheduleEntity = TravelScheduleEntity.builder()
                .travelIdx(1L)
                .userIdx(1L)
                .scheduleDescription("????????? ?????????")
                .scheduleTime(LocalDateTime.now())
                .build();

        em.persist(travelScheduleEntity);

        TravelScheduleEntity updateTravelScheduleEntity = TravelScheduleEntity.builder()
                .idx(travelScheduleEntity.getIdx())
                .travelIdx(1L)
                .userIdx(1L)
                .scheduleDescription("????????? ?????? ?????????")
                .scheduleTime(LocalDateTime.now())
                .build();

        mockMvc.perform(put("/api/travel/{idx}/schedule", travelScheduleEntity.getIdx())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(updateTravelScheduleEntity)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.scheduleDescription").value("????????? ?????? ?????????"));
    }

    @Test
    @DisplayName("?????? ?????? ????????? ?????? ?????????")
    void ????????????????????????????????????() throws Exception {
        TravelScheduleEntity travelScheduleEntity = TravelScheduleEntity.builder()
                .travelIdx(1L)
                .userIdx(1L)
                .scheduleDescription("????????? ?????????")
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
    @DisplayName("????????? ?????? ????????? ?????? ?????????")
    void ???????????????????????????????????????() throws Exception {
        List<String> recommendList = new ArrayList<>();
        recommendList.add("??????");
        recommendList.add("??????");

        TravelRecommendEntity travelRecommendEntity = TravelRecommendEntity.builder()
                .recommendName(recommendList)
                .build();

        em.persist(travelRecommendEntity);

        mockMvc.perform(get("/api/travel/recommend").param("page", "1").param("size", "100"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.travelRecommendList.length()", greaterThan(0)));
    }

    @Test
    @DisplayName("????????? ?????? ????????? ?????? ?????? ?????????")
    void ?????????????????????????????????????????????() throws Exception {
        List<String> recommendList = new ArrayList<>();
        recommendList.add("??????");
        recommendList.add("??????");

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
    @DisplayName("????????? ?????? ????????? ?????? ?????????")
    void ???????????????????????????????????????() throws Exception {
        List<String> recommendList = new ArrayList<>();
        recommendList.add("??????");
        recommendList.add("??????");

        TravelRecommendEntity travelRecommendEntity = TravelRecommendEntity.builder()
                .recommendName(recommendList)
                .build();

        mockMvc.perform(post("/api/travel/recommend")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(travelRecommendEntity)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.recommendName").value(recommendList));
    }

    @Test
    @DisplayName("????????? ?????? ????????? ?????? ?????????")
    void ???????????????????????????????????????() throws Exception {
        List<String> recommendList = new ArrayList<>();
        recommendList.add("??????");
        recommendList.add("??????");

        TravelRecommendEntity travelRecommendEntity = TravelRecommendEntity.builder()
                .recommendName(recommendList)
                .build();

        em.persist(travelRecommendEntity);

        recommendList.add("??????");
        TravelRecommendEntity updateTravelRecommendEntity = TravelRecommendEntity.builder()
                .idx(travelRecommendEntity.getIdx())
                .recommendName(recommendList)
                .build();

        mockMvc.perform(put("/api/travel/{idx}/recommend", travelRecommendEntity.getIdx())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(updateTravelRecommendEntity)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.recommendName").value(recommendList));
    }

    @Test
    @DisplayName("????????? ?????? ????????? ?????? ?????????")
    void ???????????????????????????????????????() throws Exception {
        List<String> recommendList = new ArrayList<>();
        recommendList.add("??????");
        recommendList.add("??????");

        TravelRecommendEntity travelRecommendEntity = TravelRecommendEntity.builder()
                .recommendName(recommendList)
                .build();

        em.persist(travelRecommendEntity);

        mockMvc.perform(delete("/api/travel/{idx}/recommend", travelRecommendEntity.getIdx()))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("????????? ?????? ????????? ?????? ?????????")
    void ???????????????????????????????????????() throws Exception {
        em.persist(SearchEntity.builder().searchKeyword("??????").build());
        em.persist(SearchEntity.builder().searchKeyword("??????").build());
        em.persist(SearchEntity.builder().searchKeyword("??????").build());

        mockMvc.perform(get("/api/travel/rank"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.rankList.length()", greaterThan(0)));
    }

    @Test
    @DisplayName("???????????? ?????? ????????? ?????? ?????????")
    void ??????????????????????????????????????????() throws Exception {
        mockMvc.perform(get("/api/travel/keyword").param("keyword", "??????"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.travelList.length()", greaterThan(0)));
    }

    @Test
    @DisplayName("?????? ????????? ?????? ?????? ??????")
    void ?????????????????????????????????() throws Exception {
        // ??????
        LocalDateTime dateTime = LocalDateTime.now();

        TravelFestivalEntity travelFestivalEntity = TravelFestivalEntity.builder()
                .travelCode(1)
                .festivalTitle("?????? ??????")
                .festivalDescription("?????? ??????")
                .festivalMonth(dateTime.getMonthValue())
                .festivalDay(dateTime.getDayOfMonth())
                .festivalTime(dateTime)
                .build();

        em.persist(travelFestivalEntity);

        TravelFestivalEntity travelFestivalEntity1 = TravelFestivalEntity.builder()
                .travelCode(2)
                .festivalTitle("?????? ??????")
                .festivalDescription("?????? ??????")
                .festivalMonth(dateTime.getMonthValue())
                .festivalDay(dateTime.getDayOfMonth())
                .festivalTime(dateTime)
                .build();

        em.persist(travelFestivalEntity1);
        em.flush();
        em.clear();

        mockMvc.perform(get("/api/travel/festival/list/{month}", dateTime.getMonthValue()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.festivalGroup.length()", greaterThan(0)));
    }

    @Test
    @DisplayName("?????????????????????")
    void ?????????????????????() throws Exception {
        // ??????
        LocalDateTime dateTime = LocalDateTime.now();

        TravelFestivalEntity travelFestivalEntity = TravelFestivalEntity.builder()
                .travelCode(1)
                .festivalTitle("?????? ??????")
                .festivalDescription("?????? ??????")
                .festivalMonth(dateTime.getMonthValue())
                .festivalDay(dateTime.getDayOfMonth())
                .festivalTime(dateTime)
                .build();

        em.persist(travelFestivalEntity);

        TravelFestivalEntity travelFestivalEntity1 = TravelFestivalEntity.builder()
                .travelCode(2)
                .festivalTitle("?????? ??????")
                .festivalDescription("?????? ??????")
                .festivalMonth(dateTime.getMonthValue())
                .festivalDay(dateTime.getDayOfMonth())
                .festivalTime(dateTime)
                .build();

        em.persist(travelFestivalEntity1);

        em.flush();
        em.clear();

        mockMvc.perform(get("/api/travel/festival/list/{month}/{day}", dateTime.getMonthValue(), dateTime.getDayOfMonth()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.festivalList.length()", greaterThan(0)));
    }

    @Test
    @DisplayName("?????? ?????? ?????? ?????????")
    void ???????????????????????????() throws Exception {
        // ??????
        LocalDateTime dateTime = LocalDateTime.now();

        TravelFestivalEntity travelFestivalEntity = TravelFestivalEntity.builder()
                .travelCode(1)
                .festivalTitle("?????? ??????")
                .festivalDescription("?????? ??????")
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

    @Test
    @DisplayName("?????? ?????? ?????????")
    void ?????????????????????() throws Exception {
        // ??????
        LocalDateTime dateTime = LocalDateTime.now();

        TravelFestivalEntity travelFestivalEntity = TravelFestivalEntity.builder()
                .travelCode(1)
                .festivalTitle("?????? ??????")
                .festivalDescription("?????? ??????")
                .festivalMonth(dateTime.getMonthValue())
                .festivalDay(dateTime.getDayOfMonth())
                .festivalTime(dateTime)
                .build();

        mockMvc.perform(post("/api/travel/festival")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(travelFestivalEntity)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.festivalTitle").value("?????? ??????"));
    }

    @Test
    @DisplayName("?????? ?????? ?????????")
    void ?????????????????????() throws Exception {
        // ??????
        LocalDateTime dateTime = LocalDateTime.now();

        TravelFestivalEntity travelFestivalEntity = TravelFestivalEntity.builder()
                .travelCode(1)
                .festivalTitle("?????? ??????")
                .festivalDescription("?????? ??????")
                .festivalMonth(dateTime.getMonthValue())
                .festivalDay(dateTime.getDayOfMonth())
                .festivalTime(dateTime)
                .build();

        em.persist(travelFestivalEntity);

        travelFestivalEntity = TravelFestivalEntity.builder()
                .idx(travelFestivalEntity.getIdx())
                .travelCode(1)
                .festivalTitle("?????? ?????? ??????")
                .festivalDescription("?????? ?????? ??????")
                .festivalMonth(dateTime.getMonthValue())
                .festivalDay(dateTime.getDayOfMonth())
                .festivalTime(dateTime)
                .build();

        mockMvc.perform(put("/api/travel/festival/{idx}", travelFestivalEntity.getIdx())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(travelFestivalEntity)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.festivalTitle").value("?????? ?????? ??????"));
    }

    @Test
    @DisplayName("?????? ?????? ?????????")
    void ?????????????????????() throws Exception {
        // ??????
        LocalDateTime dateTime = LocalDateTime.now();

        TravelFestivalEntity travelFestivalEntity = TravelFestivalEntity.builder()
                .travelCode(1)
                .festivalTitle("?????? ??????")
                .festivalDescription("?????? ??????")
                .festivalMonth(dateTime.getMonthValue())
                .festivalDay(dateTime.getDayOfMonth())
                .festivalTime(dateTime)
                .build();

        em.persist(travelFestivalEntity);

        mockMvc.perform(delete("/api/travel/festival/{idx}", travelFestivalEntity.getIdx()))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}
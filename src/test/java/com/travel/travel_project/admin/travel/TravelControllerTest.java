package com.travel.travel_project.admin.travel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.travel_project.domain.travel.group.TravelGroupEntity;
import com.travel.travel_project.domain.travel.group.TravelGroupUserEntity;
import com.travel.travel_project.domain.travel.review.TravelReviewEntity;
import com.travel.travel_project.domain.travel.schedule.TravelScheduleEntity;
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
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;

import java.io.FileInputStream;
import java.time.LocalDateTime;
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
@AutoConfigureTestDatabase(replace= NONE)
@DisplayName("여행지 Api Test")
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
                .alwaysExpect(status().isOk())
                .alwaysDo(print())
                .build();
    }

    @Test
    @DisplayName("여행지 조회 테스트")
    void 여행지조회테스트() throws Exception {
        mockMvc.perform(get("/api/travel/lists").param("page", "1").param("size", "100"))
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
    @DisplayName("여행지 이미지 등록 테스트")
    void 여행지이미지등록테스트() throws Exception {
        List<MultipartFile> imageFiles = of(
                new MockMultipartFile("0522045010647","0522045010647.png",
                        "image/png" , new FileInputStream("src/main/resources/static/images/0522045010647.png")),
                new MockMultipartFile("0522045010772","0522045010772.png" ,
                        "image/png" , new FileInputStream("src/main/resources/static/images/0522045010772.png"))
        );

        mockMvc.perform(multipart("/api/travel/1/images")
                        .file("images", imageFiles.get(0).getBytes())
                        .file("images", imageFiles.get(1).getBytes()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("여행지 댓글 등록 테스트")
    void 여행지댓글등록테스트() throws Exception {
        TravelReviewEntity travelReviewEntity = TravelReviewEntity.builder()
                .travelIdx(1L)
                .reviewTitle("리뷰등록테스트")
                .reviewDescription("리뷰등록테스트")
                .viewCount(0)
                .favoriteCount(0)
                .popular(false)
                .visible("Y")
                .build();

        mockMvc.perform(post("/api/travel/1/reply")
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(travelReviewEntity)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.travelIdx").value(1L));
    }

    @Test
    @DisplayName("여행지 댓글 수정 테스트")
    void 여행지댓글수정테스트() throws Exception {
        TravelReviewEntity travelReviewEntity = TravelReviewEntity.builder()
                .travelIdx(1L)
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
                .travelIdx(1L)
                .reviewTitle("리뷰수정테스트")
                .reviewDescription("리뷰수정테스트")
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
                .andExpect(jsonPath("$.reviewTitle").value("리뷰수정테스트"))
                .andExpect(jsonPath("$.reviewDescription").value("리뷰수정테스트"));
    }

    @Test
    @DisplayName("여행지 댓글 삭제 테스트")
    void 여행지댓글삭제테스트() throws Exception {
        TravelReviewEntity travelReviewEntity = TravelReviewEntity.builder()
                .travelIdx(1L)
                .reviewTitle("리뷰등록테스트")
                .reviewDescription("리뷰등록테스트")
                .viewCount(0)
                .favoriteCount(0)
                .popular(false)
                .visible("Y")
                .build();

        em.persist(travelReviewEntity);

        mockMvc.perform(delete("/api/travel/{idx}/reply", travelReviewEntity.getIdx()))
                        .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(content().string(getString(travelReviewEntity.getIdx())));
    }

    @Test
    @DisplayName("여행지 댓글 리스트 조회 테스트")
    void 여행지댓글리스트조회테스트() throws Exception {
        mockMvc.perform(get("/api/travel/1/reply"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"));
    }

    @Test
    @DisplayName("인기 여행지 선정 테스트")
    void 인기여행지선정테스트() throws Exception {
        mockMvc.perform(put("/api/travel/{idx}/popular", 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(content().string(String.valueOf(false)));
    }

    @Test
    @DisplayName("여행지 그룹 조회 테스트")
    void 여행지그룹조회테스트() throws Exception {
        mockMvc.perform(get("/api/travel/lists/group").param("page", "1").param("size", "100"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.travelGroupList.length()", greaterThan(0)));
    }

    @Test
    @DisplayName("여행지 그룹 상세 조회 테스트")
    void 여행지그룹상세조회테스트() throws Exception {
        mockMvc.perform(get("/api/travel/1/group"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.idx").value(1L));
    }

    @Test
    @DisplayName("여행지 그룹 등록 테스트")
    void 여행지그룹등록테스트() throws Exception {
        TravelGroupEntity travelGroupEntity = TravelGroupEntity.builder()
                .travelIdx(1L)
                .groupName("서울모임")
                .groupDescription("서울모임")
                .visible("Y")
                .build();

        mockMvc.perform(post("/api/travel/group")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(travelGroupEntity)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.travelIdx").value(1L))
                .andExpect(jsonPath("$.groupName").value("서울모임"))
                .andExpect(jsonPath("$.groupDescription").value("서울모임"));
    }

    @Test
    @DisplayName("여행지 그룹 수정 테스트")
    void 여행지그룹수정테스트() throws Exception {
        TravelGroupEntity travelGroupEntity = TravelGroupEntity.builder()
                .travelIdx(1L)
                .groupName("서울모임")
                .groupDescription("서울모임")
                .visible("Y")
                .build();

        em.persist(travelGroupEntity);

        TravelGroupEntity newTravelGroupEntity = TravelGroupEntity.builder()
                .idx(travelGroupEntity.getIdx())
                .travelIdx(1L)
                .groupName("인천모임")
                .groupDescription("인천모임")
                .visible("Y")
                .build();

        mockMvc.perform(put("/api/travel/{idx}/group", travelGroupEntity.getIdx())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newTravelGroupEntity)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.groupName").value("인천모임"))
                .andExpect(jsonPath("$.groupDescription").value("인천모임"));
    }

    @Test
    @DisplayName("여행지 그룹 삭제 테스트")
    void 여행지그룹삭제테스트() throws Exception {
        TravelGroupEntity travelGroupEntity = TravelGroupEntity.builder()
                .travelIdx(1L)
                .groupName("서울모임")
                .groupDescription("서울모임")
                .visible("Y")
                .build();

        em.persist(travelGroupEntity);

        mockMvc.perform(delete("/api/travel/{idx}/group", travelGroupEntity.getIdx()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(content().string(getString(travelGroupEntity.getIdx())));
    }

    @Test
    @DisplayName("유저 여행 그룹 등록 테스트")
    void 유저여행그룹등록테스트() throws Exception {
        TravelGroupUserEntity travelGroupUserEntity = TravelGroupUserEntity.builder()
                .userIdx(1L)
                .groupIdx(1L)
                .build();

        mockMvc.perform(post("/api/travel/group_user")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(travelGroupUserEntity)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.userIdx").value(1L))
                .andExpect(jsonPath("$.groupIdx").value(1L));
    }

    @Test
    @DisplayName("유저 여행 그룹 삭제 테스트")
    void 유저여행그룹삭제테스트() throws Exception {
        TravelGroupUserEntity travelGroupUserEntity = TravelGroupUserEntity.builder()
                .userIdx(1L)
                .groupIdx(1L)
                .build();

        em.persist(travelGroupUserEntity);
        mockMvc.perform(delete("/api/travel/{idx}/group_user", travelGroupUserEntity.getIdx()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(content().string(getString(travelGroupUserEntity.getIdx())));
    }

    @Test
    @DisplayName("유저 여행 스케줄 등록 테스트")
    void 유저여행스케줄등록테스트() throws Exception {
        TravelScheduleEntity travelScheduleEntity = TravelScheduleEntity.builder()
                .travelIdx(1L)
                .userIdx(1L)
                .scheduleDescription("스케줄 테스트")
                .scheduleTime(LocalDateTime.now())
                .build();

        mockMvc.perform(post("/api/travel/schedule")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(travelScheduleEntity)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.userIdx").value(1L))
                .andExpect(jsonPath("$.travelIdx").value(1L))
                .andExpect(jsonPath("$.scheduleDescription").value("스케줄 테스트"));
    }

    @Test
    @DisplayName("유저 여행 스케줄 수정 테스트")
    void 유저여행스케줄수정테스트() throws Exception {
        TravelScheduleEntity travelScheduleEntity = TravelScheduleEntity.builder()
                .travelIdx(1L)
                .userIdx(1L)
                .scheduleDescription("스케줄 테스트")
                .scheduleTime(LocalDateTime.now())
                .build();

        em.persist(travelScheduleEntity);

        TravelScheduleEntity updateTravelScheduleEntity = TravelScheduleEntity.builder()
                .idx(travelScheduleEntity.getIdx())
                .travelIdx(1L)
                .userIdx(1L)
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
                .travelIdx(1L)
                .userIdx(1L)
                .scheduleDescription("스케줄 테스트")
                .scheduleTime(LocalDateTime.now())
                .build();

        em.persist(travelScheduleEntity);

        mockMvc.perform(delete("/api/travel/{idx}/schedule", travelScheduleEntity.getIdx()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(content().string(getString(travelScheduleEntity.getIdx())));
    }
}
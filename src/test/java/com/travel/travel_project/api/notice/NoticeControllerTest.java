package com.travel.travel_project.api.notice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.travel_project.domain.notice.NoticeEntity;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.event.EventListener;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.persistence.EntityManager;

import static com.travel.travel_project.common.StringUtil.getString;
import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace= NONE)
@DisplayName("???????????? Api Test")
class NoticeControllerTest {
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
    @DisplayName("???????????? ?????? ?????????")
    void ???????????????????????????() throws Exception {
        mockMvc.perform(get("/api/notice/lists").param("page", "1").param("size", "100"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.notice.length()", greaterThan(0)));
    }

    @Test
    @DisplayName("???????????? ?????? ?????? ?????????")
    void ?????????????????????????????????() throws Exception {
        mockMvc.perform(get("/api/notice/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.idx").value(1L));
    }

    @Test
    @DisplayName("???????????? ?????? ?????????")
    void ???????????????????????????() throws Exception {
        NoticeEntity noticeEntity = NoticeEntity.builder()
                .title("???????????? ?????? ?????????")
                .description("???????????? ?????? ?????????")
                .viewCount(1)
                .topFixed(false)
                .visible("Y")
                .build();

        mockMvc.perform(post("/api/notice")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(noticeEntity)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.title").value("???????????? ?????? ?????????"))
                .andExpect(jsonPath("$.description").value("???????????? ?????? ?????????"));
    }

    @Test
    @DisplayName("???????????? ?????? ?????????")
    void ???????????????????????????() throws Exception {
        NoticeEntity noticeEntity = NoticeEntity.builder()
                .title("???????????? ?????? ?????????")
                .description("???????????? ?????? ?????????")
                .viewCount(1)
                .topFixed(false)
                .visible("Y")
                .build();

        em.persist(noticeEntity);

        NoticeEntity updateNoticeEntity = NoticeEntity.builder()
                .idx(noticeEntity.getIdx())
                .title("???????????? ?????? ?????????")
                .description("???????????? ?????? ?????????")
                .viewCount(1)
                .topFixed(false)
                .visible("Y")
                .build();

        mockMvc.perform(put("/api/notice/{idx}", updateNoticeEntity.getIdx())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(updateNoticeEntity)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.title").value("???????????? ?????? ?????????"))
                .andExpect(jsonPath("$.description").value("???????????? ?????? ?????????"));
    }

    @Test
    @DisplayName("???????????? ?????? ?????????")
    void ???????????????????????????() throws Exception {
        NoticeEntity noticeEntity = NoticeEntity.builder()
                .title("???????????? ?????? ?????????")
                .description("???????????? ?????? ?????????")
                .viewCount(1)
                .topFixed(false)
                .visible("Y")
                .build();

        em.persist(noticeEntity);

        mockMvc.perform(delete("/api/notice/{idx}", noticeEntity.getIdx()))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(content().string(getString(noticeEntity.getIdx())));
    }

    @Test
    @DisplayName("???????????? ????????? ?????? ?????????")
    void ????????????????????????????????????() throws Exception {
        NoticeEntity noticeEntity = NoticeEntity.builder()
                .title("???????????? ?????? ?????????")
                .description("???????????? ?????? ?????????")
                .viewCount(1)
                .topFixed(false)
                .visible("Y")
                .build();

        em.persist(noticeEntity);

        mockMvc.perform(put("/api/notice/{idx}/toggle-fixed", noticeEntity.getIdx()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(content().string(String.valueOf(true)));
    }
}
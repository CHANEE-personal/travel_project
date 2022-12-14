package com.travel.travel_project.api.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.travel_project.domain.post.PostEntity;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace= NONE)
@DisplayName("????????? Api Test")
class PostControllerTest {

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
        mockMvc.perform(get("/api/post/lists").param("page", "1").param("size", "100"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.postList.length()", greaterThan(0)));
    }

    @Test
    @DisplayName("????????? ?????? ?????? ?????????")
    void ??????????????????????????????() throws Exception {
        mockMvc.perform(get("/api/post/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.idx").value(1L));
    }

    @Test
    @DisplayName("????????? ?????? ?????????")
    void ????????????????????????() throws Exception {
        PostEntity postEntity = PostEntity.builder()
                .postTitle("????????? ?????????")
                .postDescription("????????? ?????????")
                .popular(false)
                .viewCount(0)
                .favoriteCount(0)
                .visible("Y")
                .build();

        mockMvc.perform(post("/api/post")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(postEntity)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.postTitle").value("????????? ?????????"))
                .andExpect(jsonPath("$.postDescription").value("????????? ?????????"));
    }

    @Test
    @DisplayName("????????? ?????? ?????????")
    void ????????????????????????() throws Exception {
        PostEntity postEntity = PostEntity.builder()
                .postTitle("????????? ?????????")
                .postDescription("????????? ?????????")
                .popular(false)
                .viewCount(0)
                .favoriteCount(0)
                .visible("Y")
                .build();

        em.persist(postEntity);

        PostEntity updatePostEntity = PostEntity.builder()
                .idx(postEntity.getIdx())
                .postTitle("????????? ?????? ?????????")
                .postDescription("????????? ?????? ?????????")
                .popular(false)
                .viewCount(0)
                .favoriteCount(0)
                .visible("Y")
                .build();

        mockMvc.perform(put("/api/post/{idx}", updatePostEntity.getIdx())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(updatePostEntity)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.postTitle").value("????????? ?????? ?????????"))
                .andExpect(jsonPath("$.postDescription").value("????????? ?????? ?????????"));
    }

    @Test
    @DisplayName("????????? ?????? ?????????")
    void ????????????????????????() throws Exception {
        PostEntity postEntity = PostEntity.builder()
                .postTitle("????????? ?????????")
                .postDescription("????????? ?????????")
                .popular(false)
                .viewCount(0)
                .favoriteCount(0)
                .visible("Y")
                .build();

        em.persist(postEntity);

        mockMvc.perform(delete("/api/post/{idx}", postEntity.getIdx()))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(content().string(getString(postEntity.getIdx())));
    }

    @Test
    @DisplayName("?????? ????????? ?????? ?????????")
    void ??????????????????????????????() throws Exception {
        PostEntity postEntity = PostEntity.builder()
                .postTitle("????????? ?????????")
                .postDescription("????????? ?????????")
                .popular(false)
                .viewCount(0)
                .favoriteCount(0)
                .visible("Y")
                .build();

        em.persist(postEntity);

        mockMvc.perform(put("/api/post/{idx}/toggle-popular", postEntity.getIdx()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(content().string(String.valueOf(true)));
    }
}
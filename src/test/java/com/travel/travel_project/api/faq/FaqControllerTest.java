package com.travel.travel_project.api.faq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.travel_project.domain.faq.FaqEntity;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.persistence.EntityManager;

import static com.travel.travel_project.common.StringUtil.getString;
import static org.hamcrest.Matchers.equalTo;
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
@AutoConfigureTestDatabase(replace = NONE)
@DisplayName("FAQ Api Test")
class FaqControllerTest {
    private MockMvc mockMvc;
    private final WebApplicationContext wac;
    private final ObjectMapper objectMapper;
    private final EntityManager em;

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void setup() {
        this.mockMvc = webAppContextSetup(wac)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))  // ?????? ??????
                .alwaysDo(print())
                .build();
    }

    @Test
    @DisplayName("FAQ ?????? ?????????")
    void FAQ???????????????() throws Exception {
        mockMvc.perform(get("/api/faq/lists").param("page", "1").param("size", "100"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.faqList.length()", equalTo(55)));
    }

    @Test
    @DisplayName("FAQ ?????? ?????? ?????????")
    void FAQ?????????????????????() throws Exception {
        LinkedMultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("jpaStartPage", "1");
        paramMap.add("size", "3");
        paramMap.add("searchType", "0");
        paramMap.add("searchKeyword", "??????");

        mockMvc.perform(get("/api/faq/lists").queryParams(paramMap))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.faqList.length()", equalTo(1)));
    }

    @Test
    @DisplayName("FAQ ?????? ?????? ?????????")
    void FAQ?????????????????????() throws Exception {
        // ??????
        mockMvc.perform(get("/api/faq/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.idx").value("1"))
                .andExpect(jsonPath("$.title").value("FAQ ?????????"))
                .andExpect(jsonPath("$.description").value("FAQ ?????????"));

        // ??????
        mockMvc.perform(get("/api/faq/-1"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.code").value("NOT_FOUND_FAQ"))
                .andExpect(jsonPath("$.message").value("?????? FAQ ??????"));
    }

    @Test
    @DisplayName("FAQ ?????? ?????????")
    void FAQ???????????????() throws Exception {
        FaqEntity faqEntity = FaqEntity.builder()
                .faqCode(1L)
                .title("FAQ ?????? ?????????")
                .description("FAQ ?????? ?????????")
                .viewCount(1)
                .visible("Y")
                .build();

        mockMvc.perform(post("/api/faq")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(faqEntity)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.title").value("FAQ ?????? ?????????"))
                .andExpect(jsonPath("$.description").value("FAQ ?????? ?????????"));
    }

    @Test
    @DisplayName("FAQ ?????? ?????????")
    void FAQ???????????????() throws Exception {
        FaqEntity faqEntity = FaqEntity.builder()
                .faqCode(1L)
                .title("FAQ ?????? ?????????")
                .description("FAQ ?????? ?????????")
                .viewCount(1)
                .visible("Y")
                .build();

        em.persist(faqEntity);

        FaqEntity updateFaqEntity = FaqEntity.builder()
                .idx(faqEntity.getIdx())
                .faqCode(1L)
                .title("FAQ ?????? ?????????")
                .description("FAQ ?????? ?????????")
                .viewCount(1)
                .visible("Y")
                .build();

        mockMvc.perform(put("/api/faq/{idx}", updateFaqEntity.getIdx())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(updateFaqEntity)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.title").value("FAQ ?????? ?????????"))
                .andExpect(jsonPath("$.description").value("FAQ ?????? ?????????"));
    }

    @Test
    @DisplayName("FAQ ?????? ?????????")
    void FAQ???????????????() throws Exception {
        FaqEntity faqEntity = FaqEntity.builder()
                .faqCode(1L)
                .title("FAQ ?????? ?????????")
                .description("FAQ ?????? ?????????")
                .viewCount(1)
                .visible("Y")
                .build();

        em.persist(faqEntity);

        mockMvc.perform(delete("/api/faq/{idx}", faqEntity.getIdx()))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(content().string(getString(faqEntity.getIdx())));
    }
}
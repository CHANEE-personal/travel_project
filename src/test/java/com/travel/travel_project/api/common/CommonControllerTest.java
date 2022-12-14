package com.travel.travel_project.api.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.travel_project.domain.common.CommonEntity;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import static com.travel.travel_project.common.StringUtil.getString;
import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;
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
@DisplayName("?????? ?????? Api Test")
class CommonControllerTest {
    private MockMvc mockMvc;
    private final WebApplicationContext wac;
    private final ObjectMapper objectMapper;
    private final EntityManager em;
    private CommonEntity commonEntity;

    @DisplayName("????????? ?????? ?????? ??????")
    void createCommonCode() {
        // ?????? ?????? ??????
        commonEntity = CommonEntity.builder()
                .commonCode(1)
                .commonName("??????")
                .visible("Y")
                .build();
    }

    @BeforeEach
    @EventListener(ApplicationEvent.class)
    public void setup() {
        this.mockMvc = webAppContextSetup(wac)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();

        createCommonCode();
    }

    @Test
    @DisplayName("?????? ?????? ?????? ?????????")
    void ???????????????????????????() throws Exception {
        mockMvc.perform(get("/api/common/lists").param("page", "1").param("size", "100"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.commonList.length()", greaterThan(0)));
    }

    @Test
    @DisplayName("?????? ?????? ?????? ?????? ?????????")
    void ?????????????????????????????????() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/common/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.idx").value(1L));
    }

    @Test
    @DisplayName("?????? ?????? ?????? ?????????")
    void ???????????????????????????() throws Exception {
        mockMvc.perform(post("/api/common")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(commonEntity)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.commonCode").value(1))
                .andExpect(jsonPath("$.commonName").value("??????"));
    }

    @Test
    @DisplayName("?????? ?????? ?????? ?????????")
    void ???????????????????????????() throws Exception {
        em.persist(commonEntity);

        commonEntity = CommonEntity.builder()
                .idx(commonEntity.getIdx())
                .commonCode(2)
                .commonName("??????")
                .visible("Y")
                .build();

        mockMvc.perform(put("/api/common/{idx}", commonEntity.getIdx())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(commonEntity)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.commonCode").value(2))
                .andExpect(jsonPath("$.commonName").value("??????"));
    }

    @Test
    @DisplayName("?????? ?????? ?????? ?????????")
    void ???????????????????????????() throws Exception {
        em.persist(commonEntity);

        mockMvc.perform(delete("/api/common/{idx}", commonEntity.getIdx()))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(content().string(getString(commonEntity.getIdx())));
    }
}
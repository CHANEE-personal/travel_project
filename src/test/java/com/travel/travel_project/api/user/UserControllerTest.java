package com.travel.travel_project.api.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.travel_project.domain.travel.schedule.TravelScheduleDTO;
import com.travel.travel_project.domain.user.AuthenticationRequest;
import com.travel.travel_project.domain.user.UserEntity;
import com.travel.travel_project.jwt.JwtUtil;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.travel.travel_project.common.StringUtil.getString;
import static com.travel.travel_project.domain.user.Role.ROLE_ADMIN;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
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
@DisplayName("?????? Api Test")
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
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        return authorities;
    }

    @DisplayName("????????? ?????? ??????")
    void createUser() {
//        passwordEncoder = createDelegatingPasswordEncoder();
//
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("admin04", "pass1234", getAuthorities());
//        String token = jwtUtil.doGenerateToken(authenticationToken.getName(), 1000L * 10);
//
//        userEntity = UserEntity.builder()
//                .userId("admin04")
//                .password("pass1234")
//                .name("test")
//                .email("test@test.com")
//                .role(ROLE_ADMIN)
//                .userToken(token)
//                .visible("Y")
//                .build();
//
//        em.persist(userEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void setup(RestDocumentationContextProvider restDocumentationContextProvider) {
        this.mockMvc = webAppContextSetup(wac)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
//                .apply(springSecurity())
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .alwaysDo(print())
                .build();

        createUser();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("?????? ?????? ?????????")
    void ????????????() throws Exception {
        mockMvc.perform(get("/api/user").param("page", "1").param("size", "100")
                        .header("Authorization", "Bearer " + userEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"));
    }

    @Test
    @Disabled
    @WithMockUser(roles = "USER")
    @DisplayName("?????? ?????? ?????? ?????????")
    void ???????????????????????????() throws Exception {
        mockMvc.perform(get("/api/user").param("page", "1").param("size", "100")
                        .header("Authorization", "Bearer " + userEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("????????? ?????????")
    void ??????????????????() throws Exception {
        mockMvc.perform(post("/api/user/login")
                        .header("Authorization", "Bearer " + userEntity.getUserToken())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userEntity)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(header().string("loginYn", "Y"))
                .andExpect(header().string("username", "test"))
                .andExpect(header().exists("authorization"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("?????? ???????????? ?????????")
    void ?????????????????????() throws Exception {
        UserEntity newAdminUserEntity = UserEntity.builder()
                .userId("test")
                .password("test")
                .name("test")
                .email("test@test.com")
                .role(ROLE_ADMIN)
                .visible("Y")
                .build();

        mockMvc.perform(post("/api/user")
                        .header("Authorization", "Bearer " + userEntity.getUserToken())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newAdminUserEntity)))
                .andDo(print())
                .andDo(document("user/post",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        relaxedRequestFields(
                                fieldWithPath("userId").type(STRING).description("?????????"),
                                fieldWithPath("password").type(STRING).description("????????????"),
                                fieldWithPath("name").type(STRING).description("??????"),
                                fieldWithPath("email").type(STRING).description("?????????")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("userId").type(STRING).description("?????????"),
                                fieldWithPath("password").type(STRING).description("????????????")
                        )))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.userId").value("test"))
                .andExpect(jsonPath("$.password").value("test"))
                .andExpect(jsonPath("$.name").value("test"))
                .andExpect(jsonPath("$.email").value("test@test.com"))
                .andExpect(jsonPath("$.role").value("ROLE_ADMIN"));
    }

    @Test
    @Disabled
    @WithMockUser(roles = "USER")
    @DisplayName("?????? ???????????? ?????? ?????? ?????????")
    void ???????????????????????????() throws Exception {
        userEntity = UserEntity.builder()
                .userId("test")
                .password("test")
                .name("test")
                .email("test@test.com")
                .visible("Y")
                .build();

        mockMvc.perform(post("/api/user")
                        .header("Authorization", "Bearer " + userEntity.getUserToken())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userEntity)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("?????? ???????????? ?????????")
    void ?????????????????????() throws Exception {
        UserEntity updateAdminUserEntity = UserEntity.builder()
                .idx(userEntity.getIdx())
                .userId("admin03")
                .password("pass1234")
                .name("admin03")
                .email("admin03@tsp.com")
                .visible("Y")
                .build();

        mockMvc.perform(put("/api/user/{idx}", updateAdminUserEntity.getIdx())
                        .header("Authorization", "Bearer " + userEntity.getUserToken())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(updateAdminUserEntity)))
                .andDo(print())
                .andDo(document("user/put",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        relaxedRequestFields(
                                fieldWithPath("userId").type(STRING).description("?????????"),
                                fieldWithPath("password").type(STRING).description("????????????"),
                                fieldWithPath("name").type(STRING).description("??????"),
                                fieldWithPath("email").type(STRING).description("?????????")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("userId").type(STRING).description("?????????"),
                                fieldWithPath("password").type(STRING).description("????????????")
                        )))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.userId").value("admin03"))
                .andExpect(jsonPath("$.name").value("admin03"));
    }

    @Test
    @Disabled
    @WithMockUser(roles = "USER")
    @DisplayName("???????????? ?????? ?????? ?????????")
    void ???????????????????????????() throws Exception {
        UserEntity updateUserEntity = UserEntity.builder()
                .idx(userEntity.getIdx())
                .userId("admin03")
                .password("pass1234")
                .name("admin03")
                .email("admin03@tsp.com")
                .visible("Y")
                .build();

        mockMvc.perform(put("/api/user/{idx}", updateUserEntity.getIdx())
                        .header("Authorization", "Bearer " + userEntity.getUserToken())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userEntity)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("?????? ???????????? ?????????")
    void ?????????????????????() throws Exception {
        mockMvc.perform(delete("/api/user/{idx}", userEntity.getIdx())
                        .header("Authorization", "Bearer " + userEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(content().string(getString(userEntity.getIdx())));
    }

    @Test
    @Disabled
    @WithMockUser(roles = "USER")
    @DisplayName("?????? ???????????? ?????? ?????????")
    void ???????????????????????????() throws Exception {
        mockMvc.perform(put("/api/user")
                        .header("Authorization", "Bearer " + userEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("JWT ?????? ?????? ?????????")
    void ?????????????????????() throws Exception {
        mockMvc.perform(post("/api/user/refresh")
                        .header("Authorization", "Bearer " + userEntity.getUserToken())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(AuthenticationRequest.builder().userId("admin01").password("pass1234").build())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.jwt").isNotEmpty())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    @DisplayName("????????? ???????????? ????????? ?????? ?????????")
    void ?????????????????????????????????????????????() throws Exception {
        List<String> favoriteIdxList = new ArrayList<>();
        favoriteIdxList.add("1");
        favoriteIdxList.add("2");

        mockMvc.perform(put("/api/user/1/favorite-travel")
                        .param("favoriteIdx", "2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.favoriteTravelIdx").value(favoriteIdxList));

        favoriteIdxList.add("3");

        mockMvc.perform(put("/api/user/1/favorite-travel")
                .param("favoriteIdx", "3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.favoriteTravelIdx").value(favoriteIdxList));
    }

    @Test
    @DisplayName("????????? ????????? ????????? ????????? ?????? ?????????")
    void ???????????????????????????????????????????????????() throws Exception {
        mockMvc.perform(get("/api/user/1/schedule"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"));
    }

    @Test
    @DisplayName("????????? ????????? ????????? ?????? ?????? ?????????")
    void ????????????????????????????????????????????????() throws Exception {
        mockMvc.perform(get("/api/user/1/schedule/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.userIdx").value(1L));
    }
}
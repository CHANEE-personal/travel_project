package com.travel.api.faq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.api.common.domain.CommonEntity;
import com.travel.api.faq.domain.FaqEntity;
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
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.travel.api.user.domain.Role.ROLE_ADMIN;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
@Transactional
@ExtendWith(RestDocumentationExtension.class)
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
    public void setup(RestDocumentationContextProvider restDocumentationContextProvider) {
        this.mockMvc = webAppContextSetup(wac)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .apply(springSecurity())
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .alwaysDo(print())
                .build();

        createUser();

        authenticationRequest = new AuthenticationRequest(adminUserEntity);
        when(myUserDetailsService.loadUserByUsername(adminUserEntity.getUserId())).thenReturn(authenticationRequest);
    }

    @Test
    @WithMockUser("ADMIN")
    @DisplayName("FAQ 조회 테스트")
    void FAQ조회테스트() throws Exception {
        CommonEntity commonEntity = CommonEntity.builder()
                .commonCode(1000)
                .commonName("자주묻는질문")
                .visible("Y")
                .build();

        em.persist(commonEntity);

        FaqEntity faqEntity = FaqEntity.builder()
                .newFaqCode(commonEntity)
                .title("FAQ 수정 테스트")
                .description("FAQ 수정 테스트")
                .viewCount(1)
                .visible("Y")
                .build();

        em.persist(faqEntity);

        mockMvc.perform(get("/admin/faq").param("pageNum", "0").param("size", "100")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.content").isNotEmpty());
    }

    @Test
    @WithMockUser("ADMIN")
    @DisplayName("FAQ 검색 조회 테스트")
    void FAQ검색조회테스트() throws Exception {
        CommonEntity commonEntity = CommonEntity.builder()
                .commonCode(1000)
                .commonName("자주묻는질문")
                .visible("Y")
                .build();

        em.persist(commonEntity);

        FaqEntity faqEntity = FaqEntity.builder()
                .newFaqCode(commonEntity)
                .title("하하")
                .description("하하")
                .viewCount(1)
                .visible("Y")
                .build();

        em.persist(faqEntity);

        LinkedMultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("searchKeyword", "하하");

        mockMvc.perform(get("/admin/faq").queryParams(paramMap).param("pageNum", "0").param("size", "100")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.content").isNotEmpty());
    }

    @Test
    @WithMockUser("ADMIN")
    @DisplayName("FAQ 상세 조회 테스트")
    void FAQ상세조회테스트() throws Exception {
        CommonEntity commonEntity = CommonEntity.builder()
                .commonCode(1000)
                .commonName("자주묻는질문")
                .visible("Y")
                .build();

        em.persist(commonEntity);

        FaqEntity faqEntity = FaqEntity.builder()
                .newFaqCode(commonEntity)
                .title("FAQ 등록")
                .description("FAQ 등록")
                .viewCount(1)
                .visible("Y")
                .build();

        em.persist(faqEntity);

        // 사용
        mockMvc.perform(RestDocumentationRequestBuilders.get("/admin/faq/{idx}", faqEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andDo(document("GET-FAQ", pathParameters(
                        parameterWithName("idx").description("FAQ IDX")
                )))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.idx").value(faqEntity.getIdx()))
                .andExpect(jsonPath("$.title").value(faqEntity.getTitle()))
                .andExpect(jsonPath("$.description").value(faqEntity.getDescription()));

        // 예외
        mockMvc.perform(RestDocumentationRequestBuilders.get("/admin/faq/-1")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.code").value("NOT_FOUND_FAQ"))
                .andExpect(jsonPath("$.message").value("FAQ 상세 없음"));
    }

    @Test
    @WithMockUser("ADMIN")
    @DisplayName("FAQ 등록 테스트")
    void FAQ등록테스트() throws Exception {
        CommonEntity commonEntity = CommonEntity.builder()
                .commonCode(999)
                .commonName("서울")
                .visible("Y")
                .build();

        em.persist(commonEntity);

        FaqEntity faqEntity = FaqEntity.builder()
                .title("FAQ 등록 테스트")
                .description("FAQ 등록 테스트")
                .viewCount(1)
                .newFaqCode(commonEntity)
                .visible("Y")
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders.post("/admin/faq")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(faqEntity)))
                .andDo(print())
                .andDo(document("INSERT-FAQ",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        relaxedRequestFields(
                                fieldWithPath("title").type(STRING).description("FAQ 제목"),
                                fieldWithPath("description").type(STRING).description("FAQ 내용"),
                                fieldWithPath("visible").type(STRING).description("FAQ 노출 여부")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("title").type(STRING).description("FAQ 제목"),
                                fieldWithPath("description").type(STRING).description("FAQ 내용"),
                                fieldWithPath("visible").type(STRING).description("FAQ 노출 여부")
                        )))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.title").value("FAQ 등록 테스트"))
                .andExpect(jsonPath("$.description").value("FAQ 등록 테스트"));
    }

    @Test
    @WithMockUser("ADMIN")
    @DisplayName("FAQ 수정 테스트")
    void FAQ수정테스트() throws Exception {
        CommonEntity commonEntity = CommonEntity.builder()
                .commonCode(999)
                .commonName("서울")
                .visible("Y")
                .build();

        em.persist(commonEntity);

        FaqEntity faqEntity = FaqEntity.builder()
                .title("FAQ 등록 테스트")
                .description("FAQ 등록 테스트")
                .viewCount(1)
                .newFaqCode(commonEntity)
                .visible("Y")
                .build();

        em.persist(faqEntity);

        FaqEntity updateFaqEntity = FaqEntity.builder()
                .idx(faqEntity.getIdx())
                .title("FAQ 수정 테스트")
                .description("FAQ 수정 테스트")
                .viewCount(1)
                .newFaqCode(commonEntity)
                .visible("Y")
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders.put("/admin/faq/{idx}", updateFaqEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(updateFaqEntity)))
                .andDo(print())
                .andDo(document("UPDATE-FAQ",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        relaxedRequestFields(
                                fieldWithPath("title").type(STRING).description("FAQ 제목"),
                                fieldWithPath("description").type(STRING).description("FAQ 내용"),
                                fieldWithPath("visible").type(STRING).description("FAQ 노출 여부")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("title").type(STRING).description("FAQ 제목"),
                                fieldWithPath("description").type(STRING).description("FAQ 내용"),
                                fieldWithPath("visible").type(STRING).description("FAQ 노출 여부")
                        )))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.title").value("FAQ 수정 테스트"))
                .andExpect(jsonPath("$.description").value("FAQ 수정 테스트"));
    }

    @Test
    @WithMockUser("ADMIN")
    @DisplayName("FAQ 삭제 테스트")
    void FAQ삭제테스트() throws Exception {
        CommonEntity commonEntity = CommonEntity.builder()
                .commonCode(999)
                .commonName("서울")
                .visible("Y")
                .build();

        em.persist(commonEntity);

        FaqEntity faqEntity = FaqEntity.builder()
                .title("FAQ 등록 테스트")
                .description("FAQ 등록 테스트")
                .viewCount(1)
                .newFaqCode(commonEntity)
                .visible("Y")
                .build();

        em.persist(faqEntity);

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/admin/faq/{idx}", faqEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andDo(document("DELETE-FAQ", pathParameters(
                        parameterWithName("idx").description("FAQ IDX")
                )))
                .andExpect(status().isNoContent());
    }
}

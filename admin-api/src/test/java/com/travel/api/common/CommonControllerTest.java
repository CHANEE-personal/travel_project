package com.travel.api.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.api.common.domain.CommonEntity;
import com.travel.api.user.domain.AuthenticationRequest;
import com.travel.api.user.domain.UserEntity;
import com.travel.jwt.JwtUtil;
import com.travel.jwt.MyUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEvent;
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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
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
@DisplayName("공통 코드 Api Test")
class CommonControllerTest {
    private MockMvc mockMvc;
    private final WebApplicationContext wac;
    private final ObjectMapper objectMapper;
    private final EntityManager em;
    private CommonEntity commonEntity;

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
    @EventListener(ApplicationEvent.class)
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
    @DisplayName("공통 코드 조회 테스트")
    void 공통코드조회테스트() throws Exception {
        mockMvc.perform(get("/admin/common").param("pageNum", "0").param("size", "100")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.content").isNotEmpty());
    }

    @Test
    @WithMockUser("ADMIN")
    @DisplayName("공통 코드 상세 조회 테스트")
    void 공통코드상세조회테스트() throws Exception {
        commonEntity = CommonEntity.builder()
                .commonCode(1000)
                .commonName("서울")
                .visible("Y")
                .build();

        em.persist(commonEntity);

        mockMvc.perform(get("/admin/common/{idx}", commonEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andDo(document("GET-COMMON", pathParameters(
                        parameterWithName("idx").description("공통코드 IDX")
                )))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.idx").value(commonEntity.getIdx()));
    }

    @Test
    @WithMockUser("ADMIN")
    @DisplayName("공통 코드 등록 테스트")
    void 공통코드등록테스트() throws Exception {
        // 공통 코드 생성
        commonEntity = CommonEntity.builder()
                .commonCode(1000)
                .commonName("서울")
                .visible("Y")
                .build();

        mockMvc.perform(post("/admin/common")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(commonEntity)))
                .andDo(print())
                .andDo(document("INSERT-COMMON",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        relaxedRequestFields(
                                fieldWithPath("commonCode").type(NUMBER).description("공통 코드"),
                                fieldWithPath("commonName").type(STRING).description("공통 코드명"),
                                fieldWithPath("visible").type(STRING).description("여행지 노출 여부")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("commonCode").type(NUMBER).description("공통 코드"),
                                fieldWithPath("commonName").type(STRING).description("공통 코드명"),
                                fieldWithPath("visible").type(STRING).description("여행지 노출 여부")
                        )))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.commonCode").value(commonEntity.getCommonCode()))
                .andExpect(jsonPath("$.commonName").value(commonEntity.getCommonName()));
    }

    @Test
    @WithMockUser("ADMIN")
    @DisplayName("공통 코드 수정 테스트")
    void 공통코드수정테스트() throws Exception {
        // 공통 코드 생성
        commonEntity = CommonEntity.builder()
                .commonCode(1000)
                .commonName("서울")
                .visible("Y")
                .build();

        em.persist(commonEntity);

        commonEntity = CommonEntity.builder()
                .idx(commonEntity.getIdx())
                .commonCode(1002)
                .commonName("인천")
                .visible("Y")
                .build();

        mockMvc.perform(put("/admin/common/{idx}", commonEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(commonEntity)))
                .andDo(print())
                .andDo(document("UPDATE-COMMON",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        relaxedRequestFields(
                                fieldWithPath("commonCode").type(NUMBER).description("공통 코드"),
                                fieldWithPath("commonName").type(STRING).description("공통 코드명"),
                                fieldWithPath("visible").type(STRING).description("여행지 노출 여부")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("commonCode").type(NUMBER).description("공통 코드"),
                                fieldWithPath("commonName").type(STRING).description("공통 코드명"),
                                fieldWithPath("visible").type(STRING).description("여행지 노출 여부")
                        )))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.commonCode").value(commonEntity.getCommonCode()))
                .andExpect(jsonPath("$.commonName").value(commonEntity.getCommonName()));
    }

    @Test
    @WithMockUser("ADMIN")
    @DisplayName("공통 코드 삭제 테스트")
    void 공통코드삭제테스트() throws Exception {
        // 공통 코드 생성
        commonEntity = CommonEntity.builder()
                .commonCode(1000)
                .commonName("서울")
                .visible("Y")
                .build();

        em.persist(commonEntity);

        mockMvc.perform(delete("/admin/common/{idx}", commonEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}

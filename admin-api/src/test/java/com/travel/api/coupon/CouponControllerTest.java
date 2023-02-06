package com.travel.api.coupon;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.api.coupon.domain.CouponEntity;
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
import java.time.LocalDateTime;
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
@DisplayName("쿠폰 Api Test")
class CouponControllerTest {

    private MockMvc mockMvc;
    private final WebApplicationContext wac;
    private final ObjectMapper objectMapper;
    private final EntityManager em;

    private final JwtUtil jwtUtil;

    private UserEntity adminUserEntity;
    protected PasswordEncoder passwordEncoder;

    private CouponEntity couponEntity;

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

        couponEntity = CouponEntity.builder()
                .title("10% 쿠폰")
                .description("10% 쿠폰")
                .salePrice(0)
                .percentage(10)
                .percentageStatus(true)
                .count(1)
                .startDate(LocalDateTime.of(2022, 2, 1, 0, 0, 0))
                .endDate(LocalDateTime.of(2022, 2, 28, 23, 59, 59))
                .status(true)
                .build();

        em.persist(couponEntity);
    }

    @Test
    @WithMockUser("ADMIN")
    @DisplayName("쿠폰 조회 테스트")
    void 쿠폰조회테스트() throws Exception {
        LinkedMultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        mockMvc.perform(get("/admin/coupon").queryParams(paramMap).param("pageNum", "0").param("size", "3")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"));
    }

    @Test
    @WithMockUser("ADMIN")
    @DisplayName("쿠폰 상세 조회 테스트")
    void 쿠폰상세조회테스트() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.get("/admin/coupon/{idx}", couponEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andDo(document("GET-COUPON", pathParameters(
                        parameterWithName("idx").description("쿠폰 IDX")
                )))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.title").value("10% 쿠폰"))
                .andExpect(jsonPath("$.description").value("10% 쿠폰"));
    }

    @Test
    @WithMockUser("ADMIN")
    @DisplayName("쿠폰 등록 테스트")
    void 쿠폰등록테스트() throws Exception {
        couponEntity = CouponEntity.builder()
                .title("20% 쿠폰")
                .description("20% 쿠폰")
                .salePrice(0)
                .percentage(20)
                .percentageStatus(true)
                .count(1)
                .startDate(LocalDateTime.of(2022, 2, 1, 0, 0, 0))
                .endDate(LocalDateTime.of(2022, 2, 28, 23, 59, 59))
                .status(true)
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders.post("/admin/coupon")
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(couponEntity)))
                .andDo(print())
                .andDo(document("INSERT-COUPON",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        relaxedRequestFields(
                                fieldWithPath("title").type(STRING).description("쿠폰명"),
                                fieldWithPath("description").type(STRING).description("쿠폰상세"),
                                fieldWithPath("salePrice").type(NUMBER).description("할인금액"),
                                fieldWithPath("percentage").type(NUMBER).description("할인퍼센트"),
                                fieldWithPath("percentageStatus").type(BOOLEAN).description("할인퍼센트여부"),
                                fieldWithPath("count").type(NUMBER).description("발급갯수"),
                                fieldWithPath("startDate").type(STRING).description("시작 일자"),
                                fieldWithPath("endDate").type(STRING).description("마감 일자"),
                                fieldWithPath("status").type(BOOLEAN).description("쿠폰 사용 가능 여부")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("title").type(STRING).description("쿠폰명"),
                                fieldWithPath("description").type(STRING).description("쿠폰상세"),
                                fieldWithPath("salePrice").type(NUMBER).description("할인금액"),
                                fieldWithPath("percentage").type(NUMBER).description("할인퍼센트"),
                                fieldWithPath("percentageStatus").type(BOOLEAN).description("할인퍼센트여부"),
                                fieldWithPath("count").type(NUMBER).description("발급갯수"),
                                fieldWithPath("startDate").type(STRING).description("시작 일자"),
                                fieldWithPath("endDate").type(STRING).description("마감 일자"),
                                fieldWithPath("status").type(BOOLEAN).description("쿠폰 사용 가능 여부")
                        )))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.title").value("20% 쿠폰"));
    }

    @Test
    @WithMockUser("ADMIN")
    @DisplayName("쿠폰 수정 테스트")
    void 쿠폰수정테스트() throws Exception {
        CouponEntity updateCouponEntity = CouponEntity.builder()
                .idx(couponEntity.getIdx())
                .title("20% 쿠폰")
                .description("20% 쿠폰")
                .salePrice(0)
                .percentage(20)
                .percentageStatus(true)
                .count(1)
                .startDate(LocalDateTime.of(2022, 2, 1, 0, 0, 0))
                .endDate(LocalDateTime.of(2022, 2, 28, 23, 59, 59))
                .status(true)
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders.put("/admin/coupon/{idx}", updateCouponEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(updateCouponEntity)))
                .andDo(print())
                .andDo(document("UPDATE-COUPON",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        relaxedRequestFields(
                                fieldWithPath("title").type(STRING).description("쿠폰명"),
                                fieldWithPath("description").type(STRING).description("쿠폰상세"),
                                fieldWithPath("salePrice").type(NUMBER).description("할인금액"),
                                fieldWithPath("percentage").type(NUMBER).description("할인퍼센트"),
                                fieldWithPath("percentageStatus").type(BOOLEAN).description("할인퍼센트여부"),
                                fieldWithPath("count").type(NUMBER).description("발급갯수"),
                                fieldWithPath("startDate").type(STRING).description("시작 일자"),
                                fieldWithPath("endDate").type(STRING).description("마감 일자"),
                                fieldWithPath("status").type(BOOLEAN).description("쿠폰 사용 가능 여부")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("title").type(STRING).description("쿠폰명"),
                                fieldWithPath("description").type(STRING).description("쿠폰상세"),
                                fieldWithPath("salePrice").type(NUMBER).description("할인금액"),
                                fieldWithPath("percentage").type(NUMBER).description("할인퍼센트"),
                                fieldWithPath("percentageStatus").type(BOOLEAN).description("할인퍼센트여부"),
                                fieldWithPath("count").type(NUMBER).description("발급갯수"),
                                fieldWithPath("startDate").type(STRING).description("시작 일자"),
                                fieldWithPath("endDate").type(STRING).description("마감 일자"),
                                fieldWithPath("status").type(BOOLEAN).description("쿠폰 사용 가능 여부")
                        )))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.title").value("20% 쿠폰"));
    }

    @Test
    @WithMockUser("ADMIN")
    @DisplayName("쿠폰 삭제 테스트")
    void 쿠폰삭제테스트() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/admin/coupon/{idx}", couponEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andDo(document("DELETE-COUPON", pathParameters(
                        parameterWithName("idx").description("쿠폰 IDX")
                )))
                .andExpect(status().isNoContent());
    }
}


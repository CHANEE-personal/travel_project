package com.travel.api.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.api.post.domain.PostEntity;
import com.travel.api.user.domain.Role;
import com.travel.api.user.domain.UserEntity;
import com.travel.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.With;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.travel.common.StringUtil.getString;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
@Transactional
@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@DisplayName("게시글 Api Test")
class PostControllerTest {

    private MockMvc mockMvc;
    private final WebApplicationContext wac;
    private final ObjectMapper objectMapper;
    private final EntityManager em;
    private PostEntity postEntity;

    private final JwtUtil jwtUtil;

    private UserEntity userEntity;
    protected PasswordEncoder passwordEncoder;

    Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_TRAVEL_USER"));
        return authorities;
    }

    @DisplayName("테스트 유저 생성")
    void createUser() {
        passwordEncoder = createDelegatingPasswordEncoder();

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("admin04", "pass1234", getAuthorities());
        String token = jwtUtil.doGenerateToken(authenticationToken.getName());

        userEntity = UserEntity.builder()
                .userId("user99")
                .password("pass1234")
                .name("test")
                .email("test@test.com")
                .role(Role.ROLE_TRAVEL_USER)
                .userToken(token)
                .visible("Y")
                .build();

        em.persist(userEntity);
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

        postEntity = PostEntity.builder()
                .postTitle("게시글 테스트")
                .postDescription("게시글 테스트")
                .popular(false)
                .viewCount(0)
                .favoriteCount(0)
                .visible("Y")
                .build();

        em.persist(postEntity);
    }

    @Test
    @WithMockUser("USER")
    @DisplayName("게시글 조회 테스트")
    void 게시글조회테스트() throws Exception {
        mockMvc.perform(get("/front/post").param("pageNum", "0").param("size", "100"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.content").isNotEmpty());
    }

    @Test
    @WithMockUser("TRAVEL_USER")
    @DisplayName("게시글 상세 조회 테스트")
    void 게시글상세조회테스트() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.get("/front/post/{idx}", postEntity.getIdx())
                        .header("Authorization", "Bearer " + userEntity.getUserToken()))
                .andDo(print())
                .andDo(document("GET-POST", pathParameters(
                        parameterWithName("idx").description("POST IDX")
                )))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.idx").value(postEntity.getIdx()));
    }

    @Test
    @WithMockUser("TRAVEL_USER")
    @DisplayName("게시글 등록 테스트")
    void 게시글등록테스트() throws Exception {
        PostEntity postEntity = PostEntity.builder()
                .postTitle("게시글 테스트")
                .postDescription("게시글 테스트")
                .popular(false)
                .viewCount(0)
                .favoriteCount(0)
                .visible("Y")
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders.post("/front/post")
                        .header("Authorization", "Bearer " + userEntity.getUserToken())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(postEntity)))
                .andDo(print())
                .andDo(document("INSERT-POST",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        relaxedRequestFields(
                                fieldWithPath("postTitle").type(STRING).description("게시글 제목"),
                                fieldWithPath("postDescription").type(STRING).description("게시글 내용"),
                                fieldWithPath("visible").type(STRING).description("게시글 노출 여부")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("postTitle").type(STRING).description("게시글 제목"),
                                fieldWithPath("postDescription").type(STRING).description("게시글 내용"),
                                fieldWithPath("visible").type(STRING).description("게시글 노출 여부")
                        )))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.postTitle").value("게시글 테스트"))
                .andExpect(jsonPath("$.postDescription").value("게시글 테스트"));
    }

    @Test
    @WithMockUser("TRAVEL_USER")
    @DisplayName("게시글 수정 테스트")
    void 게시글수정테스트() throws Exception {
        PostEntity postEntity = PostEntity.builder()
                .postTitle("게시글 테스트")
                .postDescription("게시글 테스트")
                .popular(false)
                .viewCount(0)
                .favoriteCount(0)
                .visible("Y")
                .build();

        em.persist(postEntity);

        PostEntity updatePostEntity = PostEntity.builder()
                .idx(postEntity.getIdx())
                .postTitle("게시글 수정 테스트")
                .postDescription("게시글 수정 테스트")
                .popular(false)
                .viewCount(0)
                .favoriteCount(0)
                .visible("Y")
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders.put("/front/post/{idx}", updatePostEntity.getIdx())
                        .header("Authorization", "Bearer " + userEntity.getUserToken())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(updatePostEntity)))
                .andDo(print())
                .andDo(document("UPDATE-POST",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        relaxedRequestFields(
                                fieldWithPath("postTitle").type(STRING).description("게시글 제목"),
                                fieldWithPath("postDescription").type(STRING).description("게시글 내용"),
                                fieldWithPath("visible").type(STRING).description("게시글 노출 여부")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("postTitle").type(STRING).description("게시글 제목"),
                                fieldWithPath("postDescription").type(STRING).description("게시글 내용"),
                                fieldWithPath("visible").type(STRING).description("게시글 노출 여부")
                        )))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.postTitle").value("게시글 수정 테스트"))
                .andExpect(jsonPath("$.postDescription").value("게시글 수정 테스트"));
    }

    @Test
    @WithMockUser("TRAVEL_USER")
    @DisplayName("게시글 삭제 테스트")
    void 게시글삭제테스트() throws Exception {
        PostEntity postEntity = PostEntity.builder()
                .postTitle("게시글 테스트")
                .postDescription("게시글 테스트")
                .popular(false)
                .viewCount(0)
                .favoriteCount(0)
                .visible("Y")
                .build();

        em.persist(postEntity);

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/front/post/{idx}", postEntity.getIdx())
                        .header("Authorization", "Bearer " + userEntity.getUserToken()))
                .andDo(print())
                .andDo(document("DELETE-POST", pathParameters(
                        parameterWithName("idx").description("POST IDX")
                )))
                .andExpect(status().isNoContent());
    }
}

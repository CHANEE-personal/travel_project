package com.travel.api.post.domain.repository;

import com.travel.api.post.domain.PostDTO;
import com.travel.api.post.domain.PostEntity;
import com.travel.api.post.domain.reply.ReplyEntity;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@ExtendWith(MockitoExtension.class)
@DisplayName("게시글 Repository Test")
class PostQueryRepositoryTest {

    @Mock
    private PostQueryRepository mockPostRepository;
    private final PostQueryRepository postRepository;
    private final EntityManager em;

    private PostEntity postEntity;
    private PostDTO postDTO;
    private ReplyEntity replyEntity;


    void createPost() {
        postEntity = PostEntity.builder()
                .postTitle("게시글 테스트")
                .postDescription("게시글 테스트")
                .popular(false)
                .viewCount(0)
                .favoriteCount(0)
                .visible("Y")
                .build();

        em.persist(postEntity);
        postDTO = PostEntity.toDto(postEntity);

        replyEntity = ReplyEntity.builder()
                .postEntity(postEntity)
                .commentTitle("댓글 테스트")
                .commentDescription("댓글 테스트")
                .favoriteCount(0)
                .visible("Y")
                .build();

        em.persist(replyEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createPost();
    }

    @Test
    @DisplayName("게시글조회테스트")
    void 게시글조회테스트() {
        Map<String, Object> postMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(0, 3);

        postRepository.findPostList(postMap, pageRequest);
    }

    @Test
    @DisplayName("게시글 리스트 조회 Mockito 테스트")
    void 게시글리스트조회Mockito테스트() {
        // given
        Map<String, Object> postMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(0, 3);

        List<PostDTO> postList = new ArrayList<>();
        postList.add(postDTO);

        Page<PostDTO> resultPage = new PageImpl<>(postList, pageRequest, postList.size());

        // when
        when(mockPostRepository.findPostList(postMap, pageRequest)).thenReturn(resultPage);
        Page<PostDTO> newPostList = mockPostRepository.findPostList(postMap, pageRequest);

        List<PostDTO> findPostList = newPostList.stream().collect(Collectors.toList());
        // then
        // 게시글 관련
        assertThat(findPostList.get(0).getIdx()).isEqualTo(postList.get(0).getIdx());
        assertThat(findPostList.get(0).getPostTitle()).isEqualTo("게시글 테스트");
        assertThat(findPostList.get(0).getPostDescription()).isEqualTo("게시글 테스트");

        // verify
        verify(mockPostRepository, times(1)).findPostList(postMap, pageRequest);
        verify(mockPostRepository, atLeastOnce()).findPostList(postMap, pageRequest);
        verifyNoMoreInteractions(mockPostRepository);

        InOrder inOrder = inOrder(mockPostRepository);
        inOrder.verify(mockPostRepository).findPostList(postMap, pageRequest);
    }

    @Test
    @DisplayName("게시글 상세 조회 Mockito 테스트")
    void 게시글상세조회Mockito테스트() {
        // when
        when(mockPostRepository.findOnePost(postDTO.getIdx())).thenReturn(postDTO);
        PostDTO onePost = mockPostRepository.findOnePost(postDTO.getIdx());

        // then
        assertThat(onePost.getPostTitle()).isEqualTo("게시글 테스트");
        assertThat(onePost.getPostDescription()).isEqualTo("게시글 테스트");

        // verify
        verify(mockPostRepository, times(1)).findOnePost(postDTO.getIdx());
        verify(mockPostRepository, atLeastOnce()).findOnePost(postDTO.getIdx());
        verifyNoMoreInteractions(mockPostRepository);

        InOrder inOrder = inOrder(mockPostRepository);
        inOrder.verify(mockPostRepository).findOnePost(postDTO.getIdx());
    }
}

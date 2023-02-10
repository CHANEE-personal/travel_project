package com.travel.api.post;

import com.travel.api.post.domain.PostDTO;
import com.travel.api.post.domain.PostEntity;
import com.travel.api.post.domain.reply.ReplyDTO;
import com.travel.api.post.domain.reply.ReplyEntity;
import com.travel.api.post.domain.repository.PostQueryRepository;
import com.travel.api.post.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@DisplayName("FAQ Service Test")
class PostServiceTest {

    @Mock private PostRepository postRepository;
    @Mock private PostQueryRepository postQueryRepository;
    @InjectMocks private PostService mockPostService;
    private final PostService postService;
    private final EntityManager em;

    private PostEntity postEntity;
    private PostDTO postDTO;
    private ReplyEntity replyEntity;
    private ReplyDTO replyDTO;
    private ReplyEntity replyEntity2;

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
                .commentTitle("댓글 테스트")
                .commentDescription("댓글 테스트")
                .favoriteCount(0)
                .visible("Y")
                .build();

        replyDTO = postService.insertReply(postEntity.getIdx(), replyEntity);

        replyEntity2 = ReplyEntity.builder()
                .commentTitle("대댓글 테스트")
                .commentDescription("대댓글 테스트")
                .favoriteCount(0)
                .visible("Y")
                .parent(replyEntity)
                .build();

        postService.insertReply(postEntity.getIdx(), replyEntity2);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createPost();
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
        when(postQueryRepository.findPostList(postMap, pageRequest)).thenReturn(resultPage);
        Page<PostDTO> newPostList = mockPostService.findPostList(postMap, pageRequest);

        List<PostDTO> findPostList = newPostList.stream().collect(Collectors.toList());

        // then
        assertThat(findPostList.get(0).getIdx()).isEqualTo(postList.get(0).getIdx());
        assertThat(findPostList.get(0).getPostTitle()).isEqualTo("게시글 테스트");
        assertThat(findPostList.get(0).getPostDescription()).isEqualTo("게시글 테스트");

        // verify
        verify(postQueryRepository, times(1)).findPostList(postMap, pageRequest);
        verify(postQueryRepository, atLeastOnce()).findPostList(postMap, pageRequest);
        verifyNoMoreInteractions(postQueryRepository);

        InOrder inOrder = inOrder(postQueryRepository);
        inOrder.verify(postQueryRepository).findPostList(postMap, pageRequest);
    }

    @Test
    @DisplayName("게시글 상세 조회 테스트")
    void 게시글상세조회테스트() {
        PostDTO onePost = postService.findOnePost(postEntity.getIdx());

        // 게시글
        assertThat(onePost.getPostTitle()).isEqualTo("게시글 테스트");

        // 댓글, 대댓글
        assertThat(onePost.getPostReplyList().get(0).getCommentTitle()).isEqualTo("대댓글 테스트");
        assertThat(onePost.getPostReplyList().get(1).getCommentTitle()).isEqualTo("댓글 테스트");

    }

    @Test
    @DisplayName("게시글 상세 조회 Mockito 테스트")
    void 게시글상세조회Mockito테스트() {
        // when
        when(postQueryRepository.findOnePost(postDTO.getIdx())).thenReturn(postDTO);
        PostDTO onePost = mockPostService.findOnePost(postDTO.getIdx());

        // then
        assertThat(onePost.getPostTitle()).isEqualTo("게시글 테스트");
        assertThat(onePost.getPostDescription()).isEqualTo("게시글 테스트");

        // verify
        verify(postQueryRepository, times(1)).findOnePost(postDTO.getIdx());
        verify(postQueryRepository, atLeastOnce()).findOnePost(postDTO.getIdx());
        verifyNoMoreInteractions(postQueryRepository);

        InOrder inOrder = inOrder(postQueryRepository);
        inOrder.verify(postQueryRepository).findOnePost(postDTO.getIdx());
    }

    @Test
    @DisplayName("게시글 등록 Mockito 테스트")
    void 게시글등록Mockito테스트() {
        PostEntity insertEntity = PostEntity.builder()
                .postTitle("게시글 등록 테스트")
                .postDescription("게시글 등록 테스트")
                .viewCount(0)
                .favoriteCount(0)
                .visible("Y")
                .build();

        // when
        when(postRepository.save(insertEntity)).thenReturn(insertEntity);
        PostDTO onePost = mockPostService.findOnePost(insertEntity.getIdx());

        // then
        assertThat(onePost.getPostTitle()).isEqualTo("게시글 등록 테스트");
        assertThat(onePost.getPostDescription()).isEqualTo("게시글 등록 테스트");

        // verify
        verify(postRepository, times(1)).save(insertEntity);
        verify(postRepository, atLeastOnce()).save(insertEntity);
        verifyNoMoreInteractions(postRepository);

        InOrder inOrder = inOrder(postRepository);
        inOrder.verify(postRepository).save(insertEntity);
    }

    @Test
    @DisplayName("게시글 수정 Mockito 테스트")
    void 게시글수정Mockito테스트() {
        // given
        PostEntity updatePostEntity = PostEntity.builder()
                .idx(postEntity.getIdx())
                .postTitle("게시글 수정 테스트")
                .postDescription("게시글 수정 테스트")
                .viewCount(0)
                .favoriteCount(0)
                .visible("Y")
                .build();

        PostDTO updatePost = postService.updatePost(postEntity.getIdx(), updatePostEntity);

        // when
        when(mockPostService.findOnePost(updatePost.getIdx())).thenReturn(updatePost);
        PostDTO onePost = mockPostService.findOnePost(updatePost.getIdx());

        // then
        assertThat(onePost.getPostTitle()).isEqualTo("게시글 수정 테스트");
        assertThat(onePost.getPostDescription()).isEqualTo("게시글 수정 테스트");

        // verify
        verify(mockPostService, times(1)).findOnePost(onePost.getIdx());
        verify(mockPostService, atLeastOnce()).findOnePost(onePost.getIdx());
        verifyNoMoreInteractions(mockPostService);

        InOrder inOrder = inOrder(mockPostService);
        inOrder.verify(mockPostService).findOnePost(onePost.getIdx());
    }

    @Test
    @DisplayName("게시글 삭제 Mockito 테스트")
    void 게시글삭제Mockito테스트() {
        // when
        when(mockPostService.findOnePost(postEntity.getIdx())).thenReturn(postDTO);
        Long deleteIdx = postService.deletePost(postEntity.getIdx());

        // then
        assertThat(mockPostService.findOnePost(postDTO.getIdx()).getIdx()).isEqualTo(deleteIdx);

        // verify
        verify(mockPostService, times(1)).findOnePost(postDTO.getIdx());
        verify(mockPostService, atLeastOnce()).findOnePost(postDTO.getIdx());
        verifyNoMoreInteractions(mockPostService);

        InOrder inOrder = inOrder(mockPostService);
        inOrder.verify(mockPostService).findOnePost(postDTO.getIdx());
    }

    @Test
    @DisplayName("댓글수정테스트")
    void 댓글수정테스트() {
        ReplyEntity insertEntity = ReplyEntity.builder()
                .commentTitle("등록 테스트")
                .commentDescription("등록 테스트")
                .favoriteCount(0)
                .postEntity(postEntity)
                .visible("Y")
                .build();

        em.persist(insertEntity);

        ReplyEntity updateEntity = ReplyEntity.builder()
                .idx(insertEntity.getIdx())
                .commentTitle("수정 테스트")
                .commentDescription("수정 테스트")
                .favoriteCount(0)
                .postEntity(postEntity)
                .visible("Y")
                .build();

        ReplyDTO updateReply = postService.updateReply(insertEntity.getIdx(), updateEntity);
        em.flush();
        em.clear();
        assertThat(updateReply.getCommentTitle()).isEqualTo("수정 테스트");
    }

    @Test
    @DisplayName("댓글삭제테스트")
    void 댓글삭제테스트() {
        ReplyEntity insertEntity = ReplyEntity.builder()
                .commentTitle("등록 테스트")
                .commentDescription("등록 테스트")
                .favoriteCount(0)
                .postEntity(postEntity)
                .visible("Y")
                .build();

        em.persist(insertEntity);

        Long deleteIdx = postService.deleteReply(insertEntity.getIdx());
        em.flush();
        em.clear();

        assertThat(deleteIdx).isEqualTo(insertEntity.getIdx());
    }
}

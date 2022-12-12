package com.travel.travel_project.api.post;

import com.travel.travel_project.api.post.mapper.PostMapper;
import com.travel.travel_project.api.travel.mapper.TravelMapper;
import com.travel.travel_project.domain.post.PostDTO;
import com.travel.travel_project.domain.post.PostEntity;
import com.travel.travel_project.domain.travel.TravelDTO;
import com.travel.travel_project.domain.travel.TravelEntity;
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
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
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
class PostRepositoryTest {

    @Mock
    private PostRepository mockPostRepository;
    private final PostRepository postRepository;
    private final EntityManager em;

    private PostEntity postEntity;
    private PostDTO postDTO;
    private PostEntity postChildEntity;
    private PostDTO postChildDTO;
    private PostEntity postParentEntity;
    private PostDTO postParentDTO;

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
        postDTO = PostMapper.INSTANCE.toDto(postEntity);

        postParentEntity = PostEntity.builder()
                .postTitle("게시글 댓글 테스트")
                .postDescription("게시글 댓글 테스트")
                .popular(false)
                .postParentIdx(postEntity.getIdx())
                .postTopIdx(postEntity.getIdx())
                .viewCount(0)
                .favoriteCount(0)
                .visible("Y")
                .build();

        em.persist(postParentEntity);
        postParentDTO = PostMapper.INSTANCE.toDto(postParentEntity);

        postChildEntity = PostEntity.builder()
                .postTitle("게시글 대댓글 테스트")
                .postDescription("게시글 대댓글 테스트")
                .popular(false)
                .postParentIdx(postParentEntity.getIdx())
                .postTopIdx(postEntity.getIdx())
                .viewCount(0)
                .favoriteCount(0)
                .visible("Y")
                .build();

        em.persist(postChildEntity);
        postChildDTO = PostMapper.INSTANCE.toDto(postChildEntity);
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
        postMap.put("jpaStartPage", 1);
        postMap.put("size", 3);

        List<PostDTO> postList = new ArrayList<>();
        postList.add(postDTO);
        postList.add(postParentDTO);
        postList.add(postChildDTO);

        // when
        when(mockPostRepository.findPostList(postMap)).thenReturn(postList);
        List<PostDTO> findPostList = mockPostRepository.findPostList(postMap);

        // then
        // 게시글 관련
        assertThat(findPostList.get(0).getIdx()).isEqualTo(postList.get(0).getIdx());
        assertThat(findPostList.get(0).getPostTitle()).isEqualTo("게시글 테스트");
        assertThat(findPostList.get(0).getPostDescription()).isEqualTo("게시글 테스트");

        // 게시글 댓글 관련
        assertThat(findPostList.get(1).getPostParentIdx()).isEqualTo(postList.get(0).getIdx());
        assertThat(findPostList.get(1).getPostTopIdx()).isEqualTo(postList.get(0).getIdx());
        assertThat(findPostList.get(1).getPostTitle()).isEqualTo("게시글 댓글 테스트");
        assertThat(findPostList.get(1).getPostDescription()).isEqualTo("게시글 댓글 테스트");

        // 게시글 대댓글 관련
        assertThat(findPostList.get(2).getPostParentIdx()).isEqualTo(postList.get(1).getIdx());
        assertThat(findPostList.get(2).getPostTopIdx()).isEqualTo(postList.get(0).getIdx());
        assertThat(findPostList.get(2).getPostTitle()).isEqualTo("게시글 대댓글 테스트");
        assertThat(findPostList.get(2).getPostDescription()).isEqualTo("게시글 대댓글 테스트");

        // verify
        verify(mockPostRepository, times(1)).findPostList(postMap);
        verify(mockPostRepository, atLeastOnce()).findPostList(postMap);
        verifyNoMoreInteractions(mockPostRepository);

        InOrder inOrder = inOrder(mockPostRepository);
        inOrder.verify(mockPostRepository).findPostList(postMap);
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

        PostDTO postInfo = postRepository.insertPost(insertEntity);

        // when
        when(mockPostRepository.findOnePost(postInfo.getIdx())).thenReturn(postInfo);
        PostDTO onePost = mockPostRepository.findOnePost(postInfo.getIdx());

        // then
        assertThat(onePost.getPostTitle()).isEqualTo("게시글 등록 테스트");
        assertThat(onePost.getPostDescription()).isEqualTo("게시글 등록 테스트");

        // verify
        verify(mockPostRepository, times(1)).findOnePost(onePost.getIdx());
        verify(mockPostRepository, atLeastOnce()).findOnePost(onePost.getIdx());
        verifyNoMoreInteractions(mockPostRepository);

        InOrder inOrder = inOrder(mockPostRepository);
        inOrder.verify(mockPostRepository).findOnePost(onePost.getIdx());
    }

    @Test
    @DisplayName("게시글 수정 Mockito 테스트")
    void 게시글수정Mockito테스트() {
        // given
        PostEntity insertEntity = PostEntity.builder()
                .postTitle("게시글 등록 테스트")
                .postDescription("게시글 등록 테스트")
                .viewCount(0)
                .favoriteCount(0)
                .visible("Y")
                .build();

        PostDTO postInfo = postRepository.insertPost(insertEntity);

        PostEntity updatePostEntity = PostEntity.builder()
                .idx(postInfo.getIdx())
                .postTitle("게시글 수정 테스트")
                .postDescription("게시글 수정 테스트")
                .viewCount(0)
                .favoriteCount(0)
                .visible("Y")
                .build();

        PostDTO updatePost = postRepository.updatePost(updatePostEntity);

        // when
        when(mockPostRepository.findOnePost(updatePost.getIdx())).thenReturn(updatePost);
        PostDTO onePost = mockPostRepository.findOnePost(updatePost.getIdx());

        // then
        assertThat(onePost.getPostTitle()).isEqualTo("게시글 수정 테스트");
        assertThat(onePost.getPostDescription()).isEqualTo("게시글 수정 테스트");

        // verify
        verify(mockPostRepository, times(1)).findOnePost(onePost.getIdx());
        verify(mockPostRepository, atLeastOnce()).findOnePost(onePost.getIdx());
        verifyNoMoreInteractions(mockPostRepository);

        InOrder inOrder = inOrder(mockPostRepository);
        inOrder.verify(mockPostRepository).findOnePost(onePost.getIdx());
    }

    @Test
    @DisplayName("게시글 삭제 Mockito 테스트")
    void 게시글삭제Mockito테스트() {
        // when
        when(mockPostRepository.findOnePost(postDTO.getIdx())).thenReturn(postDTO);
        Long deleteIdx = postRepository.deletePost(postDTO.getIdx());

        // then
        assertThat(mockPostRepository.findOnePost(postDTO.getIdx()).getIdx()).isEqualTo(deleteIdx);

        // verify
        verify(mockPostRepository, times(1)).findOnePost(postDTO.getIdx());
        verify(mockPostRepository, atLeastOnce()).findOnePost(postDTO.getIdx());
        verifyNoMoreInteractions(mockPostRepository);

        InOrder inOrder = inOrder(mockPostRepository);
        inOrder.verify(mockPostRepository).findOnePost(postDTO.getIdx());
    }

    @Test
    @DisplayName("인기게시글선정Mockito테스트")
    void 인기게시글선정Mockito테스트() {
        Boolean popular = postRepository.togglePopular(postDTO.getIdx());

        assertThat(popular).isTrue();
    }
}
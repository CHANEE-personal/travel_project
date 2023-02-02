package com.travel.api.post;

import com.travel.api.AdminCommonServiceTest;
import com.travel.api.post.domain.PostDto;
import com.travel.api.post.domain.PostEntity;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

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
@DisplayName("게시글 Service Test")
class PostServiceTest extends AdminCommonServiceTest {

    @Mock
    private PostService mockPostService;
    private final PostService postService;

    @Test
    @DisplayName("게시글 리스트 조회 Mockito 테스트")
    void 게시글리스트조회Mockito테스트() {
        // given
        Map<String, Object> postMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(0, 3);

        List<PostDto> postList = new ArrayList<>();
        postList.add(postDTO);

        Page<PostDto> resultPage = new PageImpl<>(postList, pageRequest, postList.size());


        // when
        when(mockPostService.findPostList(postMap, pageRequest)).thenReturn(resultPage);
        Page<PostDto> newPostList = mockPostService.findPostList(postMap, pageRequest);

        List<PostDto> findPostList = newPostList.stream().collect(Collectors.toList());
        // then
        // 게시글 관련
        assertThat(findPostList.get(0).getIdx()).isEqualTo(postList.get(0).getIdx());
        assertThat(findPostList.get(0).getPostTitle()).isEqualTo("게시글 테스트");
        assertThat(findPostList.get(0).getPostDescription()).isEqualTo("게시글 테스트");

        // verify
        verify(mockPostService, times(1)).findPostList(postMap, pageRequest);
        verify(mockPostService, atLeastOnce()).findPostList(postMap, pageRequest);
        verifyNoMoreInteractions(mockPostService);

        InOrder inOrder = inOrder(mockPostService);
        inOrder.verify(mockPostService).findPostList(postMap, pageRequest);
    }

    @Test
    @DisplayName("게시글 상세 조회 테스트")
    void 게시글상세조회테스트() {
        PostDto onePost = postService.findOnePost(postDTO.getIdx());

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
        when(mockPostService.findOnePost(postDTO.getIdx())).thenReturn(postDTO);
        PostDto onePost = mockPostService.findOnePost(postDTO.getIdx());

        // then
        assertThat(onePost.getPostTitle()).isEqualTo("게시글 테스트");
        assertThat(onePost.getPostDescription()).isEqualTo("게시글 테스트");

        // verify
        verify(mockPostService, times(1)).findOnePost(postDTO.getIdx());
        verify(mockPostService, atLeastOnce()).findOnePost(postDTO.getIdx());
        verifyNoMoreInteractions(mockPostService);

        InOrder inOrder = inOrder(mockPostService);
        inOrder.verify(mockPostService).findOnePost(postDTO.getIdx());
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

        PostDto postInfo = postService.insertPost(insertEntity);

        // when
        when(mockPostService.findOnePost(postInfo.getIdx())).thenReturn(postInfo);
        PostDto onePost = mockPostService.findOnePost(postInfo.getIdx());

        // then
        assertThat(onePost.getPostTitle()).isEqualTo("게시글 등록 테스트");
        assertThat(onePost.getPostDescription()).isEqualTo("게시글 등록 테스트");

        // verify
        verify(mockPostService, times(1)).findOnePost(onePost.getIdx());
        verify(mockPostService, atLeastOnce()).findOnePost(onePost.getIdx());
        verifyNoMoreInteractions(mockPostService);

        InOrder inOrder = inOrder(mockPostService);
        inOrder.verify(mockPostService).findOnePost(onePost.getIdx());
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

        PostDto postInfo = postService.insertPost(insertEntity);

        PostEntity updatePostEntity = PostEntity.builder()
                .idx(postInfo.getIdx())
                .postTitle("게시글 수정 테스트")
                .postDescription("게시글 수정 테스트")
                .viewCount(0)
                .favoriteCount(0)
                .visible("Y")
                .build();

        PostDto updatePost = postService.updatePost(postInfo.getIdx(), updatePostEntity);

        // when
        when(mockPostService.findOnePost(updatePost.getIdx())).thenReturn(updatePost);
        PostDto onePost = mockPostService.findOnePost(updatePost.getIdx());

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
        when(mockPostService.findOnePost(postDTO.getIdx())).thenReturn(postDTO);
        Long deleteIdx = postService.deletePost(postDTO.getIdx());

        // then
        assertThat(mockPostService.findOnePost(postDTO.getIdx()).getIdx()).isEqualTo(deleteIdx);

        // verify
        verify(mockPostService, times(1)).findOnePost(postDTO.getIdx());
        verify(mockPostService, atLeastOnce()).findOnePost(postDTO.getIdx());
        verifyNoMoreInteractions(mockPostService);

        InOrder inOrder = inOrder(mockPostService);
        inOrder.verify(mockPostService).findOnePost(postDTO.getIdx());
    }
}

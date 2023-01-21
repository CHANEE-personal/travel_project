package com.travel.api.post;

import com.travel.api.post.domain.PostDto;
import com.travel.api.post.domain.PostEntity;
import com.travel.api.post.domain.image.PostImageDto;
import com.travel.api.post.domain.image.PostImageEntity;
import com.travel.api.post.domain.repository.PostQueryRepository;
import com.travel.api.post.domain.repository.PostRepository;
import com.travel.api.post.domain.repository.ReplyRepository;
import com.travel.common.SaveFile;
import com.travel.exception.TravelException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.travel.exception.ApiExceptionType.*;


@Service
@RequiredArgsConstructor
public class PostService {

    private final PostQueryRepository postQueryRepository;
    private final PostRepository postRepository;
    private final SaveFile saveFile;

    private PostEntity onePost(Long idx) {
        return postRepository.findById(idx)
                .orElseThrow(() -> new TravelException(NOT_FOUND_POST));
    }

    /**
     * <pre>
     * 1. MethodName : findPostList
     * 2. ClassName  : PostService.java
     * 3. Comment    : 게시글 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 12. 12.
     * </pre>
     */
    @Transactional(readOnly = true)
    public Page<PostDto> findPostList(Map<String, Object> postMap, PageRequest pageRequest) {
        return postQueryRepository.findPostList(postMap, pageRequest);
    }

    /**
     * <pre>
     * 1. MethodName : findOnePost
     * 2. ClassName  : PostService.java
     * 3. Comment    : 게시글 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 12. 12.
     * </pre>
     */
    @Transactional(readOnly = true)
    public PostDto findOnePost(Long idx) {
        return postQueryRepository.findOnePost(idx);
    }

    /**
     * <pre>
     * 1. MethodName : insertPost
     * 2. ClassName  : PostService.java
     * 3. Comment    : 게시글 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 12. 12.
     * </pre>
     */
    @Transactional
    public PostDto insertPost(PostEntity postEntity) {
        try {
            return PostEntity.toDto(postRepository.save(postEntity));
        } catch (Exception e) {
            throw new TravelException(ERROR_POST);
        }
    }

    /**
     * <pre>
     * 1. MethodName : insertPostImage
     * 2. ClassName  : PostService.java
     * 3. Comment    : 게시글 이미지 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 12. 11.
     * </pre>
     */
    @Transactional
    public List<PostImageDto> insertPostImage(Long idx, List<MultipartFile> files, PostImageEntity postImageEntity) {
        try {
            return saveFile.savePostFile(onePost(idx), files, postImageEntity);
        } catch (Exception e) {
            throw new TravelException(ERROR_IMAGE);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updatePost
     * 2. ClassName  : PostService.java
     * 3. Comment    : 게시글 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 12. 12.
     * </pre>
     */
    @Transactional
    public PostDto updatePost(Long idx, PostEntity postEntity) {
        try {
            onePost(idx).update(postEntity);
            return PostEntity.toDto(postEntity);
        } catch (Exception e) {
            throw new TravelException(ERROR_UPDATE_POST);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deletePost
     * 2. ClassName  : PostService.java
     * 3. Comment    : 게시글 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 12. 12.
     * </pre>
     */
    @Transactional
    public Long deletePost(Long idx) {
        try {
            postRepository.deleteById(idx);
            return idx;
        } catch (Exception e) {
            throw new TravelException(ERROR_DELETE_POST);
        }
    }

    /**
     * <pre>
     * 1. MethodName : togglePopular
     * 2. ClassName  : PostService.java
     * 3. Comment    : 게시글 상단 고정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 12. 12.
     * </pre>
     */
    @Transactional
    public Boolean togglePopular(Long idx) {
        try {
            PostEntity onePost = onePost(idx);
            Optional.ofNullable(onePost)
                    .ifPresent(postEntity -> postEntity.togglePopular(onePost.getPopular()));

            assert onePost != null;
            return onePost.getPopular();
        } catch (Exception e) {
            throw new TravelException(ERROR_UPDATE_POST);
        }
    }
}

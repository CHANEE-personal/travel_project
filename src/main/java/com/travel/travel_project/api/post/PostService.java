package com.travel.travel_project.api.post;

import com.travel.travel_project.domain.post.PostDTO;
import com.travel.travel_project.domain.post.PostEntity;
import com.travel.travel_project.exception.TravelException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.travel.travel_project.exception.ApiExceptionType.*;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    /**
     * <pre>
     * 1. MethodName : findPostCount
     * 2. ClassName  : PostService.java
     * 3. Comment    : 게시글 리스트 갯수 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 12. 12.
     * </pre>
     */
    @Transactional
    public int findPostCount(Map<String, Object> postMap) {
        try {
            return postRepository.findPostCount(postMap);
        } catch (Exception e) {
            throw new TravelException(NOT_FOUND_POST_LIST, e);
        }
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
    @Cacheable(value = "post", key = "#postMap")
    @Transactional(readOnly = true)
    public List<PostDTO> findPostList(Map<String, Object> postMap) {
        return postRepository.findPostList(postMap);
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
    @Cacheable(value = "post", key = "#idx")
    @Transactional(readOnly = true)
    public PostDTO findOnePost(Long idx) {
        return postRepository.findOnePost(idx);
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
    @CachePut("post")
    @Modifying(clearAutomatically = true)
    @Transactional
    public PostDTO insertPost(PostEntity postEntity) {
        try {
            return postRepository.insertPost(postEntity);
        } catch (Exception e) {
            throw new TravelException(ERROR_POST, e);
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
    @CachePut(value = "post", key = "#postEntity.idx")
    @Modifying(clearAutomatically = true)
    @Transactional
    public PostDTO updatePost(PostEntity postEntity) {
        try {
            return postRepository.updatePost(postEntity);
        } catch (Exception e) {
            throw new TravelException(ERROR_UPDATE_POST, e);
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
    @CacheEvict(value = "post", key = "#idx")
    @Modifying(clearAutomatically = true)
    @Transactional
    public Long deletePost(Long idx) {
        try {
            return postRepository.deletePost(idx);
        } catch (Exception e) {
            throw new TravelException(ERROR_DELETE_POST, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : togglePopular
     * 2. ClassName  : PostService.java
     * 3. Comment    : 게시글 상단 고정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 12. 12.
     * </pre>
     */
    @CachePut(value = "post", key = "#idx")
    @Transactional
    public Boolean togglePopular(Long idx) {
        try {
            return postRepository.togglePopular(idx);
        } catch (Exception e) {
            throw new TravelException(ERROR_UPDATE_POST, e);
        }
    }
}

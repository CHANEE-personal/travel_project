package com.travel.travel_project.api.post;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travel.travel_project.domain.post.PostDTO;
import com.travel.travel_project.domain.post.PostEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.travel.travel_project.common.StringUtil.getInt;
import static com.travel.travel_project.common.StringUtil.getString;
import static com.travel.travel_project.domain.file.QCommonImageEntity.commonImageEntity;
import static com.travel.travel_project.domain.post.QPostEntity.postEntity;

@Repository
@RequiredArgsConstructor
public class PostRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    private BooleanExpression searchPost(Map<String, Object> postMap) {
        String searchKeyword = getString(postMap.get("searchKeyword"), "");

        if (!Objects.equals(searchKeyword, "")) {
            return postEntity.postTitle.contains(searchKeyword)
                    .or(postEntity.postDescription.contains(searchKeyword));
        } else {
            return null;
        }
    }

    /**
     * <pre>
     * 1. MethodName : findPostCount
     * 2. ClassName  : PostRepository.java
     * 3. Comment    : 게시글 리스트 갯수 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 12.11.
     * </pre>
     */
    public int findPostCount(Map<String, Object> postMap) {
        return queryFactory.selectFrom(postEntity)
                .where(searchPost(postMap))
                .fetch().size();
    }

    /**
     * <pre>
     * 1. MethodName : findPostList
     * 2. ClassName  : PostRepository.java
     * 3. Comment    : 게시글 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 12.11.
     * </pre>
     */
    public List<PostDTO> findPostList(Map<String, Object> postMap) {
        List<PostEntity> postList = queryFactory
                .selectFrom(postEntity)
                .orderBy(postEntity.idx.desc())
                .leftJoin(postEntity.postImageList, commonImageEntity)
                .fetchJoin()
                .where(searchPost(postMap))
                .offset(getInt(postMap.get("jpaStartPage"), 0))
                .limit(getInt(postMap.get("size"), 0))
                .fetch();

        postList.forEach(list -> postList.get(postList.indexOf(list))
                .setRowNum(getInt(postMap.get("startPage"), 1) * (getInt(postMap.get("size"), 1)) - (2 - postList.indexOf(list))));

        return PostEntity.toDtoList(postList);
    }

    /**
     * <pre>
     * 1. MethodName : findOnePost
     * 2. ClassName  : PostRepository.java
     * 3. Comment    : 게시글 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 12.11.
     * </pre>
     */
    public PostDTO findOnePost(Long idx) {
        PostEntity onePost = queryFactory
                .selectFrom(postEntity)
                .leftJoin(postEntity.postImageList, commonImageEntity)
                .where(postEntity.idx.eq(idx))
                .fetchOne();

        assert onePost != null;
        return PostEntity.toDto(onePost);
    }

    /**
     * <pre>
     * 1. MethodName : insertPost
     * 2. ClassName  : PostRepository.java
     * 3. Comment    : 게시글 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 12.11.
     * </pre>
     */
    public PostDTO insertPost(PostEntity postEntity) {
        em.persist(postEntity);
        return PostEntity.toDto(postEntity);
    }

    /**
     * <pre>
     * 1. MethodName : updatePost
     * 2. ClassName  : PostRepository.java
     * 3. Comment    : 게시글 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 12.11.
     * </pre>
     */
    public PostDTO updatePost(PostEntity postEntity) {
        em.merge(postEntity);
        em.flush();
        em.clear();
        return PostEntity.toDto(postEntity);
    }

    /**
     * <pre>
     * 1. MethodName : deletePost
     * 2. ClassName  : PostRepository.java
     * 3. Comment    : 게시글 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 12.11.
     * </pre>
     */
    public Long deletePost(Long idx) {
        em.remove(em.find(PostEntity.class, idx));
        em.flush();
        em.clear();
        return idx;
    }

    /**
     * <pre>
     * 1. MethodName : togglePopular
     * 2. ClassName  : PostRepository.java
     * 3. Comment    : 인기 게시글 설정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 12.11.
     * </pre>
     */
    public Boolean togglePopular(Long idx) {
        PostEntity onePost = em.find(PostEntity.class, idx);
        Boolean popular = !onePost.getPopular();

        queryFactory
                .update(postEntity)
                .where(postEntity.idx.eq(idx))
                .set(postEntity.popular, popular)
                .execute();

        em.flush();
        em.clear();

        return popular;
    }
}

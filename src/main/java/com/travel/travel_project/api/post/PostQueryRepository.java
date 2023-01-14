package com.travel.travel_project.api.post;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travel.travel_project.domain.post.PostDTO;
import com.travel.travel_project.domain.post.PostEntity;
import com.travel.travel_project.domain.post.reply.QReplyEntity;
import com.travel.travel_project.domain.post.reply.ReplyEntity;
import com.travel.travel_project.exception.TravelException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.*;

import static com.travel.travel_project.common.StringUtil.getInt;
import static com.travel.travel_project.common.StringUtil.getString;
import static com.travel.travel_project.domain.file.QCommonImageEntity.commonImageEntity;
import static com.travel.travel_project.domain.post.QPostEntity.postEntity;
import static com.travel.travel_project.exception.ApiExceptionType.NOT_FOUND_POST;

@Repository
@RequiredArgsConstructor
public class PostQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    private BooleanExpression searchPost(Map<String, Object> postMap) {
        String searchKeyword = getString(postMap.get("searchKeyword"), "");
        return !Objects.equals(searchKeyword, "") ? postEntity.postTitle.contains(searchKeyword).or(postEntity.postDescription.contains(searchKeyword)) : null;
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
    public Page<PostDTO> findPostList(Map<String, Object> postMap, PageRequest pageRequest) {
        List<PostEntity> postList = queryFactory
                .selectFrom(postEntity)
                .orderBy(postEntity.idx.desc())
                .where(searchPost(postMap))
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        assert postList != null;
        return new PageImpl<>(PostEntity.toDtoList(postList), pageRequest, postList.size());
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
        PostEntity onePost = Optional.ofNullable(queryFactory
                .selectFrom(postEntity)
                .leftJoin(postEntity.postImageList, commonImageEntity)
                .fetchJoin()
                .where(postEntity.idx.eq(idx))
                .fetchOne()).orElseThrow(() -> new TravelException(NOT_FOUND_POST));

        List<ReplyEntity> replyList = queryFactory
                .selectFrom(QReplyEntity.replyEntity)
                .orderBy(QReplyEntity.replyEntity.idx.desc())
                .where(QReplyEntity.replyEntity.postEntity.idx.eq(idx))
                .fetch();

        replyList.forEach(onePost::addReplyList);
        return PostEntity.toDto(onePost);
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

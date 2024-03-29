package com.travel.api.post.domain.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travel.api.post.domain.PostDTO;
import com.travel.api.post.domain.PostEntity;
import static com.travel.api.post.domain.image.QPostImageEntity.postImageEntity;
import com.travel.api.post.domain.reply.QReplyEntity;
import com.travel.api.post.domain.reply.ReplyEntity;
import com.travel.exception.TravelException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.*;

import static com.travel.api.post.domain.QPostEntity.postEntity;
import static com.travel.common.StringUtil.getString;
import static com.travel.exception.ApiExceptionType.NOT_FOUND_POST;

@Repository
@RequiredArgsConstructor
public class PostQueryRepository {

    private final JPAQueryFactory queryFactory;

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
                .leftJoin(postEntity.postImageList, postImageEntity)
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
}

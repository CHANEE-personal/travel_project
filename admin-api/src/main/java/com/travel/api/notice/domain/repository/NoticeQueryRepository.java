package com.travel.api.notice.domain.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import com.travel.api.notice.domain.NoticeDTO;
import com.travel.api.notice.domain.NoticeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.*;

import static com.travel.api.notice.domain.QNoticeEntity.noticeEntity;
import static com.travel.common.StringUtil.getString;


@Repository
@RequiredArgsConstructor
public class NoticeQueryRepository {

    private final JPAQueryFactory queryFactory;

    private BooleanExpression searchNoticeInfo(Map<String, Object> noticeMap) {
        String searchKeyword = getString(noticeMap.get("searchKeyword"), "");

        return !Objects.equals(searchKeyword, "") ? noticeEntity.title.contains(searchKeyword).or(noticeEntity.description.contains(searchKeyword)) : null;
    }

    /**
     * <pre>
     * 1. MethodName : findNoticeList
     * 2. ClassName  : NoticeRepository.java
     * 3. Comment    : 공지사항 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 28.
     * </pre>
     */
    public Page<NoticeDTO> findNoticeList(Map<String, Object> noticeMap, PageRequest pageRequest) {
        List<NoticeEntity> noticeList = queryFactory
                .selectFrom(noticeEntity)
                .orderBy(noticeEntity.idx.desc())
                .where(searchNoticeInfo(noticeMap))
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        assert noticeList != null;
        return new PageImpl<>(NoticeEntity.toDtoList(noticeList), pageRequest, noticeList.size());
    }
}

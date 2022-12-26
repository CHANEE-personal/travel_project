package com.travel.travel_project.api.notice;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travel.travel_project.domain.notice.NoticeDTO;
import com.travel.travel_project.domain.notice.NoticeEntity;

import com.travel.travel_project.exception.TravelException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.*;

import static com.travel.travel_project.common.StringUtil.getInt;
import static com.travel.travel_project.common.StringUtil.getString;
import static com.travel.travel_project.domain.notice.QNoticeEntity.*;
import static com.travel.travel_project.exception.ApiExceptionType.NOT_FOUND_NOTICE;

@Repository
@RequiredArgsConstructor
public class NoticeRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    private BooleanExpression searchNoticeInfo(Map<String, Object> noticeMap) {
        String searchKeyword = getString(noticeMap.get("searchKeyword"), "");

        return !Objects.equals(searchKeyword, "") ? noticeEntity.title.contains(searchKeyword).or(noticeEntity.description.contains(searchKeyword)) : null;
    }

    /**
     * <pre>
     * 1. MethodName : findNoticeCount
     * 2. ClassName  : NoticeRepository.java
     * 3. Comment    : 공지사항 리스트 갯수 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 28.
     * </pre>
     */
    public int findNoticeCount(Map<String, Object> noticeMap) {
        return queryFactory.selectFrom(noticeEntity)
                .where(searchNoticeInfo(noticeMap))
                .fetch().size();
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
    public List<NoticeDTO> findNoticeList(Map<String, Object> noticeMap) {
        List<NoticeEntity> noticeList = queryFactory
                .selectFrom(noticeEntity)
                .orderBy(noticeEntity.idx.desc())
                .where(searchNoticeInfo(noticeMap))
                .offset(getInt(noticeMap.get("jpaStartPage"), 0))
                .limit(getInt(noticeMap.get("size"), 0))
                .fetch();

        return noticeList != null ? NoticeEntity.toDtoList(noticeList) : Collections.emptyList();
    }

    /**
     * <pre>
     * 1. MethodName : findOneNotice
     * 2. ClassName  : NoticeRepository.java
     * 3. Comment    : 공지사항 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 28.
     * </pre>
     */
    public NoticeDTO findOneNotice(Long idx) {
        // 공지사항 조회 수 증가
        viewNotice(idx);

        NoticeEntity findOneNotice = Optional.ofNullable(queryFactory
                .selectFrom(noticeEntity)
                .where(noticeEntity.idx.eq(idx))
                .fetchOne()).orElseThrow(() -> new TravelException(NOT_FOUND_NOTICE, new Throwable()));

        return NoticeEntity.toDto(findOneNotice);
    }

    /**
     * <pre>
     * 1. MethodName : insertNotice
     * 2. ClassName  : NoticeRepository.java
     * 3. Comment    : 공지사항 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 28.
     * </pre>
     */
    public NoticeDTO insertNotice(NoticeEntity noticeEntity) {
        em.persist(noticeEntity);
        return NoticeEntity.toDto(noticeEntity);
    }

    /**
     * <pre>
     * 1. MethodName : updateNotice
     * 2. ClassName  : NoticeRepository.java
     * 3. Comment    : 공지사항 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 28.
     * </pre>
     */
    public NoticeDTO updateNotice(NoticeEntity existNoticeEntity) {
        em.merge(existNoticeEntity);
        em.flush();
        em.clear();
        return NoticeEntity.toDto(existNoticeEntity);
    }

    /**
     * <pre>
     * 1. MethodName : deleteNotice
     * 2. ClassName  : NoticeRepository.java
     * 3. Comment    : 공지사항 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 28.
     * </pre>
     */
    public Long deleteNotice(Long idx) {
        em.remove(em.find(NoticeEntity.class, idx));
        em.flush();
        em.clear();
        return idx;
    }

    /**
     * <pre>
     * 1. MethodName : viewNotice
     * 2. ClassName  : NoticeRepository.java
     * 3. Comment    : 공지사항 조회 수 증가
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 28.
     * </pre>
     */
    public void viewNotice(Long idx) {
        queryFactory
                .update(noticeEntity)
                //add , minus , multiple 다 가능하다.
                .set(noticeEntity.viewCount, noticeEntity.viewCount.add(1))
                .where(noticeEntity.idx.eq(idx))
                .execute();

        em.flush();
        em.clear();
    }

    /**
     * <pre>
     * 1. MethodName : toggleTopFixed
     * 2. ClassName  : NoticeRepository.java
     * 3. Comment    : 공지사항 상단글 설정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 28.
     * </pre>
     */
    public Boolean toggleTopFixed(Long idx) {
        NoticeEntity oneNotice = em.find(NoticeEntity.class, idx);
        Boolean fixed = !oneNotice.getTopFixed();

        queryFactory
                .update(noticeEntity)
                .where(noticeEntity.idx.eq(idx))
                .set(noticeEntity.topFixed, fixed)
                .execute();

        em.flush();
        em.clear();

        return fixed;
    }
}

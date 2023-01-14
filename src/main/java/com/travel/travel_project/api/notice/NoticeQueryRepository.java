package com.travel.travel_project.api.notice;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travel.travel_project.domain.notice.NoticeDTO;
import com.travel.travel_project.domain.notice.NoticeEntity;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.*;

import static com.travel.travel_project.common.StringUtil.getString;
import static com.travel.travel_project.domain.notice.QNoticeEntity.*;

@Repository
@RequiredArgsConstructor
public class NoticeQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

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

    /**
     * <pre>
     * 1. MethodName : viewNotice
     * 2. ClassName  : NoticeRepository.java
     * 3. Comment    : 공지사항 조회 수 증가
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 28.
     * </pre>
     */
//    public void viewNotice(Long idx) {
//        queryFactory
//                .update(noticeEntity)
//                //add , minus , multiple 다 가능하다.
//                .set(noticeEntity.viewCount, noticeEntity.viewCount.add(1))
//                .where(noticeEntity.idx.eq(idx))
//                .execute();
//
//        em.flush();
//        em.clear();
//    }

    /**
     * <pre>
     * 1. MethodName : toggleTopFixed
     * 2. ClassName  : NoticeRepository.java
     * 3. Comment    : 공지사항 상단글 설정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 28.
     * </pre>
     */
//    public Boolean toggleTopFixed(Long idx) {
//        NoticeEntity oneNotice = em.find(NoticeEntity.class, idx);
//        Boolean fixed = !oneNotice.getTopFixed();
//
//        queryFactory
//                .update(noticeEntity)
//                .where(noticeEntity.idx.eq(idx))
//                .set(noticeEntity.topFixed, fixed)
//                .execute();
//
//        em.flush();
//        em.clear();
//
//        return fixed;
//    }
}

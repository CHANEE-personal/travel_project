package com.travel.travel_project.api.notice;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travel.travel_project.domain.notice.NoticeDTO;
import com.travel.travel_project.domain.notice.NoticeEntity;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

import static com.travel.travel_project.api.notice.mapper.NoticeMapper.INSTANCE;
import static com.travel.travel_project.common.StringUtil.getInt;
import static com.travel.travel_project.common.StringUtil.getString;
import static com.travel.travel_project.domain.notice.QNoticeEntity.*;

@Repository
@RequiredArgsConstructor
public class NoticeRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    private BooleanExpression searchNoticeInfo(Map<String, Object> noticeMap) {
        String searchKeyword = getString(noticeMap.get("searchKeyword"), "");

        if (searchKeyword == null) {
            return null;
        } else {
            return noticeEntity.title.contains(searchKeyword)
                    .or(noticeEntity.description.contains(searchKeyword));
        }
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
    public Integer findNoticeCount(Map<String, Object> noticeMap) {
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

        noticeList.forEach(list -> noticeList.get(noticeList.indexOf(list))
                .setRnum(getInt(noticeMap.get("startPage"), 1) * (getInt(noticeMap.get("size"), 1)) - (2 - noticeList.indexOf(list))));

        return INSTANCE.toDtoList(noticeList);
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

        NoticeEntity findOneNotice = queryFactory
                .selectFrom(noticeEntity)
                .where(noticeEntity.idx.eq(idx))
                .fetchOne();

        return INSTANCE.toDto(findOneNotice);
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
        return INSTANCE.toDto(noticeEntity);
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
        return INSTANCE.toDto(existNoticeEntity);
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

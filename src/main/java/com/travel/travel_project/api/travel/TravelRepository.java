package com.travel.travel_project.api.travel;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travel.travel_project.domain.travel.TravelDTO;
import com.travel.travel_project.domain.travel.TravelEntity;
import com.travel.travel_project.domain.travel.group.*;
import com.travel.travel_project.domain.travel.review.TravelReviewDTO;
import com.travel.travel_project.domain.travel.review.TravelReviewEntity;
import com.travel.travel_project.domain.travel.schedule.TravelScheduleDTO;
import com.travel.travel_project.domain.travel.schedule.TravelScheduleEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.travel.travel_project.common.StringUtil.getInt;
import static com.travel.travel_project.common.StringUtil.getString;
import static com.travel.travel_project.domain.common.QCommonEntity.commonEntity;
import static com.travel.travel_project.domain.file.QCommonImageEntity.commonImageEntity;
import static com.travel.travel_project.domain.travel.QTravelEntity.travelEntity;
import static com.travel.travel_project.domain.travel.group.QTravelGroupEntity.travelGroupEntity;
import static com.travel.travel_project.domain.travel.review.QTravelReviewEntity.travelReviewEntity;
import static java.time.LocalDate.now;
import static java.time.LocalDateTime.of;

@Repository
@RequiredArgsConstructor
public class TravelRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    private BooleanExpression searchTravelCode(Map<String, Object> travelMap) {
        int searchCode = getInt(travelMap.get("searchCode"), 0);

        if (searchCode == 0) {
            return null;
        } else {
            return travelEntity.travelCode.eq(searchCode);
        }
    }

    private BooleanExpression searchTravelInfo(Map<String, Object> travelMap) {
        String searchKeyword = getString(travelMap.get("searchKeyword"), "");

        if (!Objects.equals(searchKeyword, "")) {
            return travelEntity.travelTitle.contains(searchKeyword).or(travelEntity.travelDescription.contains(searchKeyword));
        } else {
            return null;
        }
    }

    private BooleanExpression searchTravelDate(Map<String, Object> travelMap) {
        LocalDateTime startDateTime = travelMap.get("searchStartTime") != null ? (LocalDateTime) travelMap.get("searchStartTime") : now().minusDays(now().getDayOfMonth() - 1).atStartOfDay();
        LocalDateTime endDateTime = travelMap.get("searchEndTime") != null ? (LocalDateTime) travelMap.get("searchStartTime") : of(now().minusDays(now().getDayOfMonth()).plusMonths(1), LocalTime.of(23, 59, 59));
        return travelEntity.createTime.goe(startDateTime).and(travelEntity.createTime.loe(endDateTime));
    }

    /**
     * <pre>
     * 1. MethodName : findTravelCount
     * 2. ClassName  : TravelRepository.java
     * 3. Comment    : 여행지 소개 리스트 갯수 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 10. 05.
     * </pre>
     */
    public int findTravelCount(Map<String, Object> travelMap) {
        return queryFactory.selectFrom(travelEntity)
                .innerJoin(travelEntity.newTravelCode, commonEntity)
                .fetchJoin()
                .where(searchTravelCode(travelMap), searchTravelInfo(travelMap), searchTravelDate(travelMap))
                .fetch().size();
    }

    /**
     * <pre>
     * 1. MethodName : findTravelList
     * 2. ClassName  : TravelRepository.java
     * 3. Comment    : 여행지 소개 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 10. 05.
     * </pre>
     */
    public List<TravelDTO> findTravelList(Map<String, Object> travelMap) {
        List<TravelEntity> travelList = queryFactory
                .selectFrom(travelEntity)
                .orderBy(travelEntity.idx.desc())
                .innerJoin(travelEntity.newTravelCode, commonEntity)
                .fetchJoin()
                .where(searchTravelCode(travelMap), searchTravelInfo(travelMap), searchTravelDate(travelMap))
                .offset(getInt(travelMap.get("jpaStartPage"), 0))
                .limit(getInt(travelMap.get("size"), 0))
                .fetch();

        return TravelEntity.toDtoList(travelList);
    }

    /**
     * <pre>
     * 1. MethodName : findOneTravel
     * 2. ClassName  : TravelRepository.java
     * 3. Comment    : 여행지 소개 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 10. 05.
     * </pre>
     */
    public TravelDTO findOneTravel(Long idx) {
        // 조회 수 증가
        viewTravel(idx);

        TravelEntity findOneTravel = queryFactory
                .selectFrom(travelEntity)
                .innerJoin(travelEntity.newTravelCode, commonEntity)
                .fetchJoin()
                .leftJoin(travelEntity.travelReviewEntityList, travelReviewEntity)
                .leftJoin(travelEntity.commonImageEntityList, commonImageEntity)
                .fetchJoin()
                .where(travelEntity.idx.eq(idx)
                        .and(travelEntity.visible.eq("Y")))
                .fetchOne();

        assert findOneTravel != null;
        return TravelEntity.toDto(findOneTravel);
    }

    /**
     * <pre>
     * 1. MethodName : findOnePrevTravel
     * 2. ClassName  : TravelRepository.java
     * 3. Comment    : 이전 여행지 소개 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 10. 05.
     * </pre>
     */
    public TravelDTO findOnePrevTravel(Long idx) {
        TravelEntity findOnePrevTravel = queryFactory
                .selectFrom(travelEntity)
                .orderBy(travelEntity.idx.desc())
                .where(travelEntity.idx.lt(idx)
                        .and(travelEntity.visible.eq("Y")))
                .fetchFirst();

        return TravelEntity.toDto(findOnePrevTravel);
    }

    /**
     * <pre>
     * 1. MethodName : findOneNextTravel
     * 2. ClassName  : TravelRepository.java
     * 3. Comment    : 다음 여행지 소개 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 10. 05.
     * </pre>
     */
    public TravelDTO findOneNextTravel(Long idx) {
        TravelEntity findOneNextTravel = queryFactory
                .selectFrom(travelEntity)
                .orderBy(travelEntity.idx.asc())
                .where(travelEntity.idx.gt(idx)
                        .and(travelEntity.visible.eq("Y")))
                .fetchFirst();

        return TravelEntity.toDto(findOneNextTravel);
    }

    /**
     * <pre>
     * 1. MethodName : insertTravel
     * 2. ClassName  : TravelRepository.java
     * 3. Comment    : 관리자 > 여행지 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 10. 05.
     * </pre>
     */
    public TravelDTO insertTravel(TravelEntity travelEntity) {
        em.persist(travelEntity);
        return TravelEntity.toDto(travelEntity);
    }

    /**
     * <pre>
     * 1. MethodName : updateTravel
     * 2. ClassName  : TravelRepository.java
     * 3. Comment    : 관리자 > 여행지 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 10. 05.
     * </pre>
     */
    public TravelDTO updateTravel(TravelEntity travelEntity) {
        em.merge(travelEntity);
        em.flush();
        em.clear();
        return TravelEntity.toDto(travelEntity);
    }

    /**
     * <pre>
     * 1. MethodName : deleteTravel
     * 2. ClassName  : TravelRepository.java
     * 3. Comment    : 관리자 > 여행지 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 10. 05.
     * </pre>
     */
    public Long deleteTravel(Long idx) {
        em.remove(em.find(TravelEntity.class, idx));
        em.flush();
        em.clear();
        return idx;
    }

    /**
     * <pre>
     * 1. MethodName : favoriteTravel
     * 2. ClassName  : TravelRepository.java
     * 3. Comment    : 여행지 좋아요
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 10. 06.
     * </pre>
     */
    public Integer favoriteTravel(Long idx) {
        queryFactory
                .update(travelEntity)
                //add , minus , multiple 다 가능하다.
                .set(travelEntity.favoriteCount, travelEntity.favoriteCount.add(1))
                .where(travelEntity.idx.eq(idx))
                .execute();

        em.flush();
        em.clear();

        return em.find(TravelEntity.class, idx).getFavoriteCount();
    }

    /**
     * <pre>
     * 1. MethodName : viewTravel
     * 2. ClassName  : TravelRepository.java
     * 3. Comment    : 여행지 조회 수 증가
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 10. 06.
     * </pre>
     */
    public void viewTravel(Long idx) {
        queryFactory
                .update(travelEntity)
                //add , minus , multiple 다 가능하다.
                .set(travelEntity.viewCount, travelEntity.viewCount.add(1))
                .where(travelEntity.idx.eq(idx))
                .execute();

        em.flush();
        em.clear();
    }

    /**
     * <pre>
     * 1. MethodName : replyTravel
     * 2. ClassName  : TravelRepository.java
     * 3. Comment    : 여행지 댓글 달기
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 10. 30.
     * </pre>
     */
    public TravelReviewDTO replyTravel(TravelReviewEntity travelReviewEntity) {
        em.persist(travelReviewEntity);
        return TravelReviewEntity.toDto(travelReviewEntity);
    }

    /**
     * <pre>
     * 1. MethodName : updateReplyTravel
     * 2. ClassName  : TravelRepository.java
     * 3. Comment    : 여행지 댓글 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 23.
     * </pre>
     */
    public TravelReviewDTO updateReplyTravel(TravelReviewEntity travelReviewEntity) {
        em.merge(travelReviewEntity);
        em.flush();
        em.clear();
        return TravelReviewEntity.toDto(travelReviewEntity);
    }

    /**
     * <pre>
     * 1. MethodName : deleteReplyTravel
     * 2. ClassName  : TravelRepository.java
     * 3. Comment    : 여행지 댓글 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 23.
     * </pre>
     */
    public Long deleteReplyTravel(Long idx) {
        em.remove(em.find(TravelReviewEntity.class, idx));
        em.flush();
        em.clear();
        return idx;
    }

    /**
     * <pre>
     * 1. MethodName : replyTravelReview
     * 2. ClassName  : TravelRepository.java
     * 3. Comment    : 여행지 댓글 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 23.
     * </pre>
     */
    public List<TravelReviewDTO> replyTravelReview(Long idx) {
        List<TravelReviewEntity> replyTravelReview = queryFactory
                .selectFrom(travelReviewEntity)
                .where(travelReviewEntity.travelIdx.eq(idx)
                        .and(travelReviewEntity.visible.eq("Y")))
                .fetch();

        return TravelReviewEntity.toDtoList(replyTravelReview);
    }

    /**
     * <pre>
     * 1. MethodName : detailReplyTravelReview
     * 2. ClassName  : TravelRepository.java
     * 3. Comment    : 여행지 댓글 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 23.
     * </pre>
     */
    public TravelReviewDTO detailReplyTravelReview(Long idx) {
        TravelReviewEntity detailReplyTravelReview = queryFactory
                .selectFrom(travelReviewEntity)
                .where(travelReviewEntity.idx.eq(idx)
                        .and(travelReviewEntity.visible.eq("Y")))
                .fetchOne();

        assert detailReplyTravelReview != null;
        return TravelReviewEntity.toDto(detailReplyTravelReview);
    }

    /**
     * <pre>
     * 1. MethodName : togglePopular
     * 2. ClassName  : TravelRepository.java
     * 3. Comment    : 인기 여행지 선정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 10. 26.
     * </pre>
     */
    public Boolean togglePopular(Long idx) {
        TravelEntity oneTravel = em.find(TravelEntity.class, idx);
        Boolean popular = !oneTravel.getPopular();

        queryFactory
                .update(travelEntity)
                .where(travelEntity.idx.eq(idx))
                .set(travelEntity.popular, popular)
                .execute();

        em.flush();
        em.clear();

        return popular;
    }

    /**
     * <pre>
     * 1. MethodName : popularityTravel
     * 2. ClassName  : TravelRepository.java
     * 3. Comment    : 인기 여행지 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 10. 14.
     * </pre>
     */
    public List<TravelDTO> popularityTravel(Map<String, Object> travelMap) {
        List<TravelEntity> travelList = queryFactory
                .selectFrom(travelEntity)
                .orderBy(travelEntity.favoriteCount.desc())
                .innerJoin(travelEntity.newTravelCode, commonEntity)
                .fetchJoin()
                .where(searchTravelCode(travelMap), searchTravelInfo(travelMap), searchTravelDate(travelMap)
                        .and(travelEntity.visible.eq("Y")))
                .offset(getInt(travelMap.get("jpaStartPage"), 0))
                .limit(getInt(travelMap.get("size"), 0))
                .fetch();

        travelList.forEach(list -> travelList.get(travelList.indexOf(list))
                .setRowNum(getInt(travelMap.get("startPage"), 1) * (getInt(travelMap.get("size"), 1)) - (2 - travelList.indexOf(list))));

        return TravelEntity.toDtoList(travelList);
    }

    /**
     * <pre>
     * 1. MethodName : findTravelGroupCount
     * 2. ClassName  : TravelRepository.java
     * 3. Comment    : 여행지 그룹 리스트 갯수 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 25.
     * </pre>
     */
    public Integer findTravelGroupCount(Map<String, Object> groupMap) {
        return queryFactory.selectFrom(travelGroupEntity)
                .fetch().size();
    }

    /**
     * <pre>
     * 1. MethodName : findTravelGroupList
     * 2. ClassName  : TravelRepository.java
     * 3. Comment    : 여행지 그룹 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 25.
     * </pre>
     */
    public List<TravelGroupDTO> findTravelGroupList(Map<String, Object> groupMap) {
        List<TravelGroupEntity> travelGroupList = queryFactory
                .selectFrom(travelGroupEntity)
                .orderBy(travelGroupEntity.idx.desc())
                .offset(getInt(groupMap.get("jpaStartPage"), 0))
                .limit(getInt(groupMap.get("size"), 0))
                .fetch();

        travelGroupList.forEach(list -> travelGroupList.get(travelGroupList.indexOf(list))
                .setRowNum(getInt(groupMap.get("startPage"), 1) * (getInt(groupMap.get("size"), 1)) - (2 - travelGroupList.indexOf(list))));

        return TravelGroupEntity.toDtoList(travelGroupList);
    }

    /**
     * <pre>
     * 1. MethodName : findOneTravelGroup
     * 2. ClassName  : TravelRepository.java
     * 3. Comment    : 여행지 그룹 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 25.
     * </pre>
     */
    public TravelGroupDTO findOneTravelGroup(Long idx) {
        TravelGroupEntity travelGroup = queryFactory
                .selectFrom(travelGroupEntity)
                .where(travelGroupEntity.idx.eq(idx))
                .fetchOne();

        assert travelGroup != null;
        return TravelGroupEntity.toDto(travelGroup);
    }

    /**
     * <pre>
     * 1. MethodName : insertTravelGroup
     * 2. ClassName  : TravelRepository.java
     * 3. Comment    : 여행지 그룹 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 25.
     * </pre>
     */
    public TravelGroupDTO insertTravelGroup(TravelGroupEntity travelGroupEntity) {
        em.persist(travelGroupEntity);
        return TravelGroupEntity.toDto(travelGroupEntity);
    }

    /**
     * <pre>
     * 1. MethodName : updateTravelGroup
     * 2. ClassName  : TravelRepository.java
     * 3. Comment    : 여행지 그룹 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 25.
     * </pre>
     */
    public TravelGroupDTO updateTravelGroup(TravelGroupEntity travelGroupEntity) {
        em.merge(travelGroupEntity);
        em.flush();
        em.clear();
        return TravelGroupEntity.toDto(travelGroupEntity);
    }

    /**
     * <pre>
     * 1. MethodName : deleteTravelGroup
     * 2. ClassName  : TravelRepository.java
     * 3. Comment    : 여행지 그룹 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 25.
     * </pre>
     */
    public Long deleteTravelGroup(Long idx) {
        em.remove(em.find(TravelGroupEntity.class, idx));
        em.flush();
        em.clear();
        return idx;
    }

    /**
     * <pre>
     * 1. MethodName : insertTravelGroupUser
     * 2. ClassName  : TravelRepository.java
     * 3. Comment    : 유저 여행지 그룹 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 27.
     * </pre>
     */
    public TravelGroupUserDTO insertTravelGroupUser(TravelGroupUserEntity travelGroupUserEntity) {
        em.persist(travelGroupUserEntity);
        return TravelGroupUserEntity.toDto(travelGroupUserEntity);
    }

    /**
     * <pre>
     * 1. MethodName : deleteTravelGroupUser
     * 2. ClassName  : TravelRepository.java
     * 3. Comment    : 유저 여행지 그룹 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 27.
     * </pre>
     */
    public Long deleteTravelGroupUser(Long idx) {
        em.remove(em.find(TravelGroupUserEntity.class, idx));
        em.flush();
        em.clear();
        return idx;
    }

    /**
     * <pre>
     * 1. MethodName : insertTravelSchedule
     * 2. ClassName  : TravelRepository.java
     * 3. Comment    : 여행지 스케줄 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 12. 13.
     * </pre>
     */
    public TravelScheduleDTO insertTravelSchedule(TravelScheduleEntity travelScheduleEntity) {
        em.persist(travelScheduleEntity);
        return TravelScheduleEntity.toDto(travelScheduleEntity);
    }

    /**
     * <pre>
     * 1. MethodName : updateTravelSchedule
     * 2. ClassName  : TravelRepository.java
     * 3. Comment    : 여행지 스케줄 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 12. 13.
     * </pre>
     */
    public TravelScheduleDTO updateTravelSchedule(TravelScheduleEntity travelScheduleEntity) {
        em.merge(travelScheduleEntity);
        em.flush();
        em.clear();
        return TravelScheduleEntity.toDto(travelScheduleEntity);
    }

    /**
     * <pre>
     * 1. MethodName : deleteTravelSchedule
     * 2. ClassName  : TravelRepository.java
     * 3. Comment    : 여행지 스케줄 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 12. 13.
     * </pre>
     */
    public Long deleteTravelSchedule(Long idx) {
        em.remove(em.find(TravelScheduleEntity.class, idx));
        em.flush();
        em.clear();
        return idx;
    }
}

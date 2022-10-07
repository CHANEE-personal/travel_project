package com.travel.travel_project.api.travel;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travel.travel_project.domain.travel.TravelDTO;
import com.travel.travel_project.domain.travel.TravelEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static com.travel.travel_project.common.StringUtil.getInt;
import static com.travel.travel_project.common.StringUtil.getString;
import static com.travel.travel_project.api.travel.mapper.TravelMapper.INSTANCE;
import static com.travel.travel_project.domain.common.QCommonEntity.commonEntity;
import static com.travel.travel_project.domain.travel.QTravelEntity.travelEntity;
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

        if (searchKeyword == null) {
            return null;
        } else {
            return travelEntity.travelTitle.contains(searchKeyword)
                    .or(travelEntity.travelDescription.contains(searchKeyword));
        }
    }

    private BooleanExpression searchTravelDate(Map<String, Object> travelMap) {
        LocalDateTime searchStartTime = (LocalDateTime) travelMap.get("searchStartTime");
        LocalDateTime searchEndTime = (LocalDateTime) travelMap.get("searchEndTime");

        if (searchStartTime != null && searchEndTime != null) {
            searchStartTime = (LocalDateTime) travelMap.get("searchStartTime");
            searchEndTime = (LocalDateTime) travelMap.get("searchEndTime");
        } else {
            searchStartTime = now().minusDays(now().getDayOfMonth()-1).atStartOfDay();
            searchEndTime = of(now().minusDays(now().getDayOfMonth()).plusMonths(1), LocalTime.of(23,59,59));
        }

        return travelEntity.createTime.between(searchStartTime, searchEndTime);
    }

    /**
     * <pre>
     * 1. MethodName : findTravelCount
     * 2. ClassName  : TravelRepository.java
     * 3. Comment    : 여행지 소개 리스트 갯수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 10. 05.
     * </pre>
     */
    public Integer findTravelCount(Map<String, Object> travelMap) {
        return queryFactory.selectFrom(travelEntity)
                .where(searchTravelCode(travelMap), searchTravelInfo(travelMap), searchTravelDate(travelMap))
                .fetch().size();
    }

    /**
     * <pre>
     * 1. MethodName : findTravelsList
     * 2. ClassName  : TravelRepository.java
     * 3. Comment    : 여행지 소개 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 10. 05.
     * </pre>
     */
    public List<TravelDTO> findTravelsList(Map<String, Object> travelMap) {
        List<TravelEntity> travelList = queryFactory
                .selectFrom(travelEntity)
                .orderBy(travelEntity.idx.desc())
                .leftJoin(travelEntity.newTravelCode, commonEntity)
                .fetchJoin()
                .where(searchTravelCode(travelMap), searchTravelInfo(travelMap), searchTravelDate(travelMap)
                        .and(travelEntity.visible.eq("Y")))
                .offset(getInt(travelMap.get("jpaStartPage"), 0))
                .limit(getInt(travelMap.get("size"), 0))
                .fetch();

        travelList.forEach(list -> travelList.get(travelList.indexOf(list))
                .setRnum(getInt(travelMap.get("startPage"), 1) * (getInt(travelMap.get("size"), 1)) - (2 - travelList.indexOf(list))));

        return INSTANCE.toDtoList(travelList);
    }

    /**
     * <pre>
     * 1. MethodName : findOneTravel
     * 2. ClassName  : TravelRepository.java
     * 3. Comment    : 여행지 소개 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 10. 05.
     * </pre>
     */
    public TravelDTO findOneTravel(Long idx) {
        // 조회 수 증가
        viewTravel(idx);

        TravelEntity findOneTravel = queryFactory
                .selectFrom(travelEntity)
                .orderBy(travelEntity.idx.desc())
                .where(travelEntity.idx.eq(idx)
                        .and(travelEntity.visible.eq("Y")))
                .fetchOne();

        return INSTANCE.toDto(findOneTravel);
    }

    /**
     * <pre>
     * 1. MethodName : findOnePrevTravel
     * 2. ClassName  : TravelRepository.java
     * 3. Comment    : 이전 여행지 소개 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 10. 05.
     * </pre>
     */
    public TravelDTO findOnePrevTravel(Long idx) {
        TravelEntity findOnePrevTravel = queryFactory
                .selectFrom(travelEntity)
                .orderBy(travelEntity.idx.desc())
                .where(travelEntity.idx.lt(idx)
                        .and(travelEntity.visible.eq("Y")))
                .fetchFirst();

        return INSTANCE.toDto(findOnePrevTravel);
    }

    /**
     * <pre>
     * 1. MethodName : findOneNextTravel
     * 2. ClassName  : TravelRepository.java
     * 3. Comment    : 다음 여행지 소개 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 10. 05.
     * </pre>
     */
    public TravelDTO findOneNextTravel(Long idx) {
        TravelEntity findOneNextTravel = queryFactory
                .selectFrom(travelEntity)
                .orderBy(travelEntity.idx.desc())
                .where(travelEntity.idx.gt(idx)
                        .and(travelEntity.visible.eq("Y")))
                .fetchFirst();

        return INSTANCE.toDto(findOneNextTravel);
    }

    /**
     * <pre>
     * 1. MethodName : insertTravel
     * 2. ClassName  : TravelRepository.java
     * 3. Comment    : 관리자 > 여행지 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 10. 05.
     * </pre>
     */
    public TravelDTO insertTravel(TravelEntity travelEntity) {
        em.persist(travelEntity);
        return INSTANCE.toDto(travelEntity);
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
        return INSTANCE.toDto(travelEntity);
    }

    /**
     * <pre>
     * 1. MethodName : deleteTravel
     * 2. ClassName  : TravelRepository.java
     * 3. Comment    : 관리자 > 여행지 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 10. 05.
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
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 10. 06.
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
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 10. 06.
     * </pre>
     */
    public Integer viewTravel(Long idx) {
        queryFactory
                .update(travelEntity)
                //add , minus , multiple 다 가능하다.
                .set(travelEntity.viewCount, travelEntity.viewCount.add(1))
                .where(travelEntity.idx.eq(idx))
                .execute();

        em.flush();
        em.clear();

        return em.find(TravelEntity.class, idx).getViewCount();
    }

    /**
     * <pre>
     * 1. MethodName : replyTravel
     * 2. ClassName  : TravelRepository.java
     * 3. Comment    : 여행지 댓글 달기
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 10. 06.
     * </pre>
     */
    public TravelDTO replyTravel() {
        return null;
    }
}

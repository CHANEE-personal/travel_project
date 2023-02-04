package com.travel.api.travel;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travel.api.travel.domain.TravelDto;
import com.travel.api.travel.domain.TravelEntity;
import com.travel.api.travel.domain.festival.TravelFestivalDto;
import com.travel.api.travel.domain.festival.TravelFestivalEntity;
import com.travel.api.travel.domain.group.TravelGroupDto;
import com.travel.api.travel.domain.group.TravelGroupEntity;
import com.travel.api.travel.domain.recommend.QTravelRecommendEntity;
import com.travel.api.travel.domain.recommend.TravelRecommendDto;
import com.travel.api.travel.domain.recommend.TravelRecommendEntity;
import com.travel.api.travel.domain.search.QSearchEntity;
import com.travel.api.travel.domain.search.SearchDto;
import com.travel.api.travel.domain.search.SearchEntity;
import com.travel.exception.TravelException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.querydsl.core.types.Projections.fields;
import static com.travel.api.common.domain.QCommonEntity.commonEntity;
import static com.travel.api.travel.domain.QTravelEntity.travelEntity;
import static com.travel.api.travel.domain.festival.QTravelFestivalEntity.travelFestivalEntity;
import static com.travel.api.travel.domain.group.QTravelGroupEntity.travelGroupEntity;
import static com.travel.api.travel.domain.image.QTravelImageEntity.travelImageEntity;
import static com.travel.common.StringUtil.getInt;
import static com.travel.common.StringUtil.getString;
import static com.travel.exception.ApiExceptionType.NOT_FOUND_TRAVEL;
import static com.travel.exception.ApiExceptionType.NOT_FOUND_TRAVEL_GROUP;
import static java.time.LocalDate.now;
import static java.time.LocalDateTime.of;
import static java.util.Collections.emptyList;

@Repository
@RequiredArgsConstructor
public class TravelQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    private BooleanExpression searchTravelCode(Map<String, Object> travelMap) {
        return getInt(travelMap.get("searchCode"), 0) != 0 ?
                travelEntity.newTravelCode.commonCode.eq(getInt(travelMap.get("searchCode"), 0)) :
                null;
    }

    private BooleanExpression searchTravelInfo(Map<String, Object> travelMap) {
        String searchKeyword = getString(travelMap.get("searchKeyword"), "");

        // 검색어 저장
        if(!Objects.equals(searchKeyword, "")) {
            em.persist(SearchEntity.builder().searchKeyword(searchKeyword).build());
        }

        return !Objects.equals(searchKeyword, "") ?
                travelEntity.travelTitle.contains(searchKeyword).or(travelEntity.travelDescription.contains(searchKeyword)) :
                null;
    }

    private BooleanExpression searchTravelDate(Map<String, Object> travelMap) {
        LocalDateTime startDateTime = travelMap.get("searchStartTime") != null ? (LocalDateTime) travelMap.get("searchStartTime") : now().minusDays(now().getDayOfMonth() - 1).atStartOfDay();
        LocalDateTime endDateTime = travelMap.get("searchEndTime") != null ? (LocalDateTime) travelMap.get("searchStartTime") : of(now().minusDays(now().getDayOfMonth()).plusMonths(1), LocalTime.of(23, 59, 59));
        return travelEntity.createdAt.goe(startDateTime).and(travelEntity.createdAt.loe(endDateTime));
    }

    private BooleanExpression searchTravelFestival(Integer month, Integer day) {
        return travelFestivalEntity.festivalMonth.eq(month)
                .and(travelFestivalEntity.festivalDay.eq(day));
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
    public Page<TravelDto> findTravelList(Map<String, Object> travelMap, PageRequest pageRequest) {
        List<TravelEntity> travelList = queryFactory
                .selectFrom(travelEntity)
                .orderBy(travelEntity.idx.desc())
                .innerJoin(travelEntity.newTravelCode, commonEntity)
                .fetchJoin()
                .where(searchTravelCode(travelMap), searchTravelInfo(travelMap), searchTravelDate(travelMap))
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        assert travelList != null;
        return new PageImpl<>(TravelEntity.toDtoList(travelList), pageRequest, travelList.size());
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
    public TravelDto findOneTravel(Long idx) {
        // 조회 수 증가
        viewTravel(idx);

        TravelEntity findOneTravel = Optional.ofNullable(queryFactory
                .selectFrom(travelEntity)
                .innerJoin(travelEntity.newTravelCode, commonEntity)
                .fetchJoin()
                .leftJoin(travelEntity.travelImageEntityList, travelImageEntity)
                .fetchJoin()
                .where(travelEntity.idx.eq(idx).and(travelEntity.visible.eq("Y")))
                .fetchOne()).orElseThrow(() -> new TravelException(NOT_FOUND_TRAVEL));

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
    public TravelDto findOnePrevTravel(Long idx) {
        TravelEntity findOnePrevTravel = Optional.ofNullable(queryFactory
                .selectFrom(travelEntity)
                .orderBy(travelEntity.idx.desc())
                .where(travelEntity.idx.lt(idx).and(travelEntity.visible.eq("Y")))
                .fetchFirst()).orElseThrow(() -> new TravelException(NOT_FOUND_TRAVEL));

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
    public TravelDto findOneNextTravel(Long idx) {
        TravelEntity findOneNextTravel = Optional.ofNullable(queryFactory
                .selectFrom(travelEntity)
                .orderBy(travelEntity.idx.asc())
                .where(travelEntity.idx.gt(idx).and(travelEntity.visible.eq("Y")))
                .fetchFirst()).orElseThrow(() -> new TravelException(NOT_FOUND_TRAVEL));

        return TravelEntity.toDto(findOneNextTravel);
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
    public int favoriteTravel(Long idx) {
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
    public Page<TravelDto> popularityTravel(Map<String, Object> travelMap, PageRequest pageRequest) {
        List<TravelEntity> travelList = queryFactory
                .selectFrom(travelEntity)
                .orderBy(travelEntity.favoriteCount.desc())
                .innerJoin(travelEntity.newTravelCode, commonEntity)
                .fetchJoin()
                .where(searchTravelCode(travelMap), searchTravelInfo(travelMap), searchTravelDate(travelMap)
                        .and(travelEntity.visible.eq("Y")))
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        return new PageImpl<>(TravelEntity.toDtoList(travelList), pageRequest, travelList.size());
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
    public Page<TravelGroupDto> findTravelGroupList(Map<String, Object> groupMap, PageRequest pageRequest) {
        List<TravelGroupEntity> travelGroupList = queryFactory
                .selectFrom(travelGroupEntity)
                .orderBy(travelGroupEntity.idx.desc())
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        return new PageImpl<>(TravelGroupEntity.toDtoList(travelGroupList), pageRequest, travelGroupList.size());
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
    public TravelGroupDto findOneTravelGroup(Long idx) {
        TravelGroupEntity travelGroup = Optional.ofNullable(queryFactory
                .selectFrom(travelGroupEntity)
                .where(travelGroupEntity.idx.eq(idx))
                .fetchOne()).orElseThrow(() -> new TravelException(NOT_FOUND_TRAVEL_GROUP));

        return TravelGroupEntity.toDto(travelGroup);
    }

    /**
     * <pre>
     * 1. MethodName : findTravelRecommendList
     * 2. ClassName  : TravelRepository.java
     * 3. Comment    : 여행지 추천 검색어 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 04.
     * </pre>
     */
    public Page<TravelRecommendDto> findTravelRecommendList(Map<String, Object> recommendMap, PageRequest pageRequest) {
        List<TravelRecommendEntity> recommendList = queryFactory
                .selectFrom(QTravelRecommendEntity.travelRecommendEntity)
                .orderBy(QTravelRecommendEntity.travelRecommendEntity.idx.desc())
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        return new PageImpl<>(TravelRecommendEntity.toDtoList(recommendList), pageRequest, recommendList.size());
    }
    /**
     * <pre>
     * 1. MethodName : rankingTravelKeyword
     * 2. ClassName  : TravelRepository.java
     * 3. Comment    : 여행지 검색어 랭킹 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 07.
     * </pre>
     */
    public List<SearchDto> rankingTravelKeyword() {
        List<SearchEntity> keywordList = queryFactory
                .select(fields(SearchEntity.class,
                        QSearchEntity.searchEntity.searchKeyword,
                        QSearchEntity.searchEntity.searchKeyword.count().as("count")))
                .from(QSearchEntity.searchEntity)
                .groupBy(QSearchEntity.searchEntity.searchKeyword)
                .orderBy(QSearchEntity.searchEntity.searchKeyword.count().desc())
                .offset(0)
                .limit(10)
                .fetch();

        return keywordList != null ? SearchEntity.toDtoList(keywordList) : emptyList();
    }

    /**
     * <pre>
     * 1. MethodName : findTravelKeyword
     * 2. ClassName  : TravelRepository.java
     * 3. Comment    : 추천 검색어 or 검색어 랭킹을 통한 여행지 검색
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 07.
     * </pre>
     */
    public List<TravelDto> findTravelKeyword(String searchKeyword) {
        List<TravelEntity> searchTravel = queryFactory
                .selectFrom(travelEntity)
                .innerJoin(travelEntity.newTravelCode, commonEntity)
                .fetchJoin()
                .where(travelEntity.travelTitle.contains(searchKeyword)
                        .or(travelEntity.travelDescription.contains(searchKeyword)))
                .fetch();

        return searchTravel != null ? TravelEntity.toDtoList(searchTravel) : emptyList();
    }

    /**
     * <pre>
     * 1. MethodName : findTravelFestivalGroup
     * 2. ClassName  : TravelRepository.java
     * 3. Comment    : 축제 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 08.
     * </pre>
     */
    public List<TravelFestivalDto> findTravelFestivalGroup(Integer month) {
        List<TravelFestivalEntity> travelGroup = queryFactory
                .select(fields(TravelFestivalEntity.class,
                        travelFestivalEntity.festivalMonth,
                        travelFestivalEntity.festivalDay,
                        travelFestivalEntity.count().as("count")))
                .from(travelFestivalEntity)
                .where(travelFestivalEntity.festivalMonth.eq(month))
                .groupBy(travelFestivalEntity.festivalMonth,
                        travelFestivalEntity.festivalDay)
                .fetch();

        return travelGroup != null ? TravelFestivalEntity.toDtoList(travelGroup) : emptyList();
    }

    /**
     * <pre>
     * 1. MethodName : findTravelFestivalList
     * 2. ClassName  : TravelRepository.java
     * 3. Comment    : 월과 일을 이용한 축제 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 08.
     * </pre>
     */
    public List<TravelFestivalDto> findTravelFestivalList(TravelFestivalEntity existTravelFestivalEntity) {
        List<TravelFestivalEntity> festivalList = queryFactory
                .selectFrom(travelFestivalEntity)
                .orderBy(travelFestivalEntity.idx.desc())
                .where(searchTravelFestival(existTravelFestivalEntity.getFestivalMonth()
                        , existTravelFestivalEntity.getFestivalDay()))
                .fetch();

        return festivalList != null ? TravelFestivalEntity.toDtoList(festivalList) : emptyList();
    }
}

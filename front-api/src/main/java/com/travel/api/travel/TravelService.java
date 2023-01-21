package com.travel.api.travel;

import com.travel.api.travel.domain.TravelDTO;
import com.travel.api.travel.domain.TravelEntity;
import com.travel.api.travel.domain.festival.TravelFestivalDTO;
import com.travel.api.travel.domain.festival.TravelFestivalEntity;
import com.travel.api.travel.domain.festival.repository.FestivalRepository;
import com.travel.api.travel.domain.recommend.TravelRecommendDTO;
import com.travel.api.travel.domain.recommend.TravelRecommendEntity;
import com.travel.api.travel.domain.recommend.repository.RecommendRepository;
import com.travel.api.travel.domain.review.TravelReviewDTO;
import com.travel.api.travel.domain.review.TravelReviewEntity;
import com.travel.api.travel.domain.review.repository.ReviewRepository;
import com.travel.api.travel.domain.search.SearchDTO;
import com.travel.exception.TravelException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.travel.exception.ApiExceptionType.*;

@Service
@RequiredArgsConstructor
public class TravelService {

    private final TravelQueryRepository travelQueryRepository;
    private final TravelRepository travelRepository;
    private final RecommendRepository recommendRepository;
    private final FestivalRepository festivalRepository;
    private final ReviewRepository reviewRepository;

    private TravelEntity oneTravel(Long idx) {
        return travelRepository.findById(idx)
                .orElseThrow(() -> new TravelException(NOT_FOUND_TRAVEL));
    }

    private TravelRecommendEntity oneRecommend(Long idx) {
        return recommendRepository.findById(idx)
                .orElseThrow(() -> new TravelException(NOT_FOUND_TRAVEL_RECOMMEND));
    }

    private TravelFestivalEntity oneFestival(Long idx) {
        return festivalRepository.findById(idx)
                .orElseThrow(() -> new TravelException(NOT_FOUND_FESTIVAL));
    }

    private TravelReviewEntity oneReview(Long idx) {
        return reviewRepository.findById(idx)
                .orElseThrow(() -> new TravelException(NOT_FOUND_TRAVEL_REVIEW));
    }

    /**
     * <pre>
     * 1. MethodName : findTravelList
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 여행지 소개 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 10. 5.
     * </pre>
     */
    @Transactional(readOnly = true)
    public Page<TravelDTO> findTravelList(Map<String, Object> travelMap, PageRequest pageRequest) {
        return travelQueryRepository.findTravelList(travelMap, pageRequest);
    }

    /**
     * <pre>
     * 1. MethodName : findOneTravel
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 여행지 소개 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 10. 5.
     * </pre>
     */
    @Transactional
    public TravelDTO findOneTravel(Long idx) {
        return travelQueryRepository.findOneTravel(idx);
    }

    /**
     * <pre>
     * 1. MethodName : favoriteTravel
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 여행지 좋아요
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 10. 6.
     * </pre>
     */
    @Transactional
    public int favoriteTravel(Long idx) {
        try {
            oneTravel(idx).updateFavoriteCount();
            return oneTravel(idx).getFavoriteCount();
        } catch (Exception e) {
            throw new TravelException(ERROR_FAVORITE_TRAVEL);
        }
    }

    /**
     * <pre>
     * 1. MethodName : popularityTravel
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 인기 여행지 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 10. 14.
     * </pre>
     */
    @Transactional(readOnly = true)
    public Page<TravelDTO> popularityTravel(Map<String, Object> travelMap, PageRequest pageRequest) {
        return travelQueryRepository.popularityTravel(travelMap, pageRequest);
    }

    /**
     * <pre>
     * 1. MethodName : reviewTravel
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 여행지 리뷰 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 10. 30.
     * </pre>
     */
    @Transactional
    public TravelReviewDTO reviewTravel(Long idx, TravelReviewEntity travelReviewEntity) {
        try {
            oneTravel(idx).addReview(travelReviewEntity);
            return TravelReviewEntity.toDto(reviewRepository.save(travelReviewEntity));
        } catch (Exception e) {
            throw new TravelException(ERROR_REVIEW_TRAVEL);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updateReviewTravel
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 여행지 리뷰 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 23.
     * </pre>
     */
    @Transactional
    public TravelReviewDTO updateReviewTravel(Long idx, TravelReviewEntity travelReviewEntity) {
        try {
            oneReview(idx).update(travelReviewEntity);
            return TravelReviewEntity.toDto(travelReviewEntity);
        } catch (Exception e) {
            throw new TravelException(ERROR_UPDATE_REVIEW_TRAVEL);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteReviewTravel
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 여행지 리뷰 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 23.
     * </pre>
     */
    @Transactional
    public Long deleteReviewTravel(Long idx) {
        try {
            reviewRepository.deleteById(idx);
            return idx;
        } catch (Exception e) {
            throw new TravelException(ERROR_DELETE_REVIEW_TRAVEL);
        }
    }

    /**
     * <pre>
     * 1. MethodName : replyTravelReview
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 여행지 리뷰 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 23.
     * </pre>
     */
    @Transactional(readOnly = true)
    public List<TravelReviewDTO> travelReviewList(Long idx) {
        return TravelReviewEntity.toDtoList(reviewRepository.findByNewTravelEntityIdx(idx));
    }

    /**
     * <pre>
     * 1. MethodName : detailReplyTravelReview
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 여행지 리뷰 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 23.
     * </pre>
     */
    @Transactional
    public TravelReviewDTO detailTravelReview(Long idx) {
        return TravelReviewEntity.toDto(oneReview(idx));
    }

    /**
     * <pre>
     * 1. MethodName : findTravelRecommendList
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 여행지 추천 검색어 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 04.
     * </pre>
     */
    @Transactional(readOnly = true)
    public Page<TravelRecommendDTO> findTravelRecommendList(Map<String, Object> recommendMap, PageRequest pageRequest) {
        return travelQueryRepository.findTravelRecommendList(recommendMap, pageRequest);
    }

    /**
     * <pre>
     * 1. MethodName : findOneTravelRecommend
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 여행지 추천 검색어 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 04.
     * </pre>
     */
    @Transactional(readOnly = true)
    public TravelRecommendDTO findOneTravelRecommend(Long idx) {
        return TravelRecommendEntity.toDto(oneRecommend(idx));
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
    @Transactional(readOnly = true)
    public List<SearchDTO> rankingTravelKeyword() {
        return travelQueryRepository.rankingTravelKeyword();
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
    @Transactional(readOnly = true)
    public List<TravelDTO> findTravelKeyword(String searchKeyword) {
        return travelQueryRepository.findTravelKeyword(searchKeyword);
    }

    /**
     * <pre>
     * 1. MethodName : findTravelFestivalGroup
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 축제 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 08.
     * </pre>
     */
    @Transactional(readOnly = true)
    public List<TravelFestivalDTO> findTravelFestivalGroup(Integer month) {
        return travelQueryRepository.findTravelFestivalGroup(month);
    }

    /**
     * <pre>
     * 1. MethodName : findTravelFestivalList
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 월과 일을 이용한 축제 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 08.
     * </pre>
     */
    @Transactional(readOnly = true)
    public List<TravelFestivalDTO> findTravelFestivalList(TravelFestivalEntity travelFestivalEntity) {
        return travelQueryRepository.findTravelFestivalList(travelFestivalEntity);
    }

    /**
     * <pre>
     * 1. MethodName : findOneTravelFestival
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 축제 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 08.
     * </pre>
     */
    @Transactional(readOnly = true)
    public TravelFestivalDTO findOneTravelFestival(Long idx) {
        return TravelFestivalEntity.toDto(oneFestival(idx));
    }
}

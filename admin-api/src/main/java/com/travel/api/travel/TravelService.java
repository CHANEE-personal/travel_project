package com.travel.api.travel;

import com.travel.api.common.domain.CommonEntity;
import com.travel.api.common.domain.repository.CommonRepository;
import com.travel.api.travel.domain.TravelDTO;
import com.travel.api.travel.domain.TravelEntity;
import com.travel.api.travel.domain.festival.TravelFestivalDTO;
import com.travel.api.travel.domain.festival.TravelFestivalEntity;
import com.travel.api.travel.domain.festival.repository.FestivalRepository;
import com.travel.api.travel.domain.group.TravelGroupDTO;
import com.travel.api.travel.domain.group.TravelGroupEntity;
import com.travel.api.travel.domain.group.repository.GroupRepository;
import com.travel.api.travel.domain.image.TravelImageDTO;
import com.travel.api.travel.domain.image.TravelImageEntity;
import com.travel.api.travel.domain.recommend.TravelRecommendDTO;
import com.travel.api.travel.domain.recommend.TravelRecommendEntity;
import com.travel.api.travel.domain.recommend.repository.RecommendRepository;
import com.travel.api.travel.domain.repository.TravelQueryRepository;
import com.travel.api.travel.domain.repository.TravelRepository;
import com.travel.api.travel.domain.review.TravelReviewDTO;
import com.travel.api.travel.domain.review.TravelReviewEntity;
import com.travel.api.travel.domain.review.repository.ReviewRepository;
import com.travel.api.travel.domain.search.SearchDTO;
import com.travel.common.SaveFile;
import com.travel.exception.TravelException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.travel.exception.ApiExceptionType.*;

@Service
@RequiredArgsConstructor
public class TravelService {

    private final TravelQueryRepository travelQueryRepository;
    private final SaveFile saveFile;

    private final CommonRepository commonRepository;
    private final TravelRepository travelRepository;
    private final RecommendRepository recommendRepository;
    private final FestivalRepository festivalRepository;
    private final ReviewRepository reviewRepository;
    private final GroupRepository groupRepository;

    private CommonEntity oneCommon(Integer commonCode) {
        return commonRepository.findByCommonCode(commonCode)
                .orElseThrow(() -> new TravelException(NOT_FOUND_FAQ));
    }

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

    private TravelGroupEntity oneGroup(Long idx) {
        return groupRepository.findById(idx)
                .orElseThrow(() -> new TravelException(NOT_FOUND_TRAVEL_GROUP));
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
     * 1. MethodName : insertTravel
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 여행지 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 10. 5.
     * </pre>
     */
    @Transactional
    public TravelDTO insertTravel(TravelEntity travelEntity) {
        try {
            oneCommon(travelEntity.getNewTravelCode().getCommonCode()).addTravel(travelEntity);
            return TravelEntity.toDto(travelRepository.save(travelEntity));
        } catch (Exception e) {
            throw new TravelException(ERROR_TRAVEL);
        }
    }

    /**
     * <pre>
     * 1. MethodName : insertTravelImage
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 여행지 이미지 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 12. 11.
     * </pre>
     */
    @Transactional
    public List<TravelImageDTO> insertTravelImage(Long idx, List<MultipartFile> files, TravelImageEntity travelImageEntity) {
        try {
            return saveFile.saveTravelFile(oneTravel(idx), files, travelImageEntity);
        } catch (Exception e) {
            throw new TravelException(ERROR_IMAGE);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updateTravel
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 여행지 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 10. 5.
     * </pre>
     */
    @Transactional
    public TravelDTO updateTravel(Long idx, TravelEntity travelEntity) {
        try {
            oneTravel(idx).update(travelEntity);
            return TravelEntity.toDto(travelEntity);
        } catch (Exception e) {
            throw new TravelException(ERROR_UPDATE_TRAVEL);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteTravel
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 여행지 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 10. 5.
     * </pre>
     */
    @Transactional
    public Long deleteTravel(Long idx) {
        try {
            travelRepository.deleteById(idx);
            return idx;
        } catch (Exception e) {
            throw new TravelException(ERROR_DELETE_TRAVEL);
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
     * 1. MethodName : togglePopular
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 인기 여행지 선정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 10. 28.
     * </pre>
     */
    @Transactional
    public Boolean togglePopular(Long idx) {
        try {
            TravelEntity oneTravel = oneTravel(idx);
            Optional.ofNullable(oneTravel)
                    .ifPresent(travelEntity -> travelEntity.togglePopular(oneTravel.getPopular()));

            assert oneTravel != null;
            return oneTravel.getPopular();
        } catch (Exception e) {
            throw new TravelException(ERROR_UPDATE_TRAVEL);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findTravelGroupList
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 여행 그룹 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 25.
     * </pre>
     */
    @Transactional(readOnly = true)
    public Page<TravelGroupDTO> findTravelGroupList(Map<String, Object> groupMap, PageRequest pageRequest) {
        return travelQueryRepository.findTravelGroupList(groupMap, pageRequest);
    }

    /**
     * <pre>
     * 1. MethodName : findOneTravelGroup
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 여행 그룹 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 25.
     * </pre>
     */
    @Transactional(readOnly = true)
    public TravelGroupDTO findOneTravelGroup(Long idx) {
        return travelQueryRepository.findOneTravelGroup(idx);
    }

    /**
     * <pre>
     * 1. MethodName : insertTravelGroup
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 여행 그룹 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 25.
     * </pre>
     */
    @Transactional
    public TravelGroupDTO insertTravelGroup(Long idx, TravelGroupEntity travelGroupEntity) {
        try {
            oneTravel(idx).addGroup(travelGroupEntity);
            return TravelGroupEntity.toDto(groupRepository.save(travelGroupEntity));
        } catch (Exception e) {
            throw new TravelException(ERROR_TRAVEL_GROUP);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updateTravelGroup
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 여행 그룹 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 25.
     * </pre>
     */
    @Transactional
    public TravelGroupDTO updateTravelGroup(Long groupIdx, TravelGroupEntity travelGroupEntity) {
        try {
            oneGroup(groupIdx).update(travelGroupEntity);
            return TravelGroupEntity.toDto(travelGroupEntity);
        } catch (Exception e) {
            throw new TravelException(ERROR_UPDATE_TRAVEL_GROUP);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteTravelGroup
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 여행 그룹 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 25.
     * </pre>
     */
    @Transactional
    public Long deleteTravelGroup(Long idx) {
        try {
            groupRepository.deleteById(idx);
            return idx;
        } catch (Exception e) {
            throw new TravelException(ERROR_DELETE_TRAVEL_GROUP);
        }
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
     * 1. MethodName : insertTravelRecommend
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 여행지 추천 검색어 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 04.
     * </pre>
     */
    @Transactional
    public TravelRecommendDTO insertTravelRecommend(TravelRecommendEntity travelRecommendEntity) {
        try {
            return TravelRecommendEntity.toDto(recommendRepository.save(travelRecommendEntity));
        } catch (Exception e) {
            throw new TravelException(ERROR_TRAVEL_RECOMMEND);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updateTravelRecommend
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 여행지 추천 검색어 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 04.
     * </pre>
     */
    @Transactional
    public TravelRecommendDTO updateTravelRecommend(Long idx, TravelRecommendEntity travelRecommendEntity) {
        try {
            oneRecommend(idx).update(travelRecommendEntity);
            return TravelRecommendEntity.toDto(travelRecommendEntity);
        } catch (Exception e) {
            throw new TravelException(ERROR_UPDATE_TRAVEL_RECOMMEND);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteTravelRecommend
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 여행지 추천 검색어 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 04.
     * </pre>
     */
    @Transactional
    public Long deleteTravelRecommend(Long idx) {
        try {
            recommendRepository.deleteById(idx);
            return idx;
        } catch (Exception e) {
            throw new TravelException(ERROR_DELETE_TRAVEL_RECOMMEND);
        }
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

    /**
     * <pre>
     * 1. MethodName : insertTravelFestival
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 축제 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 08.
     * </pre>
     */
    @Transactional
    public TravelFestivalDTO insertTravelFestival(TravelFestivalEntity travelFestivalEntity) {
        try {
            oneCommon(travelFestivalEntity.getNewFestivalCode().getCommonCode()).addFestival(travelFestivalEntity);
            return TravelFestivalEntity.toDto(festivalRepository.save(travelFestivalEntity));
        } catch (Exception e) {
            throw new TravelException(ERROR_FESTIVAL);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updateTravelFestival
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 축제 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 08.
     * </pre>
     */
    @Transactional
    public TravelFestivalDTO updateTravelFestival(Long idx, TravelFestivalEntity travelFestivalEntity) {
        try {
            oneFestival(idx).update(travelFestivalEntity);
            return TravelFestivalEntity.toDto(travelFestivalEntity);
        } catch (Exception e) {
            throw new TravelException(ERROR_UPDATE_FESTIVAL);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteTravelFestival
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 축제 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 08.
     * </pre>
     */
    @Transactional
    public Long deleteTravelFestival(Long idx) {
        try {
            festivalRepository.deleteById(idx);
            return idx;
        } catch (Exception e) {
            throw new TravelException(ERROR_DELETE_FESTIVAL);
        }
    }
}

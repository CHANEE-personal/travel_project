package com.travel.api.travel;

import com.travel.api.common.domain.CommonEntity;
import com.travel.api.common.domain.repository.CommonRepository;
import com.travel.api.travel.domain.TravelDto;
import com.travel.api.travel.domain.TravelEntity;
import com.travel.api.travel.domain.festival.TravelFestivalDto;
import com.travel.api.travel.domain.festival.TravelFestivalEntity;
import com.travel.api.travel.domain.festival.repository.FestivalRepository;
import com.travel.api.travel.domain.group.TravelGroupDto;
import com.travel.api.travel.domain.group.TravelGroupEntity;
import com.travel.api.travel.domain.group.repository.GroupRepository;
import com.travel.api.travel.domain.image.TravelImageDto;
import com.travel.api.travel.domain.image.TravelImageEntity;
import com.travel.api.travel.domain.recommend.TravelRecommendDto;
import com.travel.api.travel.domain.recommend.TravelRecommendEntity;
import com.travel.api.travel.domain.recommend.repository.RecommendRepository;
import com.travel.api.travel.domain.reservation.TravelReservationDto;
import com.travel.api.travel.domain.reservation.TravelReservationEntity;
import com.travel.api.travel.domain.reservation.repository.ReservationRepository;
import com.travel.api.travel.domain.review.TravelReviewDto;
import com.travel.api.travel.domain.review.TravelReviewEntity;
import com.travel.api.travel.domain.review.repository.ReviewRepository;
import com.travel.api.travel.domain.search.SearchDto;
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
import java.util.stream.Collectors;

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
    private final ReservationRepository reservationRepository;

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

    private TravelReservationEntity oneReservation(Long idx) {
        return reservationRepository.findById(idx)
                .orElseThrow(() -> new TravelException(NOT_FOUND_RESERVATION));
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
    public Page<TravelDto> findTravelList(Map<String, Object> travelMap, PageRequest pageRequest) {
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
    public TravelDto findOneTravel(Long idx) {
        return TravelEntity.toDto(travelRepository.findByIdx(idx)
                .orElseThrow(() -> new TravelException(NOT_FOUND_TRAVEL)));
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOneTravel
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 이전 여행지 소개 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 10. 5.
     * </pre>
     */
    @Transactional
    public TravelDto findPrevOneTravel(Long idx) {
        return TravelEntity.toDto(travelRepository.findPrevByIdx(idx)
                .orElseThrow(() -> new TravelException(NOT_FOUND_TRAVEL)));
    }

    /**
     * <pre>
     * 1. MethodName : findNextOneTravel
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 다음 여행지 소개 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 10. 5.
     * </pre>
     */
    @Transactional
    public TravelDto findNextOneTravel(Long idx) {
        return TravelEntity.toDto(travelRepository.findNextByIdx(idx)
                .orElseThrow(() -> new TravelException(NOT_FOUND_TRAVEL)));
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
    public TravelDto insertTravel(TravelEntity travelEntity) {
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
    public List<TravelImageDto> insertTravelImage(Long idx, List<MultipartFile> files, TravelImageEntity travelImageEntity) {
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
    public TravelDto updateTravel(Long idx, TravelEntity travelEntity) {
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
    public Page<TravelDto> popularityTravel(Map<String, Object> travelMap, PageRequest pageRequest) {
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
    public TravelReviewDto reviewTravel(Long idx, TravelReviewEntity travelReviewEntity) {
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
    public TravelReviewDto updateReviewTravel(Long idx, TravelReviewEntity travelReviewEntity) {
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
    public List<TravelReviewDto> travelReviewList(Long idx) {
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
    public TravelReviewDto detailTravelReview(Long idx) {
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
    public List<TravelGroupDto> findTravelGroupList(Map<String, Object> groupMap, PageRequest pageRequest) {
        return groupRepository.findAll(pageRequest).stream()
                .map(TravelGroupEntity::toDto)
                .collect(Collectors.toList());
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
    public TravelGroupDto findOneTravelGroup(Long idx) {
        return TravelGroupEntity.toDto(oneGroup(idx));
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
    public TravelGroupDto insertTravelGroup(Long idx, TravelGroupEntity travelGroupEntity) {
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
    public TravelGroupDto updateTravelGroup(Long groupIdx, TravelGroupEntity travelGroupEntity) {
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
    public List<TravelRecommendDto> findTravelRecommendList(Map<String, Object> recommendMap, PageRequest pageRequest) {
        return recommendRepository.findAll(pageRequest)
                .stream().map(TravelRecommendEntity::toDto)
                .collect(Collectors.toList());
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
    public TravelRecommendDto findOneTravelRecommend(Long idx) {
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
    public TravelRecommendDto insertTravelRecommend(TravelRecommendEntity travelRecommendEntity) {
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
    public TravelRecommendDto updateTravelRecommend(Long idx, TravelRecommendEntity travelRecommendEntity) {
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
    public List<SearchDto> rankingTravelKeyword() {
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
    public List<TravelFestivalDto> findTravelFestivalGroup(Integer month) {
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
    public List<TravelFestivalDto> findTravelFestivalList(TravelFestivalEntity travelFestivalEntity) {
        return festivalRepository.findFestivalList(travelFestivalEntity.getFestivalMonth(), travelFestivalEntity.getFestivalDay())
                .stream().map(TravelFestivalEntity::toDto).collect(Collectors.toList());
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
    public TravelFestivalDto findOneTravelFestival(Long idx) {
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
    public TravelFestivalDto insertTravelFestival(TravelFestivalEntity travelFestivalEntity) {
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
    public TravelFestivalDto updateTravelFestival(Long idx, TravelFestivalEntity travelFestivalEntity) {
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

    /**
     * <pre>
     * 1. MethodName : findTravelReservationList
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 예약 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 28.
     * </pre>
     */
    public List<TravelReservationDto> findTravelReservationList() {
        return reservationRepository.findAll()
                .stream().map(TravelReservationEntity::toDto).collect(Collectors.toList());
    }

    /**
     * <pre>
     * 1. MethodName : findOneTravelReservation
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 예약 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 28.
     * </pre>
     */
    public TravelReservationDto findOneTravelReservation(Long idx) {
        return TravelReservationEntity.toDto(reservationRepository.findByIdx(idx)
                .orElseThrow(() -> new TravelException(NOT_FOUND_RESERVATION)));
    }

    /**
     * <pre>
     * 1. MethodName : insertTravelReservation
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 예약 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 28.
     * </pre>
     */
    public TravelReservationDto insertTravelReservation(TravelReservationEntity travelReservationEntity) {
        try {
            oneCommon(travelReservationEntity.getCommonEntity().getCommonCode()).addReservation(travelReservationEntity);
            return TravelReservationEntity.toDto(reservationRepository.save(travelReservationEntity));
        } catch (Exception e) {
            throw new TravelException(ERROR_RESERVATION);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updateTravelReservation
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 예약 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 28.
     * </pre>
     */
    public TravelReservationDto updateTravelReservation(Long idx, TravelReservationEntity travelReservationEntity) {
        try {
            oneReservation(idx).update(travelReservationEntity);
            return TravelReservationEntity.toDto(travelReservationEntity);
        } catch (Exception e) {
            throw new TravelException(ERROR_UPDATE_RESERVATION);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteTravelReservation
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 예약 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 28.
     * </pre>
     */
    public Long deleteTravelReservation(Long idx) {
        try {
            reservationRepository.deleteById(idx);
            return idx;
        } catch (Exception e) {
            throw new TravelException(ERROR_DELETE_RESERVATION);
        }
    }
}

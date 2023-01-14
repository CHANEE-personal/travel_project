package com.travel.travel_project.api.travel;

import com.travel.travel_project.api.common.CommonRepository;
import com.travel.travel_project.api.travel.festival.FestivalRepository;
import com.travel.travel_project.api.travel.recommend.RecommendRepository;
import com.travel.travel_project.api.travel.schedule.ScheduleRepository;
import com.travel.travel_project.common.SaveFile;
import com.travel.travel_project.domain.common.CommonEntity;
import com.travel.travel_project.domain.file.CommonImageDTO;
import com.travel.travel_project.domain.file.CommonImageEntity;
import com.travel.travel_project.domain.travel.festival.TravelFestivalDTO;
import com.travel.travel_project.domain.travel.festival.TravelFestivalEntity;
import com.travel.travel_project.domain.travel.group.TravelGroupDTO;
import com.travel.travel_project.domain.travel.group.TravelGroupEntity;
import com.travel.travel_project.domain.travel.group.TravelGroupUserDTO;
import com.travel.travel_project.domain.travel.group.TravelGroupUserEntity;
import com.travel.travel_project.domain.travel.recommend.TravelRecommendDTO;
import com.travel.travel_project.domain.travel.recommend.TravelRecommendEntity;
import com.travel.travel_project.domain.travel.review.TravelReviewDTO;
import com.travel.travel_project.domain.travel.review.TravelReviewEntity;
import com.travel.travel_project.domain.travel.search.SearchDTO;
import com.travel.travel_project.exception.TravelException;
import com.travel.travel_project.domain.travel.TravelDTO;
import com.travel.travel_project.domain.travel.TravelEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.travel.travel_project.exception.ApiExceptionType.*;

@Service
@RequiredArgsConstructor
public class TravelService {

    private final TravelQueryRepository travelQueryRepository;
    private final SaveFile saveFile;

    private final CommonRepository commonRepository;
    private final TravelRepository travelRepository;
    private final RecommendRepository recommendRepository;
    private final FestivalRepository festivalRepository;

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
     * 3. Comment    : 여행지 소개 리스트 조회
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
            oneCommon(travelEntity.getTravelCode()).addTravel(travelEntity);
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
    public List<CommonImageDTO> insertTravelImage(List<MultipartFile> files, CommonImageEntity commonImageEntity) {
        try {
            return saveFile.saveFile(files, commonImageEntity);
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
     * 1. MethodName : replyTravel
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 여행지 댓글 달기
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 10. 30.
     * </pre>
     */
    @Transactional
    public TravelReviewDTO replyTravel(TravelReviewEntity travelReviewEntity) {
        try {
            return travelQueryRepository.replyTravel(travelReviewEntity);
        } catch (Exception e) {
            throw new TravelException(ERROR_REVIEW_TRAVEL);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updateReplyTravel
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 여행지 댓글 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 23.
     * </pre>
     */
    @Transactional
    public TravelReviewDTO updateReplyTravel(TravelReviewEntity travelReviewEntity) {
        try {
            return travelQueryRepository.updateReplyTravel(travelReviewEntity);
        } catch (Exception e) {
            throw new TravelException(ERROR_UPDATE_REVIEW_TRAVEL);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteReplyTravel
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 여행지 댓글 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 23.
     * </pre>
     */
    @Transactional
    public Long deleteReplyTravel(Long idx) {
        try {
            return travelQueryRepository.deleteReplyTravel(idx);
        } catch (Exception e) {
            throw new TravelException(ERROR_DELETE_REVIEW_TRAVEL);
        }
    }

    /**
     * <pre>
     * 1. MethodName : replyTravelReview
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 여행지 댓글 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 23.
     * </pre>
     */
    @Transactional
    public List<TravelReviewDTO> replyTravelReview(Long idx) {
        return travelQueryRepository.replyTravelReview(idx);
    }

    /**
     * <pre>
     * 1. MethodName : detailReplyTravelReview
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 여행지 댓글 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 23.
     * </pre>
     */
    @Transactional
    public TravelReviewDTO detailReplyTravelReview(Long idx) {
        return travelQueryRepository.detailReplyTravelReview(idx);
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
     * 1. MethodName : findTravelGroupCount
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 여행지 그룹 리스트 갯수 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 25.
     * </pre>
     */
    @Transactional
    public int findTravelGroupCount(Map<String, Object> groupMap) {
        try {
            return travelQueryRepository.findTravelGroupCount(groupMap);
        } catch (Exception e) {
            throw new TravelException(NOT_FOUND_TRAVEL_GROUP_LIST);
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
    public List<TravelGroupDTO> findTravelGroupList(Map<String, Object> groupMap) {
        return travelQueryRepository.findTravelGroupList(groupMap);
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
    public TravelGroupDTO insertTravelGroup(TravelGroupEntity travelGroupEntity) {
        try {
            return travelQueryRepository.insertTravelGroup(travelGroupEntity);
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
    public TravelGroupDTO updateTravelGroup(TravelGroupEntity travelGroupEntity) {
        try {
            return travelQueryRepository.updateTravelGroup(travelGroupEntity);
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
            return travelQueryRepository.deleteTravelGroup(idx);
        } catch (Exception e) {
            throw new TravelException(ERROR_DELETE_TRAVEL_GROUP);
        }
    }

    /**
     * <pre>
     * 1. MethodName : insertTravelGroupUser
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 유저 여행 그룹 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 27.
     * </pre>
     */
    @Transactional
    public TravelGroupUserDTO insertTravelGroupUser(TravelGroupUserEntity travelGroupUserEntity) {
        try {
            return travelQueryRepository.insertTravelGroupUser(travelGroupUserEntity);
        } catch (Exception e) {
            throw new TravelException(ERROR_TRAVEL_GROUP_UESR);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteTravelGroupUser
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 유저 여행 그룹 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 27.
     * </pre>
     */
    @Transactional
    public Long deleteTravelGroupUser(Long idx) {
        try {
            return travelQueryRepository.deleteTravelGroupUser(idx);
        } catch (Exception e) {
            throw new TravelException(ERROR_DELETE_TRAVEL_GROUP_USER);
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
    public List<TravelRecommendDTO> findTravelRecommendList(Map<String, Object> recommendMap) {
        return travelQueryRepository.findTravelRecommendList(recommendMap);
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

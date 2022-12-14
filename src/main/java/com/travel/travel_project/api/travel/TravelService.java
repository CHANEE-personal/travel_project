package com.travel.travel_project.api.travel;

import com.travel.travel_project.common.SaveFile;
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
import com.travel.travel_project.domain.travel.schedule.TravelScheduleDTO;
import com.travel.travel_project.domain.travel.schedule.TravelScheduleEntity;
import com.travel.travel_project.domain.travel.search.SearchDTO;
import com.travel.travel_project.exception.TravelException;
import com.travel.travel_project.domain.travel.TravelDTO;
import com.travel.travel_project.domain.travel.TravelEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

import static com.travel.travel_project.exception.ApiExceptionType.*;

@Service
@RequiredArgsConstructor
public class TravelService {

    private final TravelRepository travelRepository;
    private final SaveFile saveFile;

    /**
     * <pre>
     * 1. MethodName : findTravelCount
     * 2. ClassName  : TravelService.java
     * 3. Comment    : ????????? ?????? ????????? ?????? ??????
     * 4. ?????????      : CHO
     * 5. ?????????      : 2022. 10. 5.
     * </pre>
     */
    @Transactional(readOnly = true)
    public int findTravelCount(Map<String, Object> travelMap) {
        return travelRepository.findTravelCount(travelMap);
    }

    /**
     * <pre>
     * 1. MethodName : findTravelList
     * 2. ClassName  : TravelService.java
     * 3. Comment    : ????????? ?????? ????????? ??????
     * 4. ?????????      : CHO
     * 5. ?????????      : 2022. 10. 5.
     * </pre>
     */
    @Cacheable(value = "travel", key = "#travelMap")
    @Transactional(readOnly = true)
    public List<TravelDTO> findTravelList(Map<String, Object> travelMap) {
        return travelRepository.findTravelList(travelMap);
    }

    /**
     * <pre>
     * 1. MethodName : findOneTravel
     * 2. ClassName  : TravelService.java
     * 3. Comment    : ????????? ?????? ????????? ??????
     * 4. ?????????      : CHO
     * 5. ?????????      : 2022. 10. 5.
     * </pre>
     */
    @Cacheable(value = "travel", key = "#idx")
    @Transactional
    public TravelDTO findOneTravel(Long idx) {
        return travelRepository.findOneTravel(idx);
    }

    /**
     * <pre>
     * 1. MethodName : insertTravel
     * 2. ClassName  : TravelService.java
     * 3. Comment    : ????????? ??????
     * 4. ?????????      : CHO
     * 5. ?????????      : 2022. 10. 5.
     * </pre>
     */
    @CachePut("travel")
    @Transactional
    public TravelDTO insertTravel(TravelEntity travelEntity) {
        try {
            return travelRepository.insertTravel(travelEntity);
        } catch (Exception e) {
            throw new TravelException(ERROR_TRAVEL, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : insertTravelImage
     * 2. ClassName  : TravelService.java
     * 3. Comment    : ????????? ????????? ??????
     * 4. ?????????      : CHO
     * 5. ?????????      : 2022. 12. 11.
     * </pre>
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    public List<CommonImageDTO> insertTravelImage(List<MultipartFile> files, CommonImageEntity commonImageEntity) {
        try {
            return saveFile.saveFile(files, commonImageEntity);
        } catch (Exception e) {
            throw new TravelException(ERROR_IMAGE, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updateTravel
     * 2. ClassName  : TravelService.java
     * 3. Comment    : ????????? ??????
     * 4. ?????????      : CHO
     * 5. ?????????      : 2022. 10. 5.
     * </pre>
     */
    @CachePut(value = "travel", key = "#travelEntity.idx")
    @Transactional
    public TravelDTO updateTravel(TravelEntity travelEntity) {
        try {
            return travelRepository.updateTravel(travelEntity);
        } catch (Exception e) {
            throw new TravelException(ERROR_UPDATE_TRAVEL, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteTravel
     * 2. ClassName  : TravelService.java
     * 3. Comment    : ????????? ??????
     * 4. ?????????      : CHO
     * 5. ?????????      : 2022. 10. 5.
     * </pre>
     */
    @CacheEvict(value = "travel", key = "#idx")
    @Transactional
    public Long deleteTravel(Long idx) {
        try {
            return travelRepository.deleteTravel(idx);
        } catch (Exception e) {
            throw new TravelException(ERROR_DELETE_TRAVEL, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : favoriteTravel
     * 2. ClassName  : TravelService.java
     * 3. Comment    : ????????? ?????????
     * 4. ?????????      : CHO
     * 5. ?????????      : 2022. 10. 6.
     * </pre>
     */
    @CachePut(value = "travel", key = "#idx")
    @Transactional
    public int favoriteTravel(Long idx) {
        try {
            return travelRepository.favoriteTravel(idx);
        } catch (Exception e) {
            throw new TravelException(ERROR_FAVORITE_TRAVEL, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : popularityTravel
     * 2. ClassName  : TravelService.java
     * 3. Comment    : ?????? ????????? ????????? ??????
     * 4. ?????????      : CHO
     * 5. ?????????      : 2022. 10. 14.
     * </pre>
     */
    @Cacheable(value = "travel", key = "#travelMap")
    @Transactional(readOnly = true)
    public List<TravelDTO> popularityTravel(Map<String, Object> travelMap) {
        return travelRepository.popularityTravel(travelMap);
    }

    /**
     * <pre>
     * 1. MethodName : replyTravel
     * 2. ClassName  : TravelService.java
     * 3. Comment    : ????????? ?????? ??????
     * 4. ?????????      : CHO
     * 5. ?????????      : 2022. 10. 30.
     * </pre>
     */
    @CachePut(value = "reply")
    @Transactional
    public TravelReviewDTO replyTravel(TravelReviewEntity travelReviewEntity) {
        try {
            return travelRepository.replyTravel(travelReviewEntity);
        } catch (Exception e) {
            throw new TravelException(ERROR_REVIEW_TRAVEL, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updateReplyTravel
     * 2. ClassName  : TravelService.java
     * 3. Comment    : ????????? ?????? ??????
     * 4. ?????????      : CHO
     * 5. ?????????      : 2022. 11. 23.
     * </pre>
     */
    @CachePut(value = "reply", key = "#travelReviewEntity.idx")
    @Transactional
    public TravelReviewDTO updateReplyTravel(TravelReviewEntity travelReviewEntity) {
        try {
            return travelRepository.updateReplyTravel(travelReviewEntity);
        } catch (Exception e) {
            throw new TravelException(ERROR_UPDATE_REVIEW_TRAVEL, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteReplyTravel
     * 2. ClassName  : TravelService.java
     * 3. Comment    : ????????? ?????? ??????
     * 4. ?????????      : CHO
     * 5. ?????????      : 2022. 11. 23.
     * </pre>
     */
    @CacheEvict(value = "reply", key = "#idx")
    @Transactional
    public Long deleteReplyTravel(Long idx) {
        try {
            return travelRepository.deleteReplyTravel(idx);
        } catch (Exception e) {
            throw new TravelException(ERROR_DELETE_REVIEW_TRAVEL, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : replyTravelReview
     * 2. ClassName  : TravelService.java
     * 3. Comment    : ????????? ?????? ????????? ??????
     * 4. ?????????      : CHO
     * 5. ?????????      : 2022. 11. 23.
     * </pre>
     */
    @Transactional
    public List<TravelReviewDTO> replyTravelReview(Long idx) {
        return travelRepository.replyTravelReview(idx);
    }

    /**
     * <pre>
     * 1. MethodName : detailReplyTravelReview
     * 2. ClassName  : TravelService.java
     * 3. Comment    : ????????? ?????? ?????? ??????
     * 4. ?????????      : CHO
     * 5. ?????????      : 2022. 11. 23.
     * </pre>
     */
    @Cacheable(value = "reply", key = "#idx")
    @Transactional
    public TravelReviewDTO detailReplyTravelReview(Long idx) {
        return travelRepository.detailReplyTravelReview(idx);
    }

    /**
     * <pre>
     * 1. MethodName : togglePopular
     * 2. ClassName  : TravelService.java
     * 3. Comment    : ?????? ????????? ??????
     * 4. ?????????      : CHO
     * 5. ?????????      : 2022. 10. 28.
     * </pre>
     */
    @CachePut(value = "travel", key = "#idx")
    @Transactional
    public Boolean togglePopular(Long idx) {
        try {
            return travelRepository.togglePopular(idx);
        } catch (Exception e) {
            throw new TravelException(ERROR_UPDATE_TRAVEL, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findTravelGroupCount
     * 2. ClassName  : TravelService.java
     * 3. Comment    : ????????? ?????? ????????? ?????? ??????
     * 4. ?????????      : CHO
     * 5. ?????????      : 2022. 11. 25.
     * </pre>
     */
    @Transactional
    public int findTravelGroupCount(Map<String, Object> groupMap) {
        try {
            return travelRepository.findTravelGroupCount(groupMap);
        } catch (Exception e) {
            throw new TravelException(NOT_FOUND_TRAVEL_GROUP_LIST, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findTravelGroupList
     * 2. ClassName  : TravelService.java
     * 3. Comment    : ?????? ?????? ????????? ??????
     * 4. ?????????      : CHO
     * 5. ?????????      : 2022. 11. 25.
     * </pre>
     */
    @Cacheable(value = "group", key = "#groupMap")
    @Transactional(readOnly = true)
    public List<TravelGroupDTO> findTravelGroupList(Map<String, Object> groupMap) {
        return travelRepository.findTravelGroupList(groupMap);
    }

    /**
     * <pre>
     * 1. MethodName : findOneTravelGroup
     * 2. ClassName  : TravelService.java
     * 3. Comment    : ?????? ?????? ?????? ??????
     * 4. ?????????      : CHO
     * 5. ?????????      : 2022. 11. 25.
     * </pre>
     */
    @Cacheable("group")
    @Transactional(readOnly = true)
    public TravelGroupDTO findOneTravelGroup(Long idx) {
        return travelRepository.findOneTravelGroup(idx);
    }

    /**
     * <pre>
     * 1. MethodName : insertTravelGroup
     * 2. ClassName  : TravelService.java
     * 3. Comment    : ?????? ?????? ??????
     * 4. ?????????      : CHO
     * 5. ?????????      : 2022. 11. 25.
     * </pre>
     */
    @CachePut("group")
    @Transactional
    public TravelGroupDTO insertTravelGroup(TravelGroupEntity travelGroupEntity) {
        try {
            return travelRepository.insertTravelGroup(travelGroupEntity);
        } catch (Exception e) {
            throw new TravelException(ERROR_TRAVEL_GROUP, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updateTravelGroup
     * 2. ClassName  : TravelService.java
     * 3. Comment    : ?????? ?????? ??????
     * 4. ?????????      : CHO
     * 5. ?????????      : 2022. 11. 25.
     * </pre>
     */
    @CachePut(value = "group", key = "#travelGroupEntity.idx")
    @Transactional
    public TravelGroupDTO updateTravelGroup(TravelGroupEntity travelGroupEntity) {
        try {
            return travelRepository.updateTravelGroup(travelGroupEntity);
        } catch (Exception e) {
            throw new TravelException(ERROR_UPDATE_TRAVEL_GROUP, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteTravelGroup
     * 2. ClassName  : TravelService.java
     * 3. Comment    : ?????? ?????? ??????
     * 4. ?????????      : CHO
     * 5. ?????????      : 2022. 11. 25.
     * </pre>
     */
    @CacheEvict(value = "group", key = "#idx")
    @Transactional
    public Long deleteTravelGroup(Long idx) {
        try {
            return travelRepository.deleteTravelGroup(idx);
        } catch (Exception e) {
            throw new TravelException(ERROR_DELETE_TRAVEL_GROUP, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : insertTravelGroupUser
     * 2. ClassName  : TravelService.java
     * 3. Comment    : ?????? ?????? ?????? ??????
     * 4. ?????????      : CHO
     * 5. ?????????      : 2022. 11. 27.
     * </pre>
     */
    @CachePut("group_user")
    @Transactional
    public TravelGroupUserDTO insertTravelGroupUser(TravelGroupUserEntity travelGroupUserEntity) {
        try {
            return travelRepository.insertTravelGroupUser(travelGroupUserEntity);
        } catch (Exception e) {
            throw new TravelException(ERROR_TRAVEL_GROUP_UESR, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteTravelGroupUser
     * 2. ClassName  : TravelService.java
     * 3. Comment    : ?????? ?????? ?????? ??????
     * 4. ?????????      : CHO
     * 5. ?????????      : 2022. 11. 27.
     * </pre>
     */
    @CacheEvict(value = "group_user", key = "#idx")
    @Transactional
    public Long deleteTravelGroupUser(Long idx) {
        try {
            return travelRepository.deleteTravelGroupUser(idx);
        } catch (Exception e) {
            throw new TravelException(ERROR_DELETE_TRAVEL_GROUP_USER, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : insertTravelSchedule
     * 2. ClassName  : TravelService.java
     * 3. Comment    : ?????? ?????? ????????? ??????
     * 4. ?????????      : CHO
     * 5. ?????????      : 2022. 12. 13.
     * </pre>
     */
    @CachePut("schedule")
    @Transactional
    public TravelScheduleDTO insertTravelSchedule(TravelScheduleEntity travelScheduleEntity) {
        try {
            return travelRepository.insertTravelSchedule(travelScheduleEntity);
        } catch (Exception e) {
            throw new TravelException(ERROR_TRAVEL_SCHEDULE, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updateTravelSchedule
     * 2. ClassName  : TravelService.java
     * 3. Comment    : ?????? ?????? ????????? ??????
     * 4. ?????????      : CHO
     * 5. ?????????      : 2022. 12. 13.
     * </pre>
     */
    @CachePut(value = "schedule", key = "#travelScheduleEntity.idx")
    @Transactional
    public TravelScheduleDTO updateTravelSchedule(TravelScheduleEntity travelScheduleEntity) {
        try {
            return travelRepository.updateTravelSchedule(travelScheduleEntity);
        } catch (Exception e) {
            throw new TravelException(ERROR_UPDATE_TRAVEL_SCHEDULE, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteTravelSchedule
     * 2. ClassName  : TravelService.java
     * 3. Comment    : ?????? ?????? ????????? ??????
     * 4. ?????????      : CHO
     * 5. ?????????      : 2022. 12. 13.
     * </pre>
     */
    @CacheEvict(value = "schedule", key = "#idx")
    @Transactional
    public Long deleteTravelSchedule(Long idx) {
        try {
            return travelRepository.deleteTravelSchedule(idx);
        } catch (Exception e) {
            throw new TravelException(ERROR_DELETE_TRAVEL_SCHEDULE, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findTravelRecommendList
     * 2. ClassName  : TravelService.java
     * 3. Comment    : ????????? ?????? ????????? ????????? ??????
     * 4. ?????????      : CHO
     * 5. ?????????      : 2023. 01. 04.
     * </pre>
     */
    @Cacheable(value = "recommend")
    @Transactional(readOnly = true)
    public List<TravelRecommendDTO> findTravelRecommendList(Map<String, Object> recommendMap) {
        return travelRepository.findTravelRecommendList(recommendMap);
    }

    /**
     * <pre>
     * 1. MethodName : findOneTravelRecommend
     * 2. ClassName  : TravelService.java
     * 3. Comment    : ????????? ?????? ????????? ?????? ??????
     * 4. ?????????      : CHO
     * 5. ?????????      : 2023. 01. 04.
     * </pre>
     */
    @Cacheable(value = "recommend",key = "#idx")
    @Transactional(readOnly = true)
    public TravelRecommendDTO findOneTravelRecommend(Long idx) {
        return travelRepository.findOneTravelRecommend(idx);
    }

    /**
     * <pre>
     * 1. MethodName : insertTravelRecommend
     * 2. ClassName  : TravelService.java
     * 3. Comment    : ????????? ?????? ????????? ??????
     * 4. ?????????      : CHO
     * 5. ?????????      : 2023. 01. 04.
     * </pre>
     */
    @CachePut(value = "recommend")
    @Transactional
    public TravelRecommendDTO insertTravelRecommend(TravelRecommendEntity travelRecommendEntity) {
        try {
            return travelRepository.changeTravelRecommend(travelRecommendEntity);
        } catch (Exception e) {
            throw new TravelException(ERROR_TRAVEL_RECOMMEND, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updateTravelRecommend
     * 2. ClassName  : TravelService.java
     * 3. Comment    : ????????? ?????? ????????? ??????
     * 4. ?????????      : CHO
     * 5. ?????????      : 2023. 01. 04.
     * </pre>
     */
    @CachePut(value = "recommend", key = "#travelRecommendEntity.idx")
    @Transactional
    public TravelRecommendDTO updateTravelRecommend(TravelRecommendEntity travelRecommendEntity) {
        try {
            return travelRepository.changeTravelRecommend(travelRecommendEntity);
        } catch (Exception e) {
            throw new TravelException(ERROR_UPDATE_TRAVEL_RECOMMEND, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteTravelRecommend
     * 2. ClassName  : TravelService.java
     * 3. Comment    : ????????? ?????? ????????? ??????
     * 4. ?????????      : CHO
     * 5. ?????????      : 2023. 01. 04.
     * </pre>
     */
    @CacheEvict(value = "recommend", key = "#idx")
    @Transactional
    public Long deleteTravelRecommend(Long idx) {
        try {
            return travelRepository.deleteTravelRecommend(idx);
        } catch (Exception e) {
            throw new TravelException(ERROR_DELETE_TRAVEL_RECOMMEND, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : rankingTravelKeyword
     * 2. ClassName  : TravelRepository.java
     * 3. Comment    : ????????? ????????? ?????? ????????? ??????
     * 4. ?????????      : CHO
     * 5. ?????????      : 2023. 01. 07.
     * </pre>
     */
    @Cacheable(value = "rankKeyword")
    @Transactional(readOnly = true)
    public List<SearchDTO> rankingTravelKeyword() {
        return travelRepository.rankingTravelKeyword();
    }

    /**
     * <pre>
     * 1. MethodName : findTravelKeyword
     * 2. ClassName  : TravelRepository.java
     * 3. Comment    : ?????? ????????? or ????????? ????????? ?????? ????????? ??????
     * 4. ?????????      : CHO
     * 5. ?????????      : 2023. 01. 07.
     * </pre>
     */
    @Cacheable(value = "searchKeyword", key = "#searchKeyword")
    @Transactional(readOnly = true)
    public List<TravelDTO> findTravelKeyword(String searchKeyword) {
        return travelRepository.findTravelKeyword(searchKeyword);
    }

    /**
     * <pre>
     * 1. MethodName : findTravelFestivalGroup
     * 2. ClassName  : TravelService.java
     * 3. Comment    : ?????? ????????? ??????
     * 4. ?????????      : CHO
     * 5. ?????????      : 2023. 01. 08.
     * </pre>
     */
    @Cacheable(value = "festival", key = "#month")
    @Transactional(readOnly = true)
    public List<TravelFestivalDTO> findTravelFestivalGroup(Integer month) {
        return travelRepository.findTravelFestivalGroup(month);
    }

    /**
     * <pre>
     * 1. MethodName : findTravelFestivalList
     * 2. ClassName  : TravelService.java
     * 3. Comment    : ?????? ?????? ????????? ?????? ????????? ??????
     * 4. ?????????      : CHO
     * 5. ?????????      : 2023. 01. 08.
     * </pre>
     */
    @Cacheable(value = "festival", key = "#travelFestivalEntity")
    @Transactional(readOnly = true)
    public List<TravelFestivalDTO> findTravelFestivalList(TravelFestivalEntity travelFestivalEntity) {
        return travelRepository.findTravelFestivalList(travelFestivalEntity);
    }

    /**
     * <pre>
     * 1. MethodName : findOneTravelFestival
     * 2. ClassName  : TravelService.java
     * 3. Comment    : ?????? ?????? ??????
     * 4. ?????????      : CHO
     * 5. ?????????      : 2023. 01. 08.
     * </pre>
     */
    @Cacheable(value = "festival", key = "#idx")
    @Transactional(readOnly = true)
    public TravelFestivalDTO findOneTravelFestival(Long idx) {
        return travelRepository.findOneTravelFestival(idx);
    }

    /**
     * <pre>
     * 1. MethodName : insertTravelFestival
     * 2. ClassName  : TravelService.java
     * 3. Comment    : ?????? ??????
     * 4. ?????????      : CHO
     * 5. ?????????      : 2023. 01. 08.
     * </pre>
     */
    @CachePut(value = "festival")
    @Transactional
    public TravelFestivalDTO insertTravelFestival(TravelFestivalEntity travelFestivalEntity) {
        try {
            return travelRepository.changeTravelFestival(travelFestivalEntity);
        } catch (Exception e) {
            throw new TravelException(ERROR_FESTIVAL, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updateTravelFestival
     * 2. ClassName  : TravelService.java
     * 3. Comment    : ?????? ??????
     * 4. ?????????      : CHO
     * 5. ?????????      : 2023. 01. 08.
     * </pre>
     */
    @CachePut(value = "festival", key = "#travelFestivalEntity.idx")
    @Transactional
    public TravelFestivalDTO updateTravelFestival(TravelFestivalEntity travelFestivalEntity) {
        try {
            return travelRepository.changeTravelFestival(travelFestivalEntity);
        } catch (Exception e) {
            throw new TravelException(ERROR_UPDATE_FESTIVAL, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteTravelFestival
     * 2. ClassName  : TravelService.java
     * 3. Comment    : ?????? ??????
     * 4. ?????????      : CHO
     * 5. ?????????      : 2023. 01. 08.
     * </pre>
     */
    @CacheEvict(value = "festival", key = "#idx")
    @Transactional
    public Long deleteTravelFestival(Long idx) {
        try {
            return travelRepository.deleteTravelFestival(idx);
        } catch (Exception e) {
            throw new TravelException(ERROR_DELETE_FESTIVAL, e);
        }
    }
}

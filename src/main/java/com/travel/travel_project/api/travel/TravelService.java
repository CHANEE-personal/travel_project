package com.travel.travel_project.api.travel;

import com.travel.travel_project.domain.travel.review.TravelReviewDTO;
import com.travel.travel_project.domain.travel.review.TravelReviewEntity;
import com.travel.travel_project.exception.TravelException;
import com.travel.travel_project.domain.travel.TravelDTO;
import com.travel.travel_project.domain.travel.TravelEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.travel.travel_project.exception.ApiExceptionType.*;

@Service
@RequiredArgsConstructor
public class TravelService {

    private final TravelRepository travelRepository;

    /**
     * <pre>
     * 1. MethodName : findTravelCount
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 여행지 소개 리스트 갯수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 10. 5.
     * </pre>
     */
    @Transactional(readOnly = true)
    public int findTravelCount(Map<String, Object> travelMap) throws TravelException {
        try {
            return travelRepository.findTravelCount(travelMap);
        } catch (Exception e) {
            throw new TravelException(NOT_FOUND_TRAVEL_LIST, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findTravelsList
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 여행지 소개 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 10. 5.
     * </pre>
     */
    @Transactional(readOnly = true)
    public List<TravelDTO> findTravelsList(Map<String, Object> travelMap) throws TravelException {
        try {
            return travelRepository.findTravelsList(travelMap);
        } catch (Exception e) {
            throw new TravelException(NOT_FOUND_TRAVEL_LIST, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findOneTravel
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 여행지 소개 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 10. 5.
     * </pre>
     */
    @Transactional(readOnly = true)
    public TravelDTO findOneTravel(Long idx) throws TravelException {
        try {
            return travelRepository.findOneTravel(idx);
        } catch (Exception e) {
            throw new TravelException(NOT_FOUND_TRAVEL, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : insertTravel
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 관리자 > 여행지 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 10. 5.
     * </pre>
     */
    @CachePut("travel")
    @Modifying(clearAutomatically = true)
    @Transactional
    public TravelDTO insertTravel(TravelEntity adminTravelEntity) throws TravelException {
        try {
            return travelRepository.insertTravel(adminTravelEntity);
        } catch (Exception e) {
            throw new TravelException(ERROR_TRAVEL, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updateTravel
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 관리자 > 여행지 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 10. 5.
     * </pre>
     */
    @CachePut("travel")
    @Modifying(clearAutomatically = true)
    @Transactional
    public TravelDTO updateTravel(TravelEntity adminTravelEntity) throws TravelException {
        try {
            return travelRepository.updateTravel(adminTravelEntity);
        } catch (Exception e) {
            throw new TravelException(ERROR_UPDATE_TRAVEL, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteTravel
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 관리자 > 여행지 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 10. 5.
     * </pre>
     */
    @CacheEvict("travel")
    @Modifying(clearAutomatically = true)
    @Transactional
    public Long deleteTravel(Long idx) throws TravelException {
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
     * 3. Comment    : 여행지 좋아요
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 10. 6.
     * </pre>
     */
    @CachePut("travel")
    @Modifying(clearAutomatically = true)
    @Transactional
    public Integer favoriteTravel(Long idx) throws TravelException {
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
     * 3. Comment    : 인기 여행지 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 10. 14.
     * </pre>
     */
    @Transactional(readOnly = true)
    public List<TravelDTO> popularityTravel(Map<String, Object> travelMap) throws TravelException {
        try {
            return travelRepository.popularityTravel(travelMap);
        } catch (Exception e) {
            throw new TravelException(NOT_FOUND_TRAVEL_LIST, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : replyTravel
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 여행지 댓글 달기
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 10. 30.
     * </pre>
     */
    @CachePut("travel")
    @Modifying(clearAutomatically = true)
    @Transactional
    public TravelReviewDTO replyTravel(TravelReviewEntity travelReviewEntity) throws TravelException {
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
     * 3. Comment    : 여행지 댓글 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 11. 23.
     * </pre>
     */
    @CachePut("travel")
    @Modifying(clearAutomatically = true)
    @Transactional
    public TravelReviewDTO updateReplyTravel(TravelReviewEntity travelReviewEntity) throws TravelException {
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
     * 3. Comment    : 여행지 댓글 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 11. 23.
     * </pre>
     */
    @CacheEvict("travel")
    @Modifying(clearAutomatically = true)
    @Transactional
    public Long deleteReplyTravel(Long idx) throws TravelException {
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
     * 3. Comment    : 여행지 댓글 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 11. 23.
     * </pre>
     */
    @Transactional
    public List<TravelReviewDTO> replyTravelReview(Long idx) throws TravelException {
        try {
            return travelRepository.replyTravelReview(idx);
        } catch (Exception e) {
            throw new TravelException(NOT_FOUND_TRAVEL_REVIEW_LIST, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : detailReplyTravelReview
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 여행지 댓글 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 11. 23.
     * </pre>
     */
    @Transactional
    public TravelReviewDTO detailReplyTravelReview(Long idx) throws TravelException {
        try {
            return travelRepository.detailReplyTravelReview(idx);
        } catch (Exception e) {
            throw new TravelException(NOT_FOUND_TRAVEL_REVIEW, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : togglePopular
     * 2. ClassName  : TravelService.java
     * 3. Comment    : 인기 여행지 선정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 10. 28.
     * </pre>
     */
    @CachePut("travel")
    @Modifying(clearAutomatically = true)
    @Transactional
    public TravelDTO togglePopular(Long idx) throws TravelException {
        try {
            return travelRepository.togglePopular(idx);
        } catch (Exception e) {
            throw new TravelException(ERROR_UPDATE_TRAVEL, e);
        }
    }
}

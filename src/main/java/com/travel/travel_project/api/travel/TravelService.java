package com.travel.travel_project.api.travel;

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

    private final TravelRepository adminTravelRepository;

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
            return adminTravelRepository.findTravelCount(travelMap);
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
            return adminTravelRepository.findTravelsList(travelMap);
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
            return adminTravelRepository.findOneTravel(idx);
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
            return adminTravelRepository.insertTravel(adminTravelEntity);
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
            return adminTravelRepository.updateTravel(adminTravelEntity);
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
            return adminTravelRepository.deleteTravel(idx);
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
            return adminTravelRepository.favoriteTravel(idx);
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
            return adminTravelRepository.popularityTravel(travelMap);
        } catch (Exception e) {
            throw new TravelException(NOT_FOUND_TRAVEL_LIST, e);
        }
    }
}

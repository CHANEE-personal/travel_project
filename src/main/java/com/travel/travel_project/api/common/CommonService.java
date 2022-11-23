package com.travel.travel_project.api.common;

import com.travel.travel_project.domain.common.CommonDTO;
import com.travel.travel_project.domain.common.CommonEntity;
import com.travel.travel_project.exception.TravelException;
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
public class CommonService {

    private final CommonRepository commonRepository;

    /**
     * <pre>
     * 1. MethodName : findCommonCount
     * 2. ClassName  : CommonService.java
     * 3. Comment    : 공통 코드 리스트 갯수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 11. 21.
     * </pre>
     */
    @Transactional(readOnly = true)
    public Integer findCommonCount(Map<String, Object> commonMap) {
        try {
            return commonRepository.findCommonCount(commonMap);
        } catch (Exception e) {
            throw new TravelException(NOT_FOUND_COMMON_LIST, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findCommonList
     * 2. ClassName  : CommonService.java
     * 3. Comment    : 공통 코드 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 11. 21.
     * </pre>
     */
    @Transactional(readOnly = true)
    public List<CommonDTO> findCommonList(Map<String, Object> commonMap) {
        try {
            return commonRepository.findCommonList(commonMap);
        } catch (Exception e) {
            throw new TravelException(NOT_FOUND_COMMON_LIST, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findOneCommon
     * 2. ClassName  : CommonService.java
     * 3. Comment    : 공통 코드 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 11. 21.
     * </pre>
     */
    @Transactional(readOnly = true)
    public CommonDTO findOneCommon(Long idx) {
        try {
            return commonRepository.findOneCommon(idx);
        } catch (Exception e) {
            throw new TravelException(NOT_FOUND_COMMON, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : insertCommonCode
     * 2. ClassName  : CommonService.java
     * 3. Comment    : 공통 코드 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 11. 21.
     * </pre>
     */
    @CachePut("common")
    @Modifying(clearAutomatically = true)
    @Transactional
    public CommonDTO insertCommonCode(CommonEntity existCommonEntity) {
        try {
            return commonRepository.insertCommonCode(existCommonEntity);
        } catch (Exception e) {
            throw new TravelException(ERROR_COMMON, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updateCommonCode
     * 2. ClassName  : CommonService.java
     * 3. Comment    : 공통 코드 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 11. 21.
     * </pre>
     */
    @CachePut("common")
    @Modifying(clearAutomatically = true)
    @Transactional
    public CommonDTO updateCommonCode(CommonEntity existCommonEntity) {
        try {
            return commonRepository.updateCommonCode(existCommonEntity);
        } catch (Exception e) {
            throw new TravelException(ERROR_UPDATE_COMMON, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteCommonCode
     * 2. ClassName  : CommonService.java
     * 3. Comment    : 공통 코드 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 11. 21.
     * </pre>
     */
    @CacheEvict("common")
    @Modifying(clearAutomatically = true)
    @Transactional
    public Long deleteCommonCode(Long idx) {
        try {
            return commonRepository.deleteCommonCode(idx);
        } catch (Exception e) {
            throw new TravelException(ERROR_DELETE_COMMON, e);
        }
    }
}

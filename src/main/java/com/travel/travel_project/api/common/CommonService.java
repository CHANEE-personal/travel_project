package com.travel.travel_project.api.common;

import com.travel.travel_project.domain.common.CommonDTO;
import com.travel.travel_project.domain.common.CommonEntity;
import com.travel.travel_project.exception.TravelException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.travel.travel_project.exception.ApiExceptionType.*;

@Service
@RequiredArgsConstructor
public class CommonService {

    private final CommonQueryRepository commonQueryRepository;
    private final CommonRepository commonRepository;

    private CommonEntity oneCommon(Long idx) {
        return commonRepository.findById(idx)
                .orElseThrow(() -> new TravelException(NOT_FOUND_COMMON));
    }

    /**
     * <pre>
     * 1. MethodName : findCommonList
     * 2. ClassName  : CommonService.java
     * 3. Comment    : 공통 코드 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 21.
     * </pre>
     */
    @Transactional(readOnly = true)
    public Page<CommonDTO> findCommonList(Map<String, Object> commonMap, PageRequest pageRequest) {
        return commonQueryRepository.findCommonList(commonMap, pageRequest);
    }

    /**
     * <pre>
     * 1. MethodName : findOneCommon
     * 2. ClassName  : CommonService.java
     * 3. Comment    : 공통 코드 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 21.
     * </pre>
     */
    @Transactional(readOnly = true)
    public CommonDTO findOneCommon(Long idx) {
        return CommonEntity.toDto(oneCommon(idx));
    }

    /**
     * <pre>
     * 1. MethodName : insertCommonCode
     * 2. ClassName  : CommonService.java
     * 3. Comment    : 공통 코드 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 21.
     * </pre>
     */
    @Transactional
    public CommonDTO insertCommonCode(CommonEntity commonEntity) {
        try {
            return CommonEntity.toDto(commonRepository.save(commonEntity));
        } catch (Exception e) {
            throw new TravelException(ERROR_COMMON);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updateCommonCode
     * 2. ClassName  : CommonService.java
     * 3. Comment    : 공통 코드 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 21.
     * </pre>
     */
    @Transactional
    public CommonDTO updateCommonCode(Long idx, CommonEntity commonEntity) {
        try {
            oneCommon(idx).update(commonEntity);
            return CommonEntity.toDto(commonEntity);
        } catch (Exception e) {
            throw new TravelException(ERROR_UPDATE_COMMON);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteCommonCode
     * 2. ClassName  : CommonService.java
     * 3. Comment    : 공통 코드 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 21.
     * </pre>
     */
    @Transactional
    public Long deleteCommonCode(Long idx) {
        try {
            commonRepository.deleteById(idx);
            return idx;
        } catch (Exception e) {
            throw new TravelException(ERROR_DELETE_COMMON);
        }
    }
}

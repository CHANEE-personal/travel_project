package com.travel.api.faq;

import com.travel.api.common.domain.CommonEntity;
import com.travel.api.common.domain.repository.CommonRepository;
import com.travel.api.faq.domain.FaqDto;
import com.travel.api.faq.domain.FaqEntity;
import com.travel.api.faq.domain.repository.FaqQueryRepository;
import com.travel.api.faq.domain.repository.FaqRepository;
import com.travel.exception.TravelException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.travel.exception.ApiExceptionType.*;


@Service
@RequiredArgsConstructor
public class FaqService {

    private final FaqQueryRepository faqQueryRepository;
    private final FaqRepository faqRepository;
    private final CommonRepository commonRepository;

    private FaqEntity oneFaq(Long idx) {
        return faqRepository.findById(idx)
                .orElseThrow(() -> new TravelException(NOT_FOUND_FAQ));
    }

    private CommonEntity oneCommon(Integer commonCode) {
        return commonRepository.findByCommonCode(commonCode)
                .orElseThrow(() -> new TravelException(NOT_FOUND_FAQ));
    }

    /**
     * <pre>
     * 1. MethodName : findFaqList
     * 2. ClassName  : FaqService.java
     * 3. Comment    : FAQ 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 29.
     * </pre>
     */
    @Transactional(readOnly = true)
    public Page<FaqDto> findFaqList(Map<String, Object> faqMap, PageRequest pageRequest) {
        return faqQueryRepository.findFaqList(faqMap, pageRequest);
    }

    /**
     * <pre>
     * 1. MethodName : findOneFaq
     * 2. ClassName  : FaqService.java
     * 3. Comment    : FAQ 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 29.
     * </pre>
     */
    @Transactional(readOnly = true)
    public FaqDto findOneFaq(Long idx) {
        return faqQueryRepository.findOneFaq(idx);
    }

    /**
     * <pre>
     * 1. MethodName : insertFaq
     * 2. ClassName  : FaqService.java
     * 3. Comment    : FAQ 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 29.
     * </pre>
     */
    @Transactional
    public FaqDto insertFaq(FaqEntity faqEntity) {
        try {
            oneCommon(faqEntity.getNewFaqCode().getCommonCode()).addCommon(faqEntity);
            return FaqEntity.toDto(faqRepository.save(faqEntity));
        } catch (Exception e) {
            throw new TravelException(ERROR_FAQ);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updateFaq
     * 2. ClassName  : FaqService.java
     * 3. Comment    : FAQ 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 29.
     * </pre>
     */
    @Transactional
    public FaqDto updateFaq(Long idx, FaqEntity faqEntity) {
        try {
            oneFaq(idx).update(faqEntity);
            return FaqEntity.toDto(faqEntity);
        } catch (Exception e) {
            throw new TravelException(ERROR_UPDATE_FAQ);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteFaq
     * 2. ClassName  : FaqService.java
     * 3. Comment    : FAQ 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 29.
     * </pre>
     */
    @Transactional
    public Long deleteFaq(Long idx) {
        try {
            faqRepository.deleteById(idx);
            return idx;
        } catch (Exception e) {
            throw new TravelException(ERROR_DELETE_FAQ);
        }
    }
}

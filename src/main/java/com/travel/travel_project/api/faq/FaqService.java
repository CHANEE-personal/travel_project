package com.travel.travel_project.api.faq;

import com.travel.travel_project.domain.faq.FaqDTO;
import com.travel.travel_project.domain.faq.FaqEntity;
import com.travel.travel_project.exception.TravelException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.travel.travel_project.exception.ApiExceptionType.*;

@Service
@RequiredArgsConstructor
public class FaqService {

    private final FaqRepository faqRepository;

    /**
     * <pre>
     * 1. MethodName : findFaqCount
     * 2. ClassName  : FaqService.java
     * 3. Comment    : FAQ 리스트 갯수 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 29.
     * </pre>
     */
    @Transactional
    public int findFaqCount(Map<String, Object> faqMap) {
        try {
            return faqRepository.findFaqCount(faqMap);
        } catch (Exception e) {
            throw new TravelException(NOT_FOUND_FAQ_LIST, e);
        }
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
    @Cacheable(value = "faq", key = "#faqMap")
    @Transactional(readOnly = true)
    public List<FaqDTO> findFaqList(Map<String, Object> faqMap) {
        return faqRepository.findFaqList(faqMap);
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
    @Cacheable(value = "faq", key = "#idx")
    @Transactional(readOnly = true)
    public FaqDTO findOneFaq(Long idx) {
        return faqRepository.findOneFaq(idx);
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
    @CachePut("faq")
    @Transactional
    public FaqDTO insertFaq(FaqEntity faqEntity) {
        try {
            return faqRepository.insertFaq(faqEntity);
        } catch (Exception e) {
            throw new TravelException(ERROR_FAQ, e);
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
    @CachePut(value = "faq", key = "#faqEntity.idx")
    @Transactional
    public FaqDTO updateFaq(FaqEntity faqEntity) {
        try {
            return faqRepository.updateFaq(faqEntity);
        } catch (Exception e) {
            throw new TravelException(ERROR_UPDATE_FAQ, e);
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
    @CacheEvict(value = "faq", key = "#idx")
    @Transactional
    public Long deleteFaq(Long idx) {
        try {
            return faqRepository.deleteFaq(idx);
        } catch (Exception e) {
            throw new TravelException(ERROR_DELETE_FAQ, e);
        }
    }
}

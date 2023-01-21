package com.travel.api.faq;

import com.travel.api.faq.domain.FaqDTO;
import com.travel.api.faq.domain.repository.FaqQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;


@Service
@RequiredArgsConstructor
public class FaqService {

    private final FaqQueryRepository faqQueryRepository;

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
    public Page<FaqDTO> findFaqList(Map<String, Object> faqMap, PageRequest pageRequest) {
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
    public FaqDTO findOneFaq(Long idx) {
        return faqQueryRepository.findOneFaq(idx);
    }
}

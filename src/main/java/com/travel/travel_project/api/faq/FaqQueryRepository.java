package com.travel.travel_project.api.faq;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travel.travel_project.domain.faq.FaqDTO;
import com.travel.travel_project.domain.faq.FaqEntity;
import com.travel.travel_project.exception.TravelException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import java.util.*;

import static com.travel.travel_project.common.StringUtil.getString;
import static com.travel.travel_project.domain.common.QCommonEntity.commonEntity;
import static com.travel.travel_project.domain.faq.FaqEntity.toDto;
import static com.travel.travel_project.domain.faq.FaqEntity.toDtoList;
import static com.travel.travel_project.domain.faq.QFaqEntity.faqEntity;
import static com.travel.travel_project.exception.ApiExceptionType.NOT_FOUND_FAQ;

@Repository
@RequiredArgsConstructor
public class FaqQueryRepository {

    private final JPAQueryFactory queryFactory;

    private BooleanExpression searchFaqInfo(Map<String, Object> faqMap) {
        String searchKeyword = getString(faqMap.get("searchKeyword"), "");

        return !Objects.equals(searchKeyword, "") ? faqEntity.title.contains(searchKeyword).or(faqEntity.description.contains(searchKeyword)) : null;
    }

    /**
     * <pre>
     * 1. MethodName : findFaqList
     * 2. ClassName  : FaqRepository.java
     * 3. Comment    : FAQ 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 29.
     * </pre>
     */
    public Page<FaqDTO> findFaqList(Map<String, Object> faqMap, PageRequest pageRequest) {
        List<FaqEntity> faqList = queryFactory.selectFrom(faqEntity)
                .orderBy(faqEntity.idx.desc())
                .innerJoin(faqEntity.newFaqCode, commonEntity)
                .where(searchFaqInfo(faqMap))
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        assert faqList != null;
        return new PageImpl<>(toDtoList(faqList), pageRequest, faqList.size());
    }

    /**
     * <pre>
     * 1. MethodName : findOneFaq
     * 2. ClassName  : FaqRepository.java
     * 3. Comment    : FAQ 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 29.
     * </pre>
     */
    public FaqDTO findOneFaq(Long idx) {
        FaqEntity oneFaq = Optional.ofNullable(queryFactory
                .selectFrom(faqEntity)
                .innerJoin(faqEntity.newFaqCode, commonEntity)
                .where(faqEntity.idx.eq(idx))
                .fetchOne()).orElseThrow(() -> new TravelException(NOT_FOUND_FAQ));

        return toDto(oneFaq);
    }
}

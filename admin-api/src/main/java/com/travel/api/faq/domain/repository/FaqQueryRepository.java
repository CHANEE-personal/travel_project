package com.travel.api.faq.domain.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travel.api.faq.domain.FaqDto;
import com.travel.api.faq.domain.FaqEntity;
import com.travel.exception.TravelException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import java.util.*;

import static com.travel.api.common.domain.QCommonEntity.commonEntity;
import static com.travel.api.faq.domain.FaqEntity.toDto;
import static com.travel.api.faq.domain.FaqEntity.toDtoList;
import static com.travel.api.faq.domain.QFaqEntity.faqEntity;
import static com.travel.common.StringUtil.getString;
import static com.travel.exception.ApiExceptionType.NOT_FOUND_FAQ;

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
    public Page<FaqDto> findFaqList(Map<String, Object> faqMap, PageRequest pageRequest) {
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
    public FaqDto findOneFaq(Long idx) {
        FaqEntity oneFaq = Optional.ofNullable(queryFactory
                .selectFrom(faqEntity)
                .innerJoin(faqEntity.newFaqCode, commonEntity)
                .where(faqEntity.idx.eq(idx))
                .fetchOne()).orElseThrow(() -> new TravelException(NOT_FOUND_FAQ));

        return toDto(oneFaq);
    }
}

package com.travel.travel_project.api.faq;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travel.travel_project.domain.faq.FaqEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

import static com.travel.travel_project.common.StringUtil.getInt;
import static com.travel.travel_project.common.StringUtil.getString;
import static com.travel.travel_project.domain.faq.QFaqEntity.faqEntity;

@Repository
@RequiredArgsConstructor
public class FaqRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    private BooleanExpression searchFaqInfo(Map<String, Object> faqMap) {
        String searchKeyword = getString(faqMap.get("searchKeyword"), "");

        if (searchKeyword == null) {
            return null;
        } else {
            return faqEntity.title.contains(searchKeyword)
                    .or(faqEntity.description.contains(searchKeyword));
        }
    }

    /**
     * <pre>
     * 1. MethodName : findFaqCount
     * 2. ClassName  : FaqRepository.java
     * 3. Comment    : FAQ 리스트 갯수 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 29.
     * </pre>
     */
    public Integer findFaqCount(Map<String, Object> faqMap) {
        return queryFactory.selectFrom(faqEntity)
                .where(searchFaqInfo(faqMap))
                .fetch().size();
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
    public List<FaqEntity> findFaqList(Map<String, Object> faqMap) {
        List<FaqEntity> faqList = queryFactory.selectFrom(faqEntity)
                .orderBy(faqEntity.idx.desc())
                .where(searchFaqInfo(faqMap))
                .fetch();

        faqList.forEach(list -> faqList.get(faqList.indexOf(list))
                .setRnum(getInt(faqMap.get("startPage"), 1) * (getInt(faqMap.get("size"), 1)) - (2 - faqList.indexOf(list))));

        return faqList;
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
    public FaqEntity findOneFaq(Long idx) {
        return queryFactory.selectFrom(faqEntity)
                .where(faqEntity.idx.eq(idx))
                .fetchOne();
    }

    /**
     * <pre>
     * 1. MethodName : insertFaq
     * 2. ClassName  : FaqRepository.java
     * 3. Comment    : FAQ 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 29.
     * </pre>
     */
    public FaqEntity insertFaq(FaqEntity faqEntity) {
        em.persist(faqEntity);
        return faqEntity;
    }

    /**
     * <pre>
     * 1. MethodName : updateFaq
     * 2. ClassName  : FaqRepository.java
     * 3. Comment    : FAQ 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 29.
     * </pre>
     */
    public FaqEntity updateFaq(FaqEntity existFaqEntity) {
        em.merge(existFaqEntity);
        em.flush();
        em.clear();
        return existFaqEntity;
    }

    /**
     * <pre>
     * 1. MethodName : deleteFaq
     * 2. ClassName  : FaqRepository.java
     * 3. Comment    : FAQ 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 29.
     * </pre>
     */
    public Long deleteFaq(Long idx) {
        em.remove(em.find(FaqEntity.class, idx));
        em.flush();
        em.clear();
        return idx;
    }
}

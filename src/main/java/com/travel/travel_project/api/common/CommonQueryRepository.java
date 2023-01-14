package com.travel.travel_project.api.common;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travel.travel_project.domain.common.CommonDTO;
import com.travel.travel_project.domain.common.CommonEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.*;

import static com.travel.travel_project.common.StringUtil.getString;
import static com.travel.travel_project.domain.common.QCommonEntity.commonEntity;

@Repository
@RequiredArgsConstructor
public class CommonQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    private BooleanExpression searchCommonInfo(Map<String, Object> commonMap) {
        String searchKeyword = getString(commonMap.get("searchKeyword"), "");
        return !Objects.equals(searchKeyword, "") ? commonEntity.commonName.contains(searchKeyword) : null;
    }

    /**
     * <pre>
     * 1. MethodName : findCommonList
     * 2. ClassName  : CommonRepository.java
     * 3. Comment    : 공통 코드 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 21.
     * </pre>
     */
    public Page<CommonDTO> findCommonList(Map<String, Object> commonMap, PageRequest pageRequest) {
        List<CommonEntity> commonCodeList = queryFactory
                .selectFrom(commonEntity)
                .orderBy(commonEntity.idx.desc())
                .where(searchCommonInfo(commonMap))
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        return new PageImpl<>(CommonEntity.toDtoList(commonCodeList), pageRequest, commonCodeList.size());
    }
}

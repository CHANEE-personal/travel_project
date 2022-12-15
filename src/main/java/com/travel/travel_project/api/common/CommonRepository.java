package com.travel.travel_project.api.common;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travel.travel_project.domain.common.CommonDTO;
import com.travel.travel_project.domain.common.CommonEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.travel.travel_project.common.StringUtil.getInt;
import static com.travel.travel_project.common.StringUtil.getString;
import static com.travel.travel_project.domain.common.QCommonEntity.commonEntity;

@Repository
@RequiredArgsConstructor
public class CommonRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    private BooleanExpression searchCommonInfo(Map<String, Object> commonMap) {
        String searchKeyword = getString(commonMap.get("searchKeyword"), "");

        if (searchKeyword == null) {
            return null;
        } else {
            return commonEntity.commonName.contains(searchKeyword);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findCommonCount
     * 2. ClassName  : CommonRepository.java
     * 3. Comment    : 공통 코드 리스트 갯수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 11. 21.
     * </pre>
     */
    public Integer findCommonCount(Map<String, Object> commonMap) {
        return queryFactory.selectFrom(commonEntity)
                .where(searchCommonInfo(commonMap))
                .fetch().size();
    }

    /**
     * <pre>
     * 1. MethodName : findCommonList
     * 2. ClassName  : CommonRepository.java
     * 3. Comment    : 공통 코드 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 11. 21.
     * </pre>
     */
    public List<CommonDTO> findCommonList(Map<String, Object> commonMap) {
        List<CommonEntity> commonCodeList = queryFactory.selectFrom(commonEntity)
                .orderBy(commonEntity.idx.desc())
                .where(searchCommonInfo(commonMap))
                .offset(getInt(commonMap.get("jpaStartPage"), 0))
                .limit(getInt(commonMap.get("size"), 0))
                .fetch();

        commonCodeList.forEach(list -> commonCodeList.get(commonCodeList.indexOf(list))
                .setRowNum(getInt(commonMap.get("startPage"), 1) * (getInt(commonMap.get("size"), 1)) - (2 - commonCodeList.indexOf(list))));

        return commonCodeList.stream().map(CommonEntity::toDto).collect(Collectors.toList());
    }

    /**
     * <pre>
     * 1. MethodName : findOneCommon
     * 2. ClassName  : CommonRepository.java
     * 3. Comment    : 공통 코드 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 11. 21.
     * </pre>
     */
    public CommonDTO findOneCommon(Long idx) {
        CommonEntity findOneCommon = queryFactory
                .selectFrom(commonEntity)
                .where(commonEntity.idx.eq(idx))
                .fetchOne();

        return CommonEntity.toDto(findOneCommon);
    }

    /**
     * <pre>
     * 1. MethodName : insertCommonCode
     * 2. ClassName  : CommonRepository.java
     * 3. Comment    : 공통 코드 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 11. 21.
     * </pre>
     */
    public CommonDTO insertCommonCode(CommonEntity commonEntity) {
        em.persist(commonEntity);
        return CommonEntity.toDto(commonEntity);
    }

    /**
     * <pre>
     * 1. MethodName : updateCommonCode
     * 2. ClassName  : CommonRepository.java
     * 3. Comment    : 공통 코드 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 11. 21.
     * </pre>
     */
    public CommonDTO updateCommonCode(CommonEntity commonEntity) {
        em.merge(commonEntity);
        em.flush();
        em.clear();
        return CommonEntity.toDto(commonEntity);
    }

    /**
     * <pre>
     * 1. MethodName : deleteCommonCode
     * 2. ClassName  : CommonRepository.java
     * 3. Comment    : 공통 코드 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 11. 21.
     * </pre>
     */
    public Long deleteCommonCode(Long idx) {
        em.remove(em.find(CommonEntity.class, idx));
        em.flush();
        em.clear();
        return idx;
    }
}

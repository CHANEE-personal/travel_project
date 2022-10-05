package com.travel.travel_project.admin.travel;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travel.travel_project.admin.travel.domain.AdminTravelDTO;
import com.travel.travel_project.admin.travel.domain.AdminTravelEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static com.travel.travel_project.admin.common.StringUtil.getInt;
import static com.travel.travel_project.admin.common.StringUtil.getString;
import static com.travel.travel_project.admin.common.domain.QAdminCommonEntity.adminCommonEntity;
import static com.travel.travel_project.admin.travel.domain.QAdminTravelEntity.*;
import static com.travel.travel_project.admin.travel.mapper.TravelMapper.INSTANCE;
import static java.time.LocalDate.now;
import static java.time.LocalDateTime.of;

@Repository
@RequiredArgsConstructor
public class AdminTravelRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    private BooleanExpression searchTravel(Map<String, Object> travelMap) {
        String searchKeyword = getString(travelMap.get("searchKeyword"), "");
        LocalDateTime searchStartTime = (LocalDateTime) travelMap.get("searchStartTime");
        LocalDateTime searchEndTime = (LocalDateTime) travelMap.get("searchEndTime");

        if (searchStartTime != null && searchEndTime != null) {
            searchStartTime = (LocalDateTime) travelMap.get("searchStartTime");
            searchEndTime = (LocalDateTime) travelMap.get("searchEndTime");
        } else {
            searchStartTime = now().minusDays(now().getDayOfMonth()-1).atStartOfDay();
            searchEndTime = of(now().minusDays(now().getDayOfMonth()).plusMonths(1), LocalTime.of(23,59,59));
        }

        if (!"".equals(searchKeyword)) {
            return adminTravelEntity.travelTitle.contains(searchKeyword)
                    .or(adminTravelEntity.travelDescription.contains(searchKeyword));
        } else {
            return adminTravelEntity.createTime.between(searchStartTime, searchEndTime);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findTravelCount
     * 2. ClassName  : AdminTravelRepository.java
     * 3. Comment    : 관리자 여행지 소개 리스트 갯수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 10. 05.
     * </pre>
     */
    public Integer findTravelCount(Map<String, Object> travelMap) {
        return queryFactory.selectFrom(adminTravelEntity)
                .where(searchTravel(travelMap))
                .fetch().size();
    }

    /**
     * <pre>
     * 1. MethodName : findTravelsList
     * 2. ClassName  : AdminTravelRepository.java
     * 3. Comment    : 관리자 여행지 소개 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 10. 05.
     * </pre>
     */
    public List<AdminTravelDTO> findTravelsList(Map<String, Object> travelMap) {
        List<AdminTravelEntity> travelList = queryFactory
                .selectFrom(adminTravelEntity)
                .orderBy(adminTravelEntity.idx.desc())
                .leftJoin(adminTravelEntity.newTravelCode, adminCommonEntity)
                .fetchJoin()
                .where(searchTravel(travelMap)
                        .and(adminTravelEntity.visible.eq("Y")))
                .offset(getInt(travelMap.get("jpaStartPage"), 0))
                .limit(getInt(travelMap.get("size"), 0))
                .fetch();

        travelList.forEach(list -> travelList.get(travelList.indexOf(list))
                .setRnum(getInt(travelMap.get("startPage"), 1) * (getInt(travelMap.get("size"), 1)) - (2 - travelList.indexOf(list))));

        return INSTANCE.toDtoList(travelList);
    }

    /**
     * <pre>
     * 1. MethodName : findOneTravel
     * 2. ClassName  : AdminTravelRepository.java
     * 3. Comment    : 관리자 여행지 소개 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 10. 05.
     * </pre>
     */
    public AdminTravelDTO findOneTravel(Long idx) {
        AdminTravelEntity findOneTravel = queryFactory
                .selectFrom(adminTravelEntity)
                .orderBy(adminTravelEntity.idx.desc())
                .where(adminTravelEntity.idx.eq(idx)
                        .and(adminTravelEntity.visible.eq("Y")))
                .fetchOne();

        return INSTANCE.toDto(findOneTravel);
    }

    /**
     * <pre>
     * 1. MethodName : insertTravel
     * 2. ClassName  : AdminTravelRepository.java
     * 3. Comment    : 관리자 여행지 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 10. 05.
     * </pre>
     */
    public AdminTravelDTO insertTravel(AdminTravelEntity adminTravelEntity) {
        em.persist(adminTravelEntity);
        return INSTANCE.toDto(adminTravelEntity);
    }

    /**
     * <pre>
     * 1. MethodName : updateTravel
     * 2. ClassName  : AdminTravelRepository.java
     * 3. Comment    : 관리자 여행지 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 10. 05.
     * </pre>
     */
    public AdminTravelDTO updateTravel(AdminTravelEntity adminTravelEntity) {
        em.merge(adminTravelEntity);
        em.flush();
        em.clear();
        return INSTANCE.toDto(adminTravelEntity);
    }

    /**
     * <pre>
     * 1. MethodName : deleteTravel
     * 2. ClassName  : AdminTravelRepository.java
     * 3. Comment    : 관리자 여행지 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 10. 05.
     * </pre>
     */
    public Long deleteTravel(Long idx) {
        em.remove(em.find(AdminTravelEntity.class, idx));
        em.flush();
        em.clear();
        return idx;
    }
}

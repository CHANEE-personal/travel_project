package com.travel.travel_project.admin.travel;

import com.travel.travel_project.admin.exception.TspException;
import com.travel.travel_project.admin.travel.domain.AdminTravelDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.travel.travel_project.admin.exception.ApiExceptionType.NOT_FOUND_TRAVEL_LIST;

@Service
@RequiredArgsConstructor
public class AdminTravelService {

    private final AdminTravelRepository adminTravelRepository;

    /**
     * <pre>
     * 1. MethodName : findTravelCount
     * 2. ClassName  : AdminTravelService.java
     * 3. Comment    : 관리자 > 여행지 소개 리스트 갯수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 10. 5.
     * </pre>
     */
    @Transactional(readOnly = true)
    public int findTravelCount(Map<String, Object> travelMap) throws TspException {
        try {
            return adminTravelRepository.findTravelCount(travelMap);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_TRAVEL_LIST, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findTravelsList
     * 2. ClassName  : AdminTravelService.java
     * 3. Comment    : 관리자 > 여행지 소개 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 10. 5.
     * </pre>
     */
    @Transactional(readOnly = true)
    public List<AdminTravelDTO> findTravelsList(Map<String, Object> travelMap) throws TspException {
        try {
            return adminTravelRepository.findTravelsList(travelMap);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_TRAVEL_LIST, e);
        }
    }
}

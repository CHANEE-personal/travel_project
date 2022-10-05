package com.travel.travel_project.admin.travel;

import com.travel.travel_project.admin.common.Page;
import com.travel.travel_project.admin.common.SearchCommon;
import com.travel.travel_project.admin.travel.domain.AdminTravelDTO;
import com.travel.travel_project.admin.travel.domain.AdminTravelEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.rmi.ServerError;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.ceil;

@RestController
@RequestMapping("/api/travel")
@Api(tags = "여행 소개 관련 API")
@RequiredArgsConstructor
public class AdminTravelController {

    private final AdminTravelService adminTravelService;
    private final SearchCommon searchCommon;

    /**
     * <pre>
     * 1. MethodName : findTravelsList
     * 2. ClassName  : AdminTravelController.java
     * 3. Comment    : 관리자 여행지 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 10. 5.
     * </pre>
     */
    @ApiOperation(value = "관리자 여행지 리스트 조회", notes = "관리자 여행지 리스트를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "여행지 리스트 조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/lists")
    public Map<String, Object> findTravelsList(@RequestParam(required = false) Map<String, Object> paramMap, Page page) {
        Map<String, Object> travelMap = new HashMap<>();

        Integer noticeCount = this.adminTravelService.findTravelCount(searchCommon.searchCommon(page, paramMap));
        List<AdminTravelDTO> travelList = new ArrayList<>();

        if (noticeCount > 0) {
            travelList = this.adminTravelService.findTravelsList(searchCommon.searchCommon(page, paramMap));
        }

        // 리스트 수
        travelMap.put("pageSize", page.getSize());
        // 전체 페이지 수
        travelMap.put("perPageListCnt", ceil((double) noticeCount / page.getSize()));
        // 전체 아이템 수
        travelMap.put("noticeListCnt", noticeCount);

        travelMap.put("noticeList", travelList);

        return travelMap;
    }

    /**
     * <pre>
     * 1. MethodName : findOneTravel
     * 2. ClassName  : AdminTravelController.java
     * 3. Comment    : 관리자 여행지 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 10. 5.
     * </pre>
     */
    @ApiOperation(value = "관리자 여행지 상세 조회", notes = "관리자 여행지를 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "여행지 상세 조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/{idx}")
    public AdminTravelDTO findOneTravel(@PathVariable Long idx) {
        return adminTravelService.findOneTravel(idx);
    }

    /**
     * <pre>
     * 1. MethodName : insertTravel
     * 2. ClassName  : AdminTravelController.java
     * 3. Comment    : 관리자 여행지 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 10. 5.
     * </pre>
     */
    @ApiOperation(value = "관리자 여행지 등록", notes = "관리자 여행지를 등록한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "여행지 등록 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping
    public AdminTravelDTO insertTravel(@Valid @RequestBody AdminTravelEntity adminTravelEntity) {
        return adminTravelService.insertTravel(adminTravelEntity);
    }

    /**
     * <pre>
     * 1. MethodName : updateTravel
     * 2. ClassName  : AdminTravelController.java
     * 3. Comment    : 관리자 여행지 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 10. 5.
     * </pre>
     */
    @ApiOperation(value = "관리자 여행지 수정", notes = "관리자 여행지를 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "여행지 수정 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping("/{idx}")
    public AdminTravelDTO updateTravel(@Valid @RequestBody AdminTravelEntity adminTravelEntity) {
        return adminTravelService.updateTravel(adminTravelEntity);
    }

    /**
     * <pre>
     * 1. MethodName : deleteTravel
     * 2. ClassName  : AdminTravelController.java
     * 3. Comment    : 관리자 여행지 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 10. 5.
     * </pre>
     */
    @ApiOperation(value = "관리자 여행지 삭제", notes = "관리자 여행지를 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "여행지 삭제 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @DeleteMapping("/{idx}")
    public Long deleteTravel(@PathVariable Long idx) {
        return adminTravelService.deleteTravel(idx);
    }
}

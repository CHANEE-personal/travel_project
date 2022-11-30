package com.travel.travel_project.api.faq;

import com.travel.travel_project.common.Page;
import com.travel.travel_project.common.SearchCommon;
import com.travel.travel_project.domain.faq.FaqDTO;
import com.travel.travel_project.domain.faq.FaqEntity;
import com.travel.travel_project.exception.TravelException;
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

import static com.travel.travel_project.exception.ApiExceptionType.NOT_FOUND_FAQ;
import static java.lang.Math.ceil;

@RestController
@RequestMapping("/api/faq")
@Api(tags = "FAQ 관련 API")
@RequiredArgsConstructor
public class FaqController {

    private final FaqService faqService;
    private final SearchCommon searchCommon;

    /**
     * <pre>
     * 1. MethodName : findFaqList
     * 2. ClassName  : FaqController.java
     * 3. Comment    : FAQ 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 11. 29.
     * </pre>
     */
    @ApiOperation(value = "FAQ 리스트 조회", notes = "FAQ 리스트를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "FAQ 리스트 조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/lists")
    public Map<String, Object> findFaqList(@RequestParam(required = false) Map<String, Object> paramMap, Page page) {
        Map<String, Object> faqMap = new HashMap<>();

        int faqCount = this.faqService.findFaqCount(searchCommon.searchCommon(page, paramMap));
        List<FaqDTO> faqList = new ArrayList<>();

        if (faqCount > 0) {
            faqList = this.faqService.findFaqList(searchCommon.searchCommon(page, paramMap));
        }

        // 리스트 수
        faqMap.put("pageSize", page.getSize());
        // 전체 페이지 수
        faqMap.put("perPageListCnt", ceil((double) faqCount / page.getSize()));
        // 전체 아이템 수
        faqMap.put("faqListCnt", faqCount);

        faqMap.put("faqList", faqList);

        return faqMap;
    }

    /**
     * <pre>
     * 1. MethodName : findOneFaq
     * 2. ClassName  : FaqController.java
     * 3. Comment    : FAQ 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 11. 29.
     * </pre>
     */
    @ApiOperation(value = "FAQ 상세 조회", notes = "FAQ를 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "FAQ 상세 조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{idx}")
    public FaqDTO findOneFaq(@PathVariable Long idx) {
        return faqService.findOneFaq(idx);
    }

    /**
     * <pre>
     * 1. MethodName : insertFaq
     * 2. ClassName  : FaqController.java
     * 3. Comment    : FAQ 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 11. 29.
     * </pre>
     */
    @ApiOperation(value = "FAQ 등록", notes = "FAQ를 등록한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "FAQ 등록 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping
    public FaqDTO insertFaq(@Valid @RequestBody FaqEntity faqEntity) {
        return faqService.insertFaq(faqEntity);
    }

    /**
     * <pre>
     * 1. MethodName : updateFaq
     * 2. ClassName  : FaqController.java
     * 3. Comment    : FAQ 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 11. 29.
     * </pre>
     */
    @ApiOperation(value = "FAQ 수정", notes = "FAQ를 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "FAQ 수정 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping("/{idx}")
    public FaqDTO updateFaq(@PathVariable Long idx, @Valid @RequestBody FaqEntity faqEntity) {
        if (faqService.findOneFaq(idx) == null) {
            throw new TravelException(NOT_FOUND_FAQ, new Throwable());
        }
        return faqService.updateFaq(faqEntity);
    }

    /**
     * <pre>
     * 1. MethodName : deleteFaq
     * 2. ClassName  : FaqController.java
     * 3. Comment    : FAQ 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 11. 29.
     * </pre>
     */
    @ApiOperation(value = "FAQ 삭제", notes = "FAQ를 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "FAQ 삭제 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @DeleteMapping("/{idx}")
    public Long deletFaq(@PathVariable Long idx) {
        return faqService.deleteFaq(idx);
    }
}

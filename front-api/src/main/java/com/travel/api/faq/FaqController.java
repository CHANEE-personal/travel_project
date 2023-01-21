package com.travel.api.faq;

import com.travel.api.faq.domain.FaqDTO;
import com.travel.common.Paging;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.rmi.ServerError;
import java.util.Map;

@RestController
@RequestMapping("/front/faq")
@Api(tags = "FAQ 관련 API")
@RequiredArgsConstructor
public class FaqController {

    private final FaqService faqService;

    /**
     * <pre>
     * 1. MethodName : findFaqList
     * 2. ClassName  : FaqController.java
     * 3. Comment    : FAQ 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 29.
     * </pre>
     */
    @ApiOperation(value = "FAQ 리스트 조회", notes = "FAQ 리스트를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "FAQ 리스트 조회 성공", response = Page.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/lists")
    public ResponseEntity<Page<FaqDTO>> findFaqList(@RequestParam(required = false) Map<String, Object> paramMap, Paging paging) {
        return ResponseEntity.ok(faqService.findFaqList(paramMap, paging.getPageRequest(paging.getPageNum(), paging.getSize())));
    }

    /**
     * <pre>
     * 1. MethodName : findOneFaq
     * 2. ClassName  : FaqController.java
     * 3. Comment    : FAQ 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 29.
     * </pre>
     */
    @ApiOperation(value = "FAQ 상세 조회", notes = "FAQ를 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "FAQ 상세 조회 성공", response = FaqDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{idx}")
    public ResponseEntity<FaqDTO> findOneFaq(@PathVariable Long idx) {
        return ResponseEntity.ok(faqService.findOneFaq(idx));
    }
}

package com.travel.travel_project.api.faq;

import com.travel.travel_project.common.Page;
import com.travel.travel_project.common.SearchCommon;
import com.travel.travel_project.domain.faq.FaqDTO;
import com.travel.travel_project.domain.faq.FaqEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.net.URI;
import java.rmi.ServerError;
import java.util.List;
import java.util.Map;

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
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 29.
     * </pre>
     */
    @ApiOperation(value = "FAQ 리스트 조회", notes = "FAQ 리스트를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "FAQ 리스트 조회 성공", response = List.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/lists")
    public ResponseEntity<List<FaqDTO>> findFaqList(@RequestParam(required = false) Map<String, Object> paramMap, Page page) {
        return ResponseEntity.ok(faqService.findFaqList(searchCommon.searchCommon(page, paramMap)));
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

    /**
     * <pre>
     * 1. MethodName : insertFaq
     * 2. ClassName  : FaqController.java
     * 3. Comment    : FAQ 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 29.
     * </pre>
     */
    @ApiOperation(value = "FAQ 등록", notes = "FAQ를 등록한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "FAQ 등록 성공", response = FaqDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping
    public ResponseEntity<FaqDTO> insertFaq(@Valid @RequestBody FaqEntity faqEntity) {
        return ResponseEntity.created(URI.create("")).body(faqService.insertFaq(faqEntity));
    }

    /**
     * <pre>
     * 1. MethodName : updateFaq
     * 2. ClassName  : FaqController.java
     * 3. Comment    : FAQ 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 29.
     * </pre>
     */
    @ApiOperation(value = "FAQ 수정", notes = "FAQ를 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "FAQ 수정 성공", response = FaqDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping("/{idx}")
    public ResponseEntity<FaqDTO> updateFaq(@PathVariable Long idx, @Valid @RequestBody FaqEntity faqEntity) {
        if (faqService.findOneFaq(idx) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faqService.updateFaq(faqEntity));
    }

    /**
     * <pre>
     * 1. MethodName : deleteFaq
     * 2. ClassName  : FaqController.java
     * 3. Comment    : FAQ 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 29.
     * </pre>
     */
    @ApiOperation(value = "FAQ 삭제", notes = "FAQ를 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "FAQ 삭제 성공", response = Long.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @DeleteMapping("/{idx}")
    public ResponseEntity<Long> deleteFaq(@PathVariable Long idx) {
        if (faqService.findOneFaq(idx) == null) {
            return ResponseEntity.notFound().build();
        }
        faqService.deleteFaq(idx);
        return ResponseEntity.noContent().build();
    }
}

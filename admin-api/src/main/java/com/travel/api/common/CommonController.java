package com.travel.api.common;

import com.travel.api.common.domain.CommonDto;
import com.travel.api.common.domain.CommonEntity;
import com.travel.common.Paging;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.net.URI;
import java.rmi.ServerError;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/common")
@Api(tags = "공통 코드 관련 API")
@RequiredArgsConstructor
public class CommonController {

    private final CommonService commonService;

    /**
     * <pre>
     * 1. MethodName : findCommonList
     * 2. ClassName  : CommonController.java
     * 3. Comment    : 공통 코드 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 21.
     * </pre>
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "공통 코드 리스트 조회", notes = "공통 코드 리스트를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "공통 코드 리스트 조회 성공", response = List.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping
    public ResponseEntity<Page<CommonDto>> findCommonList(@RequestParam(required = false) Map<String, Object> paramMap, Paging paging) {
        return ResponseEntity.ok(commonService.findCommonList(paramMap, paging.getPageRequest(paging.getPageNum(), paging.getSize())));
    }

    /**
     * <pre>
     * 1. MethodName : findOneCommon
     * 2. ClassName  : CommonController.java
     * 3. Comment    : 공통 코드 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 21.
     * </pre>
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "공통 코드 상세 조회", notes = "공통 코드를 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "공통 코드 상세 조회 성공", response = CommonDto.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{idx}")
    public ResponseEntity<CommonDto> findOneCommon(@PathVariable Long idx) {
        return ResponseEntity.ok(commonService.findOneCommon(idx));
    }

    /**
     * <pre>
     * 1. MethodName : insertCommonCode
     * 2. ClassName  : CommonController.java
     * 3. Comment    : 공통 코드 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 21.
     * </pre>
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "공통 코드 등록", notes = "공통 코드를 등록한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "공통 코드 등록 성공", response = CommonDto.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping
    public ResponseEntity<CommonDto> insertCommonCode(@Valid @RequestBody CommonEntity commonEntity) {
        return ResponseEntity.created(URI.create("")).body(commonService.insertCommonCode(commonEntity));
    }

    /**
     * <pre>
     * 1. MethodName : updateCommonCode
     * 2. ClassName  : CommonController.java
     * 3. Comment    : 공통 코드 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 21.
     * </pre>
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "공통 코드 수정", notes = "공통 코드를 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "공통 코드 수정 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping("/{idx}")
    public ResponseEntity<CommonDto> updateCommonCode(@PathVariable Long idx, @Valid @RequestBody CommonEntity commonEntity) {
        return ResponseEntity.ok(commonService.updateCommonCode(idx, commonEntity));
    }

    /**
     * <pre>
     * 1. MethodName : deleteCommonCode
     * 2. ClassName  : CommonController.java
     * 3. Comment    : 공통 코드 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 21.
     * </pre>
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "공통 코드 삭제", notes = "공통 코드를 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "공통 코드 삭제 성공", response = Long.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @DeleteMapping("/{idx}")
    public ResponseEntity<Long> deleteCommonCode(@PathVariable Long idx) {
        commonService.deleteCommonCode(idx);
        return ResponseEntity.noContent().build();
    }
}

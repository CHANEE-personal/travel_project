package com.travel.api.notice;

import com.travel.api.notice.domain.NoticeDto;
import com.travel.api.notice.domain.NoticeEntity;
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
import java.util.Map;

@RestController
@RequestMapping("/admin/notice")
@Api(tags = "공지사항 관련 API")
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;

    /**
     * <pre>
     * 1. MethodName : findNoticeList
     * 2. ClassName  : NoticeController.java
     * 3. Comment    : 공지사항 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 28.
     * </pre>
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "공지사항 리스트 조회", notes = "공지사항 리스트를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "공지사항 리스트 조회 성공", response = Page.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping
    public ResponseEntity<Page<NoticeDto>> findNoticeList(@RequestParam(required = false) Map<String, Object> paramMap, Paging paging) {
        return ResponseEntity.ok(noticeService.findNoticeList(paramMap, paging.getPageRequest(paging.getPageNum(), paging.getSize())));
    }

    /**
     * <pre>
     * 1. MethodName : findOneNotice
     * 2. ClassName  : NoticeController.java
     * 3. Comment    : 공지사항 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 28.
     * </pre>
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "공지사항 상세 조회", notes = "공지사항 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "공지사항 상세 조회 성공", response = NoticeDto.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/{idx}")
    public ResponseEntity<NoticeDto> findOneNotice(@PathVariable Long idx) {
        return ResponseEntity.ok(noticeService.findOneNotice(idx));
    }

    /**
     * <pre>
     * 1. MethodName : insertNotice
     * 2. ClassName  : NoticeController.java
     * 3. Comment    : 공지사항 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 28.
     * </pre>
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "공지사항 등록", notes = "공지사항 등록한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "공지사항 등록 성공", response = NoticeDto.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping
    public ResponseEntity<NoticeDto> insertNotice(@Valid @RequestBody NoticeEntity noticeEntity) {
        return ResponseEntity.created(URI.create("")).body(noticeService.insertNotice(noticeEntity));
    }

    /**
     * <pre>
     * 1. MethodName : updateNotice
     * 2. ClassName  : NoticeController.java
     * 3. Comment    : 공지사항 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 28.
     * </pre>
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "공지사항 수정", notes = "공지사항 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "공지사항 수정 성공", response = NoticeDto.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping("/{idx}")
    public ResponseEntity<NoticeDto> updateNotice(@PathVariable Long idx, @Valid @RequestBody NoticeEntity noticeEntity) {
        return ResponseEntity.ok(noticeService.updateNotice(idx, noticeEntity));
    }

    /**
     * <pre>
     * 1. MethodName : deleteNotice
     * 2. ClassName  : NoticeController.java
     * 3. Comment    : 공지사항 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 28.
     * </pre>
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "공지사항 삭제", notes = "공지사항 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "공지사항 삭제 성공", response = Long.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @DeleteMapping("/{idx}")
    public ResponseEntity<Long> deleteNotice(@PathVariable Long idx) {
        noticeService.deleteNotice(idx);
        return ResponseEntity.noContent().build();
    }

    /**
     * <pre>
     * 1. MethodName : toggleFixed
     * 2. ClassName  : NoticeController.java
     * 3. Comment    : 공지사항 고정글 설정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 28.
     * </pre>
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "공지사항 고정글 설정", notes = "공지사항을 고정글 설정한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "공지사항 고정글 설정 성공", response = Boolean.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping("/{idx}/toggle-fixed")
    public ResponseEntity<Boolean> toggleFixed(@PathVariable Long idx) {
        return ResponseEntity.ok(noticeService.toggleTopFixed(idx));
    }
}

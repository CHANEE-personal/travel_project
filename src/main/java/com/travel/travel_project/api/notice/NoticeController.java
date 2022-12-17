package com.travel.travel_project.api.notice;

import com.travel.travel_project.common.Page;
import com.travel.travel_project.common.SearchCommon;
import com.travel.travel_project.domain.notice.NoticeDTO;
import com.travel.travel_project.domain.notice.NoticeEntity;
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
import java.util.List;
import java.util.Map;

import static com.travel.travel_project.exception.ApiExceptionType.NOT_FOUND_NOTICE;

@RestController
@RequestMapping("/api/notice")
@Api(tags = "공지사항 관련 API")
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;
    private final SearchCommon searchCommon;

    /**
     * <pre>
     * 1. MethodName : findNoticeList
     * 2. ClassName  : NoticeController.java
     * 3. Comment    : 공지사항 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 11. 28.
     * </pre>
     */
    @ApiOperation(value = "공지사항 리스트 조회", notes = "공지사항 리스트를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "공지사항 리스트 조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/lists")
    public List<NoticeDTO> findNoticeList(@RequestParam(required = false) Map<String, Object> paramMap, Page page) {
        return this.noticeService.findNoticeList(searchCommon.searchCommon(page, paramMap));
    }

    /**
     * <pre>
     * 1. MethodName : findOneNotice
     * 2. ClassName  : NoticeController.java
     * 3. Comment    : 공지사항 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 11. 28.
     * </pre>
     */
    @ApiOperation(value = "공지사항 상세 조회", notes = "공지사항 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "공지사항 상세 조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/{idx}")
    public NoticeDTO findOneNotice(@PathVariable Long idx) {
        return noticeService.findOneNotice(idx);
    }

    /**
     * <pre>
     * 1. MethodName : insertNotice
     * 2. ClassName  : NoticeController.java
     * 3. Comment    : 공지사항 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 11. 28.
     * </pre>
     */
    @ApiOperation(value = "공지사항 등록", notes = "공지사항 등록한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "공지사항 등록 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping
    public NoticeDTO insertNotice(@Valid @RequestBody NoticeEntity noticeEntity) {
        return noticeService.insertNotice(noticeEntity);
    }

    /**
     * <pre>
     * 1. MethodName : updateNotice
     * 2. ClassName  : NoticeController.java
     * 3. Comment    : 공지사항 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 11. 28.
     * </pre>
     */
    @ApiOperation(value = "공지사항 수정", notes = "공지사항 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "공지사항 수정 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping("/{idx}")
    public NoticeDTO updateNotice(@PathVariable Long idx, @Valid @RequestBody NoticeEntity noticeEntity) {
        if (noticeService.findOneNotice(idx) == null) {
            throw new TravelException(NOT_FOUND_NOTICE, new Throwable());
        } else {
            return noticeService.updateNotice(noticeEntity);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteNotice
     * 2. ClassName  : NoticeController.java
     * 3. Comment    : 공지사항 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 11. 28.
     * </pre>
     */
    @ApiOperation(value = "공지사항 삭제", notes = "공지사항 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "공지사항 삭제 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @DeleteMapping("/{idx}")
    public Long deleteNotice(@PathVariable Long idx) {
        return noticeService.deleteNotice(idx);
    }

    /**
     * <pre>
     * 1. MethodName : toggleFixed
     * 2. ClassName  : NoticeController.java
     * 3. Comment    : 공지사항 고정글 설정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 11. 28.
     * </pre>
     */
    @ApiOperation(value = "공지사항 고정글 설정", notes = "공지사항을 고정글 설정한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "공지사항 고정글 설정 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping("/{idx}/toggle-fixed")
    public Boolean toggleFixed(@PathVariable Long idx) {
        return noticeService.toggleTopFixed(idx);
    }
}

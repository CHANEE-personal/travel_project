package com.travel.travel_project.api.travel;

import com.travel.travel_project.api.user.UserService;
import com.travel.travel_project.common.Page;
import com.travel.travel_project.common.SearchCommon;
import com.travel.travel_project.domain.common.EntityType;
import com.travel.travel_project.domain.file.CommonImageDTO;
import com.travel.travel_project.domain.file.CommonImageEntity;
import com.travel.travel_project.domain.travel.TravelDTO;
import com.travel.travel_project.domain.travel.TravelEntity;
import com.travel.travel_project.domain.travel.group.TravelGroupDTO;
import com.travel.travel_project.domain.travel.group.TravelGroupEntity;
import com.travel.travel_project.domain.travel.group.TravelGroupUserDTO;
import com.travel.travel_project.domain.travel.group.TravelGroupUserEntity;
import com.travel.travel_project.domain.travel.review.TravelReviewDTO;
import com.travel.travel_project.domain.travel.review.TravelReviewEntity;
import com.travel.travel_project.domain.travel.schedule.TravelScheduleDTO;
import com.travel.travel_project.domain.travel.schedule.TravelScheduleEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.net.URI;
import java.rmi.ServerError;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.ceil;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("/api/travel")
@Api(tags = "여행 소개 관련 API")
@RequiredArgsConstructor
public class TravelController {

    private final TravelService travelService;
    private final UserService userService;
    private final SearchCommon searchCommon;

    /**
     * <pre>
     * 1. MethodName : findTravelList
     * 2. ClassName  : TravelController.java
     * 3. Comment    : 여행지 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 10. 5.
     * </pre>
     */
    @ApiOperation(value = "여행지 리스트 조회", notes = "여행지 리스트를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "여행지 리스트 조회 성공", response = TravelDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/lists")
    public ResponseEntity<List<TravelDTO>> findTravelList(@RequestParam(required = false) Map<String, Object> paramMap, Page page) {
        return ResponseEntity.ok(travelService.findTravelList(searchCommon.searchCommon(page, paramMap)));
    }

    /**
     * <pre>
     * 1. MethodName : findOneTravel
     * 2. ClassName  : TravelController.java
     * 3. Comment    : 여행지 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 10. 5.
     * </pre>
     */
    @ApiOperation(value = "여행지 상세 조회", notes = "여행지를 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "여행지 상세 조회 성공", response = TravelDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/{idx}")
    public ResponseEntity<TravelDTO> findOneTravel(@PathVariable Long idx) {
        return ResponseEntity.ok(travelService.findOneTravel(idx));
    }

    /**
     * <pre>
     * 1. MethodName : insertTravel
     * 2. ClassName  : TravelController.java
     * 3. Comment    : 관리자 > 여행지 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 10. 5.
     * </pre>
     */
    @ApiOperation(value = "여행지 등록", notes = "여행지를 등록한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "여행지 등록 성공", response = TravelDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping
    public ResponseEntity<TravelDTO> insertTravel(@Valid @RequestBody TravelEntity travelEntity) {
        return ResponseEntity.created(URI.create("")).body(travelService.insertTravel(travelEntity));
    }

    /**
     * <pre>
     * 1. MethodName : insertTravelImage
     * 2. ClassName  : TravelController.java
     * 3. Comment    : 여행지 이미지 저장
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 07.
     * </pre>
     */
    @ApiOperation(value = "여행지 이미지 저장", notes = "여행지 이미지를 저장한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "여행지 이미지 등록성공", response = CommonImageDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping(value = "/{idx}/images", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<CommonImageDTO>> insertTravelImage(@PathVariable Long idx, @RequestParam(value = "images") List<MultipartFile> fileName) {
        return ResponseEntity.created(URI.create("")).body(travelService.insertTravelImage(fileName, CommonImageEntity.builder().typeIdx(idx).entityType(EntityType.TRAVEL).build()));
    }

    /**
     * <pre>
     * 1. MethodName : updateTravel
     * 2. ClassName  : TravelController.java
     * 3. Comment    : 관리자 > 여행지 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 10. 5.
     * </pre>
     */
    @ApiOperation(value = "여행지 수정", notes = "여행지를 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "여행지 수정 성공", response = TravelDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping("/{idx}")
    public ResponseEntity<TravelDTO> updateTravel(@Valid @RequestBody TravelEntity travelEntity) {
        if (travelService.findOneTravel(travelEntity.getIdx()) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(travelService.updateTravel(travelEntity));
    }

    /**
     * <pre>
     * 1. MethodName : deleteTravel
     * 2. ClassName  : TravelController.java
     * 3. Comment    : 관리자 > 여행지 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 10. 5.
     * </pre>
     */
    @ApiOperation(value = "여행지 삭제", notes = "여행지를 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "여행지 삭제 성공", response = Long.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @DeleteMapping("/{idx}")
    public ResponseEntity<Long> deleteTravel(@PathVariable Long idx) {
        if (travelService.findOneTravel(idx) == null) {
            return ResponseEntity.notFound().build();
        }
        travelService.deleteTravel(idx);
        return ResponseEntity.noContent().build();
    }

    /**
     * <pre>
     * 1. MethodName : favoriteTravel
     * 2. ClassName  : TravelController.java
     * 3. Comment    : 여행지 좋아요
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 10. 6.
     * </pre>
     */
    @ApiOperation(value = "여행지 좋아요", notes = "여행지를 좋아요 처리한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "여행지 좋아요 성공", response = Integer.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping("/{idx}/favorite")
    public ResponseEntity<Integer> favoriteTravel(@PathVariable Long idx) {
        if (travelService.findOneTravel(idx) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(travelService.favoriteTravel(idx));
    }

    /**
     * <pre>
     * 1. MethodName : popularityTravel
     * 2. ClassName  : TravelController.java
     * 3. Comment    : 인기 여행지 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 10. 14.
     * </pre>
     */
    @ApiOperation(value = "인기 여행지 리스트 조회", notes = "인기 여행지 리스트를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "인기 여행지 리스트 조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/popularity")
    public ResponseEntity<Map<String, Object>> popularityTravel(@RequestParam(required = false) Map<String, Object> paramMap, Page page) {
        Map<String, Object> travelMap = new HashMap<>();

        int travelCount = this.travelService.findTravelCount(searchCommon.searchCommon(page, paramMap));
        List<TravelDTO> travelList = new ArrayList<>();

        if (travelCount > 0) {
            travelList = this.travelService.popularityTravel(searchCommon.searchCommon(page, paramMap));
        }

        // 리스트 수
        travelMap.put("pageSize", page.getSize());
        // 전체 페이지 수
        travelMap.put("perPageListCnt", ceil((double) travelCount / page.getSize()));
        // 전체 아이템 수
        travelMap.put("travelListCnt", travelCount);

        travelMap.put("travelList", travelList);

        return ResponseEntity.ok().body(travelMap);
    }

    /**
     * <pre>
     * 1. MethodName : replyTravel
     * 2. ClassName  : TravelController.java
     * 3. Comment    : 여행지 댓글 달기
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 10. 30.
     * </pre>
     */
    @ApiOperation(value = "여행지 댓글 달기", notes = "여행지 댓글을 등록한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "여행지 댓글 등록", response = TravelReviewDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping(value = "/{idx}/reply")
    public ResponseEntity<TravelReviewDTO> replyTravel(@Valid @RequestBody TravelReviewEntity travelReviewEntity) {
        return ResponseEntity.created(URI.create("")).body(travelService.replyTravel(travelReviewEntity));
    }

    /**
     * <pre>
     * 1. MethodName : updateReplyTravel
     * 2. ClassName  : TravelController.java
     * 3. Comment    : 여행지 댓글 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 23.
     * </pre>
     */
    @ApiOperation(value = "여행지 댓글 수정", notes = "여행지 댓글을 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "여행지 댓글 수정", response = TravelReviewDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping("/{idx}/reply")
    public ResponseEntity<TravelReviewDTO> updateReplyTravel(@PathVariable Long idx, @Valid @RequestBody TravelReviewEntity travelReviewEntity) {
        if (travelService.detailReplyTravelReview(idx) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(travelService.updateReplyTravel(travelReviewEntity));
    }

    /**
     * <pre>
     * 1. MethodName : deleteReplyTravel
     * 2. ClassName  : TravelController.java
     * 3. Comment    : 여행지 댓글 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 23.
     * </pre>
     */
    @ApiOperation(value = "여행지 댓글 삭제", notes = "여행지 댓글을 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "여행지 댓글 삭제", response = Long.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @DeleteMapping("/{idx}/reply")
    public ResponseEntity<Long> deleteReplyTravel(@PathVariable Long idx) {
        if (travelService.detailReplyTravelReview(idx) == null) {
            return ResponseEntity.notFound().build();
        }
        travelService.deleteReplyTravel(idx);
        return ResponseEntity.noContent().build();
    }

    /**
     * <pre>
     * 1. MethodName : replyTravelReview
     * 2. ClassName  : TravelController.java
     * 3. Comment    : 여행지 댓글 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 23.
     * </pre>
     */
    @ApiOperation(value = "여행지 댓글 리스트 조회", notes = "여행지 댓글 리스트를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "여행지 댓글 리스트 조회 성공", response = List.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/{idx}/reply")
    public ResponseEntity<List<TravelReviewDTO>> replyTravelReview(@PathVariable Long idx) {
        return ResponseEntity.ok(travelService.replyTravelReview(idx));
    }

    /**
     * <pre>
     * 1. MethodName : togglePopular
     * 2. ClassName  : TravelController.java
     * 3. Comment    : 인기 여행지 선정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 10. 28.
     * </pre>
     */
    @ApiOperation(value = "인기 여행지 선정", notes = "인기 여행지 선정한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "인기 여행지 선정", response = Boolean.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping(value = "/{idx}/popular")
    public ResponseEntity<Boolean> togglePopular(@PathVariable Long idx) {
        return ResponseEntity.ok(travelService.togglePopular(idx));
    }

    /**
     * <pre>
     * 1. MethodName : findTravelGroupList
     * 2. ClassName  : TravelController.java
     * 3. Comment    : 여행지 그룹 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 25.
     * </pre>
     */
    @ApiOperation(value = "여행 그룹 리스트 조회", notes = "여행 그룹 리스트를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "여행 그룹 리스트 조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/group/lists")
    public ResponseEntity<Map<String, Object>> findTravelGroupList(@RequestParam Map<String, Object> paramMap, Page page) {
        Map<String, Object> groupMap = new HashMap<>();

        int travelGroupCount = this.travelService.findTravelGroupCount(searchCommon.searchCommon(page, paramMap));
        List<TravelGroupDTO> travelGroupList = new ArrayList<>();

        if (travelGroupCount > 0) {
            travelGroupList = this.travelService.findTravelGroupList(searchCommon.searchCommon(page, paramMap));
        }

        // 리스트 수
        groupMap.put("pageSize", page.getSize());
        // 전체 페이지 수
        groupMap.put("perPageListCnt", ceil((double) travelGroupCount / page.getSize()));
        // 전체 아이템 수
        groupMap.put("travelGroupListCnt", travelGroupCount);

        groupMap.put("travelGroupList", travelGroupList);

        return ResponseEntity.ok().body(groupMap);
    }

    /**
     * <pre>
     * 1. MethodName : findOneTravelGroup
     * 2. ClassName  : TravelController.java
     * 3. Comment    : 여행지 그룹 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 25.
     * </pre>
     */
    @ApiOperation(value = "여행지 그룹 상세 조회", notes = "여행지 그룹을 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "여행지 그룹 상세 조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/{idx}/group")
    public ResponseEntity<TravelGroupDTO> findOneTravelGroup(@PathVariable Long idx) {
        return ResponseEntity.ok(travelService.findOneTravelGroup(idx));
    }

    /**
     * <pre>
     * 1. MethodName : insertTravelGroup
     * 2. ClassName  : TravelController.java
     * 3. Comment    : 여행지 그룹 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 25.
     * </pre>
     */
    @ApiOperation(value = "여행지 그룹 등록", notes = "여행지 그룹을 등록한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "여행지 그룹 등록 성공", response = TravelGroupDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping("/group")
    public ResponseEntity<TravelGroupDTO> insertTravelGroup(@Valid @RequestBody TravelGroupEntity travelGroupEntity) {
        return ResponseEntity.created(URI.create("")).body(travelService.insertTravelGroup(travelGroupEntity));
    }

    /**
     * <pre>
     * 1. MethodName : updateTravelGroup
     * 2. ClassName  : TravelController.java
     * 3. Comment    : 여행지 그룹 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 25.
     * </pre>
     */
    @ApiOperation(value = "여행지 그룹 수정", notes = "여행지 그룹을 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "여행지 그룹 수정 성공", response = TravelGroupDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping("/{idx}/group")
    public ResponseEntity<TravelGroupDTO> updateTravelGroup(@PathVariable Long idx, @Valid @RequestBody TravelGroupEntity travelGroupEntity) {
        if (travelService.findOneTravelGroup(idx) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(travelService.updateTravelGroup(travelGroupEntity));
    }

    /**
     * <pre>
     * 1. MethodName : deleteTravelGroup
     * 2. ClassName  : TravelController.java
     * 3. Comment    : 여행지 그룹 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 25.
     * </pre>
     */
    @ApiOperation(value = "여행지 그룹 삭제", notes = "여행지 그룹을 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "여행지 그룹 삭제 성공", response = Long.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @DeleteMapping("/{idx}/group")
    public ResponseEntity<Long> deleteTravelGroup(@PathVariable Long idx) {
        if (travelService.findOneTravelGroup(idx) == null) {
            return ResponseEntity.notFound().build();
        }
        travelService.deleteTravelGroup(idx);
        return ResponseEntity.noContent().build();
    }

    /**
     * <pre>
     * 1. MethodName : insertTravelGroupUser
     * 2. ClassName  : TravelController.java
     * 3. Comment    : 유저 여행 그룹 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 27.
     * </pre>
     */
    @ApiOperation(value = "유저 여행 그룹 등록", notes = "유저 여행 그룹을 등록한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "유저 여행 그룹 등록 성공", response = TravelGroupUserDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping("/group_user")
    public ResponseEntity<TravelGroupUserDTO> insertTravelGroupUser(@Valid @RequestBody TravelGroupUserEntity travelGroupUserEntity) {
        return ResponseEntity.created(URI.create("")).body(travelService.insertTravelGroupUser(travelGroupUserEntity));
    }

    /**
     * <pre>
     * 1. MethodName : deleteTravelGroupUser
     * 2. ClassName  : TravelController.java
     * 3. Comment    : 유저 여행 그룹 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 27.
     * </pre>
     */
    @ApiOperation(value = "유저 여행 그룹 삭제", notes = "유저 여행 그룹을 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "유저 여행 그룹 삭제 성공", response = Long.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @DeleteMapping("/{idx}/group_user")
    public ResponseEntity<Long> deleteTravelGroupUser(@PathVariable Long idx) {
        if (travelService.findOneTravel(idx) == null) {
            return ResponseEntity.notFound().build();
        }
        travelService.deleteTravelGroupUser(idx);
        return ResponseEntity.noContent().build();
    }

    /**
     * <pre>
     * 1. MethodName : insertTravelSchedule
     * 2. ClassName  : TravelController.java
     * 3. Comment    : 유저 여행 스케줄 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 12. 13.
     * </pre>
     */
    @ApiOperation(value = "유저 여행 스케줄 등록", notes = "유저 여행 스케줄을 등록한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "유저 여행 스케줄 등록 성공", response = TravelScheduleDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping("/schedule")
    public ResponseEntity<TravelScheduleDTO> insertTravelSchedule(@RequestBody TravelScheduleEntity travelScheduleEntity) {
        return ResponseEntity.created(URI.create("")).body(travelService.insertTravelSchedule(travelScheduleEntity));
    }

    /**
     * <pre>
     * 1. MethodName : updateTravelSchedule
     * 2. ClassName  : TravelController.java
     * 3. Comment    : 유저 여행 스케줄 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 12. 13.
     * </pre>
     */
    @ApiOperation(value = "유저 여행 스케줄 수정", notes = "유저 여행 스케줄을 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "유저 여행 스케줄 수정 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping("/{idx}/schedule")
    public ResponseEntity<TravelScheduleDTO> updateTravelSchedule(@PathVariable Long idx, @RequestBody TravelScheduleEntity travelScheduleEntity) {
        if (userService.findOneUser(idx) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(travelService.updateTravelSchedule(travelScheduleEntity));
    }

    /**
     * <pre>
     * 1. MethodName : deleteTravelSchedule
     * 2. ClassName  : TravelController.java
     * 3. Comment    : 유저 여행 스케줄 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 12. 13.
     * </pre>
     */
    @ApiOperation(value = "유저 여행 스케줄 삭제", notes = "유저 여행 스케줄을 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "유저 여행 스케줄 삭제 성공", response = Long.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @DeleteMapping("/{idx}/schedule")
    public ResponseEntity<Long> deleteTravelSchedule(@PathVariable Long idx) {
        if (userService.findOneUser(idx) == null) {
            return ResponseEntity.notFound().build();
        }
        travelService.deleteTravelSchedule(idx);
        return ResponseEntity.noContent().build();
    }
}

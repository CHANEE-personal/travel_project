package com.travel.travel_project.api.travel;

import com.travel.travel_project.common.Paging;
import com.travel.travel_project.domain.common.EntityType;
import com.travel.travel_project.domain.travel.image.TravelImageDTO;
import com.travel.travel_project.domain.travel.image.TravelImageEntity;
import com.travel.travel_project.domain.travel.TravelDTO;
import com.travel.travel_project.domain.travel.TravelEntity;
import com.travel.travel_project.domain.travel.festival.TravelFestivalDTO;
import com.travel.travel_project.domain.travel.festival.TravelFestivalEntity;
import com.travel.travel_project.domain.travel.group.TravelGroupDTO;
import com.travel.travel_project.domain.travel.group.TravelGroupEntity;
import com.travel.travel_project.domain.travel.recommend.TravelRecommendDTO;
import com.travel.travel_project.domain.travel.recommend.TravelRecommendEntity;
import com.travel.travel_project.domain.travel.review.TravelReviewDTO;
import com.travel.travel_project.domain.travel.review.TravelReviewEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.net.URI;
import java.rmi.ServerError;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("/api/travel")
@Api(tags = "여행 소개 관련 API")
@RequiredArgsConstructor
public class TravelController {

    private final TravelService travelService;

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
    public ResponseEntity<Page<TravelDTO>> findTravelList(@RequestParam(required = false) Map<String, Object> paramMap, Paging paging) {
        return ResponseEntity.ok(travelService.findTravelList(paramMap, paging.getPageRequest(paging.getPageNum(), paging.getSize())));
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
            @ApiResponse(code = 201, message = "여행지 이미지 등록성공", response = TravelImageDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping(value = "/{idx}/images", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<TravelImageDTO>> insertTravelImage(@PathVariable Long idx, @RequestParam(value = "images") List<MultipartFile> fileName) {
        return ResponseEntity.created(URI.create("")).body(travelService.insertTravelImage(idx, fileName, TravelImageEntity.builder().entityType(EntityType.TRAVEL).build()));
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
    public ResponseEntity<TravelDTO> updateTravel(@PathVariable Long idx, @Valid @RequestBody TravelEntity travelEntity) {
        return ResponseEntity.ok(travelService.updateTravel(idx, travelEntity));
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
    public ResponseEntity<Page<TravelDTO>> popularityTravel(@RequestParam(required = false) Map<String, Object> paramMap, Paging paging) {
        return ResponseEntity.ok(travelService.popularityTravel(paramMap, paging.getPageRequest(paging.getPageNum(), paging.getSize())));
    }

    /**
     * <pre>
     * 1. MethodName : reviewTravel
     * 2. ClassName  : TravelController.java
     * 3. Comment    : 여행지 리뷰 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 10. 30.
     * </pre>
     */
    @ApiOperation(value = "여행지 리뷰 등록", notes = "여행지 리뷰를 등록한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "여행지 리뷰 등록", response = TravelReviewDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping(value = "/{idx}/review")
    public ResponseEntity<TravelReviewDTO> reviewTravel(@PathVariable Long idx, @Valid @RequestBody TravelReviewEntity travelReviewEntity) {
        return ResponseEntity.created(URI.create("")).body(travelService.reviewTravel(idx, travelReviewEntity));
    }

    /**
     * <pre>
     * 1. MethodName : updateReviewTravel
     * 2. ClassName  : TravelController.java
     * 3. Comment    : 여행지 리뷰 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 23.
     * </pre>
     */
    @ApiOperation(value = "여행지 리뷰 수정", notes = "여행지 리뷰를 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "여행지 리뷰 수정", response = TravelReviewDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping("/{idx}/review")
    public ResponseEntity<TravelReviewDTO> updateReviewTravel(@PathVariable Long idx, @Valid @RequestBody TravelReviewEntity travelReviewEntity) {
        return ResponseEntity.ok(travelService.updateReviewTravel(idx, travelReviewEntity));
    }

    /**
     * <pre>
     * 1. MethodName : deleteReviewTravel
     * 2. ClassName  : TravelController.java
     * 3. Comment    : 여행지 리뷰 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 23.
     * </pre>
     */
    @ApiOperation(value = "여행지 리뷰 삭제", notes = "여행지 리뷰를 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "여행지 댓글 삭제", response = Long.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @DeleteMapping("/{idx}/review")
    public ResponseEntity<Long> deleteReviewTravel(@PathVariable Long idx) {
        travelService.deleteReviewTravel(idx);
        return ResponseEntity.noContent().build();
    }

    /**
     * <pre>
     * 1. MethodName : travelReviewList
     * 2. ClassName  : TravelController.java
     * 3. Comment    : 여행지 리뷰 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 23.
     * </pre>
     */
    @ApiOperation(value = "여행지 리뷰 리스트 조회", notes = "여행지 리뷰 리스트를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "여행지 리뷰 리스트 조회 성공", response = List.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/{idx}/reply")
    public ResponseEntity<List<TravelReviewDTO>> travelReviewList(@PathVariable Long idx) {
        return ResponseEntity.ok(travelService.travelReviewList(idx));
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
            @ApiResponse(code = 200, message = "여행 그룹 리스트 조회 성공", response = Page.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/group/lists")
    public ResponseEntity<Page<TravelGroupDTO>> findTravelGroupList(@RequestParam Map<String, Object> paramMap, Paging paging) {
        return ResponseEntity.ok(travelService.findTravelGroupList(paramMap, paging.getPageRequest(paging.getPageNum(), paging.getSize())));
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
    @PostMapping("/{idx}/group")
    public ResponseEntity<TravelGroupDTO> insertTravelGroup(@PathVariable Long idx, @Valid @RequestBody TravelGroupEntity travelGroupEntity) {
        return ResponseEntity.created(URI.create("")).body(travelService.insertTravelGroup(idx, travelGroupEntity));
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
    @PutMapping("/{idx}/group/{groupIdx}")
    public ResponseEntity<TravelGroupDTO> updateTravelGroup(@PathVariable Long groupIdx, @Valid @RequestBody TravelGroupEntity travelGroupEntity) {
        return ResponseEntity.ok(travelService.updateTravelGroup(groupIdx, travelGroupEntity));
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
    @DeleteMapping("/group/{groupIdx}")
    public ResponseEntity<Long> deleteTravelGroup(@PathVariable Long groupIdx) {
        travelService.deleteTravelGroup(groupIdx);
        return ResponseEntity.noContent().build();
    }

    /**
     * <pre>
     * 1. MethodName : findTravelRecommendList
     * 2. ClassName  : TravelController.java
     * 3. Comment    : 여행지 추천 검색어 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 04.
     * </pre>
     */
    @ApiOperation(value = "여행지 추천 검색어 리스트 조회", notes = "여행지 추천 검색어 리스트를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "여행지 추천 검색어 리스트 조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/recommend")
    public ResponseEntity<Page<TravelRecommendDTO>> findTravelRecommendList(@RequestParam Map<String, Object> paramMap, Paging paging) {
        return ResponseEntity.ok().body(travelService.findTravelRecommendList(paramMap, paging.getPageRequest(paging.getPageNum(), paging.getSize())));
    }

    /**
     * <pre>
     * 1. MethodName : findOneTravelRecommend
     * 2. ClassName  : TravelController.java
     * 3. Comment    : 여행지 추천 검색어 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 04.
     * </pre>
     */
    @ApiOperation(value = "여행지 추천 검색어 상세 조회", notes = "여행지 추천 검색어 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "여행지 추천 검색어 상세 조회 성공", response = TravelRecommendDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/{idx}/recommend")
    public ResponseEntity<TravelRecommendDTO> findOneTravelRecommend(@PathVariable Long idx) {
        return ResponseEntity.ok(travelService.findOneTravelRecommend(idx));
    }

    /**
     * <pre>
     * 1. MethodName : insertTravelRecommend
     * 2. ClassName  : TravelController.java
     * 3. Comment    : 여행지 추천 검색어 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 04.
     * </pre>
     */
    @ApiOperation(value = "여행지 추천 검색어 등록", notes = "여행지 추천 검색어를 등록한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "여행지 추천 검색어 등록 성공", response = TravelRecommendDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping("/recommend")
    public ResponseEntity<TravelRecommendDTO> insertTravelRecommend(@Valid @RequestBody TravelRecommendEntity travelRecommendEntity) {
        return ResponseEntity.created(URI.create("")).body(travelService.insertTravelRecommend(travelRecommendEntity));
    }

    /**
     * <pre>
     * 1. MethodName : updateTravelRecommend
     * 2. ClassName  : TravelController.java
     * 3. Comment    : 여행지 추천 검색어 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 04.
     * </pre>
     */
    @ApiOperation(value = "여행지 추천 검색어 수정", notes = "여행지 추천 검색어를 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "여행지 추천 검색어 수정 성공", response = TravelRecommendDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping(value = "/{idx}/recommend")
    public ResponseEntity<TravelRecommendDTO> updateTravelRecommend(@PathVariable Long idx, @Valid @RequestBody TravelRecommendEntity travelRecommendEntity) {
        return ResponseEntity.ok(travelService.updateTravelRecommend(idx, travelRecommendEntity));
    }

    /**
     * <pre>
     * 1. MethodName : deleteTravelRecommend
     * 2. ClassName  : TravelController.java
     * 3. Comment    : 여행지 추천 검색어 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 04.
     * </pre>
     */
    @ApiOperation(value = "여행지 추천 검색어 삭제", notes = "여행지 추천 검색어를 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "여행지 추천 검색어 삭제 성공", response = Long.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @DeleteMapping(value = "/{idx}/recommend")
    public ResponseEntity<Long> deleteTravelRecommend(@PathVariable Long idx) {
        travelService.deleteTravelRecommend(idx);
        return ResponseEntity.noContent().build();
    }

    /**
     * <pre>
     * 1. MethodName : rankingTravelKeyword
     * 2. ClassName  : TravelController.java
     * 3. Comment    : 여행지 검색어 랭킹 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 07.
     * </pre>
     */
    @ApiOperation(value = "여행지 검색어 랭킹 조회", notes = "여행지 검색어 랭킹을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "여행지 검색어 랭킹 조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/rank")
    public ResponseEntity<Map<String, Object>> rankingTravelKeyword() {
        Map<String, Object> rankMap = new HashMap<>();
        rankMap.put("rankList", travelService.rankingTravelKeyword());
        return ResponseEntity.ok().body(rankMap);
    }

    /**
     * <pre>
     * 1. MethodName : findTravelKeyword
     * 2. ClassName  : TravelController.java
     * 3. Comment    : 추천 검색어 or 랭킹 검색어를 통한 여행지 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 07.
     * </pre>
     */
    @ApiOperation(value = "추천 or 랭킹 검색어를 통한 여행지 조회", notes = "추천 or 랭킹 검색어를 통해 여행지를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "여행지 검색 조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/keyword")
    public ResponseEntity<Map<String, Object>> findTravelKeyword(@RequestParam String keyword) {
        Map<String, Object> travelMap = new HashMap<>();
        travelMap.put("travelList", travelService.findTravelKeyword(keyword));
        return ResponseEntity.ok().body(travelMap);
    }

    /**
     * <pre>
     * 1. MethodName : findTravelFestivalGroup
     * 2. ClassName  : TravelController.java
     * 3. Comment    : 축제 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 08.
     * </pre>
     */
    @ApiOperation(value = "축제 리스트 조회", notes = "축제 리스트를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "축제 리스트 조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/festival/list/{month}")
    public ResponseEntity<Map<String, Object>> findTravelFestivalGroup(@PathVariable Integer month) {
        Map<String, Object> festivalMap = new HashMap<>();
        festivalMap.put("festivalGroup", travelService.findTravelFestivalGroup(month));
        return ResponseEntity.ok().body(festivalMap);
    }

    /**
     * <pre>
     * 1. MethodName : findTravelFestivalList
     * 2. ClassName  : TravelController.java
     * 3. Comment    : 월과 일을 이용한 축제 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 08.
     * </pre>
     */
    @ApiOperation(value = "축제 리스트 조회", notes = "축제 리스트를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "축제 리스트 조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/festival/list/{month}/{day}")
    public ResponseEntity<Map<String, Object>> findTravelFestivalList(@PathVariable Integer month, @PathVariable Integer day) {
        Map<String, Object> festivalMap = new HashMap<>();
        festivalMap.put("festivalList", travelService.findTravelFestivalList(TravelFestivalEntity.builder()
                .festivalMonth(month).festivalDay(day).build()));
        return ResponseEntity.ok().body(festivalMap);
    }

    /**
     * <pre>
     * 1. MethodName : findOneTravelFestival
     * 2. ClassName  : TravelController.java
     * 3. Comment    : 축제 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 08.
     * </pre>
     */
    @ApiOperation(value = "축제 상세 조회", notes = "축제를 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "축제 상세 조회 성공", response = TravelFestivalDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/festival/{idx}")
    public ResponseEntity<TravelFestivalDTO> findOneTravelFestival(@PathVariable Long idx) {
        return ResponseEntity.ok(travelService.findOneTravelFestival(idx));
    }

    /**
     * <pre>
     * 1. MethodName : insertTravelFestival
     * 2. ClassName  : TravelController.java
     * 3. Comment    : 축제 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 08.
     * </pre>
     */
    @ApiOperation(value = "축제 등록", notes = "축제를 등록한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "축제 등록 성공", response = TravelFestivalDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping("/festival")
    public ResponseEntity<TravelFestivalDTO> insertTravelFestival(@Valid @RequestBody TravelFestivalEntity travelFestivalEntity) {
        return ResponseEntity.created(URI.create("")).body(travelService.insertTravelFestival(travelFestivalEntity));
    }

    /**
     * <pre>
     * 1. MethodName : updateTravelFestival
     * 2. ClassName  : TravelController.java
     * 3. Comment    : 축제 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 08.
     * </pre>
     */
    @ApiOperation(value = "축제 수정", notes = "축제를 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "축제 수정 성공", response = TravelFestivalDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping("/festival/{idx}")
    public ResponseEntity<TravelFestivalDTO> updateTravelFestival(@PathVariable Long idx, @Valid @RequestBody TravelFestivalEntity travelFestivalEntity) {
        return ResponseEntity.ok(travelService.updateTravelFestival(idx, travelFestivalEntity));
    }

    /**
     * <pre>
     * 1. MethodName : deleteTravelFestival
     * 2. ClassName  : TravelController.java
     * 3. Comment    : 축제 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 08.
     * </pre>
     */
    @ApiOperation(value = "축제 삭제", notes = "축제를 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "축제 삭제 성공", response = Long.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @DeleteMapping("/festival/{idx}")
    public ResponseEntity<Long> deleteTravelFestival(@PathVariable Long idx) {
        travelService.deleteTravelFestival(idx);
        return ResponseEntity.noContent().build();
    }
}

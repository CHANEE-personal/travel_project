package com.travel.api.coupon;

import com.travel.api.coupon.domain.CouponDto;
import com.travel.api.coupon.domain.CouponEntity;
import com.travel.common.Paging;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
@RequiredArgsConstructor
@RequestMapping("/admin/coupon")
public class CouponController {

    private final CouponService couponService;

    /**
     * <pre>
     * 1. MethodName : findCouponList
     * 2. ClassName  : CouponController.java
     * 3. Comment    : 쿠폰 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 02. 05.
     * </pre>
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "쿠폰 리스트 조회", notes = "쿠폰 리스트를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "쿠폰 리스트 조회 성공", response = Page.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping
    public ResponseEntity<List<CouponDto>> findCouponList(@RequestParam(required = false) Map<String, Object> paramMap, Paging paging) {
        return ResponseEntity.ok(couponService.findCouponList(paramMap, PageRequest.of(paging.getPageNum(), paging.getSize())));
    }

    /**
     * <pre>
     * 1. MethodName : findOneCoupon
     * 2. ClassName  : CouponController.java
     * 3. Comment    : 쿠폰 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 02. 05.
     * </pre>
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "쿠폰 상세 조회", notes = "쿠폰을 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "쿠폰 상세 조회 성공", response = CouponDto.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{idx}")
    public ResponseEntity<CouponDto> findOneCoupon(@PathVariable Long idx) {
        return ResponseEntity.ok(couponService.findOneCoupon(idx));
    }

    /**
     * <pre>
     * 1. MethodName : insertCoupon
     * 2. ClassName  : CouponController.java
     * 3. Comment    : 쿠폰 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 02. 05.
     * </pre>
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "쿠폰 등록", notes = "쿠폰을 등록한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "쿠폰 등록 성공", response = CouponDto.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping
    public ResponseEntity<CouponDto> insertCoupon(@Valid @RequestBody CouponEntity couponEntity) {
        return ResponseEntity.created(URI.create("")).body(couponService.insertCoupon(couponEntity));
    }

    /**
     * <pre>
     * 1. MethodName : updateCoupon
     * 2. ClassName  : CouponController.java
     * 3. Comment    : 쿠폰 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 02. 05.
     * </pre>
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "쿠폰 수정", notes = "쿠폰을 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "쿠폰 수정 성공", response = CouponDto.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping("/{idx}")
    public ResponseEntity<CouponDto> updateCoupon(@PathVariable Long idx, @Valid @RequestBody CouponEntity couponEntity) {
        return ResponseEntity.ok(couponService.updateCoupon(idx, couponEntity));
    }

    /**
     * <pre>
     * 1. MethodName : deleteCoupon
     * 2. ClassName  : CouponController.java
     * 3. Comment    : 쿠폰 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 02. 05.
     * </pre>
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "쿠폰 삭제", notes = "쿠폰을 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "쿠폰 삭제 성공", response = Long.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @DeleteMapping("/{idx}")
    public ResponseEntity<Long> deleteCoupon(@PathVariable Long idx) {
        couponService.deleteCoupon(idx);
        return ResponseEntity.noContent().build();
    }
}

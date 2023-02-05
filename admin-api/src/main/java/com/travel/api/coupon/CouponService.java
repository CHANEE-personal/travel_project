package com.travel.api.coupon;

import com.travel.api.coupon.domain.CouponDto;
import com.travel.api.coupon.domain.CouponEntity;
import com.travel.api.coupon.domain.repository.CouponRepository;
import com.travel.exception.TravelException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.travel.exception.ApiExceptionType.*;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    private CouponEntity oneCoupon(Long idx) {
        return couponRepository.findById(idx)
                .orElseThrow(() -> new TravelException(NOT_FOUND_COUPON));
    }

    /**
     * <pre>
     * 1. MethodName : findCouponList
     * 2. ClassName  : CouponService.java
     * 3. Comment    : 쿠폰 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 02. 05.
     * </pre>
     */
    @Transactional(readOnly = true)
    public List<CouponDto> findCouponList(Map<String, Object> couponMap, PageRequest pageRequest) {
        return couponRepository.findAll(pageRequest)
                .stream().map(CouponEntity::toDto).collect(Collectors.toList());
    }

    /**
     * <pre>
     * 1. MethodName : findOneCoupon
     * 2. ClassName  : CouponService.java
     * 3. Comment    : 쿠폰 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 02. 05.
     * </pre>
     */
    @Transactional(readOnly = true)
    public CouponDto findOneCoupon(Long idx) {
        return CouponEntity.toDto(oneCoupon(idx));
    }

    /**
     * <pre>
     * 1. MethodName : insertCoupon
     * 2. ClassName  : CouponService.java
     * 3. Comment    : 쿠폰 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 02. 05.
     * </pre>
     */
    @Transactional
    public CouponDto insertCoupon(CouponEntity couponEntity) {
        try {
            return CouponEntity.toDto(couponRepository.save(couponEntity));
        } catch (Exception e) {
            throw new TravelException(ERROR_COUPON);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updateCoupon
     * 2. ClassName  : CouponService.java
     * 3. Comment    : 쿠폰 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 02. 05.
     * </pre>
     */
    @Transactional
    public CouponDto updateCoupon(Long idx, CouponEntity couponEntity) {
        try {
            oneCoupon(idx).update(couponEntity);
            return CouponEntity.toDto(couponEntity);
        } catch (Exception e) {
            throw new TravelException(ERROR_UPDATE_COUPON);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteCoupon
     * 2. ClassName  : CouponService.java
     * 3. Comment    : 쿠폰 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 02. 05.
     * </pre>
     */
    @Transactional
    public Long deleteCoupon(Long idx) {
        try {
            couponRepository.deleteById(idx);
            return idx;
        } catch (Exception e) {
            throw new TravelException(ERROR_DELETE_COUPON);
        }
    }
}

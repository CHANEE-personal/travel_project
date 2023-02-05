package com.travel.api.coupon;

import com.travel.api.AdminCommonServiceTest;
import com.travel.api.coupon.domain.CouponDto;
import com.travel.api.coupon.domain.CouponEntity;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@DisplayName("Coupon Service Test")
class CouponServiceTest extends AdminCommonServiceTest {

    private final CouponService couponService;

    @Test
    @DisplayName("쿠폰 리스트 조회 테스트")
    void 쿠폰리스트조회테스트() {
        // when
        Map<String, Object> couponMap = new HashMap<>();
        List<CouponDto> couponList = couponService.findCouponList(couponMap, PageRequest.of(0, 3, Sort.by("idx").descending()));

        // then
        assertThat(couponList.get(0).getTitle()).isEqualTo("10% 쿠폰");
        assertThat(couponList.get(0).getDescription()).isEqualTo("10% 쿠폰");
        assertThat(couponList.get(0).getPercentage()).isEqualTo(10);
    }

    @Test
    @DisplayName("쿠폰 상세 조회 테스트")
    void 쿠폰상세조회테스트() {
        // when
        CouponDto oneCoupon = couponService.findOneCoupon(couponDTO.getIdx());

        // then
        assertThat(oneCoupon.getTitle()).isEqualTo("10% 쿠폰");
        assertThat(oneCoupon.getDescription()).isEqualTo("10% 쿠폰");
        assertThat(oneCoupon.getPercentage()).isEqualTo(10);
    }

    @Test
    @DisplayName("쿠폰 등록 테스트")
    void 쿠폰등록테스트() {
        // given
        CouponEntity insertCoupon = CouponEntity.builder()
                .title("20% 할인 쿠폰")
                .description("20% 할인 쿠폰")
                .salePrice(0)
                .percentage(20)
                .percentageStatus(true)
                .count(1)
                .startDate(LocalDateTime.of(2022, 2, 1, 0, 0, 0))
                .endDate(LocalDateTime.of(2022, 2, 28, 23, 59, 59))
                .status(true)
                .build();

        // when
        CouponDto couponDto = couponService.insertCoupon(insertCoupon);

        // then
        assertThat(couponDto.getTitle()).isEqualTo("20% 할인 쿠폰");
        assertThat(couponDto.getDescription()).isEqualTo("20% 할인 쿠폰");
        assertThat(couponDto.getPercentage()).isEqualTo(20);
    }

    @Test
    @DisplayName("쿠폰 수정 테스트")
    void 쿠폰수정테스트() {
        CouponEntity updateCoupon = CouponEntity.builder()
                .idx(couponEntity.getIdx())
                .title("20% 할인 쿠폰")
                .description("20% 할인 쿠폰")
                .salePrice(0)
                .percentage(20)
                .percentageStatus(true)
                .count(1)
                .startDate(LocalDateTime.of(2022, 2, 1, 0, 0, 0))
                .endDate(LocalDateTime.of(2022, 2, 28, 23, 59, 59))
                .status(true)
                .build();

        // when
        CouponDto couponDto = couponService.updateCoupon(updateCoupon.getIdx(), updateCoupon);

        // then
        assertThat(couponDto.getTitle()).isEqualTo("20% 할인 쿠폰");
        assertThat(couponDto.getDescription()).isEqualTo("20% 할인 쿠폰");
        assertThat(couponDto.getPercentage()).isEqualTo(20);
    }

    @Test
    @DisplayName("쿠폰 삭제 테스트")
    void 쿠폰삭제테스트() {
        Long deleteIdx = couponService.deleteCoupon(couponEntity.getIdx());
        assertThat(deleteIdx).isEqualTo(couponEntity.getIdx());
    }
}

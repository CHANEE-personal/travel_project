package com.travel.api.coupon.domain;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicUpdate
@Table(name = "travel_coupon")
public class CouponEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @Column(name = "title")
    @NotEmpty(message = "쿠폰명 입력은 필수입니다.")
    private String title;

    @Column(name = "description")
    @Lob
    @NotEmpty(message = "쿠폰 상세 내용 입력은 필수입니다.")
    private String description;

    @Column(name = "percentage_status")
    private Boolean percentageStatus;

    @Column(name = "start_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;

    @NotNull(message = "쿠폰 발급 갯수 입력 필수입니다.")
    @Column(name = "count")
    private int count;

    @Column(name = "percentage")
    private int percentage;

    @Column(name = "sale_price")
    private int salePrice;

    @Column(name = "status")
    private Boolean status;

    public void update(CouponEntity couponEntity) {
        this.title = couponEntity.getTitle();
        this.description = couponEntity.getDescription();
        this.count = couponEntity.getCount();
        this.percentage = couponEntity.getPercentage();
        this.percentageStatus = couponEntity.getPercentageStatus();
        this.startDate = couponEntity.getStartDate();
        this.endDate = couponEntity.getEndDate();
        this.salePrice = couponEntity.getSalePrice();
        this.status = couponEntity.getStatus();
    }

    public static CouponDto toDto(CouponEntity entity) {
        if (entity == null) return null;
        return CouponDto.builder()
                .idx(entity.getIdx())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .percentageStatus(entity.getPercentageStatus())
                .percentage(entity.getPercentage())
                .salePrice(entity.getSalePrice())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .status(entity.getStatus())
                .count(entity.getCount())
                .build();
    }
}

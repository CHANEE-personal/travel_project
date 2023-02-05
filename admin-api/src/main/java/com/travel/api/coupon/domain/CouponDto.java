package com.travel.api.coupon.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@Getter
@Builder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ApiModel(value = "쿠폰 관련")
public class CouponDto {

    @ApiModelProperty(required = true, value = "idx", hidden = true)
    private Long idx;

    @NotEmpty(message = "쿠폰명 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "title")
    private String title;

    @NotEmpty(message = "쿠폰 상세 내용 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "description")
    private String description;

    @ApiModelProperty(required = true, value = "percentageStatus")
    private Boolean percentageStatus;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @ApiModelProperty(required = true, value = "시작 일자", hidden = true)
    private LocalDateTime startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @ApiModelProperty(required = true, value = "마감 일자", hidden = true)
    private LocalDateTime endDate;

    @NotNull(message = "쿠폰 발급 갯수 입력은 필수입니다.")
    @ApiModelProperty(value = "쿠폰 발급 갯수((ex)0)", example = "1")
    private int count;

    @ApiModelProperty(value = "쿠폰 퍼센트((ex)10)", example = "10")
    private int percentage;

    @ApiModelProperty(value = "쿠폰 할인 가격((ex)10,000)", example = "10,000")
    private int salePrice;

    @ApiModelProperty(value = "쿠폰 사용 가능 여부((ex)true)", example = "true")
    private Boolean status;
}

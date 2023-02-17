package com.travel.api.travel.domain.review;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Builder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ApiModel
public class TravelReviewDto  {

    @ApiModelProperty(required = true, value = "idx", hidden = true, example = "1")
    private Long idx;

    @NotEmpty(message = "여행지 제목 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "여행지명((ex)서울)")
    private String travelTitle;

    @NotEmpty(message = "여행지 상세 내용 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "여행지내용((ex)서울)")
    private String travelDescription;

    @NotEmpty(message = "여행지 리뷰 제목은 필수입니다.")
    @ApiModelProperty(required = true, value = "여행지 리뷰 제목((ex)이 여행지는...)")
    private String reviewTitle;

    @NotEmpty(message = "여행지 리뷰 상세 내용 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "여행지 리뷰 상세 내용((ex)이 여행지는...)")
    private String reviewDescription;

    @ApiModelProperty(value = "리뷰 조회수((ex)0)", example = "1")
    private int viewCount;

    @ApiModelProperty(value = "여행지 좋아요 수((ex)0)", example = "1")
    private int favoriteCount;

    @ApiModelProperty(required = true, value = "여행지 노출 여부((ex)Y,N)")
    private String visible;

    @ApiModelProperty(required = true, value = "인기 여행지 여부((ex)true, false)")
    private Boolean popular;
}

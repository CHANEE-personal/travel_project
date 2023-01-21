package com.travel.api.travel.domain;

import com.travel.api.common.domain.NewCommonDto;
import com.travel.api.travel.domain.image.TravelImageDto;
import com.travel.api.travel.domain.review.TravelReviewDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ApiModel
public class TravelDto extends NewCommonDto {

    @ApiModelProperty(required = true, value = "idx", hidden = true, example = "1")
    private Long idx;

    @NotEmpty(message = "여행지 제목은 필수입니다.")
    @ApiModelProperty(required = true, value = "여행지 제목((ex)이 여행지는...)")
    private String travelTitle;

    @NotEmpty(message = "여행지 상세 내용 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "여행지 상세 내용((ex)이 여행지는...)")
    private String travelDescription;

    @NotEmpty(message = "여행지 코드 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "여행지 코드((ex)1(서울))")
    private Integer travelCode;

    @NotEmpty(message = "여행지 주소 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "여행지 상세 주소((ex)인천 광역시 서구...)")
    private String travelAddress;

    @NotEmpty(message = "여행지 우편 주소 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "여행지 우편 주소((ex)xxx-xxx)")
    private String travelZipCode;

    @ApiModelProperty(value = "여행지 조회수((ex)0)", example = "1")
    private int viewCount;

    @ApiModelProperty(value = "여행지 좋아요 수((ex)0)", example = "1")
    private int favoriteCount;

    @ApiModelProperty(required = true, value = "여행지 노출 여부((ex)Y,N)")
    private String visible;

    @ApiModelProperty(required = true, value = "인기 여행지 여부((ex)true, false)")
    private Boolean popular;

    @ApiModelProperty(value = "travelReviewList", hidden = true)
    private List<TravelReviewDto> reviewList = new ArrayList<>();

    @ApiModelProperty(value = "travelImageList", hidden = true)
    private List<TravelImageDto> imageList = new ArrayList<>();
}

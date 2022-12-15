package com.travel.travel_project.domain.post;

import com.travel.travel_project.domain.common.NewCommonDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "게시글 관련 변수")
public class PostDTO extends NewCommonDTO {
    @ApiModelProperty(required = true, value = "rnum", hidden = true, example = "1")
    private Integer rowNum;

    @ApiModelProperty(required = true, value = "idx", hidden = true, example = "1")
    private Long idx;

    @NotEmpty(message = "게시글 제목은 필수입니다.")
    @ApiModelProperty(required = true, value = "게시글 제목((ex)이 여행지는...)")
    private String postTitle;

    @NotEmpty(message = "게시글 상세 내용 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "게시글 상세 내용((ex)이 여행지는...)")
    private String postDescription;

    @ApiModelProperty(required = true, value = "부모 idx((ex)1)")
    private Long postParentIdx;

    @ApiModelProperty(required = true, value = "최상위 idx((ex)1)")
    private Long postTopIdx;

    @ApiModelProperty(value = "게시글 조회수((ex)0)", example = "1")
    private int viewCount;

    @ApiModelProperty(value = "게시글 좋아요 수((ex)0)", example = "1")
    private int favoriteCount;

    @ApiModelProperty(required = true, value = "여행지 노출 여부((ex)Y,N)")
    private String visible;

    @ApiModelProperty(required = true, value = "인기 게시글 여부((ex)true, false)")
    private Boolean popular;
}

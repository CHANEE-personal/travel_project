package com.travel.api.post.domain.reply;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ApiModel(value = "댓글 관련 변수")
public class ReplyDTO {

    @ApiModelProperty(required = true, value = "idx", hidden = true, example = "1")
    private Long idx;

    @NotEmpty(message = "댓글 제목은 필수입니다.")
    @ApiModelProperty(required = true, value = "댓글 제목((ex)이 여행지는...)")
    private String commentTitle;

    @NotEmpty(message = "댓글 상세 내용 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "댓글 상세 내용((ex)이 여행지는...)")
    private String commentDescription;

    @ApiModelProperty(required = true, value = "부모 idx((ex)1)")
    private Long postIdx;

    @ApiModelProperty(required = true, value = "최상위 idx((ex)1)")
    private Long parentIdx;

    @ApiModelProperty(value = "게시글 좋아요 수((ex)0)", example = "1")
    private int favoriteCount;

    @ApiModelProperty(required = true, value = "여행지 노출 여부((ex)Y,N)")
    private String visible;
}

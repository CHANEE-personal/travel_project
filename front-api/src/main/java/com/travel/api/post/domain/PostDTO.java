package com.travel.api.post.domain;

import com.travel.api.common.domain.NewCommonDTO;
import com.travel.api.post.domain.reply.ReplyDTO;
import com.travel.api.travel.domain.image.TravelImageDTO;
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
@AllArgsConstructor
@ApiModel(value = "게시글 관련 변수")
public class PostDTO extends NewCommonDTO {

    @ApiModelProperty(required = true, value = "idx", hidden = true, example = "1")
    private Long idx;

    @NotEmpty(message = "게시글 제목은 필수입니다.")
    @ApiModelProperty(required = true, value = "게시글 제목((ex)이 여행지는...)")
    private String postTitle;

    @NotEmpty(message = "게시글 상세 내용 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "게시글 상세 내용((ex)이 여행지는...)")
    private String postDescription;

    @ApiModelProperty(value = "게시글 조회수((ex)0)", example = "1")
    private int viewCount;

    @ApiModelProperty(value = "게시글 좋아요 수((ex)0)", example = "1")
    private int favoriteCount;

    @ApiModelProperty(required = true, value = "여행지 노출 여부((ex)Y,N)")
    private String visible;

    @ApiModelProperty(required = true, value = "인기 게시글 여부((ex)true, false)")
    private Boolean popular;

    @ApiModelProperty(value = "postImageList", hidden = true)
    private List<TravelImageDTO> postImageList = new ArrayList<>();

    @ApiModelProperty(value = "postReplyList", hidden = true)
    private List<ReplyDTO> postReplyList = new ArrayList<>();
}

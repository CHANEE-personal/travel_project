package com.travel.api.faq.domain;

import com.travel.api.common.domain.CommonDTO;
import com.travel.api.common.domain.NewCommonDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ApiModel(value = "FAQ 관련")
public class FaqDTO extends NewCommonDTO {

    @ApiModelProperty(required = true, value = "idx", hidden = true)
    private Long idx;

    @NotNull(message = "FAQ CODE 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "faq_code")
    private Integer faqCode;

    @NotEmpty(message = "FAQ 제목 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "title")
    private String title;

    @NotEmpty(message = "FAQ 내용 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "description")
    private String description;

    @ApiModelProperty(required = true, value = "viewCount")
    private int viewCount;

    @NotEmpty(message = "공지사항 노출 여부 선택은 필수입니다.")
    @ApiModelProperty(required = true, value = "visible")
    private String visible;
}

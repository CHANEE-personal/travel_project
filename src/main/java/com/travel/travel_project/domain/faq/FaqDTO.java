package com.travel.travel_project.domain.faq;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "FAQ 관련")
public class FaqDTO {
    @ApiModelProperty(required = true, value = "rnum", hidden = true)
    private Integer rnum;

    @ApiModelProperty(required = true, value = "idx", hidden = true)
    private Long idx;

    @NotNull(message = "FAQ CODE 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "faq_code")
    private Long faqCode;

    @NotEmpty(message = "FAQ 제목 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "title")
    private String title;

    @NotEmpty(message = "FAQ 내용 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "description")
    private String description;

    @ApiModelProperty(required = true, value = "viewCount")
    private Integer viewCount;

    @NotEmpty(message = "공지사항 노출 여부 선택은 필수입니다.")
    @ApiModelProperty(required = true, value = "visible")
    private String visible;
}

package com.travel.travel_project.domain.travel.search;

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
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class SearchDTO extends NewCommonDTO {
    @ApiModelProperty(required = true, value = "rowNum", hidden = true, example = "1")
    private Integer rowNum;

    @ApiModelProperty(required = true, value = "idx", hidden = true, example = "1")
    private Long idx;

    @NotEmpty(message = "검색 키워드 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "검색 키워드((ex)서울)")
    private String searchKeyword;
}

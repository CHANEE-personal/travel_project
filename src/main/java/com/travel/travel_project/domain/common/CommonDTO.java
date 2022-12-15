package com.travel.travel_project.domain.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class CommonDTO extends NewCommonDTO {

    @ApiModelProperty(required = true, value = "rnum", hidden = true, example = "1")
    private Integer rowNum;

    @ApiModelProperty(required = true, value = "idx", hidden = true, example = "1")
    private Long idx;

    @NotEmpty(message = "공통 코드 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "공통 코드((ex)01)")
    private Integer commonCode;

    @NotEmpty(message = "공통 코드명 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "공통 코드명((ex)서울)")
    private String commonName;

    @NotEmpty(message = "공통 코드 사용 여부는 필수입니다.")
    @ApiModelProperty(required = true, value = "사용 여부((ex)Y,N)")
    private String visible;
}

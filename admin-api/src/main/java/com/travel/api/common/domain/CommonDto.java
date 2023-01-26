package com.travel.api.common.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ApiModel
public class CommonDto  {

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

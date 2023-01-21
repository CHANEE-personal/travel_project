package com.travel.api.travel.domain.group;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(of = "idx", callSuper = false)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ApiModel
public class TravelGroupUserDto {

    @ApiModelProperty(required = true, value = "idx", hidden = true, example = "1")
    private Long idx;

    @NotNull(message = "유저 idx 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "유저 idx((ex)1)")
    private Long userIdx;

    @NotNull(message = "그룹 idx 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "그룹 idx((ex)1)")
    private Long groupIdx;
}

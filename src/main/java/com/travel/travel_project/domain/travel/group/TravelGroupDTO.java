package com.travel.travel_project.domain.travel.group;

import com.travel.travel_project.domain.common.NewCommonDTO;
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
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ApiModel
public class TravelGroupDTO extends NewCommonDTO {
    @ApiModelProperty(required = true, value = "rowNum", hidden = true, example = "1")
    private Integer rowNum;

    @ApiModelProperty(required = true, value = "idx", hidden = true, example = "1")
    private Long idx;

    @NotNull(message = "여행지 코드 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "여행지 idx((ex)1)")
    private Long travelIdx;

    @NotEmpty(message = "그룹명 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "그룹명((ex)서울모임)")
    private String groupName;

    @NotEmpty(message = "그룹 상세 내용 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "그룹명((ex)서울모임)")
    private String groupDescription;

    @ApiModelProperty(value = "사용여부((ex)Y, N)")
    private String visible;
}

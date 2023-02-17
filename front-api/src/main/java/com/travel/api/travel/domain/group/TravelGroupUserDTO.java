package com.travel.api.travel.domain.group;

import com.travel.api.common.domain.NewCommonDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Builder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ApiModel
public class TravelGroupUserDTO extends NewCommonDTO {

    @ApiModelProperty(required = true, value = "idx", hidden = true, example = "1")
    private Long idx;

    @NotEmpty(message = "유저 아이디 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "유저아이디((ex)test)")
    private String userId;

    @NotEmpty(message = "유저명 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "유저명((ex)test)")
    private String userName;

    @NotEmpty(message = "그룹명 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "그룹명((ex)서울그룹)")
    private String groupName;

    @NotEmpty(message = "그룹 상세 내용 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "그룹내용((ex)서울그룹)")
    private String groupDescription;
}

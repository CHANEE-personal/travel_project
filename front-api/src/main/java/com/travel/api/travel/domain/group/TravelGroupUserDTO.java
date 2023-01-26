package com.travel.api.travel.domain.group;

import com.travel.api.common.domain.NewCommonDTO;
import com.travel.api.user.domain.UserDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Builder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ApiModel
public class TravelGroupUserDTO extends NewCommonDTO {

    @ApiModelProperty(required = true, value = "idx", hidden = true, example = "1")
    private Long idx;

    @ApiModelProperty(required = true, value = "유저 idx((ex)1)")
    private UserDTO userDTO;

    @ApiModelProperty(required = true, value = "그룹 idx((ex)1)")
    private TravelGroupDTO groupDTO;
}

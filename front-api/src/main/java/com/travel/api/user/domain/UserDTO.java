package com.travel.api.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.travel.api.common.domain.NewCommonDTO;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Getter
@SuperBuilder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@TypeDef(name = "json", typeClass = JsonStringType.class)
@ApiModel
public class UserDTO extends NewCommonDTO {
    @ApiModelProperty(required = true, value = "rowNum", hidden = true, example = "1")
    private Integer rowNum;

    @ApiModelProperty(required = true, value = "user Seq", hidden = true, example = "1")
    private Long idx;

    @NotEmpty(message = "유저 아이디 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "user Id")
    private String userId;

    @NotEmpty(message = "유저 패스워드 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "user Password")
    private String password;

    @NotEmpty(message = "유저 이름 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "user Name", hidden = true)
    private String name;

    @NotEmpty(message = "유저 이메일 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "user email", hidden = true)
    private String email;

    @NotEmpty(message = "유저 사용여부 선택은 필수입니다.")
    @ApiModelProperty(required = true, value = "user visible", hidden = true)
    private String visible;

    @ApiModelProperty(value = "user Token", hidden = true)
    private String userToken;

    @ApiModelProperty(value = "user refresh Token", hidden = true)
    private String userRefreshToken;

    @ApiModelProperty(value = "role", hidden = true)
    private Role role;

    @Type(type = "json")
    @Nullable
    @ApiModelProperty(value = "user favorite travel idx", hidden = true)
    private List<String> favoriteTravelIdx = new ArrayList<>();
}

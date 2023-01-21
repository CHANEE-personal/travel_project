package com.travel.api.travel.domain.recommend;

import com.travel.api.common.domain.NewCommonDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

@Getter
@SuperBuilder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ApiModel
public class TravelRecommendDto extends NewCommonDto {

    @ApiModelProperty(required = true, value = "idx", hidden = true, example = "1")
    private Long idx;

    @Type(type = "json")
    @Nullable
    @ApiModelProperty(value = "recommend travel name", hidden = true)
    private List<String> recommendName = new ArrayList<>();
}

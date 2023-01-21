package com.travel.api.travel.domain.festival;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.travel.api.common.domain.NewCommonDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(of = "idx", callSuper = false)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ApiModel
public class TravelFestivalDTO extends NewCommonDTO {

    @ApiModelProperty(required = true, value = "idx", hidden = true, example = "1")
    private Long idx;

    @NotEmpty(message = "여행지 코드 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "여행지 코드((ex)1(서울))")
    private Integer travelCode;

    @NotEmpty(message = "축제 제목은 필수입니다.")
    @ApiModelProperty(required = true, value = "축제 제목((ex)이 축제는...)")
    private String festivalTitle;

    @NotEmpty(message = "축제 상세 내용 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "축제 상세 내용((ex)이 축제는...)")
    private String festivalDescription;

    @NotNull(message = "축제가 열리는 월 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "festival month", hidden = true, example = "1")
    private Integer festivalMonth;

    @NotNull(message = "축제가 열리는 일 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "festival day", hidden = true, example = "1")
    private Integer festivalDay;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @ApiModelProperty(required = true, value = "축제 일자", hidden = true)
    private LocalDateTime festivalTime;
}

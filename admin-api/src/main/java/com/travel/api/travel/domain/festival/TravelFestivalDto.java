package com.travel.api.travel.domain.festival;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@Getter
@Builder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ApiModel
public class TravelFestivalDto  {

    @ApiModelProperty(required = true, value = "idx", hidden = true, example = "1")
    private Long idx;

    @NotNull(message = "공통 코드 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "여행지 코드((ex)1(서울))")
    private Integer commonCode;

    @NotEmpty(message = "공통 코드명 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "여행지 코드((ex)서울(서울))")
    private String commonName;

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

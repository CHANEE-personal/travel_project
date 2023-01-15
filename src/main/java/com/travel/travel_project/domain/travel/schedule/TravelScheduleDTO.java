package com.travel.travel_project.domain.travel.schedule;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.travel.travel_project.domain.common.NewCommonDTO;
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
public class TravelScheduleDTO extends NewCommonDTO {

    @ApiModelProperty(required = true, value = "rowNum", hidden = true, example = "1")
    private Integer rowNum;

    @ApiModelProperty(required = true, value = "idx", hidden = true, example = "1")
    private Long idx;

    @NotNull(message = "유저 IDX 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "userIdx", example = "1")
    private Long userIdx;

    @NotNull(message = "여행지 코드 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "travelCode", example = "1")
    private Integer travelCode;

    @NotEmpty(message = "스케줄 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "scheduleDescription")
    private String scheduleDescription;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @ApiModelProperty(required = true, value = "스케줄 일자", hidden = true)
    private LocalDateTime scheduleTime;
}

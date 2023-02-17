package com.travel.api.travel.domain.schedule;

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
public class TravelScheduleDto  {

    @ApiModelProperty(required = true, value = "idx", hidden = true, example = "1")
    private Long idx;

    @NotEmpty(message = "유저 아이디 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "userId")
    private String userId;

    @NotEmpty(message = "유저명 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "userName")
    private String userName;

    @NotNull(message = "여행지 코드 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "commonCode", example = "1")
    private Integer commonCode;

    @NotEmpty(message = "공통 코드 내용 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "commonName")
    private String commonName;

    @NotEmpty(message = "스케줄 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "scheduleDescription")
    private String scheduleDescription;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @ApiModelProperty(required = true, value = "스케줄 일자", hidden = true)
    private LocalDateTime scheduleTime;
}

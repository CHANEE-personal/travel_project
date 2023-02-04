package com.travel.api.user.domain.reservation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.travel.api.travel.domain.reservation.TravelReservationEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@Getter
@Builder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ApiModel
public class UserReservationDTO {

    @ApiModelProperty(required = true, value = "idx", hidden = true, example = "1")
    private Long idx;

    @NotNull(message = "가격 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "예약지 가격((ex)10,000)")
    private int price;

    @ApiModelProperty(value = "예약지 할인 가격((ex)10,000)")
    private int salePrice;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @ApiModelProperty(required = true, value = "시작 일자", hidden = true)
    private LocalDateTime startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @ApiModelProperty(required = true, value = "마감 일자", hidden = true)
    private LocalDateTime endDate;

    @NotNull(message = "예약 인원수 입력은 필수입니다.")
    @ApiModelProperty(value = "예약 인원수((ex)0)", example = "1")
    private int userCount;

    @ApiModelProperty(value = "travelReservationList", hidden = true)
    private List<TravelReservationEntity> travelReservationList = new ArrayList<>();
}

package com.travel.api.travel.domain.reservation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.travel.api.common.domain.CommonDTO;
import com.travel.api.travel.domain.image.TravelImageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
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
public class TravelReservationDTO {

    @ApiModelProperty(required = true, value = "idx", hidden = true, example = "1")
    private Long idx;

    @NotNull(message = "여행지 공통 코드 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "공통 코드((ex)1)")
    private Integer commonCode;

    @NotNull(message = "여행지 공통 코드명 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "공통 코드명((ex)서울)")
    private String commonName;

    @NotEmpty(message = "예약 제목 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "예약 제목((ex)이 여행지는...)")
    private String title;

    @NotEmpty(message = "예약 상세 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "예약 내용((ex)이 여행지는...)")
    private String description;

    @NotEmpty(message = "예약지 주소 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "예약지 상세 주소((ex)인천 광역시 서구...)")
    private String address;

    @NotEmpty(message = "예약지 우편 주소 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "예약지 우편 주소((ex)xxx-xxx)")
    private String zipCode;

    @NotNull(message = "예약지 가격 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "예약지 가격((ex)10,000)")
    private int price;

    @NotNull(message = "예약 가능 인원수 입력은 필수입니다.")
    @ApiModelProperty(value = "예약 가능 인원수((ex)0)", example = "1")
    private int possibleCount;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @ApiModelProperty(required = true, value = "시작 일자", hidden = true)
    private LocalDateTime startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @ApiModelProperty(required = true, value = "마감 일자", hidden = true)
    private LocalDateTime endDate;

    @NotNull(message = "예약 가능 여부 입력은 필수입니다.")
    @ApiModelProperty(value = "예약 가능 여부((ex)true, false)", example = "true")
    private Boolean status;

    @ApiModelProperty(value = "travelImageList", hidden = true)
    private List<TravelImageDTO> imageList = new ArrayList<>();
}

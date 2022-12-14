package com.travel.travel_project.domain.travel.schedule;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.travel.travel_project.domain.common.NewCommonMappedClass;
import com.travel.travel_project.domain.user.UserEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(of = "idx", callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "travel_schedule")
public class TravelScheduleEntity extends NewCommonMappedClass {

    @Transient
    private Integer rowNum;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @NotNull(message = "여행지 코드 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "여행지 idx((ex)1)")
    @Column(name = "travel_idx")
    private Long travelIdx;

    @NotNull(message = "유저 idx 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "유저 idx((ex)1)")
    @Column(name = "user_idx")
    private Long userIdx;

    @Column(name = "schedule_description")
    @Lob
    @NotEmpty(message = "스케줄 상세 내용 입력은 필수입니다.")
    private String scheduleDescription;

    @Column(name = "schedule_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "스케줄 일정 입력은 필수입니다.")
    private LocalDateTime scheduleTime;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_idx", insertable = false, updatable = false)
    private UserEntity userEntity;

    public static TravelScheduleDTO toDto(TravelScheduleEntity entity) {
        if (entity == null) return null;
        return TravelScheduleDTO.builder()
                .rowNum(entity.getRowNum())
                .idx(entity.getIdx())
                .userIdx(entity.getUserIdx())
                .travelIdx(entity.getTravelIdx())
                .scheduleDescription(entity.getScheduleDescription())
                .scheduleTime(entity.getScheduleTime())
                .build();
    }

    public static List<TravelScheduleDTO> toDtoList(List<TravelScheduleEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(TravelScheduleEntity::toDto)
                .collect(Collectors.toList());
    }
}

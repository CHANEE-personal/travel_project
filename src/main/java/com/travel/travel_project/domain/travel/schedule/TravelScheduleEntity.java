package com.travel.travel_project.domain.travel.schedule;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.travel.travel_project.domain.common.CommonEntity;
import com.travel.travel_project.domain.common.NewCommonMappedClass;
import com.travel.travel_project.domain.travel.TravelEntity;
import com.travel.travel_project.domain.user.UserEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@Table(name = "travel_schedule")
public class TravelScheduleEntity extends NewCommonMappedClass {

    @Transient
    private Integer rowNum;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idx")
    private Long idx;

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
    @JoinColumn(name = "user_idx", nullable = false)
    private UserEntity userEntity;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name ="travel_code", nullable = false)
    private CommonEntity commonEntity;

    public void update(TravelScheduleEntity travelScheduleEntity) {
        this.scheduleDescription = travelScheduleEntity.scheduleDescription;
        this.scheduleTime = travelScheduleEntity.scheduleTime;
    }

    public static TravelScheduleDTO toDto(TravelScheduleEntity entity) {
        if (entity == null) return null;
        return TravelScheduleDTO.builder()
                .rowNum(entity.getRowNum())
                .idx(entity.getIdx())
                .userIdx(entity.userEntity.getIdx())
                .travelCode(entity.commonEntity.getCommonCode())
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

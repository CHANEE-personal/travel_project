package com.travel.api.travel.domain.schedule;

import com.travel.api.common.domain.CommonEntity;
import com.travel.api.common.domain.NewCommonMappedClass;
import com.travel.api.user.domain.UserEntity;
import lombok.*;
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
@Builder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicUpdate
@Table(name = "travel_schedule")
public class TravelScheduleEntity extends NewCommonMappedClass {

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx", nullable = false)
    private UserEntity userEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="travel_code", nullable = false)
    private CommonEntity commonEntity;

    public void update(TravelScheduleEntity travelScheduleEntity) {
        this.scheduleDescription = travelScheduleEntity.scheduleDescription;
        this.scheduleTime = travelScheduleEntity.scheduleTime;
    }

    public static TravelScheduleDto toDto(TravelScheduleEntity entity) {
        if (entity == null) return null;
        return TravelScheduleDto.builder()
                .idx(entity.idx)
                .userId(entity.userEntity.getUserId())
                .userName(entity.userEntity.getName())
                .commonCode(entity.commonEntity.getCommonCode())
                .commonName(entity.commonEntity.getCommonName())
                .scheduleDescription(entity.scheduleDescription)
                .scheduleTime(entity.scheduleTime)
                .build();
    }

    public static List<TravelScheduleDto> toDtoList(List<TravelScheduleEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(TravelScheduleEntity::toDto)
                .collect(Collectors.toList());
    }
}

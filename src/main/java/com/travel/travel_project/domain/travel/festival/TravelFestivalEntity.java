package com.travel.travel_project.domain.travel.festival;

import com.travel.travel_project.domain.common.NewCommonMappedClass;
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
@Table(name = "travel_festival")
public class TravelFestivalEntity extends NewCommonMappedClass {

    @Transient
    private Integer rowNum;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @Column(name = "travel_code")
    @NotNull(message = "여행지 코드 입력은 필수입니다.")
    private Integer travelCode;

    @Column(name = "festival_title")
    @Lob
    @NotEmpty
    private String festivalTitle;

    @Column(name = "festival_description")
    @Lob
    @NotEmpty
    private String festivalDescription;

    @Column(name = "festival_month")
    @NotNull(message = "축제가 열리는 월 입력은 필수입니다.")
    private Integer festivalMonth;

    @Column(name = "festival_day")
    @NotNull(message = "축제가 열리는 일 입력은 필수입니다.")
    private Integer festivalDay;

    @Column(name = "festival_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "축제 일정 입력은 필수입니다.")
    private LocalDateTime festivalTime;

    public void update(TravelFestivalEntity travelFestivalEntity) {
        this.festivalTitle = travelFestivalEntity.festivalTitle;
        this.festivalDescription = travelFestivalEntity.festivalDescription;
        this.festivalDay = travelFestivalEntity.festivalDay;
        this.festivalMonth = travelFestivalEntity.festivalMonth;
        this.festivalTime = travelFestivalEntity.festivalTime;
    }

    public static TravelFestivalDTO toDto(TravelFestivalEntity entity) {
        if (entity == null) return null;
        return TravelFestivalDTO.builder()
                .rowNum(entity.getRowNum())
                .idx(entity.getIdx())
                .travelCode(entity.getTravelCode())
                .festivalTitle(entity.getFestivalTitle())
                .festivalDescription(entity.getFestivalDescription())
                .festivalMonth(entity.getFestivalMonth())
                .festivalDay(entity.getFestivalDay())
                .festivalTime(entity.getFestivalTime())
                .creator(entity.getCreator())
                .createTime(entity.getCreateTime())
                .updater(entity.getUpdater())
                .updateTime(entity.getUpdateTime())
                .build();
    }

    public static List<TravelFestivalDTO> toDtoList(List<TravelFestivalEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(TravelFestivalEntity::toDto)
                .collect(Collectors.toList());
    }
}

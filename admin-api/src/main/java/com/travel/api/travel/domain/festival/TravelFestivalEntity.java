package com.travel.api.travel.domain.festival;

import com.travel.api.common.domain.CommonEntity;
import com.travel.api.common.domain.NewCommonMappedClass;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@Table(name = "travel_festival")
public class TravelFestivalEntity extends NewCommonMappedClass {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idx")
    private Long idx;

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

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "travel_code", referencedColumnName = "common_code")
    private CommonEntity newFestivalCode;

    public void update(TravelFestivalEntity travelFestivalEntity) {
        this.festivalTitle = travelFestivalEntity.festivalTitle;
        this.festivalDescription = travelFestivalEntity.festivalDescription;
        this.festivalDay = travelFestivalEntity.festivalDay;
        this.festivalMonth = travelFestivalEntity.festivalMonth;
        this.festivalTime = travelFestivalEntity.festivalTime;
    }

    public static TravelFestivalDto toDto(TravelFestivalEntity entity) {
        if (entity == null) return null;
        return TravelFestivalDto.builder()
                .idx(entity.idx)
                .commonCode(entity.newFestivalCode.getCommonCode())
                .commonName(entity.newFestivalCode.getCommonName())
                .festivalTitle(entity.festivalTitle)
                .festivalDescription(entity.festivalDescription)
                .festivalMonth(entity.festivalMonth)
                .festivalDay(entity.festivalDay)
                .festivalTime(entity.festivalTime)
                .build();
    }

    public static List<TravelFestivalDto> toDtoList(List<TravelFestivalEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(TravelFestivalEntity::toDto)
                .collect(Collectors.toList());
    }
}

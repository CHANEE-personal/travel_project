package com.travel.travel_project.domain.common;

import com.travel.travel_project.domain.faq.FaqEntity;
import com.travel.travel_project.domain.travel.TravelEntity;
import com.travel.travel_project.domain.travel.festival.TravelFestivalEntity;
import com.travel.travel_project.domain.travel.schedule.TravelScheduleEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
@SuperBuilder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@AllArgsConstructor
@Table(name = "tv_cmm_code")
public class CommonEntity extends NewCommonMappedClass implements Serializable {

    @Transient
    private Integer rowNum;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @Column(name = "common_code")
    @NotNull(message = "공통 코드 입력은 필수입니다.")
    private Integer commonCode;

    @Column(name = "common_name")
    @NotEmpty(message = "공통 코드명 입력은 필수입니다.")
    private String commonName;

    @Column(name = "visible")
    @NotEmpty(message = "공통 코드 사용 여부는 필수입니다.")
    private String visible;

    @Builder.Default
    @OneToMany(mappedBy = "newTravelCode", cascade = ALL, fetch = LAZY)
    private List<TravelEntity> adminTravelEntityList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "newFaqCode", cascade = ALL, fetch = LAZY)
    private List<FaqEntity> faqEntityList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "commonEntity", cascade = ALL, fetch = LAZY)
    private List<TravelScheduleEntity> travelScheduleEntity = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "newFestivalCode", cascade = ALL, fetch = LAZY)
    private List<TravelFestivalEntity> festivalEntityList = new ArrayList<>();

    public void addCommon(FaqEntity faqEntity) {
        faqEntity.setNewFaqCode(this);
        this.faqEntityList.add(faqEntity);
    }

    public void addTravel(TravelEntity travelEntity) {
        travelEntity.setNewTravelCode(this);
        this.adminTravelEntityList.add(travelEntity);
    }

    public void addSchedule(TravelScheduleEntity travelScheduleEntity) {
        travelScheduleEntity.setCommonEntity(this);
        this.travelScheduleEntity.add(travelScheduleEntity);
    }

    public void addFestival(TravelFestivalEntity travelFestivalEntity) {
        travelFestivalEntity.setNewFestivalCode(this);
        this.festivalEntityList.add(travelFestivalEntity);
    }

    public void update(CommonEntity commonEntity) {
        this.commonCode = commonEntity.commonCode;
        this.commonName = commonEntity.commonName;
        this.visible = commonEntity.visible;
    }

    public static CommonDTO toDto(CommonEntity entity) {
        if (entity == null) return null;
        return CommonDTO.builder()
                .rowNum(entity.getRowNum())
                .idx(entity.getIdx())
                .commonCode(entity.getCommonCode())
                .commonName(entity.getCommonName())
                .visible(entity.getVisible())
                .build();
    }

    public static List<CommonDTO> toDtoList(List<CommonEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(CommonEntity::toDto)
                .collect(Collectors.toList());
    }
}

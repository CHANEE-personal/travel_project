package com.travel.travel_project.domain.travel.recommend;

import com.travel.travel_project.domain.common.NewCommonMappedClass;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;

import javax.persistence.*;

import java.util.ArrayList;
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
@Table(name = "travel_recommend")
public class TravelRecommendEntity extends NewCommonMappedClass {
    @Transient
    private Integer rowNum;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @Type(type = "json")
    @Column(columnDefinition = "json", name = "recommend_name")
    private List<String> recommendName = new ArrayList<>();

    public void update(TravelRecommendEntity travelRecommendEntity) {
        this.recommendName = travelRecommendEntity.recommendName;
    }

    public static TravelRecommendDTO toDto(TravelRecommendEntity entity) {
        if (entity == null) return null;
        return TravelRecommendDTO.builder()
                .rowNum(entity.getRowNum())
                .idx(entity.getIdx())
                .recommendName(entity.getRecommendName())
                .creator(entity.getCreator())
                .createTime(entity.getCreateTime())
                .updater(entity.getUpdater())
                .updateTime(entity.getUpdateTime())
                .build();
    }

    public static List<TravelRecommendDTO> toDtoList(List<TravelRecommendEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(TravelRecommendEntity::toDto)
                .collect(Collectors.toList());
    }
}

package com.travel.api.travel.domain.recommend;

import com.travel.api.common.domain.NewCommonMappedClass;
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
@Builder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicUpdate
@Table(name = "travel_recommend")
public class TravelRecommendEntity extends NewCommonMappedClass {

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
                .idx(entity.idx)
                .recommendName(entity.recommendName)
                .build();
    }

    public static List<TravelRecommendDTO> toDtoList(List<TravelRecommendEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(TravelRecommendEntity::toDto)
                .collect(Collectors.toList());
    }
}

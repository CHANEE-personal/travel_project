package com.travel.api.travel.domain.review;

import com.travel.api.common.domain.NewCommonMappedClass;
import com.travel.api.travel.domain.TravelEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicUpdate
@Table(name = "travel_review")
public class TravelReviewEntity extends NewCommonMappedClass {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @Lob
    @Column(name = "review_title")
    @NotEmpty(message = "여행지 리뷰 제목 입력은 필수입니다.")
    private String reviewTitle;

    @Lob
    @Column(name = "review_description")
    @NotEmpty(message = "여행지 리뷰 내용 입력은 필수입니다.")
    private String reviewDescription;

    @Column(name = "view_count")
    private int viewCount;

    @Column(name = "favorite_count")
    private int favoriteCount;

    @Column(name = "visible")
    @NotEmpty(message = "여행지 리뷰 노출 여부 선택은 필수입니다.")
    private String visible;

    @Column(name = "popular")
    private Boolean popular;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "travel_idx", referencedColumnName = "idx")
    private TravelEntity newTravelEntity;

    public void update(TravelReviewEntity travelReviewEntity) {
        this.reviewTitle = travelReviewEntity.reviewTitle;
        this.reviewDescription = travelReviewEntity.reviewDescription;
        this.visible = travelReviewEntity.visible;
        this.popular = travelReviewEntity.popular;
    }

    public static TravelReviewDto toDto(TravelReviewEntity entity) {
        if (entity == null) return null;
        return TravelReviewDto.builder()
                .idx(entity.idx)
                .travelTitle(entity.newTravelEntity.getTravelTitle())
                .travelDescription(entity.newTravelEntity.getTravelDescription())
                .reviewTitle(entity.reviewTitle)
                .reviewDescription(entity.reviewDescription)
                .favoriteCount(entity.favoriteCount)
                .viewCount(entity.viewCount)
                .visible(entity.visible)
                .popular(entity.popular)
                .build();
    }

    public static List<TravelReviewDto> toDtoList(List<TravelReviewEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(TravelReviewEntity::toDto)
                .collect(Collectors.toList());
    }
}

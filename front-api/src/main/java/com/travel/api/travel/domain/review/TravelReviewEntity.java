package com.travel.api.travel.domain.review;

import com.travel.api.common.domain.NewCommonMappedClass;
import com.travel.api.travel.domain.TravelEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
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
@SuperBuilder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    public static TravelReviewDTO toDto(TravelReviewEntity entity) {
        if (entity == null) return null;
        return TravelReviewDTO.builder()
                .idx(entity.getIdx())
                .travelIdx(entity.newTravelEntity.getIdx())
                .reviewTitle(entity.getReviewTitle())
                .reviewDescription(entity.getReviewDescription())
                .favoriteCount(entity.getFavoriteCount())
                .viewCount(entity.getViewCount())
                .visible(entity.getVisible())
                .popular(entity.getPopular())
                .creator(entity.getCreator())
                .createTime(entity.getCreateTime())
                .updater(entity.getUpdater())
                .updateTime(entity.getUpdateTime())
                .build();
    }

    public static List<TravelReviewDTO> toDtoList(List<TravelReviewEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(TravelReviewEntity::toDto)
                .collect(Collectors.toList());
    }
}

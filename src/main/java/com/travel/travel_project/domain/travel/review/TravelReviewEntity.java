package com.travel.travel_project.domain.travel.review;

import com.travel.travel_project.domain.common.NewCommonMappedClass;
import com.travel.travel_project.domain.travel.TravelEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(of = "idx", callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "travel_review")
public class TravelReviewEntity extends NewCommonMappedClass {
    @Transient
    private Integer rowNum;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @Column(name = "travel_idx")
    @NotNull(message = "여행지 idx 입력은 필수입니다.")
    private Long travelIdx;

    @Lob
    @Column(name = "review_title")
    @NotEmpty(message = "여행지 리뷰 제목 입력은 필수입니다.")
    private String reviewTitle;

    @Lob
    @Column(name = "review_description")
    @NotEmpty(message = "여행지 리뷰 내용 입력은 필수입니다.")
    private String reviewDescription;

    @Column(name = "review_parent_idx")
    private Long reviewParentIdx;

    @Column(name = "review_top_idx")
    private Long reviewTopIdx;

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
    @JoinColumn(name = "travel_idx", referencedColumnName = "idx", insertable = false, updatable = false)
    private TravelEntity newTravelEntity;

    public static TravelReviewDTO toDto(TravelReviewEntity entity) {
        if (entity == null) return null;
        return TravelReviewDTO.builder()
                .idx(entity.getIdx())
                .rowNum(entity.getRowNum())
                .travelIdx(entity.getTravelIdx())
                .reviewTitle(entity.getReviewTitle())
                .reviewDescription(entity.getReviewDescription())
                .reviewParentIdx(entity.getReviewParentIdx())
                .reviewTopIdx(entity.getReviewTopIdx())
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

    public static TravelReviewEntity toEntity(TravelReviewDTO dto) {
        if (dto == null) return null;
        return TravelReviewEntity.builder()
                .idx(dto.getIdx())
                .rowNum(dto.getRowNum())
                .travelIdx(dto.getTravelIdx())
                .reviewTitle(dto.getReviewTitle())
                .reviewDescription(dto.getReviewDescription())
                .reviewParentIdx(dto.getReviewParentIdx())
                .reviewTopIdx(dto.getReviewTopIdx())
                .favoriteCount(dto.getFavoriteCount())
                .viewCount(dto.getViewCount())
                .visible(dto.getVisible())
                .popular(dto.getPopular())
                .creator(dto.getCreator())
                .createTime(dto.getCreateTime())
                .updater(dto.getUpdater())
                .updateTime(dto.getUpdateTime())
                .build();
    }

    public static List<TravelReviewDTO> toDtoList(List<TravelReviewEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(TravelReviewEntity::toDto)
                .collect(Collectors.toList());
    }

    public static List<TravelReviewEntity> toEntityList(List<TravelReviewDTO> dtoList) {
        if (dtoList == null) return null;
        return dtoList.stream()
                .map(TravelReviewEntity::toEntity)
                .collect(Collectors.toList());
    }
}

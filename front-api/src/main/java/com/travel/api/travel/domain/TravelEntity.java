package com.travel.api.travel.domain;

import com.travel.api.common.domain.CommonEntity;
import com.travel.api.common.domain.NewCommonMappedClass;
import com.travel.api.travel.domain.group.TravelGroupEntity;
import com.travel.api.travel.domain.image.TravelImageEntity;
import com.travel.api.travel.domain.review.TravelReviewEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import java.util.ArrayList;
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
@Table(name = "tv_info_mst")
public class TravelEntity extends NewCommonMappedClass {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @Column(name = "travel_title")
    @NotEmpty(message = "여행지 제목 입력은 필수입니다.")
    private String travelTitle;

    @Column(name = "travel_description")
    @Lob
    @NotEmpty(message = "여행지 상세 내용 입력은 필수입니다.")
    private String travelDescription;

    @Column(name = "travel_code", insertable = false, updatable = false)
    private Integer travelCode;

    @Column(name = "travel_address")
    @NotEmpty(message = "여행지 주소 입력은 필수입니다.")
    private String travelAddress;

    @Column(name = "travel_zip_code")
    @NotEmpty(message = "여행지 우편 주소 입력은 필수입니다.")
    private String travelZipCode;

    @Column(name = "view_count")
    private int viewCount;

    @Column(name = "favorite_count")
    private int favoriteCount;

    @Column(name = "visible")
    @NotEmpty(message = "여행지 노출 여부 선택은 필수입니다.")
    private String visible;

    @Column(name = "popular")
    private Boolean popular;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "travel_code", referencedColumnName = "common_code")
    private CommonEntity newTravelCode;

    @Builder.Default
    @BatchSize(size = 20)
    @OneToMany(mappedBy = "newTravelEntity", fetch = LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<TravelReviewEntity> travelReviewEntityList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "travelEntity", fetch = LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<TravelGroupEntity> travelGroupEntityList = new ArrayList<>();

    @Builder.Default
    @BatchSize(size = 20)
    @Where(clause = "type_name = 'travel'")
    @OneToMany(mappedBy = "newTravelImageEntity", fetch = LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<TravelImageEntity> travelImageEntityList = new ArrayList<>();

    public void update(TravelEntity travelEntity) {
        this.travelTitle = travelEntity.travelTitle;
        this.travelDescription = travelEntity.travelDescription;
        this.travelAddress = travelEntity.travelAddress;
        this.travelZipCode = travelEntity.travelZipCode;
        this.visible = travelEntity.visible;
        this.popular = travelEntity.popular;
    }

    public void addReview(TravelReviewEntity travelReviewEntity) {
        travelReviewEntity.setNewTravelEntity(this);
        this.travelReviewEntityList.add(travelReviewEntity);
    }

    public void addImage(TravelImageEntity travelImageEntity) {
        travelImageEntity.setNewTravelImageEntity(this);
        this.travelImageEntityList.add(travelImageEntity);
    }

    public void updateViewCount() {
        this.viewCount++;
    }

    public void updateFavoriteCount() {
        this.favoriteCount++;
    }

    public static TravelDTO toDto(TravelEntity entity) {
        if (entity == null) return null;
        return TravelDTO.builder()
                .idx(entity.getIdx())
                .travelTitle(entity.getTravelTitle())
                .travelDescription(entity.getTravelDescription())
                .travelAddress(entity.getTravelAddress())
                .travelZipCode(entity.getTravelZipCode())
                .favoriteCount(entity.getFavoriteCount())
                .viewCount(entity.getViewCount())
                .visible(entity.getVisible())
                .popular(entity.getPopular())
                .creator(entity.getCreator())
                .createTime(entity.getCreateTime())
                .updater(entity.getUpdater())
                .updateTime(entity.getUpdateTime())
                .imageList(TravelImageEntity.toDtoList(entity.getTravelImageEntityList()))
                .reviewList(TravelReviewEntity.toDtoList(entity.getTravelReviewEntityList()))
                .build();
    }

    public static List<TravelDTO> toDtoList(List<TravelEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(TravelEntity::toDto)
                .collect(Collectors.toList());
    }
}

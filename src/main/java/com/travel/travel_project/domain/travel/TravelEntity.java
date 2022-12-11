package com.travel.travel_project.domain.travel;

import com.travel.travel_project.domain.common.CommonEntity;
import com.travel.travel_project.domain.common.NewCommonMappedClass;
import com.travel.travel_project.domain.file.CommonImageEntity;
import com.travel.travel_project.domain.travel.review.TravelReviewEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(of = "idx", callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tv_info_mst")
public class TravelEntity extends NewCommonMappedClass {

    @Transient
    private Integer rnum;

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

    @Column(name = "travel_code")
    @NotNull(message = "여행지 코드 입력은 필수입니다.")
    private Integer travelCode;

    @Column(name = "travel_address")
    @NotEmpty(message = "여행지 주소 입력은 필수입니다.")
    private String travelAddress;

    @Column(name = "travel_zip_code")
    @NotEmpty(message = "여행지 우편 주소 입력은 필수입니다.")
    private String travelZipCode;

    @Column(name = "view_count")
    private Integer viewCount;

    @Column(name = "favorite_count")
    private Integer favoriteCount;

    @Column(name = "visible")
    @NotEmpty(message = "여행지 노출 여부 선택은 필수입니다.")
    private String visible;

    @Column(name = "popular")
    private Boolean popular;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "travel_code", referencedColumnName = "common_code", insertable = false, updatable = false)
    private CommonEntity newTravelCode;

    @OneToMany(mappedBy = "newTravelEntity", fetch = LAZY)
    private List<TravelReviewEntity> travelReviewEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "travelImageEntity", fetch = LAZY)
    private List<CommonImageEntity> commonImageEntityList = new ArrayList<>();

    public void togglePopular(Boolean popular) {
        this.popular = !popular;
    }

    public void updateViewCount() {
        this.viewCount++;
    }

    public void updateFavoriteCount() {
        this.favoriteCount++;
    }
}

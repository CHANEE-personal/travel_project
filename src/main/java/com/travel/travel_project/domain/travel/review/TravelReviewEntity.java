package com.travel.travel_project.domain.travel.review;

import com.travel.travel_project.domain.common.NewCommonMappedClass;
import com.travel.travel_project.domain.travel.TravelEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
    private Integer rnum;

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
}

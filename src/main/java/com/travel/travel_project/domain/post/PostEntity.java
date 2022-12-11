package com.travel.travel_project.domain.post;

import com.travel.travel_project.domain.common.NewCommonMappedClass;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(of = "idx", callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "travel_post")
public class PostEntity extends NewCommonMappedClass {
    @Transient
    private Integer rnum;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @Lob
    @Column(name = "post_title")
    @NotEmpty(message = "게시글 제목 입력은 필수입니다.")
    private String postTitle;

    @Lob
    @Column(name = "post_description")
    @NotEmpty(message = "게시글 내용 입력은 필수입니다.")
    private String postDescription;

    @Column(name = "post_parent_idx")
    private Long postIdx;

    @Column(name = "post_top_idx")
    private Long postTopIdx;

    @Column(name = "view_count")
    private int viewCount;

    @Column(name = "favorite_count")
    private int favoriteCount;

    @Column(name = "visible")
    @NotEmpty(message = "게시글 노출 여부 선택은 필수입니다.")
    private String visible;

    @Column(name = "popular")
    private Boolean popular;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_parent_idx", referencedColumnName = "idx", insertable = false, updatable = false)
    private PostEntity postImageEntity;

}

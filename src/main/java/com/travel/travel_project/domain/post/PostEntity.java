package com.travel.travel_project.domain.post;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.travel.travel_project.domain.common.NewCommonMappedClass;
import com.travel.travel_project.domain.file.CommonImageEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.BatchSize;
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
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "travel_post")
public class PostEntity extends NewCommonMappedClass {
    @Transient
    private Integer rowNum;

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
    private Long postParentIdx;

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

    @JsonIgnore
    @BatchSize(size = 100)
    @Where(clause = "type_name = 'post'")
    @OneToMany(mappedBy = "postImageEntity", fetch = LAZY)
    private List<CommonImageEntity> postImageList = new ArrayList<>();

    public static PostDTO toDto(PostEntity entity) {
        if (entity == null) return null;
        return PostDTO.builder()
                .rowNum(entity.getRowNum())
                .idx(entity.getIdx())
                .postTitle(entity.getPostTitle())
                .postDescription(entity.getPostDescription())
                .postParentIdx(entity.getPostParentIdx())
                .postTopIdx(entity.getPostTopIdx())
                .visible(entity.getVisible())
                .viewCount(entity.getViewCount())
                .favoriteCount(entity.getFavoriteCount())
                .postImageList(CommonImageEntity.toDtoList(entity.getPostImageList()))
                .build();
    }

    public static PostDTO toPartDto(PostEntity entity) {
        if (entity == null) return null;
        return PostDTO.builder()
                .rowNum(entity.getRowNum())
                .idx(entity.getIdx())
                .postTitle(entity.getPostTitle())
                .postDescription(entity.getPostDescription())
                .postParentIdx(entity.getPostParentIdx())
                .postTopIdx(entity.getPostTopIdx())
                .visible(entity.getVisible())
                .viewCount(entity.getViewCount())
                .favoriteCount(entity.getFavoriteCount())
                .build();
    }

    public static PostEntity toEntity(PostDTO dto) {
        if (dto == null) return null;
        return PostEntity.builder()
                .rowNum(dto.getRowNum())
                .idx(dto.getIdx())
                .postTitle(dto.getPostTitle())
                .postDescription(dto.getPostDescription())
                .postParentIdx(dto.getPostParentIdx())
                .postTopIdx(dto.getPostTopIdx())
                .visible(dto.getVisible())
                .viewCount(dto.getViewCount())
                .favoriteCount(dto.getFavoriteCount())
                .build();
    }

    public static List<PostDTO> toDtoList(List<PostEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(PostEntity::toDto)
                .collect(Collectors.toList());
    }

    public static List<PostDTO> toPartDtoList(List<PostEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(PostEntity::toPartDto)
                .collect(Collectors.toList());
    }

    public static List<PostEntity> toEntityList(List<PostDTO> dtoList) {
        if (dtoList == null) return null;
        return dtoList.stream()
                .map(PostEntity::toEntity)
                .collect(Collectors.toList());
    }
}

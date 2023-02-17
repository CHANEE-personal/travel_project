package com.travel.api.post.domain.image;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.travel.api.common.domain.EntityType;
import com.travel.api.post.domain.PostEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;
import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicUpdate
@Table(name = "post_image")
public class PostImageEntity {

    @Id
    @GeneratedValue
    @Column(name = "idx")
    private Long idx;

    @Column(name = "type_name")
    @Enumerated(EnumType.STRING)
    private EntityType entityType;

    @Column(name = "file_num")
    @NotNull(message = "fileNum 필수입니다.")
    private Integer fileNum;

    @Column(name = "file_name")
    @NotEmpty(message = "파일명은 필수입니다.")
    private String fileName;

    @Column(name = "file_size")
    @NotNull(message = "fileSize 필수입니다.")
    private Long fileSize;

    @Column(name = "file_mask")
    @NotEmpty(message = "fileMask 필수입니다.")
    private String fileMask;

    @Column(name = "file_path")
    @NotEmpty(message = "filePath 필수입니다.")
    private String filePath;

    @Column(name = "image_type")
    @NotEmpty(message = "imageType 필수입니다.")
    private String imageType;

    @Column(name = "visible")
    @NotEmpty(message = "visible 필수입니다.")
    private String visible;

    @Column(name = "reg_date", insertable = false, updatable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime regDate;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "type_idx", referencedColumnName = "idx", nullable = false)
    private PostEntity newPostImageEntity;

    public static PostImageDTO toDto(PostImageEntity entity) {
        if (entity == null) return null;
        return PostImageDTO.builder()
                .idx(entity.idx)
                .entityType(entity.entityType)
                .postTitle(entity.newPostImageEntity.getPostTitle())
                .postDescription(entity.newPostImageEntity.getPostDescription())
                .fileMask(entity.fileMask)
                .fileSize(entity.fileSize)
                .fileName(entity.fileName)
                .fileNum(entity.fileNum)
                .filePath(entity.filePath)
                .imageType(entity.imageType)
                .visible(entity.visible)
                .regDate(entity.regDate)
                .build();
    }

    public static List<PostImageDTO> toDtoList(List<PostImageEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(PostImageEntity::toDto)
                .collect(Collectors.toList());
    }
}

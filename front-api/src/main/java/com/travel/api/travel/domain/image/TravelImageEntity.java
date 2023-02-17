package com.travel.api.travel.domain.image;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.travel.api.common.domain.EntityType;
import com.travel.api.post.domain.PostEntity;
import com.travel.api.travel.domain.TravelEntity;
import com.travel.api.travel.domain.reservation.TravelReservationEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
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
@Table(name = "travel_image")
public class TravelImageEntity {

    @Id
    @GeneratedValue
    @Column(name = "idx")
    @ApiModelProperty(value = "파일 IDX", required = true, hidden = true)
    private Long idx;

    @Column(name = "type_idx")
    @ApiModelProperty(value = "분야 IDX", required = true, hidden = true)
    private Long typeIdx;

    @Column(name = "type_name")
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(value = "분야명", required = true, hidden = true)
    private EntityType entityType;

    @Column(name = "file_num")
    @ApiModelProperty(value = "파일 Number", required = true, hidden = true)
    private Integer fileNum;

    @Column(name = "file_name")
    @ApiModelProperty(required = true, value = "파일명", hidden = true)
    private String fileName;

    @Column(name = "file_size")
    @ApiModelProperty(value = "파일SIZE", hidden = true)
    private Long fileSize;

    @Column(name = "file_mask")
    @ApiModelProperty(value = "파일MASK", hidden = true)
    private String fileMask;

    @Column(name = "file_path")
    @ApiModelProperty(value = "파일경로", hidden = true)
    private String filePath;

    @Column(name = "image_type")
    @ApiModelProperty(value = "메인 이미지 구분", hidden = true)
    private String imageType;

    @Column(name = "visible")
    @ApiModelProperty(value = "사용 여부", hidden = true)
    private String visible;

    @Column(name = "reg_date", insertable = false, updatable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @ApiModelProperty(value = "등록일자", hidden = true)
    private LocalDateTime regDate;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "type_idx", referencedColumnName = "idx", insertable = false, updatable = false)
    private TravelEntity newTravelImageEntity;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "type_idx", referencedColumnName = "idx", insertable = false, updatable = false)
    private PostEntity newPostImageEntity;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "type_idx", referencedColumnName = "idx", insertable = false, updatable = false)
    private TravelReservationEntity travelReservationEntity;

    public static TravelImageDTO toDto(TravelImageEntity entity) {
        if (entity == null) return null;
        return TravelImageDTO.builder()
                .idx(entity.idx)
                .travelTitle(entity.newTravelImageEntity.getTravelTitle())
                .travelDescription(entity.newTravelImageEntity.getTravelDescription())
                .entityType(entity.entityType)
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

    public static List<TravelImageDTO> toDtoList(List<TravelImageEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(TravelImageEntity::toDto)
                .collect(Collectors.toList());
    }
}

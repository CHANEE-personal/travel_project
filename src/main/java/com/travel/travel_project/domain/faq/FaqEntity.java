package com.travel.travel_project.domain.faq;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.travel.travel_project.domain.common.CommonEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;

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
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@Table(name = "travel_faq")
public class FaqEntity {
    @Transient
    private Integer rowNum;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @Column(name = "title")
    @NotEmpty(message = "제목 입력은 필수입니다.")
    private String title;

    @Column(name = "description")
    @Lob
    @NotEmpty(message = "공지사항 내용 입력은 필수입니다.")
    private String description;

    @Column(name = "view_count")
    private Integer viewCount;

    @Column(name = "visible")
    @NotEmpty(message = "공지사항 노출 여부 선택은 필수입니다.")
    private String visible;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "faq_code", referencedColumnName = "common_code")
    private CommonEntity newFaqCode;

    // 조회 수 증가
    public void updateViewCount() {
        this.viewCount++;
    }

    public void update(FaqEntity faqEntity) {
        this.title = faqEntity.title;
        this.description = faqEntity.description;
        this.visible = faqEntity.visible;
    }

    public static FaqDTO toDto(FaqEntity entity) {
        if (entity == null) return null;
        return FaqDTO.builder()
                .rowNum(entity.getRowNum())
                .idx(entity.getIdx())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .viewCount(entity.getViewCount())
                .visible(entity.getVisible())
                .build();
    }

    public static FaqEntity toEntity(FaqDTO dto) {
        if (dto == null) return null;
        return FaqEntity.builder()
                .rowNum(dto.getRowNum())
                .idx(dto.getIdx())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .viewCount(dto.getViewCount())
                .visible(dto.getVisible())
                .build();
    }

    public static List<FaqDTO> toDtoList(List<FaqEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(FaqEntity::toDto)
                .collect(Collectors.toList());
    }

    public List<FaqEntity> toEntityList(List<FaqDTO> dtoList) {
        if (dtoList == null) return null;
        return dtoList.stream()
                .map(FaqEntity::toEntity)
                .collect(Collectors.toList());
    }
}

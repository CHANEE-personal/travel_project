package com.travel.travel_project.domain.faq;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.travel.travel_project.domain.common.CommonEntity;
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
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "travel_faq")
public class FaqEntity {
    @Transient
    private Integer rowNum;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @Column(name = "faq_code")
    @NotNull(message = "FAQ Code 입력은 필수입니다.")
    private Long faqCode;

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
    @JoinColumn(name = "faq_code", insertable = false, updatable = false)
    private CommonEntity newFaqCode;

    // 조회 수 증가
    public void updateViewCount() {
        this.viewCount++;
    }

    public static FaqDTO toDto(FaqEntity entity) {
        if (entity == null) return null;
        return FaqDTO.builder()
                .rowNum(entity.getRowNum())
                .idx(entity.getIdx())
                .faqCode(entity.getFaqCode())
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
                .faqCode(dto.getFaqCode())
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

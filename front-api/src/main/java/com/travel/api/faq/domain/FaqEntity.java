package com.travel.api.faq.domain;

import com.travel.api.common.domain.CommonEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Builder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicUpdate
@Table(name = "travel_faq")
public class FaqEntity {

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
    private int viewCount;

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
                .idx(entity.getIdx())
                .title(entity.getTitle())
                .newFaqCode(CommonEntity.toDto(entity.newFaqCode))
                .description(entity.getDescription())
                .viewCount(entity.getViewCount())
                .visible(entity.getVisible())
                .build();
    }

    public static List<FaqDTO> toDtoList(List<FaqEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(FaqEntity::toDto)
                .collect(Collectors.toList());
    }
}

package com.travel.api.notice.domain;

import com.travel.api.common.domain.NewCommonMappedClass;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Builder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicUpdate
@Table(name = "travel_notice")
public class NoticeEntity extends NewCommonMappedClass {

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

    @Column(name = "top_fixed")
    private Boolean topFixed;

    // 조회 수 증가
    public void updateViewCount() {
        this.viewCount++;
    }

    public static NoticeDTO toDto(NoticeEntity entity) {
        return NoticeDTO.builder()
                .idx(entity.idx)
                .title(entity.title)
                .description(entity.description)
                .topFixed(entity.topFixed)
                .visible(entity.visible)
                .viewCount(entity.viewCount)
                .build();
    }

    public static List<NoticeDTO> toDtoList(List<NoticeEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(NoticeEntity::toDto)
                .collect(Collectors.toList());
    }
}

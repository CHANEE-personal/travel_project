package com.travel.travel_project.domain.notice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.travel.travel_project.domain.common.NewCommonMappedClass;
import com.travel.travel_project.domain.faq.FaqDTO;
import com.travel.travel_project.domain.faq.FaqEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "travel_notice")
public class NoticeEntity extends NewCommonMappedClass {
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

    @Column(name = "top_fixed")
    private Boolean topFixed;

    // 조회 수 증가
    public void updateViewCount() {
        this.viewCount++;
    }

    // 고정글 수정
    public void toggleTopFixed(Boolean topFixed) {
        this.topFixed = !topFixed;
    }

    public static NoticeDTO toDto(NoticeEntity entity) {
        return NoticeDTO.builder()
                .rowNum(entity.getRowNum())
                .idx(entity.getIdx())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .topFixed(entity.getTopFixed())
                .visible(entity.getVisible())
                .viewCount(entity.getViewCount())
                .build();

    }

    public NoticeEntity toEntity(NoticeDTO dto) {
        return NoticeEntity.builder()
                .rowNum(dto.getRowNum())
                .idx(dto.getIdx())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .topFixed(dto.getTopFixed())
                .viewCount(dto.getViewCount())
                .visible(dto.getVisible())
                .build();
    }

    public List<NoticeDTO> toDtoList(List<NoticeEntity> entityList) {
        List<NoticeDTO> list = new ArrayList<>(entityList.size());
        for (NoticeEntity noticeEntity : entityList) {
            list.add(toDto(noticeEntity));
        }

        return list;
    }

    public List<NoticeEntity> toEntityList(List<NoticeDTO> dtoList) {
        List<NoticeEntity> list = new ArrayList<>(dtoList.size());
        for (NoticeDTO noticeDTO : dtoList) {
            list.add(toEntity(noticeDTO));
        }

        return list;
    }
}

package com.travel.travel_project.domain.notice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.travel.travel_project.domain.common.NewCommonMappedClass;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

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
    private Integer rnum;

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
}
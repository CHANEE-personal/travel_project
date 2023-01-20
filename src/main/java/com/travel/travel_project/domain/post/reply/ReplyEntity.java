package com.travel.travel_project.domain.post.reply;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.travel.travel_project.domain.common.NewCommonMappedClass;
import com.travel.travel_project.domain.post.PostEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(of = "idx", callSuper = false)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "travel_post_comment")
public class ReplyEntity extends NewCommonMappedClass {

    @Transient
    private Integer rowNum;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @Lob
    @Column(name = "comment_title")
    @NotEmpty(message = "댓글 제목 입력은 필수입니다.")
    private String commentTitle;

    @Lob
    @Column(name = "comment_description")
    @NotEmpty(message = "댓글 내용 입력은 필수입니다.")
    private String commentDescription;

    @Column(name = "visible")
    @NotEmpty(message = "댓글 노출 여부 선택은 필수입니다.")
    private String visible;

    @Column(name = "favorite_count")
    private int favoriteCount;

    @JoinColumn(name= "post_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private PostEntity postEntity;

    @JoinColumn(name = "parent_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ReplyEntity parent;

    @Builder.Default
    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ReplyEntity> children = new ArrayList<>();

    public void update(ReplyEntity replyEntity) {
        this.commentTitle = replyEntity.commentTitle;
        this.commentDescription = replyEntity.commentDescription;
        this.visible = replyEntity.visible;
    }

    public void addReply(PostEntity postEntity, ReplyEntity replyEntity) {
        this.postEntity = postEntity;
        this.parent = replyEntity;
        this.children.add(this);
    }

    public static ReplyDTO toDto(ReplyEntity entity) {
        if (entity == null) return null;
        return ReplyDTO.builder()
                .rowNum(entity.getRowNum())
                .idx(entity.getIdx())
                .commentTitle(entity.getCommentTitle())
                .commentDescription(entity.getCommentDescription())
                .visible(entity.getVisible())
                .favoriteCount(entity.getFavoriteCount())
                .build();
    }

    public static List<ReplyDTO> toDtoList(List<ReplyEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(ReplyEntity::toDto)
                .collect(Collectors.toList());
    }
}
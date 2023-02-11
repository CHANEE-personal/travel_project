package com.travel.api.post.domain;

import com.travel.api.common.domain.NewCommonMappedClass;
import com.travel.api.post.domain.reply.ReplyEntity;
import com.travel.api.travel.domain.image.TravelImageEntity;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicUpdate;
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
@Builder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicUpdate
@Table(name = "travel_post")
public class PostEntity extends NewCommonMappedClass {

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

    @Column(name = "view_count")
    private int viewCount;

    @Column(name = "favorite_count")
    private int favoriteCount;

    @Column(name = "visible")
    @NotEmpty(message = "게시글 노출 여부 선택은 필수입니다.")
    private String visible;

    @Column(name = "popular")
    private Boolean popular;

    @Builder.Default
    @OneToMany(mappedBy = "postEntity", fetch = LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ReplyEntity> replyEntityList = new ArrayList<>();

    @Builder.Default
    @BatchSize(size = 100)
    @Where(clause = "type_name = 'post'")
    @OneToMany(mappedBy = "newPostImageEntity", fetch = LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<TravelImageEntity> postImageList = new ArrayList<>();

    public void addPostImage(TravelImageEntity commonImageEntity) {
        commonImageEntity.setNewPostImageEntity(this);
        this.postImageList.add(commonImageEntity);
    }

    public void addReplyList(ReplyEntity reply) {
        reply.setPostEntity(this);
        this.replyEntityList.add(reply);
    }

    public void update(PostEntity postEntity) {
        this.postTitle = postEntity.postTitle;
        this.postDescription = postEntity.postDescription;
        this.visible = postEntity.visible;
        this.popular = postEntity.popular;
    }

    public static PostDTO toDto(PostEntity entity) {
        if (entity == null) return null;
        return PostDTO.builder()
                .idx(entity.getIdx())
                .postTitle(entity.getPostTitle())
                .postDescription(entity.getPostDescription())
                .visible(entity.getVisible())
                .viewCount(entity.getViewCount())
                .favoriteCount(entity.getFavoriteCount())
                .postReplyList(ReplyEntity.toDtoList(entity.getReplyEntityList()))
                .postImageList(TravelImageEntity.toDtoList(entity.getPostImageList()))
                .build();
    }

    public static List<PostDTO> toDtoList(List<PostEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(PostEntity::toDto)
                .collect(Collectors.toList());
    }
}

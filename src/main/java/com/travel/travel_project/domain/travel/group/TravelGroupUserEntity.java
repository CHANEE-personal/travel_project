package com.travel.travel_project.domain.travel.group;

import com.travel.travel_project.domain.user.UserEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

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
@NoArgsConstructor
@Table(name = "tv_group_user")
public class TravelGroupUserEntity {
    @Transient
    private Integer rowNum;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @Column(name = "user_idx")
    @NotNull(message = "유저 idx 입력은 필수입니다.")
    private Long userIdx;

    @Column(name = "group_idx")
    @NotNull(message = "그룹 idx 입력은 필수입니다.")
    private Long groupIdx;

    @ManyToOne
    @JoinColumn(name = "idx", insertable = false, updatable = false)
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "idx", insertable = false, updatable = false)
    private TravelGroupEntity travelGroupEntity;

    public static TravelGroupUserDTO toDto(TravelGroupUserEntity entity) {
        if (entity == null) return null;
        return TravelGroupUserDTO.builder()
                .idx(entity.getIdx())
                .rowNum(entity.getRowNum())
                .userIdx(entity.getUserIdx())
                .groupIdx(entity.getGroupIdx())
                .build();
    }

    public static List<TravelGroupUserDTO> toDtoList(List<TravelGroupUserEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(TravelGroupUserEntity::toDto)
                .collect(Collectors.toList());
    }
}

package com.travel.api.travel.domain.group;

import com.travel.api.user.domain.UserEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicUpdate
@Table(name = "tv_group_user")
public class TravelGroupUserEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx", nullable = false)
    private UserEntity userEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_idx", nullable = false)
    private TravelGroupEntity travelGroupEntity;

    public static TravelGroupUserDTO toDto(TravelGroupUserEntity entity) {
        if (entity == null) return null;
        return TravelGroupUserDTO.builder()
                .idx(entity.getIdx())
                .userDTO(UserEntity.toDto(entity.userEntity))
                .groupDTO(TravelGroupEntity.toDto(entity.travelGroupEntity))
                .build();
    }

    public static List<TravelGroupUserDTO> toDtoList(List<TravelGroupUserEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(TravelGroupUserEntity::toDto)
                .collect(Collectors.toList());
    }
}

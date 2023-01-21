package com.travel.api.travel.domain.group;

import com.travel.api.common.domain.NewCommonMappedClass;
import com.travel.api.travel.domain.TravelEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;

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
@SuperBuilder
@EqualsAndHashCode(of = "idx", callSuper = false)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@Table(name = "tv_group_mst")
public class TravelGroupEntity extends NewCommonMappedClass {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @Column(name = "group_name")
    @NotEmpty(message = "그룹명 입력은 필수입니다.")
    @Lob
    private String groupName;

    @Column(name = "group_description")
    @NotEmpty(message = "그룹명 상세 내용 입력은 필수입니다.")
    @Lob
    private String groupDescription;

    @Column(name = "visible")
    private String visible;

    @Builder.Default
    @OneToMany(mappedBy = "travelGroupEntity", cascade = CascadeType.REMOVE)
    private List<TravelGroupUserEntity> travelGroupList = new ArrayList<>();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "travel_idx", referencedColumnName = "idx")
    private TravelEntity travelEntity;

    public void addGroup(TravelGroupUserEntity travelGroupUserEntity) {
        travelGroupUserEntity.setTravelGroupEntity(this);
        this.travelGroupList.add(travelGroupUserEntity);
    }

    public void update(TravelGroupEntity travelGroupEntity) {
        this.groupName = travelGroupEntity.groupName;
        this.groupDescription = travelGroupEntity.groupDescription;
        this.visible = travelGroupEntity.visible;
    }

    public static TravelGroupDto toDto(TravelGroupEntity entity) {
        if (entity == null) return null;
        return TravelGroupDto.builder()
                .idx(entity.getIdx())
                .travelIdx(entity.getTravelEntity().getIdx())
                .groupName(entity.getGroupName())
                .groupDescription(entity.getGroupDescription())
                .visible(entity.getVisible())
                .creator(entity.getCreator())
                .createTime(entity.getCreateTime())
                .updater(entity.getUpdater())
                .updateTime(entity.getUpdateTime())
                .build();
    }

    public static List<TravelGroupDto> toDtoList(List<TravelGroupEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(TravelGroupEntity::toDto)
                .collect(Collectors.toList());
    }
}

package com.travel.travel_project.domain.travel.group;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.travel.travel_project.domain.common.NewCommonMappedClass;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
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
@Table(name = "tv_group_mst")
public class TravelGroupEntity extends NewCommonMappedClass {
    @Transient
    private Integer rowNum;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @Column(name = "travel_idx")
    @NotNull(message = "여행지 idx 입력은 필수입니다.")
    private Long travelIdx;

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

    @JsonIgnore
    @OneToMany(mappedBy = "travelGroupEntity", cascade = CascadeType.REMOVE)
    private List<TravelGroupUserEntity> travelGroupList = new ArrayList<>();

    public static TravelGroupDTO toDto(TravelGroupEntity entity) {
        if (entity == null) return null;
        return TravelGroupDTO.builder()
                .rowNum(entity.getRowNum())
                .idx(entity.getIdx())
                .travelIdx(entity.getTravelIdx())
                .groupName(entity.getGroupName())
                .groupDescription(entity.getGroupDescription())
                .visible(entity.getVisible())
                .creator(entity.getCreator())
                .createTime(entity.getCreateTime())
                .updater(entity.getUpdater())
                .updateTime(entity.getUpdateTime())
                .build();
    }

    public static List<TravelGroupDTO> toDtoList(List<TravelGroupEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(TravelGroupEntity::toDto)
                .collect(Collectors.toList());
    }
}

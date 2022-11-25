package com.travel.travel_project.api.travel.mapper.group;

import com.travel.travel_project.domain.travel.group.TravelGroupDTO;
import com.travel.travel_project.domain.travel.group.TravelGroupEntity;

import java.util.ArrayList;
import java.util.List;

public class TravelGroupMapperImpl implements TravelGroupMapper {

    @Override
    public TravelGroupDTO toDto(TravelGroupEntity entity) {
        if (entity == null) return null;
        return TravelGroupDTO.builder()
                .rnum(entity.getRnum())
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

    @Override
    public TravelGroupEntity toEntity(TravelGroupDTO dto) {
        if (dto == null) return null;
        return TravelGroupEntity.builder()
                .rnum(dto.getRnum())
                .idx(dto.getIdx())
                .travelIdx(dto.getTravelIdx())
                .groupName(dto.getGroupName())
                .groupDescription(dto.getGroupDescription())
                .visible(dto.getVisible())
                .creator(dto.getCreator())
                .createTime(dto.getCreateTime())
                .updater(dto.getUpdater())
                .updateTime(dto.getUpdateTime())
                .build();
    }

    @Override
    public List<TravelGroupDTO> toDtoList(List<TravelGroupEntity> entityList) {
        if (entityList == null) return null;
        List<TravelGroupDTO> list = new ArrayList<>(entityList.size());
        for (TravelGroupEntity travelGroupEntity : entityList) {
            list.add(toDto(travelGroupEntity));
        }

        return list;
    }

    @Override
    public List<TravelGroupEntity> toEntityList(List<TravelGroupDTO> dtoList) {
        if (dtoList == null) return null;
        List<TravelGroupEntity> list = new ArrayList<>(dtoList.size());
        for (TravelGroupDTO travelGroupDTO : dtoList) {
            list.add(toEntity(travelGroupDTO));
        }

        return list;
    }
}

package com.travel.travel_project.api.travel.mapper.group;

import com.travel.travel_project.domain.travel.group.TravelGroupUserDTO;
import com.travel.travel_project.domain.travel.group.TravelGroupUserEntity;

import java.util.ArrayList;
import java.util.List;

public class TravelGroupUserMapperImpl implements TravelGroupUserMapper {

    @Override
    public TravelGroupUserDTO toDto(TravelGroupUserEntity entity) {
        if (entity == null) return null;
        return TravelGroupUserDTO.builder()
                .idx(entity.getIdx())
                .userIdx(entity.getUserIdx())
                .groupIdx(entity.getGroupIdx())
                .build();
    }

    @Override
    public TravelGroupUserEntity toEntity(TravelGroupUserDTO dto) {
        if (dto == null) return null;
        return TravelGroupUserEntity.builder()
                .idx(dto.getIdx())
                .userIdx(dto.getUserIdx())
                .groupIdx(dto.getGroupIdx())
                .build();
    }

    @Override
    public List<TravelGroupUserDTO> toDtoList(List<TravelGroupUserEntity> entityList) {
        if (entityList == null) return null;
        List<TravelGroupUserDTO> list = new ArrayList<>(entityList.size());
        for (TravelGroupUserEntity travelGroupUserEntity : entityList) {
            list.add(toDto(travelGroupUserEntity));
        }

        return list;
    }

    @Override
    public List<TravelGroupUserEntity> toEntityList(List<TravelGroupUserDTO> dtoList) {
        if (dtoList == null) return null;
        List<TravelGroupUserEntity> list = new ArrayList<>(dtoList.size());
        for (TravelGroupUserDTO travelGroupUserDTO : dtoList) {
            list.add(toEntity(travelGroupUserDTO));
        }

        return list;
    }
}

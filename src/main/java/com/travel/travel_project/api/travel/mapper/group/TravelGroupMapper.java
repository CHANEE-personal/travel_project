package com.travel.travel_project.api.travel.mapper.group;

import com.travel.travel_project.common.StructMapper;
import com.travel.travel_project.domain.travel.group.TravelGroupDTO;
import com.travel.travel_project.domain.travel.group.TravelGroupEntity;

import java.util.List;

import static org.mapstruct.factory.Mappers.getMapper;

public interface TravelGroupMapper extends StructMapper<TravelGroupDTO, TravelGroupEntity> {
    TravelGroupMapper INSTANCE = getMapper(TravelGroupMapper.class);

    @Override
    TravelGroupDTO toDto(TravelGroupEntity entity);

    @Override
    TravelGroupEntity toEntity(TravelGroupDTO dto);

    @Override
    List<TravelGroupDTO> toDtoList(List<TravelGroupEntity> entityList);

    @Override
    List<TravelGroupEntity> toEntityList(List<TravelGroupDTO> dtoList);
}

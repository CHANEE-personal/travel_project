package com.travel.travel_project.api.travel.mapper.group;

import com.travel.travel_project.common.StructMapper;
import com.travel.travel_project.domain.travel.group.TravelGroupUserDTO;
import com.travel.travel_project.domain.travel.group.TravelGroupUserEntity;

import java.util.List;

import static org.mapstruct.factory.Mappers.getMapper;

public interface TravelGroupUserMapper extends StructMapper<TravelGroupUserDTO, TravelGroupUserEntity> {
    TravelGroupUserMapper INSTANCE = getMapper(TravelGroupUserMapper.class);

    @Override
    TravelGroupUserDTO toDto(TravelGroupUserEntity entity);

    @Override
    TravelGroupUserEntity toEntity(TravelGroupUserDTO dto);

    @Override
    List<TravelGroupUserDTO> toDtoList(List<TravelGroupUserEntity> entityList);

    @Override
    List<TravelGroupUserEntity> toEntityList(List<TravelGroupUserDTO> dtoList);
}

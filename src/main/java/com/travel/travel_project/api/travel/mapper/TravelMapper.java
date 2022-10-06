package com.travel.travel_project.api.travel.mapper;

import com.travel.travel_project.common.StructMapper;
import com.travel.travel_project.domain.travel.TravelDTO;
import com.travel.travel_project.domain.travel.TravelEntity;
import org.mapstruct.Mapper;

import java.util.List;

import static org.mapstruct.factory.Mappers.getMapper;

@Mapper
public interface TravelMapper extends StructMapper<TravelDTO, TravelEntity> {
    TravelMapper INSTANCE = getMapper(TravelMapper.class);

    @Override
    TravelDTO toDto(TravelEntity entity);

    @Override
    TravelEntity toEntity(TravelDTO dto);

    @Override
    List<TravelDTO> toDtoList(List<TravelEntity> entityList);

    @Override
    List<TravelEntity> toEntityList(List<TravelDTO> dtoList);
}

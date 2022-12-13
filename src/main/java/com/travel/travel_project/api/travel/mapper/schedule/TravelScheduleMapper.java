package com.travel.travel_project.api.travel.mapper.schedule;

import com.travel.travel_project.common.StructMapper;
import com.travel.travel_project.domain.travel.schedule.TravelScheduleDTO;
import com.travel.travel_project.domain.travel.schedule.TravelScheduleEntity;

import java.util.List;

import static org.mapstruct.factory.Mappers.getMapper;

public interface TravelScheduleMapper extends StructMapper<TravelScheduleDTO, TravelScheduleEntity> {

    TravelScheduleMapper INSTANCE = getMapper(TravelScheduleMapper.class);

    @Override
    TravelScheduleDTO toDto(TravelScheduleEntity entity);

    @Override
    TravelScheduleEntity toEntity(TravelScheduleDTO dto);

    @Override
    List<TravelScheduleDTO> toDtoList(List<TravelScheduleEntity> entityList);

    @Override
    List<TravelScheduleEntity> toEntityList(List<TravelScheduleDTO> dtoList);
}

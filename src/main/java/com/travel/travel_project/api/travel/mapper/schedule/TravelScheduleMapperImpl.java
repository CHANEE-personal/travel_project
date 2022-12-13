package com.travel.travel_project.api.travel.mapper.schedule;

import com.travel.travel_project.domain.travel.schedule.TravelScheduleDTO;
import com.travel.travel_project.domain.travel.schedule.TravelScheduleEntity;

import java.util.ArrayList;
import java.util.List;

public class TravelScheduleMapperImpl implements TravelScheduleMapper {

    @Override
    public TravelScheduleDTO toDto(TravelScheduleEntity entity) {
        if (entity == null) return null;

        return TravelScheduleDTO.builder()
                .rowNum(entity.getRowNum())
                .idx(entity.getIdx())
                .userIdx(entity.getUserIdx())
                .travelIdx(entity.getTravelIdx())
                .scheduleDescription(entity.getScheduleDescription())
                .scheduleTime(entity.getScheduleTime())
                .build();
    }

    @Override
    public TravelScheduleEntity toEntity(TravelScheduleDTO dto) {
        if (dto == null) return null;

        return TravelScheduleEntity.builder()
                .rowNum(dto.getRowNum())
                .idx(dto.getIdx())
                .userIdx(dto.getUserIdx())
                .travelIdx(dto.getTravelIdx())
                .scheduleDescription(dto.getScheduleDescription())
                .scheduleTime(dto.getScheduleTime())
                .build();
    }

    @Override
    public List<TravelScheduleDTO> toDtoList(List<TravelScheduleEntity> entityList) {
        if (entityList == null) return null;

        List<TravelScheduleDTO> list = new ArrayList<>(entityList.size());
        for (TravelScheduleEntity travelScheduleEntity : entityList) {
            list.add(toDto(travelScheduleEntity));
        }

        return list;
    }

    @Override
    public List<TravelScheduleEntity> toEntityList(List<TravelScheduleDTO> dtoList) {
        if (dtoList == null) return null;

        List<TravelScheduleEntity> list = new ArrayList<>(dtoList.size());
        for (TravelScheduleDTO travelScheduleDTO : dtoList) {
            list.add(toEntity(travelScheduleDTO));
        }

        return list;
    }
}

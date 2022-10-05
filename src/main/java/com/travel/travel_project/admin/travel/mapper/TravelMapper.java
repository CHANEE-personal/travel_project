package com.travel.travel_project.admin.travel.mapper;

import com.travel.travel_project.admin.common.StructMapper;
import com.travel.travel_project.admin.travel.domain.AdminTravelDTO;
import com.travel.travel_project.admin.travel.domain.AdminTravelEntity;
import org.mapstruct.Mapper;

import java.util.List;

import static org.mapstruct.factory.Mappers.getMapper;

@Mapper
public interface TravelMapper extends StructMapper<AdminTravelDTO, AdminTravelEntity> {
    TravelMapper INSTANCE = getMapper(TravelMapper.class);

    @Override
    AdminTravelDTO toDto(AdminTravelEntity entity);

    @Override
    AdminTravelEntity toEntity(AdminTravelDTO dto);

    @Override
    List<AdminTravelDTO> toDtoList(List<AdminTravelEntity> entityList);

    @Override
    List<AdminTravelEntity> toEntityList(List<AdminTravelDTO> dtoList);
}

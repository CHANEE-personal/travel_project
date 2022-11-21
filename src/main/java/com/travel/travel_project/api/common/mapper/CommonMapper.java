package com.travel.travel_project.api.common.mapper;

import com.travel.travel_project.common.StructMapper;
import com.travel.travel_project.domain.common.CommonDTO;
import com.travel.travel_project.domain.common.CommonEntity;

import java.util.List;

import static org.mapstruct.factory.Mappers.getMapper;

public interface CommonMapper extends StructMapper<CommonDTO, CommonEntity> {
    CommonMapper INSTANCE = getMapper(CommonMapper.class);

    @Override
    CommonDTO toDto(CommonEntity entity);

    @Override
    CommonEntity toEntity(CommonDTO dto);

    @Override
    List<CommonDTO> toDtoList(List<CommonEntity> entityList);

    @Override
    List<CommonEntity> toEntityList(List<CommonDTO> dtoList);
}

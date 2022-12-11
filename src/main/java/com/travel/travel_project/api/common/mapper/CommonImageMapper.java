package com.travel.travel_project.api.common.mapper;

import com.travel.travel_project.common.StructMapper;
import com.travel.travel_project.domain.file.CommonImageDTO;
import com.travel.travel_project.domain.file.CommonImageEntity;

import java.util.List;

import static org.mapstruct.factory.Mappers.getMapper;

public interface CommonImageMapper extends StructMapper<CommonImageDTO, CommonImageEntity> {
    CommonImageMapper INSTANCE = getMapper(CommonImageMapper.class);

    @Override
    CommonImageDTO toDto(CommonImageEntity entity);

    @Override
    CommonImageEntity toEntity(CommonImageDTO dto);

    @Override
    List<CommonImageDTO> toDtoList(List<CommonImageEntity> entityList);

    @Override
    List<CommonImageEntity> toEntityList(List<CommonImageDTO> dtoList);
}

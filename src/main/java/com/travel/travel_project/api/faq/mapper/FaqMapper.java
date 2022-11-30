package com.travel.travel_project.api.faq.mapper;

import com.travel.travel_project.common.StructMapper;
import com.travel.travel_project.domain.faq.FaqDTO;
import com.travel.travel_project.domain.faq.FaqEntity;

import java.util.List;

import static org.mapstruct.factory.Mappers.getMapper;

public interface FaqMapper extends StructMapper<FaqDTO, FaqEntity> {

    FaqMapper INSTANCE = getMapper(FaqMapper.class);

    @Override
    FaqDTO toDto(FaqEntity entity);

    @Override
    FaqEntity toEntity(FaqDTO dto);

    @Override
    List<FaqDTO> toDtoList(List<FaqEntity> entityList);

    @Override
    List<FaqEntity> toEntityList(List<FaqDTO> dtoList);
}

package com.travel.travel_project.api.travel.mapper.review;


import com.travel.travel_project.api.travel.mapper.TravelMapper;
import com.travel.travel_project.common.StructMapper;
import com.travel.travel_project.domain.travel.review.TravelReviewDTO;
import com.travel.travel_project.domain.travel.review.TravelReviewEntity;
import org.mapstruct.Mapper;

import java.util.List;

import static org.mapstruct.factory.Mappers.getMapper;

@Mapper
public interface TravelReviewMapper extends StructMapper<TravelReviewDTO, TravelReviewEntity> {

    TravelReviewMapper INSTANCE = getMapper(TravelReviewMapper.class);

    @Override
    TravelReviewDTO toDto(TravelReviewEntity entity);

    @Override
    TravelReviewEntity toEntity(TravelReviewDTO dto);

    @Override
    List<TravelReviewDTO> toDtoList(List<TravelReviewEntity> entityList);

    @Override
    List<TravelReviewEntity> toEntityList(List<TravelReviewDTO> dtoList);
}

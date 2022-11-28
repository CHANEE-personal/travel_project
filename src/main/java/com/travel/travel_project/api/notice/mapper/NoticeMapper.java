package com.travel.travel_project.api.notice.mapper;

import com.travel.travel_project.common.StructMapper;
import com.travel.travel_project.domain.notice.NoticeDTO;
import com.travel.travel_project.domain.notice.NoticeEntity;

import java.util.List;

import static org.mapstruct.factory.Mappers.getMapper;

public interface NoticeMapper extends StructMapper<NoticeDTO, NoticeEntity> {

    NoticeMapper INSTANCE = getMapper(NoticeMapper.class);

    @Override
    NoticeDTO toDto(NoticeEntity entity);

    @Override
    NoticeEntity toEntity(NoticeDTO dto);

    @Override
    List<NoticeDTO> toDtoList(List<NoticeEntity> entityList);

    @Override
    List<NoticeEntity> toEntityList(List<NoticeDTO> dtoList);
}

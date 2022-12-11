package com.travel.travel_project.api.post.mapper;

import com.travel.travel_project.common.StructMapper;
import com.travel.travel_project.domain.post.PostDTO;
import com.travel.travel_project.domain.post.PostEntity;

import java.util.List;

import static org.mapstruct.factory.Mappers.getMapper;

public interface PostMapper extends StructMapper<PostDTO, PostEntity> {
    PostMapper INSTANCE = getMapper(PostMapper.class);

    @Override
    PostDTO toDto(PostEntity entity);

    @Override
    PostEntity toEntity(PostDTO dto);

    @Override
    List<PostDTO> toDtoList(List<PostEntity> entityList);

    @Override
    List<PostEntity> toEntityList(List<PostDTO> dtoList);
}

package com.travel.travel_project.api.user.mapper;

import com.travel.travel_project.common.StructMapper;
import com.travel.travel_project.domain.user.UserDTO;
import com.travel.travel_project.domain.user.UserEntity;

import java.util.List;

import static org.mapstruct.factory.Mappers.getMapper;

public interface UserMapper extends StructMapper<UserDTO, UserEntity> {
    UserMapper INSTANCE = getMapper(UserMapper.class);

    @Override
    UserDTO toDto(UserEntity entity);

    @Override
    UserEntity toEntity(UserDTO dto);

    @Override
    List<UserDTO> toDtoList(List<UserEntity> entityList);

    @Override
    List<UserEntity> toEntityList(List<UserDTO> dtoList);
}

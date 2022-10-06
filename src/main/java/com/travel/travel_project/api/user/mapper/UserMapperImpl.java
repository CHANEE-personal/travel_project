package com.travel.travel_project.api.user.mapper;

import com.travel.travel_project.domain.user.UserDTO;
import com.travel.travel_project.domain.user.UserEntity;

import java.util.ArrayList;
import java.util.List;

public class UserMapperImpl implements UserMapper {
    @Override
    public UserDTO toDto(UserEntity entity) {
        if (entity == null) return null;

        return UserDTO.builder()
                .idx(entity.getIdx())
                .userId(entity.getUserId())
                .password(entity.getPassword())
                .name(entity.getName())
                .email(entity.getEmail())
                .visible(entity.getVisible())
                .userToken(entity.getUserToken())
                .userRefreshToken(entity.getUserRefreshToken())
                .role(entity.getRole())
                .creator(entity.getCreator())
                .createTime(entity.getCreateTime())
                .updater(entity.getUpdater())
                .updateTime(entity.getUpdateTime())
                .build();
    }

    @Override
    public UserEntity toEntity(UserDTO dto) {
        if (dto == null) return null;

        return UserEntity.builder()
                .idx(dto.getIdx())
                .userId(dto.getUserId())
                .password(dto.getPassword())
                .name(dto.getName())
                .email(dto.getEmail())
                .visible(dto.getVisible())
                .userToken(dto.getUserToken())
                .userRefreshToken(dto.getUserRefreshToken())
                .role(dto.getRole())
                .creator(dto.getCreator())
                .createTime(dto.getCreateTime())
                .updater(dto.getUpdater())
                .updateTime(dto.getUpdateTime())
                .build();
    }

    @Override
    public List<UserDTO> toDtoList(List<UserEntity> entityList) {
        if (entityList == null) return null;

        List<UserDTO> list = new ArrayList<>(entityList.size());
        for (UserEntity userEntity : entityList) {
            list.add(toDto(userEntity));
        }

        return list;
    }

    @Override
    public List<UserEntity> toEntityList(List<UserDTO> dtoList) {
        if (dtoList == null) return null;

        List<UserEntity> list = new ArrayList<>(dtoList.size());
        for (UserDTO userDTO : dtoList) {
            list.add(toEntity(userDTO));
        }

        return list;
    }
}

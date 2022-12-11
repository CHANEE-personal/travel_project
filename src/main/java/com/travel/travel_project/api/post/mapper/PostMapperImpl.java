package com.travel.travel_project.api.post.mapper;

import com.travel.travel_project.domain.post.PostDTO;
import com.travel.travel_project.domain.post.PostEntity;

import java.util.ArrayList;
import java.util.List;

public class PostMapperImpl implements PostMapper {
    @Override
    public PostDTO toDto(PostEntity entity) {
        if (entity == null) return null;

        return PostDTO.builder()
                .rnum(entity.getRnum())
                .idx(entity.getIdx())
                .postTitle(entity.getPostTitle())
                .postDescription(entity.getPostDescription())
                .postParentIdx(entity.getPostParentIdx())
                .postTopIdx(entity.getPostTopIdx())
                .visible(entity.getVisible())
                .viewCount(entity.getViewCount())
                .favoriteCount(entity.getFavoriteCount())
                .build();
    }

    @Override
    public PostEntity toEntity(PostDTO dto) {
        if (dto == null) return null;
        return PostEntity.builder()
                .rnum(dto.getRnum())
                .idx(dto.getIdx())
                .postTitle(dto.getPostTitle())
                .postDescription(dto.getPostDescription())
                .postParentIdx(dto.getPostParentIdx())
                .postTopIdx(dto.getPostTopIdx())
                .visible(dto.getVisible())
                .viewCount(dto.getViewCount())
                .favoriteCount(dto.getFavoriteCount())
                .build();
    }

    @Override
    public List<PostDTO> toDtoList(List<PostEntity> entityList) {
        if (entityList == null) return null;

        List<PostDTO> list = new ArrayList<>(entityList.size());
        for (PostEntity postEntity : entityList) {
            list.add(toDto(postEntity));
        }

        return list;
    }

    @Override
    public List<PostEntity> toEntityList(List<PostDTO> dtoList) {
        if (dtoList == null) return null;

        List<PostEntity> list = new ArrayList<>(dtoList.size());
        for (PostDTO postDTO : dtoList) {
            list.add(toEntity(postDTO));
        }

        return list;
    }
}

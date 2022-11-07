package com.travel.travel_project.api.travel.mapper.review;

import com.travel.travel_project.domain.travel.review.TravelReviewDTO;
import com.travel.travel_project.domain.travel.review.TravelReviewEntity;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper
public class TravelReviewMapperImpl implements TravelReviewMapper {

    @Override
    public TravelReviewDTO toDto(TravelReviewEntity entity) {
        if (entity == null) return null;

        return TravelReviewDTO.builder()
                .idx(entity.getIdx())
                .rnum(entity.getRnum())
                .travelIdx(entity.getTravelIdx())
                .reviewTitle(entity.getReviewTitle())
                .reviewDescription(entity.getReviewDescription())
                .reviewParentIdx(entity.getReviewParentIdx())
                .reviewTopIdx(entity.getReviewTopIdx())
                .favoriteCount(entity.getFavoriteCount())
                .viewCount(entity.getViewCount())
                .visible(entity.getVisible())
                .popular(entity.getPopular())
                .creator(entity.getCreator())
                .createTime(entity.getCreateTime())
                .updater(entity.getUpdater())
                .updateTime(entity.getUpdateTime())
                .build();
    }

    @Override
    public TravelReviewEntity toEntity(TravelReviewDTO dto) {
        if (dto == null) return null;

        return TravelReviewEntity.builder()
                .idx(dto.getIdx())
                .rnum(dto.getRnum())
                .travelIdx(dto.getTravelIdx())
                .reviewTitle(dto.getReviewTitle())
                .reviewDescription(dto.getReviewDescription())
                .reviewParentIdx(dto.getReviewParentIdx())
                .reviewTopIdx(dto.getReviewTopIdx())
                .favoriteCount(dto.getFavoriteCount())
                .viewCount(dto.getViewCount())
                .visible(dto.getVisible())
                .popular(dto.getPopular())
                .creator(dto.getCreator())
                .createTime(dto.getCreateTime())
                .updater(dto.getUpdater())
                .updateTime(dto.getUpdateTime())
                .build();
    }

    @Override
    public List<TravelReviewDTO> toDtoList(List<TravelReviewEntity> entityList) {
        if (entityList == null) return null;

        List<TravelReviewDTO> list = new ArrayList<>(entityList.size());
        for (TravelReviewEntity travelReviewEntity : entityList) {
            list.add(toDto(travelReviewEntity));
        }

        return list;
    }

    @Override
    public List<TravelReviewEntity> toEntityList(List<TravelReviewDTO> dtoList) {
        if (dtoList == null) return null;

        List<TravelReviewEntity> list = new ArrayList<>(dtoList.size());
        for (TravelReviewDTO travelReviewDTO : dtoList) {
            list.add(toEntity(travelReviewDTO));
        }

        return list;
    }
}

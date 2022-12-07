package com.travel.travel_project.api.travel.mapper;

import com.travel.travel_project.domain.travel.TravelDTO;
import com.travel.travel_project.domain.travel.TravelEntity;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper
public class TravelMapperImpl implements TravelMapper {
    @Override
    public TravelDTO toDto(TravelEntity entity) {
        if (entity == null) return null;

        return TravelDTO.builder()
                .idx(entity.getIdx())
                .rnum(entity.getRnum())
                .travelCode(entity.getTravelCode())
                .travelTitle(entity.getTravelTitle())
                .travelDescription(entity.getTravelDescription())
                .travelAddress(entity.getTravelAddress())
                .travelZipCode(entity.getTravelZipCode())
                .favoriteCount(entity.getFavoriteCount())
                .viewCount(entity.getViewCount())
                .visible(entity.getVisible())
                .popular(entity.getPopular())
                .reviewList(entity.getTravelReviewEntityList())
                .creator(entity.getCreator())
                .createTime(entity.getCreateTime())
                .updater(entity.getUpdater())
                .updateTime(entity.getUpdateTime())
                .build();
    }

    @Override
    public TravelEntity toEntity(TravelDTO dto) {
        if (dto == null) return null;

        return TravelEntity.builder()
                .idx(dto.getIdx())
                .rnum(dto.getRnum())
                .travelCode(dto.getTravelCode())
                .travelTitle(dto.getTravelTitle())
                .travelDescription(dto.getTravelDescription())
                .travelAddress(dto.getTravelAddress())
                .travelZipCode(dto.getTravelZipCode())
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
    public List<TravelDTO> toDtoList(List<TravelEntity> entityList) {
        if (entityList == null) return null;

        List<TravelDTO> list = new ArrayList<>(entityList.size());
        for (TravelEntity travelEntity : entityList) {
            list.add(toDto(travelEntity));
        }

        return list;
    }

    @Override
    public List<TravelEntity> toEntityList(List<TravelDTO> dtoList) {
        if (dtoList == null) return null;

        List<TravelEntity> list = new ArrayList<>(dtoList.size());
        for (TravelDTO travelDTO : dtoList) {
            list.add(toEntity(travelDTO));
        }

        return list;
    }
}

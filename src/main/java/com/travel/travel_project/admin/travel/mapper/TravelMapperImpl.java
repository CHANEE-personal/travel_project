package com.travel.travel_project.admin.travel.mapper;

import com.travel.travel_project.admin.travel.domain.AdminTravelDTO;
import com.travel.travel_project.admin.travel.domain.AdminTravelEntity;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper
public class TravelMapperImpl implements TravelMapper {
    @Override
    public AdminTravelDTO toDto(AdminTravelEntity entity) {
        if (entity == null) return null;

        return AdminTravelDTO.builder()
                .idx(entity.getIdx())
                .rnum(entity.getRnum())
                .travelCode(entity.getTravelCode())
                .travelTitle(entity.getTravelTitle())
                .travelDescription(entity.getTravelDescription())
                .travelAddr(entity.getTravelAddr())
                .travelZipCode(entity.getTravelZipCode())
                .favoriteCount(entity.getFavoriteCount())
                .viewCount(entity.getViewCount())
                .visible(entity.getVisible())
                .creator(entity.getCreator())
                .createTime(entity.getCreateTime())
                .updater(entity.getUpdater())
                .updateTime(entity.getUpdateTime())
                .build();
    }

    @Override
    public AdminTravelEntity toEntity(AdminTravelDTO dto) {
        if (dto == null) return null;

        return AdminTravelEntity.builder()
                .idx(dto.getIdx())
                .rnum(dto.getRnum())
                .travelCode(dto.getTravelCode())
                .travelTitle(dto.getTravelTitle())
                .travelDescription(dto.getTravelDescription())
                .travelAddr(dto.getTravelAddr())
                .travelZipCode(dto.getTravelZipCode())
                .favoriteCount(dto.getFavoriteCount())
                .viewCount(dto.getViewCount())
                .visible(dto.getVisible())
                .creator(dto.getCreator())
                .createTime(dto.getCreateTime())
                .updater(dto.getUpdater())
                .updateTime(dto.getUpdateTime())
                .build();
    }

    @Override
    public List<AdminTravelDTO> toDtoList(List<AdminTravelEntity> entityList) {
        if (entityList == null) return null;

        List<AdminTravelDTO> list = new ArrayList<>(entityList.size());
        for (AdminTravelEntity adminTravelEntity : entityList) {
            list.add(toDto(adminTravelEntity));
        }

        return list;
    }

    @Override
    public List<AdminTravelEntity> toEntityList(List<AdminTravelDTO> dtoList) {
        if (dtoList == null) return null;

        List<AdminTravelEntity> list = new ArrayList<>(dtoList.size());
        for (AdminTravelDTO adminTravelDTO : dtoList) {
            list.add(toEntity(adminTravelDTO));
        }

        return list;
    }
}

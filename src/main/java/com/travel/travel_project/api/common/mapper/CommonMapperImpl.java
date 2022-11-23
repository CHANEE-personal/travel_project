package com.travel.travel_project.api.common.mapper;

import com.travel.travel_project.domain.common.CommonDTO;
import com.travel.travel_project.domain.common.CommonEntity;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper
public class CommonMapperImpl implements CommonMapper {

    @Override
    public CommonDTO toDto(CommonEntity entity) {
        if (entity == null) return null;

        return CommonDTO.builder()
                .rnum(entity.getRnum())
                .idx(entity.getIdx())
                .commonCode(entity.getCommonCode())
                .commonName(entity.getCommonName())
                .visible(entity.getVisible())
                .build();
    }

    @Override
    public CommonEntity toEntity(CommonDTO dto) {
        if (dto == null) return null;

        return CommonEntity.builder()
                .rnum(dto.getRnum())
                .idx(dto.getIdx())
                .commonCode(dto.getCommonCode())
                .commonName(dto.getCommonName())
                .visible(dto.getVisible())
                .build();
    }

    @Override
    public List<CommonDTO> toDtoList(List<CommonEntity> entityList) {
        if (entityList == null) return null;

        List<CommonDTO> list = new ArrayList<>(entityList.size());
        for (CommonEntity commonEntity : entityList) {
            list.add(toDto(commonEntity));
        }
        return list;
    }

    @Override
    public List<CommonEntity> toEntityList(List<CommonDTO> dtoList) {
        if (dtoList == null) return null;

        List<CommonEntity> list = new ArrayList<>(dtoList.size());
        for (CommonDTO commonDTO : dtoList) {
            list.add(toEntity(commonDTO));
        }
        return list;
    }
}

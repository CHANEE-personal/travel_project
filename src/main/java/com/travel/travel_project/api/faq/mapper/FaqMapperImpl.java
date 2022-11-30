package com.travel.travel_project.api.faq.mapper;

import com.travel.travel_project.domain.faq.FaqDTO;
import com.travel.travel_project.domain.faq.FaqEntity;

import java.util.ArrayList;
import java.util.List;

public class FaqMapperImpl implements FaqMapper {

    @Override
    public FaqDTO toDto(FaqEntity entity) {
        if (entity == null) return null;
        return FaqDTO.builder()
                .rnum(entity.getRnum())
                .idx(entity.getIdx())
                .faqCode(entity.getFaqCode())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .viewCount(entity.getViewCount())
                .visible(entity.getVisible())
                .build();
    }

    @Override
    public FaqEntity toEntity(FaqDTO dto) {
        if (dto == null) return null;
        return FaqEntity.builder()
                .rnum(dto.getRnum())
                .idx(dto.getIdx())
                .faqCode(dto.getFaqCode())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .viewCount(dto.getViewCount())
                .visible(dto.getVisible())
                .build();
    }

    @Override
    public List<FaqDTO> toDtoList(List<FaqEntity> entityList) {
        if (entityList == null) return null;
        List<FaqDTO> list = new ArrayList<>(entityList.size());
        for (FaqEntity faqEntity : entityList) {
            list.add(toDto(faqEntity));
        }

        return list;
    }

    @Override
    public List<FaqEntity> toEntityList(List<FaqDTO> dtoList) {
        if (dtoList == null) return null;
        List<FaqEntity> list = new ArrayList<>(dtoList.size());
        for (FaqDTO faqDTO : dtoList) {
            list.add(toEntity(faqDTO));
        }

        return list;
    }
}

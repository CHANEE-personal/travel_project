package com.travel.travel_project.api.notice.mapper;

import com.travel.travel_project.domain.notice.NoticeDTO;
import com.travel.travel_project.domain.notice.NoticeEntity;

import java.util.ArrayList;
import java.util.List;

public class NoticeMapperImpl implements NoticeMapper {

    @Override
    public NoticeDTO toDto(NoticeEntity entity) {
        if (entity == null) return null;
        return NoticeDTO.builder()
                .rnum(entity.getRnum())
                .idx(entity.getIdx())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .topFixed(entity.getTopFixed())
                .visible(entity.getVisible())
                .viewCount(entity.getViewCount())
                .build();

    }

    @Override
    public NoticeEntity toEntity(NoticeDTO dto) {
        if (dto == null) return null;
        return NoticeEntity.builder()
                .rnum(dto.getRnum())
                .idx(dto.getIdx())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .topFixed(dto.getTopFixed())
                .viewCount(dto.getViewCount())
                .visible(dto.getVisible())
                .build();
    }

    @Override
    public List<NoticeDTO> toDtoList(List<NoticeEntity> entityList) {
        if (entityList == null) return null;
        List<NoticeDTO> list = new ArrayList<>(entityList.size());
        for (NoticeEntity noticeEntity : entityList) {
            list.add(toDto(noticeEntity));
        }

        return list;
    }

    @Override
    public List<NoticeEntity> toEntityList(List<NoticeDTO> dtoList) {
        if (dtoList == null) return null;
        List<NoticeEntity> list = new ArrayList<>(dtoList.size());
        for (NoticeDTO noticeDTO : dtoList) {
            list.add(toEntity(noticeDTO));
        }

        return list;
    }
}

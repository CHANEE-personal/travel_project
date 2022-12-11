package com.travel.travel_project.api.common.mapper;

import com.travel.travel_project.domain.file.CommonImageDTO;
import com.travel.travel_project.domain.file.CommonImageEntity;

import java.util.ArrayList;
import java.util.List;

public class CommonImageMapperImpl implements CommonImageMapper {
    @Override
    public CommonImageDTO toDto(CommonImageEntity entity) {
        if (entity == null) return null;

        return CommonImageDTO.builder()
                .idx(entity.getIdx())
                .typeIdx(entity.getTypeIdx())
                .entityType(entity.getEntityType())
                .fileMask(entity.getFileMask())
                .fileSize(entity.getFileSize())
                .fileName(entity.getFileName())
                .fileNum(entity.getFileNum())
                .filePath(entity.getFilePath())
                .imageType(entity.getImageType())
                .visible(entity.getVisible())
                .regDate(entity.getRegDate())
                .build();
    }

    @Override
    public CommonImageEntity toEntity(CommonImageDTO dto) {
        if (dto == null) return null;

        return CommonImageEntity.builder()
                .idx(dto.getIdx())
                .typeIdx(dto.getTypeIdx())
                .entityType(dto.getEntityType())
                .fileMask(dto.getFileMask())
                .fileSize(dto.getFileSize())
                .fileName(dto.getFileName())
                .fileNum(dto.getFileNum())
                .filePath(dto.getFilePath())
                .imageType(dto.getImageType())
                .visible(dto.getVisible())
                .regDate(dto.getRegDate())
                .build();
    }

    @Override
    public List<CommonImageDTO> toDtoList(List<CommonImageEntity> entityList) {
        if (entityList == null) return null;

        List<CommonImageDTO> list = new ArrayList<>(entityList.size());
        for (CommonImageEntity commonImageEntity : entityList) {
            list.add(toDto(commonImageEntity));
        }

        return list;
    }

    @Override
    public List<CommonImageEntity> toEntityList(List<CommonImageDTO> dtoList) {
        if (dtoList == null) return null;

        List<CommonImageEntity> list = new ArrayList<>(dtoList.size());
        for (CommonImageDTO commonImageDTO : dtoList) {
            list.add(toEntity(commonImageDTO));
        }

        return list;
    }
}

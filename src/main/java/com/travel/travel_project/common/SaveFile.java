package com.travel.travel_project.common;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travel.travel_project.domain.common.EntityType;
import com.travel.travel_project.domain.post.PostEntity;
import com.travel.travel_project.domain.post.image.PostImageDTO;
import com.travel.travel_project.domain.post.image.PostImageEntity;
import com.travel.travel_project.domain.travel.image.TravelImageDTO;
import com.travel.travel_project.domain.travel.image.TravelImageEntity;
import com.travel.travel_project.domain.travel.TravelEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.travel.travel_project.domain.common.EntityType.REVIEW;
import static com.travel.travel_project.domain.common.EntityType.TRAVEL;
import static com.travel.travel_project.domain.file.QCommonImageEntity.commonImageEntity;

@Component
@RequiredArgsConstructor
public class SaveFile {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    @Value("${image.uploadPath}")
    private String fileDirPath;

    /**
     * <pre>
     * 1. MethodName : saveTravelFile
     * 2. ClassName  : SaveFile.java
     * 3. Comment    : 다중 이미지 저장
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 12. 11.
     * </pre>
     */
    public List<TravelImageDTO> saveTravelFile(TravelEntity travelEntity, List<MultipartFile> multipartFiles, TravelImageEntity travelImageEntity) throws IOException {
        List<TravelImageEntity> travelImageEntityList = new ArrayList<>();
        int index = 0;
        for(MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                travelImageEntityList.add(saveTravelFile(multipartFile, travelImageEntity.getEntityType(), travelEntity, index));
            }
            index++;
        }
        return travelImageEntityList.stream().map(TravelImageEntity::toDto).collect(Collectors.toList());
    }

    /**
     * <pre>
     * 1. MethodName : saveTravelFile
     * 2. ClassName  : SaveFile.java
     * 3. Comment    : 단일 이미지 저장
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 12. 11.
     * </pre>
     */
    public TravelImageEntity saveTravelFile(MultipartFile multipartFile, EntityType entityType, TravelEntity travelEntity, int index) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String fileId = createSaveFileName(multipartFile.getOriginalFilename());
        long fileSize = multipartFile.getSize();
        String mainOrSub = index == 0 ? "main" : "sub" + index;

        // 파일 업로드
        multipartFile.transferTo(new File(saveFilePath(fileId, entityType)));
//        getRuntime().exec("chmod -R 755 " + saveFilePath(fileId, entityType));

        TravelImageEntity imageEntity = TravelImageEntity.builder()
                .filePath(saveFilePath(fileId, entityType))
                .fileName(multipartFile.getOriginalFilename())
                .fileSize(fileSize)
                .fileMask(fileId)
                .fileNum(index)
                .entityType(entityType)
                .travelImageEntity(travelEntity)
                .imageType(mainOrSub)
                .visible("Y")
                .regDate(LocalDateTime.now())
                .build();

        em.persist(imageEntity);

        return imageEntity;
    }

    /**
     * <pre>
     * 1. MethodName : saveTravelFile
     * 2. ClassName  : SaveFile.java
     * 3. Comment    : 다중 이미지 저장
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 12. 11.
     * </pre>
     */
    public List<PostImageDTO> savePostFile(PostEntity postEntity, List<MultipartFile> multipartFiles, PostImageEntity postImageEntity) throws IOException {
        List<PostImageEntity> postImageEntityList = new ArrayList<>();
        int index = 0;
        for(MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                postImageEntityList.add(savePostFile(multipartFile, postImageEntity.getEntityType(), postEntity, index));
            }
            index++;
        }
        return postImageEntityList.stream().map(PostImageEntity::toDto).collect(Collectors.toList());
    }

    /**
     * <pre>
     * 1. MethodName : saveTravelFile
     * 2. ClassName  : SaveFile.java
     * 3. Comment    : 단일 이미지 저장
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 12. 11.
     * </pre>
     */
    public PostImageEntity savePostFile(MultipartFile multipartFile, EntityType entityType, PostEntity postEntity, int index) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String fileId = createSaveFileName(multipartFile.getOriginalFilename());
        long fileSize = multipartFile.getSize();
        String mainOrSub = index == 0 ? "main" : "sub" + index;

        // 파일 업로드
        multipartFile.transferTo(new File(saveFilePath(fileId, entityType)));
//        getRuntime().exec("chmod -R 755 " + saveFilePath(fileId, entityType));

        PostImageEntity imageEntity = PostImageEntity.builder()
                .filePath(saveFilePath(fileId, entityType))
                .fileName(multipartFile.getOriginalFilename())
                .fileSize(fileSize)
                .fileMask(fileId)
                .fileNum(index)
                .entityType(entityType)
                .postImageEntity(postEntity)
                .imageType(mainOrSub)
                .visible("Y")
                .regDate(LocalDateTime.now())
                .build();

        em.persist(imageEntity);

        return imageEntity;
    }

    /**
     * <pre>
     * 1. MethodName : saveFilePath
     * 2. ClassName  : SaveFile.java
     * 3. Comment    : 이미지 업로드 경로
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 12. 11.
     * </pre>
     */
    public String saveFilePath(String saveFileName, EntityType entityType) {
        String typePath = (entityType == TRAVEL) ? "/travel/" : (entityType == REVIEW) ? "/review/" : "/post/";
        File dir = new File(fileDirPath+typePath);
        if (!dir.exists()) dir.mkdirs();
        return fileDirPath + typePath + saveFileName;
    }

    /**
     * <pre>
     * 1. MethodName : createSaveFileName
     * 2. ClassName  : SaveFile.java
     * 3. Comment    : 업로드 이미지 파일명 생성
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 12. 11.
     * </pre>
     */
    private String createSaveFileName(String originalFileName) {
        String uuid = UUID.randomUUID().toString();
        String ext = extractExt(originalFileName);

        return uuid + ext;
    }

    /**
     * <pre>
     * 1. MethodName : extractExt
     * 2. ClassName  : SaveFile.java
     * 3. Comment    : 이미지 확장자 추출
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 12. 11.
     * </pre>
     */
    private String extractExt(String originalFileName) {
        int idx = originalFileName.lastIndexOf(".");
        return originalFileName.substring(idx);
    }

    /**
     * <pre>
     * 1. MethodName : maxSubCnt
     * 2. ClassName  : SaveFile.java
     * 3. Comment    : 이미지 파일 최대 값 가져오기
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 12. 11.
     * </pre>
     */
    public Integer maxSubCnt(TravelImageEntity exTravelImageEntity) {
        return queryFactory.selectFrom(commonImageEntity)
                .select(commonImageEntity.fileNum.max())
                .where(commonImageEntity.entityType.eq(exTravelImageEntity.getEntityType()))
                .fetchFirst();
    }
}

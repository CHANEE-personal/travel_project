package com.travel.travel_project.common;

import com.travel.travel_project.domain.common.EntityType;
import com.travel.travel_project.domain.file.CommonImageEntity;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@ExtendWith(MockitoExtension.class)
@DisplayName("이미지 Repository Test")
class SaveFileTest {

    private final SaveFile saveFile;

    @Test
    @DisplayName("여행지이미지저장테스트")
    void 여행지이미지저장테스트() throws IOException {
        List<MultipartFile> imageFiles = of(
                new MockMultipartFile("0522045010647","0522045010647.png",
                        "image/png" , new FileInputStream("src/main/resources/static/images/0522045010647.png")),
                new MockMultipartFile("0522045010772","0522045010772.png" ,
                        "image/png" , new FileInputStream("src/main/resources/static/images/0522045010772.png"))
        );

        saveFile.saveFile(imageFiles, CommonImageEntity.builder().entityType(EntityType.TRAVEL).typeIdx(1L).build());
        saveFile.saveFile(imageFiles, CommonImageEntity.builder().entityType(EntityType.REVIEW).typeIdx(1L).build());
    }

}
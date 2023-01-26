package com.travel.api.post.domain.image;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.travel.api.common.domain.EntityType;
import com.travel.api.post.domain.PostDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@Getter
@Builder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ApiModel
public class PostImageDto {

    @ApiModelProperty(value = "파일 IDX", required = true, hidden = true, example = "1")
    private Long idx;

    @ApiModelProperty(value = "분야 IDX", required = true, hidden = true, example = "1")
    private PostDto newPostDto;

    @Enumerated(EnumType.STRING)
    @ApiModelProperty(value = "분야명", required = true, hidden = true)
    private EntityType entityType;

    @ApiModelProperty(value = "파일 Number", required = true, hidden = true, example = "1")
    private Integer fileNum;

    @ApiModelProperty(required = true, value = "파일명", hidden = true)
    private String fileName;

    @ApiModelProperty(value = "파일SIZE", hidden = true)
    private Long fileSize;

    @ApiModelProperty(value = "파일MASK", hidden = true)
    private String fileMask;

    @ApiModelProperty(value = "파일경로", hidden = true)
    private String filePath;

    @ApiModelProperty(value = "메인 이미지 구분", hidden = true)
    private String imageType;

    @ApiModelProperty(value = "이미지 사용 여부", hidden = true)
    private String visible;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @ApiModelProperty(value = "등록일자", hidden = true)
    private LocalDateTime regDate;
}

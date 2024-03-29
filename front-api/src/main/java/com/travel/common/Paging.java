package com.travel.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@ApiModel
public class Paging {
    @ApiModelProperty(value = "페이지 번호(0..N)", example = "1")
    private Integer pageNum;

    @ApiModelProperty(value = "페이지 크기", allowableValues = "range[0,100]", example = "1")
    private Integer size;

    public PageRequest getPageRequest(Integer page, Integer size) {
        return PageRequest.of(page, size, Sort.by("idx").descending());
    }
}

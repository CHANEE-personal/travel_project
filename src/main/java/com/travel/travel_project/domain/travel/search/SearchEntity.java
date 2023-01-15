package com.travel.travel_project.domain.travel.search;

import com.travel.travel_project.domain.common.NewCommonMappedClass;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
@SuperBuilder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@AllArgsConstructor
@Table(name = "travel_search")
public class SearchEntity extends NewCommonMappedClass {
    @Transient
    private Integer rowNum;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @Column(name = "search_keyword")
    @NotEmpty(message = "검색 키워드는 필수입니다.")
    private String searchKeyword;

    public static SearchDTO toDto(SearchEntity entity) {
        if (entity == null) return null;
        return SearchDTO.builder()
                .rowNum(entity.getRowNum())
                .idx(entity.getIdx())
                .searchKeyword(entity.getSearchKeyword())
                .creator(entity.getCreator())
                .createTime(entity.getCreateTime())
                .updater(entity.getUpdater())
                .updateTime(entity.getUpdateTime())
                .build();
    }

    public static List<SearchDTO> toDtoList(List<SearchEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(SearchEntity::toDto)
                .collect(Collectors.toList());
    }
}

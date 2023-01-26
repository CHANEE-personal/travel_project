package com.travel.api.travel.domain.search;

import com.travel.api.common.domain.NewCommonMappedClass;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Entity
@Builder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@Table(name = "travel_search")
public class SearchEntity extends NewCommonMappedClass {

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
                .idx(entity.getIdx())
                .searchKeyword(entity.getSearchKeyword())
                .build();
    }

    public static List<SearchDTO> toDtoList(List<SearchEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(SearchEntity::toDto)
                .collect(Collectors.toList());
    }
}

package com.travel.travel_project.domain.common;

import com.travel.travel_project.domain.travel.TravelEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
@SuperBuilder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "travel_cmm_code")
public class CommonEntity extends NewCommonMappedClass {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @Column(name = "common_code")
    @NotEmpty(message = "공통 코드 입력은 필수입니다.")
    private Integer commonCode;

    @Column(name = "common_name")
    @NotEmpty(message = "공통 코드명 입력은 필수입니다.")
    private String commonName;

    @Column(name = "visible")
    @NotEmpty(message = "공통 코드 사용 여부는 필수입니다.")
    private String visible;

    @OneToMany(mappedBy = "newTravelCode", cascade = MERGE, fetch = LAZY)
    private List<TravelEntity> adminTravelEntityList = new ArrayList<>();

}

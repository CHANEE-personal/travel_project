package com.travel.travel_project.admin.common.domain;

import com.travel.travel_project.admin.travel.domain.AdminTravelEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
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
public class AdminCommonEntity extends NewCommonMappedClass {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @Column(name = "travel_code")
    private Integer travelCode;

    @Column(name = "travel_name")
    private String travelName;

    @Column(name = "visible")
    private String visible;

    @OneToMany(mappedBy = "newTravelCode", cascade = MERGE, fetch = LAZY)
    private List<AdminTravelEntity> adminTravelEntityList = new ArrayList<>();

}

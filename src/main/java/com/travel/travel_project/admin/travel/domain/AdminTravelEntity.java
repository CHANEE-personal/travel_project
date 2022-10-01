package com.travel.travel_project.admin.travel.domain;

import com.travel.travel_project.admin.common.domain.NewCommonMappedClass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "travel")
public class AdminTravelEntity extends NewCommonMappedClass {

    @Transient
    private Integer rnum;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @Column(name = "travel_code")
    private String travelCode;

    @Column(name = "travel_addr")
    private String travelAddr;

    @Column(name = "travel_zip_code")
    private String travelZipCode;

    @Column(name = "favorite_count")
    private Integer favoriteCount;

    @Column(name = "visible")
    private String visible;
}

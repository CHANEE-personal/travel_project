package com.travel.travel_project.domain.travel.group;

import com.travel.travel_project.domain.user.UserEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(of = "idx", callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tv_group_user")
public class TravelGroupUserEntity {
    @Transient
    private Integer rnum;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @Column(name = "user_idx", insertable = false, updatable = false)
    @NotNull(message = "유저 idx 입력은 필수입니다.")
    private Long userIdx;

    @Column(name = "group_idx", insertable = false, updatable = false)
    @NotNull(message = "그룹 idx 입력은 필수입니다.")
    private Long groupIdx;

    @ManyToOne
    @JoinColumn(name = "user_idx")
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "group_idx")
    private TravelGroupEntity travelGroupEntity;
}

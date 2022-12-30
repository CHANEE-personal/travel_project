package com.travel.travel_project.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.travel.travel_project.domain.common.NewCommonMappedClass;
import com.travel.travel_project.domain.faq.FaqDTO;
import com.travel.travel_project.domain.travel.group.TravelGroupUserEntity;
import com.travel.travel_project.domain.travel.schedule.TravelScheduleEntity;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@TypeDef(name = "json", typeClass = JsonStringType.class)
@Table(name = "travel_user")
public class UserEntity extends NewCommonMappedClass {
    @Transient
    private Integer rowNum;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @Column(name = "user_id", unique = true)
    @NotEmpty(message = "유저 ID 입력은 필수입니다.")
    private String userId;

    @Column(name = "password")
    @NotEmpty(message = "유저 Password 입력은 필수입니다.")
    private String password;

    @Column(name = "name")
    @NotEmpty(message = "유저 이름 입력은 필수입니다.")
    private String name;

    @Column(name = "email", unique = true)
    @Email
    @NotEmpty(message = "유저 이메일 입력은 필수입니다.")
    private String email;

    @Column(name = "visible")
    @NotEmpty(message = "유저 사용 여부 선택은 필수입니다.")
    private String visible;

    @Column(name = "user_token")
    private String userToken;

    @Column(name = "user_refresh_token")
    private String userRefreshToken;

    @Enumerated(value = STRING)
    private Role role;

    @Type(type = "json")
    @Column(columnDefinition = "json", name = "favorite_travel_ids")
    private List<String> favoriteTravelIdx = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.REMOVE)
    private List<TravelGroupUserEntity> userList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.REMOVE)
    private List<TravelScheduleEntity> userScheduleList = new ArrayList<>();

    public static UserDTO toDto(UserEntity entity) {
        if (entity == null) return null;
        return UserDTO.builder()
                .idx(entity.getIdx())
                .userId(entity.getUserId())
                .password(entity.getPassword())
                .name(entity.getName())
                .email(entity.getEmail())
                .visible(entity.getVisible())
                .userToken(entity.getUserToken())
                .userRefreshToken(entity.getUserRefreshToken())
                .favoriteTravelIdx(entity.getFavoriteTravelIdx())
                .role(entity.getRole())
                .creator(entity.getCreator())
                .createTime(entity.getCreateTime())
                .updater(entity.getUpdater())
                .updateTime(entity.getUpdateTime())
                .build();
    }

    public static UserEntity toEntity(UserDTO dto) {
        if (dto == null) return null;
        return UserEntity.builder()
                .idx(dto.getIdx())
                .userId(dto.getUserId())
                .password(dto.getPassword())
                .name(dto.getName())
                .email(dto.getEmail())
                .visible(dto.getVisible())
                .userToken(dto.getUserToken())
                .userRefreshToken(dto.getUserRefreshToken())
                .role(dto.getRole())
                .creator(dto.getCreator())
                .createTime(dto.getCreateTime())
                .updater(dto.getUpdater())
                .updateTime(dto.getUpdateTime())
                .build();
    }

    public static List<UserDTO> toDtoList(List<UserEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(UserEntity::toDto)
                .collect(Collectors.toList());
    }

    public List<UserEntity> toEntityList(List<UserDTO> dtoList) {
        if (dtoList == null) return null;
        return dtoList.stream()
                .map(UserEntity::toEntity)
                .collect(Collectors.toList());
    }
}

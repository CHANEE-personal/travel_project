package com.travel.api.user.domain;

import com.travel.api.common.domain.NewCommonMappedClass;
import com.travel.api.travel.domain.group.TravelGroupUserEntity;
import com.travel.api.travel.domain.schedule.TravelScheduleEntity;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
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
@Builder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicUpdate
@TypeDef(name = "json", typeClass = JsonStringType.class)
@Table(name = "travel_user")
public class UserEntity extends NewCommonMappedClass {

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

    @Builder.Default
    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.REMOVE)
    private List<TravelGroupUserEntity> userList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.REMOVE)
    private List<TravelScheduleEntity> userScheduleList = new ArrayList<>();

    public void update(UserEntity userEntity) {
        this.userId = userEntity.userId;
        this.password = userEntity.password;
        this.name = userEntity.name;
        this.email = userEntity.email;
        this.visible = userEntity.visible;
        this.role = userEntity.role;
    }

    public void updateToken(String token) {
        this.userToken = token;
    }

    public void updateRefreshToken(String refreshToken) {
        this.userRefreshToken = refreshToken;
    }

    public void addGroup(TravelGroupUserEntity travelGroupUserEntity) {
        travelGroupUserEntity.setUserEntity(this);
        this.userList.add(travelGroupUserEntity);
    }

    public static UserDto toDto(UserEntity entity) {
        if (entity == null) return null;
        return UserDto.builder()
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
                .build();
    }

    public static List<UserDto> toDtoList(List<UserEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(UserEntity::toDto)
                .collect(Collectors.toList());
    }
}

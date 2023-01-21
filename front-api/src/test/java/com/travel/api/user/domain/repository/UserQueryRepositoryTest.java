package com.travel.api.user.domain.repository;

import com.travel.api.user.domain.UserDTO;
import com.travel.api.user.domain.UserEntity;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.TypeDef;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.event.EventListener;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.travel.api.user.domain.Role.ROLE_TRAVEL_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@ExtendWith(MockitoExtension.class)
@DisplayName("유저 Repository Test")
@TypeDef(name = "json", typeClass = JsonStringType.class)
class UserQueryRepositoryTest {
    @Mock
    private UserQueryRepository mockUserQueryRepository;
    private final UserQueryRepository userQueryRepository;
    private final EntityManager em;

    private UserEntity userEntity;
    private UserDTO userDTO;

    void createUser() {
        userEntity = UserEntity.builder()
                .userId("cksgml159")
                .email("cksgml159@naver.com")
                .role(ROLE_TRAVEL_USER)
                .visible("Y")
                .build();

        userDTO = UserEntity.toDto(userEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createUser();
    }

    @Test
    @DisplayName("유저 좋아하는 여행지 추가")
    void 유저좋아하는여행지추가() throws JSONException {
        List<String> list = new ArrayList<>();
        list.add("1");

        userEntity = UserEntity.builder()
                .userId("cksgml159")
                .password("test159")
                .email("cksgml159@naver.com")
                .name("test")
                .role(ROLE_TRAVEL_USER)
                .favoriteTravelIdx(list)
                .visible("Y")
                .build();

        em.persist(userEntity);

        JSONArray jsonArray = new JSONArray();
        jsonArray.put(userEntity.getFavoriteTravelIdx());
        Object insertObject = jsonArray.get(0);

        assertThat(userEntity.getFavoriteTravelIdx()).isEqualTo(insertObject);

        UserDTO updateUser = userQueryRepository.addFavoriteTravel(userEntity.getIdx(), 2L);

        jsonArray.remove(0);
        jsonArray.put(updateUser.getFavoriteTravelIdx());
        Object updateObject = jsonArray.get(0);

        assertThat(updateUser.getFavoriteTravelIdx()).isEqualTo(updateObject);
    }
}

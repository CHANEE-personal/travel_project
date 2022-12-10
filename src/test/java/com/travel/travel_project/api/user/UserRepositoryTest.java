package com.travel.travel_project.api.user;

import com.travel.travel_project.domain.user.UserDTO;
import com.travel.travel_project.domain.user.UserEntity;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.TypeDef;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.travel.travel_project.api.user.mapper.UserMapper.INSTANCE;
import static com.travel.travel_project.domain.user.Role.ROLE_ADMIN;
import static com.travel.travel_project.domain.user.Role.ROLE_TRAVEL_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
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
class UserRepositoryTest {
    @Mock
    private UserRepository mockUserRepository;
    private final UserRepository userRepository;
    private final EntityManager em;

    private UserEntity userEntity;
    private UserDTO userDTO;

    void createUser() {
        userEntity = UserEntity.builder()
                .userId("cksgml159")
                .email("cksgml159@naver.com")
                .role(ROLE_ADMIN)
                .visible("Y")
                .build();

        userDTO = INSTANCE.toDto(userEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createUser();
    }

    @Test
    @Disabled
    @DisplayName("유저리스트조회테스트")
    void 유저리스트조회테스트() {
        // given
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("jpaStartPage", 1);
        userMap.put("size", 3);

        // then
        assertThat(userRepository.findUsersList(userMap)).isNotEmpty();
    }

    @Test
    @DisplayName("유저 리스트 Mockito 검색 조회 테스트")
    void 유저리스트Mockito검색조회테스트() {
        // given
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("jpaStartPage", 1);
        userMap.put("size", 3);

        List<UserDTO> userList = new ArrayList<>();
        userList.add(UserDTO.builder().idx(1L).userId("test01")
                        .adminName("관리자01").name("조찬희").password("test01")
                        .email("test01@test.com").visible("Y").build());

        // when
        when(mockUserRepository.findUsersList(userMap)).thenReturn(userList);
        List<UserDTO> newUserList = mockUserRepository.findUsersList(userMap);

        // then
        assertThat(newUserList.get(0).getIdx()).isEqualTo(userList.get(0).getIdx());
        assertThat(newUserList.get(0).getUserId()).isEqualTo(userList.get(0).getUserId());
        assertThat(newUserList.get(0).getAdminName()).isEqualTo(userList.get(0).getAdminName());
        assertThat(newUserList.get(0).getName()).isEqualTo(userList.get(0).getName());
        assertThat(newUserList.get(0).getEmail()).isEqualTo(userList.get(0).getEmail());

        // verify
        verify(mockUserRepository, times(1)).findUsersList(userMap);
        verify(mockUserRepository, atLeastOnce()).findUsersList(userMap);
        verifyNoMoreInteractions(mockUserRepository);

        InOrder inOrder = inOrder(mockUserRepository);
        inOrder.verify(mockUserRepository).findUsersList(userMap);
    }

    @Test
    @DisplayName("유저 리스트 BDD 검색 조회 테스트")
    void 유저리스트BDD검색조회테스트() {
        // given
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("jpaStartPage", 1);
        userMap.put("size", 3);

        List<UserDTO> userList = new ArrayList<>();
        userList.add(UserDTO.builder().idx(1L).userId("test01")
                .adminName("관리자01").name("조찬희").password("test01")
                .email("test01@test.com").visible("Y").build());

        // when
        given(mockUserRepository.findUsersList(userMap)).willReturn(userList);
        List<UserDTO> newUserList = mockUserRepository.findUsersList(userMap);

        // then
        assertThat(newUserList.get(0).getIdx()).isEqualTo(userList.get(0).getIdx());
        assertThat(newUserList.get(0).getUserId()).isEqualTo(userList.get(0).getUserId());
        assertThat(newUserList.get(0).getAdminName()).isEqualTo(userList.get(0).getAdminName());
        assertThat(newUserList.get(0).getName()).isEqualTo(userList.get(0).getName());
        assertThat(newUserList.get(0).getEmail()).isEqualTo(userList.get(0).getEmail());

        // verify
        then(mockUserRepository).should(times(1)).findUsersList(userMap);
        then(mockUserRepository).should(atLeastOnce()).findUsersList(userMap);
        then(mockUserRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("유저 회원가입 Mockito 테스트")
    void 유저회원가입Mockito테스트() {
        // given
        UserEntity userEntity = UserEntity.builder()
                .idx(1L).userId("test01")
                .name("조찬희").password("test01")
                .email("test01@test.com").visible("Y").build();
        userRepository.insertUser(userEntity);
        userDTO = INSTANCE.toDto(userEntity);

        // when
        when(mockUserRepository.findOneUser(userEntity.getIdx())).thenReturn(userDTO);
        UserDTO userInfo = mockUserRepository.findOneUser(userEntity.getIdx());

        // then
        assertThat(userInfo.getUserId()).isEqualTo("test01");
        assertThat(userInfo.getName()).isEqualTo("조찬희");
        assertThat(userInfo.getEmail()).isEqualTo("test01@test.com");

        // verify
        verify(mockUserRepository, times(1)).findOneUser(userEntity.getIdx());
        verify(mockUserRepository, atLeastOnce()).findOneUser(userEntity.getIdx());
        verifyNoMoreInteractions(mockUserRepository);

        InOrder inOrder = inOrder(mockUserRepository);
        inOrder.verify(mockUserRepository).findOneUser(userEntity.getIdx());
    }

    @Test
    @DisplayName("유저 회원가입 BDD 테스트")
    void 유저회원가입BDD테스트() {
        // given
        UserEntity userEntity = UserEntity.builder()
                .idx(1L).userId("test01")
                .name("조찬희").password("test01")
                .email("test01@test.com").visible("Y").build();
        userRepository.insertUser(userEntity);
        userDTO = INSTANCE.toDto(userEntity);

        // when
        given(mockUserRepository.findOneUser(userEntity.getIdx())).willReturn(userDTO);
        UserDTO userInfo = mockUserRepository.findOneUser(userEntity.getIdx());

        // then
        assertThat(userInfo.getUserId()).isEqualTo("test01");
        assertThat(userInfo.getName()).isEqualTo("조찬희");
        assertThat(userInfo.getEmail()).isEqualTo("test01@test.com");

        // verify
        then(mockUserRepository).should(times(1)).findOneUser(userEntity.getIdx());
        then(mockUserRepository).should(atLeastOnce()).findOneUser(userEntity.getIdx());
        then(mockUserRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("유저 회원수정 Mockito 테스트")
    void 유저회원수정Mockito테스트() {
        // given
        UserEntity userEntity = UserEntity.builder()
                .userId("test")
                .password("test")
                .name("test")
                .email("test@test.com")
                .visible("Y")
                .build();

        Long idx = userRepository.insertUser(userEntity).getIdx();

        UserEntity newUserEntity = UserEntity.builder()
                .idx(idx)
                .userId("test1")
                .password("test1")
                .name("test1")
                .email("test1@test.com")
                .visible("Y")
                .build();

        userRepository.updateUser(newUserEntity);
        UserDTO newUserDTO = INSTANCE.toDto(newUserEntity);

        // when
        when(mockUserRepository.findOneUser(newUserEntity.getIdx())).thenReturn(newUserDTO);
        UserDTO userInfo = mockUserRepository.findOneUser(newUserEntity.getIdx());

        // then
        assertThat(userInfo.getUserId()).isEqualTo("test1");
        assertThat(userInfo.getName()).isEqualTo("test1");

        // verify
        verify(mockUserRepository, times(1)).findOneUser(newUserEntity.getIdx());
        verify(mockUserRepository, atLeastOnce()).findOneUser(newUserEntity.getIdx());
        verifyNoMoreInteractions(mockUserRepository);

        InOrder inOrder = inOrder(mockUserRepository);
        inOrder.verify(mockUserRepository).findOneUser(newUserEntity.getIdx());
    }

    @Test
    @DisplayName("유저 회원수정 BDD 테스트")
    void 유저회원수정BDD테스트() {
        // given
        UserEntity userEntity = UserEntity.builder()
                .userId("test")
                .password("test")
                .name("test")
                .email("test@test.com")
                .visible("Y")
                .build();

        Long idx = userRepository.insertUser(userEntity).getIdx();

        UserEntity newUserEntity = UserEntity.builder()
                .idx(idx)
                .userId("test1")
                .password("test1")
                .name("test1")
                .email("test1@test.com")
                .visible("Y")
                .build();

        userRepository.updateUser(newUserEntity);
        UserDTO newUserDTO = INSTANCE.toDto(newUserEntity);

        // when
        given(mockUserRepository.findOneUser(newUserEntity.getIdx())).willReturn(newUserDTO);
        UserDTO userInfo = mockUserRepository.findOneUser(newUserEntity.getIdx());

        // then
        assertThat(userInfo.getUserId()).isEqualTo("test1");
        assertThat(userInfo.getName()).isEqualTo("test1");

        // verify
        then(mockUserRepository).should(times(1)).findOneUser(newUserEntity.getIdx());
        then(mockUserRepository).should(atLeastOnce()).findOneUser(newUserEntity.getIdx());
        then(mockUserRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("유저 회원탈퇴 테스트")
    void 유저탈퇴테스트() {
        assertThat(userRepository.deleteUser(userEntity.getIdx())).isEqualTo(userDTO.getIdx());
    }

    @Test
    @DisplayName("유저 회원탈퇴 Mockito 테스트")
    void 유저회원탈퇴Mockito테스트() {
        UserDTO userDTO = userRepository.insertUser(userEntity);
        // when
        when(mockUserRepository.findOneUser(userDTO.getIdx())).thenReturn(userDTO);
        Long deleteIdx = userRepository.deleteUser(userDTO.getIdx());

        // then
        assertThat(mockUserRepository.findOneUser(userDTO.getIdx()).getIdx()).isEqualTo(deleteIdx);

        // verify
        verify(mockUserRepository, times(1)).findOneUser(userDTO.getIdx());
        verify(mockUserRepository, atLeastOnce()).findOneUser(userDTO.getIdx());
        verifyNoMoreInteractions(mockUserRepository);

        InOrder inOrder = inOrder(mockUserRepository);
        inOrder.verify(mockUserRepository).findOneUser(userDTO.getIdx());
    }

    @Test
    @DisplayName("유저 회원탈퇴 BDD 테스트")
    void 유저회원탈퇴BDD테스트() {
        UserDTO userDTO = userRepository.insertUser(userEntity);
        // when
        given(mockUserRepository.findOneUser(userDTO.getIdx())).willReturn(userDTO);
        Long deleteIdx = userRepository.deleteUser(userDTO.getIdx());

        // then
        assertThat(mockUserRepository.findOneUser(userDTO.getIdx()).getIdx()).isEqualTo(deleteIdx);

        // verify
        then(mockUserRepository).should(times(1)).findOneUser(userDTO.getIdx());
        then(mockUserRepository).should(atLeastOnce()).findOneUser(userDTO.getIdx());
        then(mockUserRepository).shouldHaveNoMoreInteractions();
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

        UserDTO oneUser = userRepository.insertUser(userEntity);

        JSONArray jsonArray = new JSONArray();
        jsonArray.put(oneUser.getFavoriteTravelIdx());
        Object insertObject = jsonArray.get(0);

        assertThat(oneUser.getFavoriteTravelIdx()).isEqualTo(insertObject);

        UserDTO updateUser = userRepository.addFavoriteTravel(oneUser.getIdx(), 2L);

        jsonArray.remove(0);
        jsonArray.put(updateUser.getFavoriteTravelIdx());
        Object updateObject = jsonArray.get(0);

        assertThat(updateUser.getFavoriteTravelIdx()).isEqualTo(updateObject);
    }
}
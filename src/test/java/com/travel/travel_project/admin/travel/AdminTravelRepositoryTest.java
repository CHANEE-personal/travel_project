package com.travel.travel_project.admin.travel;

import com.travel.travel_project.admin.travel.domain.AdminTravelDTO;
import com.travel.travel_project.admin.travel.domain.AdminTravelEntity;
import com.travel.travel_project.admin.travel.mapper.TravelMapper;
import lombok.RequiredArgsConstructor;
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
@DisplayName("여행지 소개 Repository Test")
class AdminTravelRepositoryTest {
    @Mock
    private AdminTravelRepository mockAdminTravelRepository;
    private final AdminTravelRepository adminTravelRepository;
    private final EntityManager em;

    private AdminTravelEntity adminTravelEntity;
    private AdminTravelDTO adminTravelDTO;

    void createTravel() {
        adminTravelEntity = AdminTravelEntity.builder()
                .travelCode(1)
                .travelTitle("여행지 소개")
                .travelDescription("여행지 소개")
                .travelAddr("인천광역시 서구")
                .travelZipCode("123-456")
                .visible("Y")
                .build();

        adminTravelDTO = TravelMapper.INSTANCE.toDto(adminTravelEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createTravel();
    }

    @Test
    @Disabled
    @DisplayName("여행지소개리스트조회테스트")
    void 여행지소개리스트조회테스트() {
        // given
        Map<String, Object> travelMap = new HashMap<>();
        travelMap.put("jpaStartPage", 1);
        travelMap.put("size", 3);

        // then
        assertThat(adminTravelRepository.findTravelsList(travelMap)).isNotEmpty();
    }

    @Test
    @DisplayName("여행지소개리스트Mockito검색조회테스트")
    void 여행지소개리스트Mockito검색조회테스트() {
        // given
        Map<String, Object> travelMap = new HashMap<>();
        travelMap.put("jpaStartPage", 1);
        travelMap.put("size", 3);
        travelMap.put("searchCode", 1);

        List<AdminTravelDTO> travelList = new ArrayList<>();
        travelList.add(AdminTravelDTO.builder().idx(1L).travelCode(1)
                .travelTitle("여행지 소개").travelDescription("여행지 소개")
                .travelAddr("인천광역시 서구").travelZipCode("123-456").visible("Y").build());

        // when
        when(mockAdminTravelRepository.findTravelsList(travelMap)).thenReturn(travelList);
        List<AdminTravelDTO> newTravelList = mockAdminTravelRepository.findTravelsList(travelMap);

        // then
        assertThat(newTravelList.get(0).getIdx()).isEqualTo(travelList.get(0).getIdx());
        assertThat(newTravelList.get(0).getTravelCode()).isEqualTo(travelList.get(0).getTravelCode());
        assertThat(newTravelList.get(0).getTravelTitle()).isEqualTo(travelList.get(0).getTravelTitle());
        assertThat(newTravelList.get(0).getTravelDescription()).isEqualTo(travelList.get(0).getTravelDescription());
        assertThat(newTravelList.get(0).getTravelAddr()).isEqualTo(travelList.get(0).getTravelAddr());
        assertThat(newTravelList.get(0).getTravelZipCode()).isEqualTo(travelList.get(0).getTravelZipCode());

        // verify
        verify(mockAdminTravelRepository, times(1)).findTravelsList(travelMap);
        verify(mockAdminTravelRepository, atLeastOnce()).findTravelsList(travelMap);
        verifyNoMoreInteractions(mockAdminTravelRepository);

        InOrder inOrder = inOrder(mockAdminTravelRepository);
        inOrder.verify(mockAdminTravelRepository).findTravelsList(travelMap);
    }

    @Test
    @DisplayName("여행지소개리스트Mockito조회테스트")
    void 여행지소개리스트Mockito조회테스트() {
        // given
        Map<String, Object> travelMap = new HashMap<>();
        travelMap.put("jpaStartPage", 1);
        travelMap.put("size", 3);

        List<AdminTravelDTO> travelList = new ArrayList<>();
        travelList.add(AdminTravelDTO.builder().idx(1L).travelCode(1)
                        .travelTitle("여행지 소개").travelDescription("여행지 소개")
                        .travelAddr("인천광역시 서구").travelZipCode("123-456").visible("Y").build());

        // when
        when(mockAdminTravelRepository.findTravelsList(travelMap)).thenReturn(travelList);
        List<AdminTravelDTO> newTravelList = mockAdminTravelRepository.findTravelsList(travelMap);

        // then
        assertThat(newTravelList.get(0).getIdx()).isEqualTo(travelList.get(0).getIdx());
        assertThat(newTravelList.get(0).getTravelCode()).isEqualTo(travelList.get(0).getTravelCode());
        assertThat(newTravelList.get(0).getTravelTitle()).isEqualTo(travelList.get(0).getTravelTitle());
        assertThat(newTravelList.get(0).getTravelDescription()).isEqualTo(travelList.get(0).getTravelDescription());
        assertThat(newTravelList.get(0).getTravelAddr()).isEqualTo(travelList.get(0).getTravelAddr());
        assertThat(newTravelList.get(0).getTravelZipCode()).isEqualTo(travelList.get(0).getTravelZipCode());

        // verify
        verify(mockAdminTravelRepository, times(1)).findTravelsList(travelMap);
        verify(mockAdminTravelRepository, atLeastOnce()).findTravelsList(travelMap);
        verifyNoMoreInteractions(mockAdminTravelRepository);

        InOrder inOrder = inOrder(mockAdminTravelRepository);
        inOrder.verify(mockAdminTravelRepository).findTravelsList(travelMap);
    }

    @Test
    @DisplayName("여행지소개리스트BDD조회테스트")
    void 여행지소개리스트BDD조회테스트() {
        // given
        Map<String, Object> travelMap = new HashMap<>();
        travelMap.put("jpaStartPage", 1);
        travelMap.put("size", 3);

        List<AdminTravelDTO> travelList = new ArrayList<>();
        travelList.add(AdminTravelDTO.builder().idx(1L).travelCode(1)
                .travelTitle("여행지 소개").travelDescription("여행지 소개")
                .travelAddr("인천광역시 서구").travelZipCode("123-456").visible("Y").build());

        // when
        given(mockAdminTravelRepository.findTravelsList(travelMap)).willReturn(travelList);
        List<AdminTravelDTO> newTravelList = mockAdminTravelRepository.findTravelsList(travelMap);

        // then
        assertThat(newTravelList.get(0).getIdx()).isEqualTo(travelList.get(0).getIdx());
        assertThat(newTravelList.get(0).getTravelCode()).isEqualTo(travelList.get(0).getTravelCode());
        assertThat(newTravelList.get(0).getTravelTitle()).isEqualTo(travelList.get(0).getTravelTitle());
        assertThat(newTravelList.get(0).getTravelDescription()).isEqualTo(travelList.get(0).getTravelDescription());
        assertThat(newTravelList.get(0).getTravelAddr()).isEqualTo(travelList.get(0).getTravelAddr());
        assertThat(newTravelList.get(0).getTravelZipCode()).isEqualTo(travelList.get(0).getTravelZipCode());

        // verify
        then(mockAdminTravelRepository).should(times(1)).findTravelsList(travelMap);
        then(mockAdminTravelRepository).should(atLeastOnce()).findTravelsList(travelMap);
        then(mockAdminTravelRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("여행지소개상세Mockito테스트")
    void 여행지소개상세Mockito테스트() {
        // given
        adminTravelDTO = AdminTravelDTO.builder()
                .idx(1L)
                .travelCode(1)
                .travelTitle("여행지 테스트").travelDescription("여행지 테스트")
                .travelAddr("인천광역시 서구").travelZipCode("123-456").visible("Y")
                .build();

        // when
        given(mockAdminTravelRepository.findOneTravel(1L)).willReturn(adminTravelDTO);
        AdminTravelDTO newAdminTravel = mockAdminTravelRepository.findOneTravel(1L);

        // then
        assertThat(newAdminTravel.getIdx()).isEqualTo(adminTravelDTO.getIdx());
        assertThat(newAdminTravel.getTravelCode()).isEqualTo(adminTravelDTO.getTravelCode());
        assertThat(newAdminTravel.getTravelTitle()).isEqualTo(adminTravelDTO.getTravelTitle());
        assertThat(newAdminTravel.getTravelDescription()).isEqualTo(adminTravelDTO.getTravelDescription());
        assertThat(newAdminTravel.getTravelAddr()).isEqualTo(adminTravelDTO.getTravelAddr());
        assertThat(newAdminTravel.getTravelZipCode()).isEqualTo(adminTravelDTO.getTravelZipCode());

        // verify
        verify(mockAdminTravelRepository, times(1)).findOneTravel(1L);
        verify(mockAdminTravelRepository, atLeastOnce()).findOneTravel(1L);
        verifyNoMoreInteractions(mockAdminTravelRepository);

        InOrder inOrder = inOrder(mockAdminTravelRepository);
        inOrder.verify(mockAdminTravelRepository).findOneTravel(1L);
    }

    @Test
    @DisplayName("여행지소개상세BDD테스트")
    void 여행지소개상세BDD테스트() {
        // given
        adminTravelDTO = AdminTravelDTO.builder()
                .idx(1L)
                .travelCode(1)
                .travelTitle("여행지 테스트").travelDescription("여행지 테스트")
                .travelAddr("인천광역시 서구").travelZipCode("123-456").visible("Y")
                .build();

        // when
        when(mockAdminTravelRepository.findOneTravel(1L)).thenReturn(adminTravelDTO);
        AdminTravelDTO newAdminTravel = mockAdminTravelRepository.findOneTravel(1L);

        // then
        assertThat(newAdminTravel.getIdx()).isEqualTo(adminTravelDTO.getIdx());
        assertThat(newAdminTravel.getTravelCode()).isEqualTo(adminTravelDTO.getTravelCode());
        assertThat(newAdminTravel.getTravelTitle()).isEqualTo(adminTravelDTO.getTravelTitle());
        assertThat(newAdminTravel.getTravelDescription()).isEqualTo(adminTravelDTO.getTravelDescription());
        assertThat(newAdminTravel.getTravelAddr()).isEqualTo(adminTravelDTO.getTravelAddr());
        assertThat(newAdminTravel.getTravelZipCode()).isEqualTo(adminTravelDTO.getTravelZipCode());

        // verify
        then(mockAdminTravelRepository).should(times(1)).findOneTravel(1L);
        then(mockAdminTravelRepository).should(atLeastOnce()).findOneTravel(1L);
        then(mockAdminTravelRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("여행지등록Mockito테스트")
    void 여행지등록Mockito테스트() {
        // given
        adminTravelEntity = AdminTravelEntity.builder()
                .travelCode(1)
                .travelTitle("여행지 테스트").travelDescription("여행지 테스트")
                .travelAddr("인천광역시 서구").travelZipCode("123-456").visible("Y")
                .build();

        adminTravelDTO = TravelMapper.INSTANCE.toDto(adminTravelEntity);

        // when
        when(mockAdminTravelRepository.insertTravel(adminTravelEntity)).thenReturn(adminTravelDTO);
        AdminTravelDTO newAdminTravel = mockAdminTravelRepository.insertTravel(adminTravelEntity);

        // then
        assertThat(newAdminTravel.getTravelCode()).isEqualTo(adminTravelEntity.getTravelCode());
        assertThat(newAdminTravel.getTravelTitle()).isEqualTo(adminTravelEntity.getTravelTitle());
        assertThat(newAdminTravel.getTravelDescription()).isEqualTo(adminTravelEntity.getTravelDescription());
        assertThat(newAdminTravel.getTravelAddr()).isEqualTo(adminTravelEntity.getTravelAddr());
        assertThat(newAdminTravel.getTravelZipCode()).isEqualTo(adminTravelEntity.getTravelZipCode());

        // verify
        verify(mockAdminTravelRepository, times(1)).findOneTravel(newAdminTravel.getIdx());
        verify(mockAdminTravelRepository, atLeastOnce()).findOneTravel(newAdminTravel.getIdx());
        verifyNoMoreInteractions(mockAdminTravelRepository);

        InOrder inOrder = inOrder(mockAdminTravelRepository);
        inOrder.verify(mockAdminTravelRepository).findOneTravel(newAdminTravel.getIdx());
    }

    @Test
    @DisplayName("여행지등록BDD테스트")
    void 여행지등록BDD테스트() {
        // given
        adminTravelEntity = AdminTravelEntity.builder()
                .travelCode(1)
                .travelTitle("여행지 테스트").travelDescription("여행지 테스트")
                .travelAddr("인천광역시 서구").travelZipCode("123-456").visible("Y")
                .build();

        adminTravelDTO = TravelMapper.INSTANCE.toDto(adminTravelEntity);

        // when
        given(mockAdminTravelRepository.insertTravel(adminTravelEntity)).willReturn(adminTravelDTO);
        AdminTravelDTO newAdminTravel = mockAdminTravelRepository.insertTravel(adminTravelEntity);

        // then
        assertThat(newAdminTravel.getTravelCode()).isEqualTo(adminTravelEntity.getTravelCode());
        assertThat(newAdminTravel.getTravelTitle()).isEqualTo(adminTravelEntity.getTravelTitle());
        assertThat(newAdminTravel.getTravelDescription()).isEqualTo(adminTravelEntity.getTravelDescription());
        assertThat(newAdminTravel.getTravelAddr()).isEqualTo(adminTravelEntity.getTravelAddr());
        assertThat(newAdminTravel.getTravelZipCode()).isEqualTo(adminTravelEntity.getTravelZipCode());

        // verify
        then(mockAdminTravelRepository).should(times(1)).findOneTravel(newAdminTravel.getIdx());
        then(mockAdminTravelRepository).should(atLeastOnce()).findOneTravel(newAdminTravel.getIdx());
        then(mockAdminTravelRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("여행지수정Mockito테스트")
    void 여행지수정Mockito테스트() {
        // given
        adminTravelEntity = AdminTravelEntity.builder()
                .travelCode(1)
                .travelTitle("여행지 테스트").travelDescription("여행지 테스트")
                .travelAddr("인천광역시 서구").travelZipCode("123-456").visible("Y")
                .build();

        AdminTravelDTO adminTravelDTO = adminTravelRepository.insertTravel(adminTravelEntity);

        AdminTravelEntity newAdminTravelEntity = AdminTravelEntity.builder()
                .idx(adminTravelDTO.getIdx())
                .travelCode(1)
                .travelTitle("여행지 수정 테스트").travelDescription("여행지 수정 테스트")
                .travelAddr("서울특별시 강남구").travelZipCode("123-456")
                .visible("Y").build();

        adminTravelRepository.updateTravel(newAdminTravelEntity);

        AdminTravelDTO newAdminTravelDTO = TravelMapper.INSTANCE.toDto(newAdminTravelEntity);

        // when
        when(mockAdminTravelRepository.findOneTravel(newAdminTravelEntity.getIdx())).thenReturn(newAdminTravelDTO);
        AdminTravelDTO travelInfo = mockAdminTravelRepository.findOneTravel(newAdminTravelDTO.getIdx());

        // then
        assertThat(travelInfo.getIdx()).isEqualTo(newAdminTravelDTO.getIdx());
        assertThat(travelInfo.getTravelCode()).isEqualTo(newAdminTravelDTO.getTravelCode());
        assertThat(travelInfo.getTravelTitle()).isEqualTo(newAdminTravelDTO.getTravelTitle());

        // verify
        verify(mockAdminTravelRepository, times(1)).findOneTravel(travelInfo.getIdx());
        verify(mockAdminTravelRepository, atLeastOnce()).findOneTravel(travelInfo.getIdx());
        verifyNoMoreInteractions(mockAdminTravelRepository);

        InOrder inOrder = inOrder(mockAdminTravelRepository);
        inOrder.verify(mockAdminTravelRepository).findOneTravel(travelInfo.getIdx());
    }

    @Test
    @DisplayName("여행지수정BDD테스트")
    void 여행지수정BDD테스트() {
        // given
        adminTravelEntity = AdminTravelEntity.builder()
                .travelCode(1)
                .travelTitle("여행지 테스트").travelDescription("여행지 테스트")
                .travelAddr("인천광역시 서구").travelZipCode("123-456").visible("Y")
                .build();

        AdminTravelDTO adminTravelDTO = adminTravelRepository.insertTravel(adminTravelEntity);

        AdminTravelEntity newAdminTravelEntity = AdminTravelEntity.builder()
                .idx(adminTravelDTO.getIdx())
                .travelCode(1)
                .travelTitle("여행지 수정 테스트").travelDescription("여행지 수정 테스트")
                .travelAddr("서울특별시 강남구").travelZipCode("123-456")
                .visible("Y").build();

        adminTravelRepository.updateTravel(newAdminTravelEntity);

        AdminTravelDTO newAdminTravelDTO = TravelMapper.INSTANCE.toDto(newAdminTravelEntity);

        // when
        given(mockAdminTravelRepository.findOneTravel(newAdminTravelEntity.getIdx())).willReturn(newAdminTravelDTO);
        AdminTravelDTO travelInfo = mockAdminTravelRepository.findOneTravel(newAdminTravelDTO.getIdx());

        // then
        assertThat(travelInfo.getIdx()).isEqualTo(newAdminTravelDTO.getIdx());
        assertThat(travelInfo.getTravelCode()).isEqualTo(newAdminTravelDTO.getTravelCode());
        assertThat(travelInfo.getTravelTitle()).isEqualTo(newAdminTravelDTO.getTravelTitle());

        // verify
        then(mockAdminTravelRepository).should(times(1)).findOneTravel(travelInfo.getIdx());
        then(mockAdminTravelRepository).should(atLeastOnce()).findOneTravel(travelInfo.getIdx());
        then(mockAdminTravelRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("여행지삭제Mockito테스트")
    void 여행지삭제Mockito테스트() {
        // given
        em.persist(adminTravelEntity);
        adminTravelDTO = TravelMapper.INSTANCE.toDto(adminTravelEntity);

        // when
        when(mockAdminTravelRepository.findOneTravel(adminTravelDTO.getIdx())).thenReturn(adminTravelDTO);
        Long deleteIdx = adminTravelRepository.deleteTravel(adminTravelDTO.getIdx());

        // then
        assertThat(mockAdminTravelRepository.findOneTravel(adminTravelDTO.getIdx()).getIdx()).isEqualTo(deleteIdx);

        // verify
        verify(mockAdminTravelRepository, times(1)).findOneTravel(adminTravelDTO.getIdx());
        verify(mockAdminTravelRepository, atLeastOnce()).findOneTravel(adminTravelDTO.getIdx());
        verifyNoMoreInteractions(mockAdminTravelRepository);

        InOrder inOrder = inOrder(mockAdminTravelRepository);
        inOrder.verify(mockAdminTravelRepository).findOneTravel(adminTravelDTO.getIdx());
    }

    @Test
    @DisplayName("여행지삭제BDD테스트")
    void 여행지삭제BDD테스트() {
        // given
        em.persist(adminTravelEntity);
        adminTravelDTO = TravelMapper.INSTANCE.toDto(adminTravelEntity);

        // when
        given(mockAdminTravelRepository.findOneTravel(adminTravelDTO.getIdx())).willReturn(adminTravelDTO);
        Long deleteIdx = adminTravelRepository.deleteTravel(adminTravelDTO.getIdx());

        // then
        assertThat(mockAdminTravelRepository.findOneTravel(adminTravelDTO.getIdx()).getIdx()).isEqualTo(deleteIdx);

        // verify
        then(mockAdminTravelRepository).should(times(1)).findOneTravel(adminTravelDTO.getIdx());
        then(mockAdminTravelRepository).should(atLeastOnce()).findOneTravel(adminTravelDTO.getIdx());
        then(mockAdminTravelRepository).shouldHaveNoMoreInteractions();
    }
}
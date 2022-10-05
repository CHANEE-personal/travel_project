package com.travel.travel_project.admin.travel;

import com.travel.travel_project.admin.travel.domain.AdminTravelDTO;
import com.travel.travel_project.admin.travel.domain.AdminTravelEntity;
import com.travel.travel_project.admin.travel.mapper.TravelMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@DisplayName("여행지 Service Test")
class AdminTravelServiceTest {
    @Mock
    private AdminTravelService mockAdminTravelService;
    private final AdminTravelService adminTravelService;
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
        assertThat(adminTravelService.findTravelsList(travelMap)).isNotEmpty();
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
        when(mockAdminTravelService.findTravelsList(travelMap)).thenReturn(travelList);
        List<AdminTravelDTO> newTravelList = mockAdminTravelService.findTravelsList(travelMap);

        // then
        assertThat(newTravelList.get(0).getIdx()).isEqualTo(travelList.get(0).getIdx());
        assertThat(newTravelList.get(0).getTravelCode()).isEqualTo(travelList.get(0).getTravelCode());
        assertThat(newTravelList.get(0).getTravelTitle()).isEqualTo(travelList.get(0).getTravelTitle());
        assertThat(newTravelList.get(0).getTravelDescription()).isEqualTo(travelList.get(0).getTravelDescription());
        assertThat(newTravelList.get(0).getTravelAddr()).isEqualTo(travelList.get(0).getTravelAddr());
        assertThat(newTravelList.get(0).getTravelZipCode()).isEqualTo(travelList.get(0).getTravelZipCode());

        // verify
        verify(mockAdminTravelService, times(1)).findTravelsList(travelMap);
        verify(mockAdminTravelService, atLeastOnce()).findTravelsList(travelMap);
        verifyNoMoreInteractions(mockAdminTravelService);

        InOrder inOrder = inOrder(mockAdminTravelService);
        inOrder.verify(mockAdminTravelService).findTravelsList(travelMap);
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
        given(mockAdminTravelService.findTravelsList(travelMap)).willReturn(travelList);
        List<AdminTravelDTO> newTravelList = mockAdminTravelService.findTravelsList(travelMap);

        // then
        assertThat(newTravelList.get(0).getIdx()).isEqualTo(travelList.get(0).getIdx());
        assertThat(newTravelList.get(0).getTravelCode()).isEqualTo(travelList.get(0).getTravelCode());
        assertThat(newTravelList.get(0).getTravelTitle()).isEqualTo(travelList.get(0).getTravelTitle());
        assertThat(newTravelList.get(0).getTravelDescription()).isEqualTo(travelList.get(0).getTravelDescription());
        assertThat(newTravelList.get(0).getTravelAddr()).isEqualTo(travelList.get(0).getTravelAddr());
        assertThat(newTravelList.get(0).getTravelZipCode()).isEqualTo(travelList.get(0).getTravelZipCode());

        // verify
        then(mockAdminTravelService).should(times(1)).findTravelsList(travelMap);
        then(mockAdminTravelService).should(atLeastOnce()).findTravelsList(travelMap);
        then(mockAdminTravelService).shouldHaveNoMoreInteractions();
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
        given(mockAdminTravelService.findOneTravel(1L)).willReturn(adminTravelDTO);
        AdminTravelDTO newAdminTravel = mockAdminTravelService.findOneTravel(1L);

        // then
        assertThat(newAdminTravel.getIdx()).isEqualTo(adminTravelDTO.getIdx());
        assertThat(newAdminTravel.getTravelCode()).isEqualTo(adminTravelDTO.getTravelCode());
        assertThat(newAdminTravel.getTravelTitle()).isEqualTo(adminTravelDTO.getTravelTitle());
        assertThat(newAdminTravel.getTravelDescription()).isEqualTo(adminTravelDTO.getTravelDescription());
        assertThat(newAdminTravel.getTravelAddr()).isEqualTo(adminTravelDTO.getTravelAddr());
        assertThat(newAdminTravel.getTravelZipCode()).isEqualTo(adminTravelDTO.getTravelZipCode());

        // verify
        verify(mockAdminTravelService, times(1)).findOneTravel(1L);
        verify(mockAdminTravelService, atLeastOnce()).findOneTravel(1L);
        verifyNoMoreInteractions(mockAdminTravelService);

        InOrder inOrder = inOrder(mockAdminTravelService);
        inOrder.verify(mockAdminTravelService).findOneTravel(1L);
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
        when(mockAdminTravelService.findOneTravel(1L)).thenReturn(adminTravelDTO);
        AdminTravelDTO newAdminTravel = mockAdminTravelService.findOneTravel(1L);

        // then
        assertThat(newAdminTravel.getIdx()).isEqualTo(adminTravelDTO.getIdx());
        assertThat(newAdminTravel.getTravelCode()).isEqualTo(adminTravelDTO.getTravelCode());
        assertThat(newAdminTravel.getTravelTitle()).isEqualTo(adminTravelDTO.getTravelTitle());
        assertThat(newAdminTravel.getTravelDescription()).isEqualTo(adminTravelDTO.getTravelDescription());
        assertThat(newAdminTravel.getTravelAddr()).isEqualTo(adminTravelDTO.getTravelAddr());
        assertThat(newAdminTravel.getTravelZipCode()).isEqualTo(adminTravelDTO.getTravelZipCode());

        // verify
        then(mockAdminTravelService).should(times(1)).findOneTravel(1L);
        then(mockAdminTravelService).should(atLeastOnce()).findOneTravel(1L);
        then(mockAdminTravelService).shouldHaveNoMoreInteractions();
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
        when(mockAdminTravelService.insertTravel(adminTravelEntity)).thenReturn(adminTravelDTO);
        AdminTravelDTO newAdminTravel = mockAdminTravelService.insertTravel(adminTravelEntity);

        // then
        assertThat(newAdminTravel.getTravelCode()).isEqualTo(adminTravelEntity.getTravelCode());
        assertThat(newAdminTravel.getTravelTitle()).isEqualTo(adminTravelEntity.getTravelTitle());
        assertThat(newAdminTravel.getTravelDescription()).isEqualTo(adminTravelEntity.getTravelDescription());
        assertThat(newAdminTravel.getTravelAddr()).isEqualTo(adminTravelEntity.getTravelAddr());
        assertThat(newAdminTravel.getTravelZipCode()).isEqualTo(adminTravelEntity.getTravelZipCode());

        // verify
        verify(mockAdminTravelService, times(1)).findOneTravel(newAdminTravel.getIdx());
        verify(mockAdminTravelService, atLeastOnce()).findOneTravel(newAdminTravel.getIdx());
        verifyNoMoreInteractions(mockAdminTravelService);

        InOrder inOrder = inOrder(mockAdminTravelService);
        inOrder.verify(mockAdminTravelService).findOneTravel(newAdminTravel.getIdx());
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
        given(mockAdminTravelService.insertTravel(adminTravelEntity)).willReturn(adminTravelDTO);
        AdminTravelDTO newAdminTravel = mockAdminTravelService.insertTravel(adminTravelEntity);

        // then
        assertThat(newAdminTravel.getTravelCode()).isEqualTo(adminTravelEntity.getTravelCode());
        assertThat(newAdminTravel.getTravelTitle()).isEqualTo(adminTravelEntity.getTravelTitle());
        assertThat(newAdminTravel.getTravelDescription()).isEqualTo(adminTravelEntity.getTravelDescription());
        assertThat(newAdminTravel.getTravelAddr()).isEqualTo(adminTravelEntity.getTravelAddr());
        assertThat(newAdminTravel.getTravelZipCode()).isEqualTo(adminTravelEntity.getTravelZipCode());

        // verify
        then(mockAdminTravelService).should(times(1)).findOneTravel(newAdminTravel.getIdx());
        then(mockAdminTravelService).should(atLeastOnce()).findOneTravel(newAdminTravel.getIdx());
        then(mockAdminTravelService).shouldHaveNoMoreInteractions();
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

        AdminTravelDTO adminTravelDTO = adminTravelService.insertTravel(adminTravelEntity);

        AdminTravelEntity newAdminTravelEntity = AdminTravelEntity.builder()
                .idx(adminTravelDTO.getIdx())
                .travelCode(1)
                .travelTitle("여행지 수정 테스트").travelDescription("여행지 수정 테스트")
                .travelAddr("서울특별시 강남구").travelZipCode("123-456")
                .visible("Y").build();

        adminTravelService.updateTravel(newAdminTravelEntity);

        AdminTravelDTO newAdminTravelDTO = TravelMapper.INSTANCE.toDto(newAdminTravelEntity);

        // when
        when(mockAdminTravelService.findOneTravel(newAdminTravelEntity.getIdx())).thenReturn(newAdminTravelDTO);
        AdminTravelDTO travelInfo = mockAdminTravelService.findOneTravel(newAdminTravelDTO.getIdx());

        // then
        assertThat(travelInfo.getIdx()).isEqualTo(newAdminTravelDTO.getIdx());
        assertThat(travelInfo.getTravelCode()).isEqualTo(newAdminTravelDTO.getTravelCode());
        assertThat(travelInfo.getTravelTitle()).isEqualTo(newAdminTravelDTO.getTravelTitle());

        // verify
        verify(mockAdminTravelService, times(1)).findOneTravel(travelInfo.getIdx());
        verify(mockAdminTravelService, atLeastOnce()).findOneTravel(travelInfo.getIdx());
        verifyNoMoreInteractions(mockAdminTravelService);

        InOrder inOrder = inOrder(mockAdminTravelService);
        inOrder.verify(mockAdminTravelService).findOneTravel(travelInfo.getIdx());
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

        AdminTravelDTO adminTravelDTO = adminTravelService.insertTravel(adminTravelEntity);

        AdminTravelEntity newAdminTravelEntity = AdminTravelEntity.builder()
                .idx(adminTravelDTO.getIdx())
                .travelCode(1)
                .travelTitle("여행지 수정 테스트").travelDescription("여행지 수정 테스트")
                .travelAddr("서울특별시 강남구").travelZipCode("123-456")
                .visible("Y").build();

        adminTravelService.updateTravel(newAdminTravelEntity);

        AdminTravelDTO newAdminTravelDTO = TravelMapper.INSTANCE.toDto(newAdminTravelEntity);

        // when
        given(mockAdminTravelService.findOneTravel(newAdminTravelEntity.getIdx())).willReturn(newAdminTravelDTO);
        AdminTravelDTO travelInfo = mockAdminTravelService.findOneTravel(newAdminTravelDTO.getIdx());

        // then
        assertThat(travelInfo.getIdx()).isEqualTo(newAdminTravelDTO.getIdx());
        assertThat(travelInfo.getTravelCode()).isEqualTo(newAdminTravelDTO.getTravelCode());
        assertThat(travelInfo.getTravelTitle()).isEqualTo(newAdminTravelDTO.getTravelTitle());

        // verify
        then(mockAdminTravelService).should(times(1)).findOneTravel(travelInfo.getIdx());
        then(mockAdminTravelService).should(atLeastOnce()).findOneTravel(travelInfo.getIdx());
        then(mockAdminTravelService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("여행지삭제Mockito테스트")
    void 여행지삭제Mockito테스트() {
        // given
        em.persist(adminTravelEntity);
        adminTravelDTO = TravelMapper.INSTANCE.toDto(adminTravelEntity);

        // when
        when(mockAdminTravelService.findOneTravel(adminTravelDTO.getIdx())).thenReturn(adminTravelDTO);
        Long deleteIdx = adminTravelService.deleteTravel(adminTravelDTO.getIdx());

        // then
        assertThat(mockAdminTravelService.findOneTravel(adminTravelDTO.getIdx()).getIdx()).isEqualTo(deleteIdx);

        // verify
        verify(mockAdminTravelService, times(1)).findOneTravel(adminTravelDTO.getIdx());
        verify(mockAdminTravelService, atLeastOnce()).findOneTravel(adminTravelDTO.getIdx());
        verifyNoMoreInteractions(mockAdminTravelService);

        InOrder inOrder = inOrder(mockAdminTravelService);
        inOrder.verify(mockAdminTravelService).findOneTravel(adminTravelDTO.getIdx());
    }

    @Test
    @DisplayName("여행지삭제BDD테스트")
    void 여행지삭제BDD테스트() {
        // given
        em.persist(adminTravelEntity);
        adminTravelDTO = TravelMapper.INSTANCE.toDto(adminTravelEntity);

        // when
        given(mockAdminTravelService.findOneTravel(adminTravelDTO.getIdx())).willReturn(adminTravelDTO);
        Long deleteIdx = adminTravelService.deleteTravel(adminTravelDTO.getIdx());

        // then
        assertThat(mockAdminTravelService.findOneTravel(adminTravelDTO.getIdx()).getIdx()).isEqualTo(deleteIdx);

        // verify
        then(mockAdminTravelService).should(times(1)).findOneTravel(adminTravelDTO.getIdx());
        then(mockAdminTravelService).should(atLeastOnce()).findOneTravel(adminTravelDTO.getIdx());
        then(mockAdminTravelService).shouldHaveNoMoreInteractions();
    }
}
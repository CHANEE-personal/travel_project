package com.travel.travel_project.admin.travel;

import com.travel.travel_project.domain.travel.TravelDTO;
import com.travel.travel_project.domain.travel.TravelEntity;
import com.travel.travel_project.api.travel.TravelRepository;
import com.travel.travel_project.api.travel.mapper.TravelMapper;
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
import static org.assertj.core.api.InstanceOfAssertFactories.INSTANT;
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
class TravelRepositoryTest {
    @Mock
    private TravelRepository mockTravelRepository;
    private final TravelRepository travelRepository;
    private final EntityManager em;

    private TravelEntity travelEntity;
    private TravelDTO travelDTO;

    void createTravel() {
        travelEntity = TravelEntity.builder()
                .travelCode(1)
                .travelTitle("여행지 테스트").travelDescription("여행지 테스트").favoriteCount(1).viewCount(0)
                .travelAddress("인천광역시 서구").travelZipCode("123-456").visible("Y").popular(false)
                .build();

        travelDTO = TravelMapper.INSTANCE.toDto(travelEntity);
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
        assertThat(travelRepository.findTravelsList(travelMap)).isNotEmpty();
    }

    @Test
    @DisplayName("여행지 소개 리스트 Mockito 검색 조회 테스트")
    void 여행지소개리스트Mockito검색조회테스트() {
        // given
        Map<String, Object> travelMap = new HashMap<>();
        travelMap.put("jpaStartPage", 1);
        travelMap.put("size", 3);
        travelMap.put("searchCode", 1);

        List<TravelDTO> travelList = new ArrayList<>();
        travelList.add(TravelDTO.builder().idx(1L).travelCode(1)
                .travelTitle("여행지 소개").travelDescription("여행지 소개")
                .travelAddr("인천광역시 서구").travelZipCode("123-456").visible("Y").build());

        // when
        when(mockTravelRepository.findTravelsList(travelMap)).thenReturn(travelList);
        List<TravelDTO> newTravelList = mockTravelRepository.findTravelsList(travelMap);

        // then
        assertThat(newTravelList.get(0).getIdx()).isEqualTo(travelList.get(0).getIdx());
        assertThat(newTravelList.get(0).getTravelCode()).isEqualTo(travelList.get(0).getTravelCode());
        assertThat(newTravelList.get(0).getTravelTitle()).isEqualTo(travelList.get(0).getTravelTitle());
        assertThat(newTravelList.get(0).getTravelDescription()).isEqualTo(travelList.get(0).getTravelDescription());
        assertThat(newTravelList.get(0).getTravelAddr()).isEqualTo(travelList.get(0).getTravelAddr());
        assertThat(newTravelList.get(0).getTravelZipCode()).isEqualTo(travelList.get(0).getTravelZipCode());

        // verify
        verify(mockTravelRepository, times(1)).findTravelsList(travelMap);
        verify(mockTravelRepository, atLeastOnce()).findTravelsList(travelMap);
        verifyNoMoreInteractions(mockTravelRepository);

        InOrder inOrder = inOrder(mockTravelRepository);
        inOrder.verify(mockTravelRepository).findTravelsList(travelMap);
    }

    @Test
    @DisplayName("여행지 소개 리스트 Mockito 조회 테스트")
    void 여행지소개리스트Mockito조회테스트() {
        // given
        Map<String, Object> travelMap = new HashMap<>();
        travelMap.put("jpaStartPage", 1);
        travelMap.put("size", 3);

        List<TravelDTO> travelList = new ArrayList<>();
        travelList.add(TravelDTO.builder().idx(1L).travelCode(1)
                        .travelTitle("여행지 소개").travelDescription("여행지 소개")
                        .travelAddr("인천광역시 서구").travelZipCode("123-456").visible("Y").build());

        // when
        when(mockTravelRepository.findTravelsList(travelMap)).thenReturn(travelList);
        List<TravelDTO> newTravelList = mockTravelRepository.findTravelsList(travelMap);

        // then
        assertThat(newTravelList.get(0).getIdx()).isEqualTo(travelList.get(0).getIdx());
        assertThat(newTravelList.get(0).getTravelCode()).isEqualTo(travelList.get(0).getTravelCode());
        assertThat(newTravelList.get(0).getTravelTitle()).isEqualTo(travelList.get(0).getTravelTitle());
        assertThat(newTravelList.get(0).getTravelDescription()).isEqualTo(travelList.get(0).getTravelDescription());
        assertThat(newTravelList.get(0).getTravelAddr()).isEqualTo(travelList.get(0).getTravelAddr());
        assertThat(newTravelList.get(0).getTravelZipCode()).isEqualTo(travelList.get(0).getTravelZipCode());

        // verify
        verify(mockTravelRepository, times(1)).findTravelsList(travelMap);
        verify(mockTravelRepository, atLeastOnce()).findTravelsList(travelMap);
        verifyNoMoreInteractions(mockTravelRepository);

        InOrder inOrder = inOrder(mockTravelRepository);
        inOrder.verify(mockTravelRepository).findTravelsList(travelMap);
    }

    @Test
    @DisplayName("여행지 소개 리스트 BDD 조회 테스트")
    void 여행지소개리스트BDD조회테스트() {
        // given
        Map<String, Object> travelMap = new HashMap<>();
        travelMap.put("jpaStartPage", 1);
        travelMap.put("size", 3);

        List<TravelDTO> travelList = new ArrayList<>();
        travelList.add(TravelDTO.builder().idx(1L).travelCode(1)
                .travelTitle("여행지 소개").travelDescription("여행지 소개")
                .travelAddr("인천광역시 서구").travelZipCode("123-456").visible("Y").build());

        // when
        given(mockTravelRepository.findTravelsList(travelMap)).willReturn(travelList);
        List<TravelDTO> newTravelList = mockTravelRepository.findTravelsList(travelMap);

        // then
        assertThat(newTravelList.get(0).getIdx()).isEqualTo(travelList.get(0).getIdx());
        assertThat(newTravelList.get(0).getTravelCode()).isEqualTo(travelList.get(0).getTravelCode());
        assertThat(newTravelList.get(0).getTravelTitle()).isEqualTo(travelList.get(0).getTravelTitle());
        assertThat(newTravelList.get(0).getTravelDescription()).isEqualTo(travelList.get(0).getTravelDescription());
        assertThat(newTravelList.get(0).getTravelAddr()).isEqualTo(travelList.get(0).getTravelAddr());
        assertThat(newTravelList.get(0).getTravelZipCode()).isEqualTo(travelList.get(0).getTravelZipCode());

        // verify
        then(mockTravelRepository).should(times(1)).findTravelsList(travelMap);
        then(mockTravelRepository).should(atLeastOnce()).findTravelsList(travelMap);
        then(mockTravelRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("여행지 소개 상세 Mockito 테스트")
    void 여행지소개상세Mockito테스트() {
        // given
        travelDTO = TravelDTO.builder()
                .idx(1L)
                .travelCode(1)
                .travelTitle("여행지 테스트").travelDescription("여행지 테스트")
                .travelAddr("인천광역시 서구").travelZipCode("123-456").visible("Y")
                .build();

        // when
        given(mockTravelRepository.findOneTravel(1L)).willReturn(travelDTO);
        TravelDTO newAdminTravel = mockTravelRepository.findOneTravel(1L);

        // then
        assertThat(newAdminTravel.getIdx()).isEqualTo(travelDTO.getIdx());
        assertThat(newAdminTravel.getTravelCode()).isEqualTo(travelDTO.getTravelCode());
        assertThat(newAdminTravel.getTravelTitle()).isEqualTo(travelDTO.getTravelTitle());
        assertThat(newAdminTravel.getTravelDescription()).isEqualTo(travelDTO.getTravelDescription());
        assertThat(newAdminTravel.getTravelAddr()).isEqualTo(travelDTO.getTravelAddr());
        assertThat(newAdminTravel.getTravelZipCode()).isEqualTo(travelDTO.getTravelZipCode());

        // verify
        verify(mockTravelRepository, times(1)).findOneTravel(1L);
        verify(mockTravelRepository, atLeastOnce()).findOneTravel(1L);
        verifyNoMoreInteractions(mockTravelRepository);

        InOrder inOrder = inOrder(mockTravelRepository);
        inOrder.verify(mockTravelRepository).findOneTravel(1L);
    }

    @Test
    @DisplayName("여행지 소개 상세 BDD 테스트")
    void 여행지소개상세BDD테스트() {
        // given
        travelDTO = TravelDTO.builder()
                .idx(1L)
                .travelCode(1)
                .travelTitle("여행지 테스트").travelDescription("여행지 테스트")
                .travelAddr("인천광역시 서구").travelZipCode("123-456").visible("Y")
                .build();

        // when
        when(mockTravelRepository.findOneTravel(1L)).thenReturn(travelDTO);
        TravelDTO newAdminTravel = mockTravelRepository.findOneTravel(1L);

        // then
        assertThat(newAdminTravel.getIdx()).isEqualTo(travelDTO.getIdx());
        assertThat(newAdminTravel.getTravelCode()).isEqualTo(travelDTO.getTravelCode());
        assertThat(newAdminTravel.getTravelTitle()).isEqualTo(travelDTO.getTravelTitle());
        assertThat(newAdminTravel.getTravelDescription()).isEqualTo(travelDTO.getTravelDescription());
        assertThat(newAdminTravel.getTravelAddr()).isEqualTo(travelDTO.getTravelAddr());
        assertThat(newAdminTravel.getTravelZipCode()).isEqualTo(travelDTO.getTravelZipCode());

        // verify
        then(mockTravelRepository).should(times(1)).findOneTravel(1L);
        then(mockTravelRepository).should(atLeastOnce()).findOneTravel(1L);
        then(mockTravelRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("이전 여행지 상세 조회 Mockito 테스트")
    void 이전여행지상세조회Mockito테스트() {
        // given
        travelEntity = TravelEntity.builder().idx(2L).build();

        // when
        travelDTO = travelRepository.findOnePrevTravel(travelDTO.getIdx());

        when(mockTravelRepository.findOnePrevTravel(travelDTO.getIdx())).thenReturn(travelDTO);
        TravelDTO travelInfo = mockTravelRepository.findOnePrevTravel(travelDTO.getIdx());

        // then
        assertThat(travelInfo.getIdx()).isEqualTo(1L);

        // verify
        verify(mockTravelRepository, times(1)).findOnePrevTravel(travelDTO.getIdx());
        verify(mockTravelRepository, atLeastOnce()).findOnePrevTravel(travelDTO.getIdx());
        verifyNoMoreInteractions(mockTravelRepository);

        InOrder inOrder = inOrder(mockTravelRepository);
        inOrder.verify(mockTravelRepository).findOnePrevTravel(travelDTO.getIdx());
    }

    @Test
    @DisplayName("이전 여행지 상세 조회 BDD 테스트")
    void 이전여행지상세조회BDD테스트() {
        // given
        travelEntity = TravelEntity.builder().idx(2L).build();

        // when
        travelDTO = travelRepository.findOnePrevTravel(travelDTO.getIdx());

        given(mockTravelRepository.findOnePrevTravel(travelDTO.getIdx())).willReturn(travelDTO);
        TravelDTO travelInfo = mockTravelRepository.findOnePrevTravel(travelDTO.getIdx());

        // then
        assertThat(travelInfo.getIdx()).isEqualTo(1L);

        // verify
        then(mockTravelRepository).should(times(1)).findOnePrevTravel(2L);
        then(mockTravelRepository).should(atLeastOnce()).findOnePrevTravel(2L);
        then(mockTravelRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("다음 여행지 상세 조회 Mockito 테스트")
    void 다음여행지상세조회Mockito테스트() {
        // given
        travelEntity = TravelEntity.builder().idx(2L).build();

        // when
        travelDTO = travelRepository.findOneNextTravel(travelEntity.getIdx());

        when(mockTravelRepository.findOneNextTravel(travelEntity.getIdx())).thenReturn(travelDTO);
        TravelDTO travelInfo = mockTravelRepository.findOneNextTravel(travelEntity.getIdx());

        // then
        assertThat(travelInfo.getIdx()).isEqualTo(1L);

        // verify
        verify(mockTravelRepository, times(1)).findOneNextTravel(travelEntity.getIdx());
        verify(mockTravelRepository, atLeastOnce()).findOneNextTravel(travelEntity.getIdx());
        verifyNoMoreInteractions(mockTravelRepository);

        InOrder inOrder = inOrder(mockTravelRepository);
        inOrder.verify(mockTravelRepository).findOneNextTravel(travelEntity.getIdx());
    }

    @Test
    @DisplayName("여행지 등록 Mockito 테스트")
    void 여행지등록Mockito테스트() {
        // given
        travelEntity = TravelEntity.builder()
                .travelCode(1)
                .travelTitle("여행지 테스트").travelDescription("여행지 테스트").favoriteCount(0).viewCount(0)
                .travelAddress("인천광역시 서구").travelZipCode("123-456").visible("Y")
                .build();

        TravelDTO travelInfo = travelRepository.insertTravel(travelEntity);

        // when
        when(mockTravelRepository.findOneTravel(travelEntity.getIdx())).thenReturn(travelInfo);
        TravelDTO newAdminTravel = mockTravelRepository.findOneTravel(travelEntity.getIdx());

        // then
        assertThat(newAdminTravel.getTravelCode()).isEqualTo(travelEntity.getTravelCode());
        assertThat(newAdminTravel.getTravelTitle()).isEqualTo(travelEntity.getTravelTitle());
        assertThat(newAdminTravel.getTravelDescription()).isEqualTo(travelEntity.getTravelDescription());
        assertThat(newAdminTravel.getTravelAddr()).isEqualTo(travelEntity.getTravelAddress());
        assertThat(newAdminTravel.getTravelZipCode()).isEqualTo(travelEntity.getTravelZipCode());

        // verify
        verify(mockTravelRepository, times(1)).findOneTravel(newAdminTravel.getIdx());
        verify(mockTravelRepository, atLeastOnce()).findOneTravel(newAdminTravel.getIdx());
        verifyNoMoreInteractions(mockTravelRepository);

        InOrder inOrder = inOrder(mockTravelRepository);
        inOrder.verify(mockTravelRepository).findOneTravel(newAdminTravel.getIdx());
    }

    @Test
    @DisplayName("여행지 등록 BDD 테스트")
    void 여행지등록BDD테스트() {
        // given
        travelEntity = TravelEntity.builder()
                .travelCode(1)
                .travelTitle("여행지 테스트").travelDescription("여행지 테스트").favoriteCount(0).viewCount(0)
                .travelAddress("인천광역시 서구").travelZipCode("123-456").visible("Y")
                .build();

        TravelDTO travelInfo = travelRepository.insertTravel(travelEntity);

        // when
        given(mockTravelRepository.findOneTravel(travelEntity.getIdx())).willReturn(travelInfo);
        TravelDTO newAdminTravel = mockTravelRepository.findOneTravel(travelEntity.getIdx());

        // then
        assertThat(newAdminTravel.getTravelCode()).isEqualTo(travelEntity.getTravelCode());
        assertThat(newAdminTravel.getTravelTitle()).isEqualTo(travelEntity.getTravelTitle());
        assertThat(newAdminTravel.getTravelDescription()).isEqualTo(travelEntity.getTravelDescription());
        assertThat(newAdminTravel.getTravelAddr()).isEqualTo(travelEntity.getTravelAddress());
        assertThat(newAdminTravel.getTravelZipCode()).isEqualTo(travelEntity.getTravelZipCode());

        // verify
        then(mockTravelRepository).should(times(1)).findOneTravel(newAdminTravel.getIdx());
        then(mockTravelRepository).should(atLeastOnce()).findOneTravel(newAdminTravel.getIdx());
        then(mockTravelRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("여행지 수정 Mockito 테스트")
    void 여행지수정Mockito테스트() {
        // given
        travelEntity = TravelEntity.builder()
                .travelCode(1)
                .travelTitle("여행지 테스트").travelDescription("여행지 테스트").favoriteCount(0).viewCount(0)
                .travelAddress("인천광역시 서구").travelZipCode("123-456").visible("Y")
                .build();

        TravelDTO adminTravelDTO = travelRepository.insertTravel(travelEntity);

        TravelEntity newAdminTravelEntity = TravelEntity.builder()
                .idx(adminTravelDTO.getIdx())
                .travelCode(1)
                .travelTitle("여행지 수정 테스트").travelDescription("여행지 수정 테스트").favoriteCount(0).viewCount(0)
                .travelAddress("서울특별시 강남구").travelZipCode("123-456")
                .visible("Y").build();

        travelRepository.updateTravel(newAdminTravelEntity);

        TravelDTO newAdminTravelDTO = TravelMapper.INSTANCE.toDto(newAdminTravelEntity);

        // when
        when(mockTravelRepository.findOneTravel(newAdminTravelEntity.getIdx())).thenReturn(newAdminTravelDTO);
        TravelDTO travelInfo = mockTravelRepository.findOneTravel(newAdminTravelDTO.getIdx());

        // then
        assertThat(travelInfo.getIdx()).isEqualTo(newAdminTravelDTO.getIdx());
        assertThat(travelInfo.getTravelCode()).isEqualTo(newAdminTravelDTO.getTravelCode());
        assertThat(travelInfo.getTravelTitle()).isEqualTo(newAdminTravelDTO.getTravelTitle());

        // verify
        verify(mockTravelRepository, times(1)).findOneTravel(travelInfo.getIdx());
        verify(mockTravelRepository, atLeastOnce()).findOneTravel(travelInfo.getIdx());
        verifyNoMoreInteractions(mockTravelRepository);

        InOrder inOrder = inOrder(mockTravelRepository);
        inOrder.verify(mockTravelRepository).findOneTravel(travelInfo.getIdx());
    }

    @Test
    @DisplayName("여행지 수정 BDD 테스트")
    void 여행지수정BDD테스트() {
        // given
        travelEntity = TravelEntity.builder()
                .travelCode(1)
                .travelTitle("여행지 테스트").travelDescription("여행지 테스트").favoriteCount(0).viewCount(0)
                .travelAddress("인천광역시 서구").travelZipCode("123-456").visible("Y")
                .build();

        TravelDTO adminTravelDTO = travelRepository.insertTravel(travelEntity);

        TravelEntity newAdminTravelEntity = TravelEntity.builder()
                .idx(adminTravelDTO.getIdx())
                .travelCode(1)
                .travelTitle("여행지 수정 테스트").travelDescription("여행지 수정 테스트").favoriteCount(0).viewCount(0)
                .travelAddress("서울특별시 강남구").travelZipCode("123-456")
                .visible("Y").build();

        travelRepository.updateTravel(newAdminTravelEntity);

        TravelDTO newAdminTravelDTO = TravelMapper.INSTANCE.toDto(newAdminTravelEntity);

        // when
        given(mockTravelRepository.findOneTravel(newAdminTravelEntity.getIdx())).willReturn(newAdminTravelDTO);
        TravelDTO travelInfo = mockTravelRepository.findOneTravel(newAdminTravelDTO.getIdx());

        // then
        assertThat(travelInfo.getIdx()).isEqualTo(newAdminTravelDTO.getIdx());
        assertThat(travelInfo.getTravelCode()).isEqualTo(newAdminTravelDTO.getTravelCode());
        assertThat(travelInfo.getTravelTitle()).isEqualTo(newAdminTravelDTO.getTravelTitle());

        // verify
        then(mockTravelRepository).should(times(1)).findOneTravel(travelInfo.getIdx());
        then(mockTravelRepository).should(atLeastOnce()).findOneTravel(travelInfo.getIdx());
        then(mockTravelRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("여행지 삭제 Mockito 테스트")
    void 여행지삭제Mockito테스트() {
        // given
        em.persist(travelEntity);
        travelDTO = TravelMapper.INSTANCE.toDto(travelEntity);

        // when
        when(mockTravelRepository.findOneTravel(travelDTO.getIdx())).thenReturn(travelDTO);
        Long deleteIdx = travelRepository.deleteTravel(travelDTO.getIdx());

        // then
        assertThat(mockTravelRepository.findOneTravel(travelDTO.getIdx()).getIdx()).isEqualTo(deleteIdx);

        // verify
        verify(mockTravelRepository, times(1)).findOneTravel(travelDTO.getIdx());
        verify(mockTravelRepository, atLeastOnce()).findOneTravel(travelDTO.getIdx());
        verifyNoMoreInteractions(mockTravelRepository);

        InOrder inOrder = inOrder(mockTravelRepository);
        inOrder.verify(mockTravelRepository).findOneTravel(travelDTO.getIdx());
    }

    @Test
    @DisplayName("여행지 삭제 BDD 테스트")
    void 여행지삭제BDD테스트() {
        // given
        em.persist(travelEntity);
        travelDTO = TravelMapper.INSTANCE.toDto(travelEntity);

        // when
        given(mockTravelRepository.findOneTravel(travelDTO.getIdx())).willReturn(travelDTO);
        Long deleteIdx = travelRepository.deleteTravel(travelDTO.getIdx());

        // then
        assertThat(mockTravelRepository.findOneTravel(travelDTO.getIdx()).getIdx()).isEqualTo(deleteIdx);

        // verify
        then(mockTravelRepository).should(times(1)).findOneTravel(travelDTO.getIdx());
        then(mockTravelRepository).should(atLeastOnce()).findOneTravel(travelDTO.getIdx());
        then(mockTravelRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("여행지좋아요Mockito테스트")
    void 여행지좋아요Mockito테스트() {
        // given
        travelEntity = TravelEntity.builder()
                .travelCode(1)
                .travelTitle("여행지 테스트").travelDescription("여행지 테스트").favoriteCount(0).viewCount(0)
                .travelAddress("인천광역시 서구").travelZipCode("123-456").visible("Y")
                .build();

        TravelDTO travelDTO = travelRepository.insertTravel(travelEntity);
        
        // when
        // 좋아요 수 증가
        Integer favoriteCount = travelRepository.favoriteTravel(travelDTO.getIdx());
        when(mockTravelRepository.favoriteTravel(travelDTO.getIdx())).thenReturn(favoriteCount);

        // then
        // 증가한 좋아요 수 조회
        Integer addFavoriteCount = mockTravelRepository.favoriteTravel(travelDTO.getIdx());
        assertThat(favoriteCount).isEqualTo(addFavoriteCount);
        
        // verify
        verify(mockTravelRepository, times(1)).favoriteTravel(travelDTO.getIdx());
        verify(mockTravelRepository, atLeastOnce()).favoriteTravel(travelDTO.getIdx());
        verifyNoMoreInteractions(mockTravelRepository);

        InOrder inOrder = inOrder(mockTravelRepository);
        inOrder.verify(mockTravelRepository).favoriteTravel(travelDTO.getIdx());
    }

    @Test
    @DisplayName("여행지좋아요BDD테스트")
    void 여행지좋아요BDD테스트() {
        // given
        travelEntity = TravelEntity.builder()
                .travelCode(1)
                .travelTitle("여행지 테스트").travelDescription("여행지 테스트").favoriteCount(1).viewCount(0)
                .travelAddress("인천광역시 서구").travelZipCode("123-456").visible("Y")
                .build();

        TravelDTO travelDTO = travelRepository.insertTravel(travelEntity);

        // when
        // 좋아요 수 증가
        Integer favoriteCount = travelRepository.favoriteTravel(travelDTO.getIdx());
        given(mockTravelRepository.favoriteTravel(travelDTO.getIdx())).willReturn(favoriteCount);

        // then
        // 증가한 좋아요 수 조회
        Integer addFavoriteCount = mockTravelRepository.favoriteTravel(travelDTO.getIdx());
        assertThat(favoriteCount).isEqualTo(addFavoriteCount);

        // verify
        then(mockTravelRepository).should(times(1)).favoriteTravel(travelDTO.getIdx());
        then(mockTravelRepository).should(atLeastOnce()).favoriteTravel(travelDTO.getIdx());
        then(mockTravelRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("인기여행지선정Mockito테스트")
    void 인기여행지선정Mockito테스트() {
        // given
        Long idx = travelRepository.insertTravel(travelEntity).getIdx();

        Boolean popular = travelRepository.togglePopular(idx).getPopular();

        travelEntity = TravelEntity.builder()
                .travelCode(1)
                .travelTitle("여행지 테스트").travelDescription("여행지 테스트").favoriteCount(1).viewCount(0)
                .travelAddress("인천광역시 서구").travelZipCode("123-456").visible("Y").popular(popular)
                .build();

        TravelDTO travelDTO = TravelMapper.INSTANCE.toDto(travelEntity);

        // when
        when(mockTravelRepository.findOneTravel(travelEntity.getIdx())).thenReturn(travelDTO);
        TravelDTO travelInfo = mockTravelRepository.findOneTravel(travelEntity.getIdx());

        // then
        assertThat(travelInfo.getPopular()).isTrue();

        // verify
        verify(mockTravelRepository, times(1)).findOneTravel(travelEntity.getIdx());
        verify(mockTravelRepository, atLeastOnce()).findOneTravel(travelEntity.getIdx());
        verifyNoMoreInteractions(mockTravelRepository);

        InOrder inOrder = inOrder(mockTravelRepository);
        inOrder.verify(mockTravelRepository).findOneTravel(travelEntity.getIdx());
    }

    @Test
    @DisplayName("인기여행지선정BDD테스트")
    void 인기여행지선정BDD테스트() {
        // given
        Long idx = travelRepository.insertTravel(travelEntity).getIdx();

        Boolean popular = travelRepository.togglePopular(idx).getPopular();

        travelEntity = TravelEntity.builder()
                .travelCode(1)
                .travelTitle("여행지 테스트").travelDescription("여행지 테스트").favoriteCount(1).viewCount(0)
                .travelAddress("인천광역시 서구").travelZipCode("123-456").visible("Y").popular(popular)
                .build();

        TravelDTO travelDTO = TravelMapper.INSTANCE.toDto(travelEntity);

        // when
        given(mockTravelRepository.findOneTravel(travelEntity.getIdx())).willReturn(travelDTO);
        TravelDTO travelInfo = mockTravelRepository.findOneTravel(travelEntity.getIdx());

        // then
        assertThat(travelInfo.getPopular()).isTrue();

        // verify
        then(mockTravelRepository).should(times(1)).favoriteTravel(travelDTO.getIdx());
        then(mockTravelRepository).should(atLeastOnce()).favoriteTravel(travelDTO.getIdx());
        then(mockTravelRepository).shouldHaveNoMoreInteractions();
    }
}
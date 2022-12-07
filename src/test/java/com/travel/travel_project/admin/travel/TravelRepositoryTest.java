package com.travel.travel_project.admin.travel;

import com.travel.travel_project.api.travel.mapper.group.TravelGroupMapper;
import com.travel.travel_project.api.travel.mapper.review.TravelReviewMapper;
import com.travel.travel_project.domain.travel.TravelDTO;
import com.travel.travel_project.domain.travel.TravelEntity;
import com.travel.travel_project.api.travel.TravelRepository;
import com.travel.travel_project.api.travel.mapper.TravelMapper;
import com.travel.travel_project.domain.travel.group.TravelGroupDTO;
import com.travel.travel_project.domain.travel.group.TravelGroupEntity;
import com.travel.travel_project.domain.travel.group.TravelGroupUserDTO;
import com.travel.travel_project.domain.travel.group.TravelGroupUserEntity;
import com.travel.travel_project.domain.travel.review.TravelReviewDTO;
import com.travel.travel_project.domain.travel.review.TravelReviewEntity;
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
    @DisplayName("여행지소개리스트조회테스트")
    void 여행지소개리스트조회테스트() {
        // given
        Map<String, Object> travelMap = new HashMap<>();
        travelMap.put("jpaStartPage", 0);
        travelMap.put("size", 3);

        // then
        assertThat(travelRepository.findTravelList(travelMap)).isNotEmpty();
    }

    @Test
    @DisplayName("여행지상세조회테스트")
    void 여행지_상세_조회_테스트() {
        TravelDTO oneTravel = travelRepository.findOneTravel(1L);

        assertThat(oneTravel.getIdx()).isEqualTo(1L);
        assertThat(oneTravel.getTravelCode()).isEqualTo(1);
        assertThat(oneTravel.getTravelTitle()).isEqualTo("서울 여행지");
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
                .travelAddress("인천광역시 서구").travelZipCode("123-456").visible("Y").build());

        // when
        when(mockTravelRepository.findTravelList(travelMap)).thenReturn(travelList);
        List<TravelDTO> newTravelList = mockTravelRepository.findTravelList(travelMap);

        // then
        assertThat(newTravelList.get(0).getIdx()).isEqualTo(travelList.get(0).getIdx());
        assertThat(newTravelList.get(0).getTravelCode()).isEqualTo(travelList.get(0).getTravelCode());
        assertThat(newTravelList.get(0).getTravelTitle()).isEqualTo(travelList.get(0).getTravelTitle());
        assertThat(newTravelList.get(0).getTravelDescription()).isEqualTo(travelList.get(0).getTravelDescription());
        assertThat(newTravelList.get(0).getTravelAddress()).isEqualTo(travelList.get(0).getTravelAddress());
        assertThat(newTravelList.get(0).getTravelZipCode()).isEqualTo(travelList.get(0).getTravelZipCode());

        // verify
        verify(mockTravelRepository, times(1)).findTravelList(travelMap);
        verify(mockTravelRepository, atLeastOnce()).findTravelList(travelMap);
        verifyNoMoreInteractions(mockTravelRepository);

        InOrder inOrder = inOrder(mockTravelRepository);
        inOrder.verify(mockTravelRepository).findTravelList(travelMap);
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
                        .travelAddress("인천광역시 서구").travelZipCode("123-456").visible("Y").build());

        // when
        when(mockTravelRepository.findTravelList(travelMap)).thenReturn(travelList);
        List<TravelDTO> newTravelList = mockTravelRepository.findTravelList(travelMap);

        // then
        assertThat(newTravelList.get(0).getIdx()).isEqualTo(travelList.get(0).getIdx());
        assertThat(newTravelList.get(0).getTravelCode()).isEqualTo(travelList.get(0).getTravelCode());
        assertThat(newTravelList.get(0).getTravelTitle()).isEqualTo(travelList.get(0).getTravelTitle());
        assertThat(newTravelList.get(0).getTravelDescription()).isEqualTo(travelList.get(0).getTravelDescription());
        assertThat(newTravelList.get(0).getTravelAddress()).isEqualTo(travelList.get(0).getTravelAddress());
        assertThat(newTravelList.get(0).getTravelZipCode()).isEqualTo(travelList.get(0).getTravelZipCode());

        // verify
        verify(mockTravelRepository, times(1)).findTravelList(travelMap);
        verify(mockTravelRepository, atLeastOnce()).findTravelList(travelMap);
        verifyNoMoreInteractions(mockTravelRepository);

        InOrder inOrder = inOrder(mockTravelRepository);
        inOrder.verify(mockTravelRepository).findTravelList(travelMap);
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
                .travelAddress("인천광역시 서구").travelZipCode("123-456").visible("Y").build());

        // when
        given(mockTravelRepository.findTravelList(travelMap)).willReturn(travelList);
        List<TravelDTO> newTravelList = mockTravelRepository.findTravelList(travelMap);

        // then
        assertThat(newTravelList.get(0).getIdx()).isEqualTo(travelList.get(0).getIdx());
        assertThat(newTravelList.get(0).getTravelCode()).isEqualTo(travelList.get(0).getTravelCode());
        assertThat(newTravelList.get(0).getTravelTitle()).isEqualTo(travelList.get(0).getTravelTitle());
        assertThat(newTravelList.get(0).getTravelDescription()).isEqualTo(travelList.get(0).getTravelDescription());
        assertThat(newTravelList.get(0).getTravelAddress()).isEqualTo(travelList.get(0).getTravelAddress());
        assertThat(newTravelList.get(0).getTravelZipCode()).isEqualTo(travelList.get(0).getTravelZipCode());

        // verify
        then(mockTravelRepository).should(times(1)).findTravelList(travelMap);
        then(mockTravelRepository).should(atLeastOnce()).findTravelList(travelMap);
        then(mockTravelRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("여행지 소개 상세 Mockito 테스트")
    void 여행지소개상세Mockito테스트() {
        // given
        travelEntity = TravelEntity.builder()
                .idx(1L)
                .travelCode(1)
                .travelTitle("여행지 테스트").travelDescription("여행지 테스트")
                .travelAddress("인천광역시 서구").travelZipCode("123-456").visible("Y")
                .build();

        travelDTO = TravelMapper.INSTANCE.toDto(travelEntity);

        // when
        when(mockTravelRepository.findOneTravel(1L)).thenReturn(travelDTO);
        TravelDTO newTravelInfo = mockTravelRepository.findOneTravel(1L);

        // then
        assertThat(newTravelInfo.getIdx()).isEqualTo(travelEntity.getIdx());
        assertThat(newTravelInfo.getTravelCode()).isEqualTo(travelEntity.getTravelCode());
        assertThat(newTravelInfo.getTravelTitle()).isEqualTo(travelEntity.getTravelTitle());
        assertThat(newTravelInfo.getTravelDescription()).isEqualTo(travelEntity.getTravelDescription());
        assertThat(newTravelInfo.getTravelAddress()).isEqualTo(travelEntity.getTravelAddress());
        assertThat(newTravelInfo.getTravelZipCode()).isEqualTo(travelEntity.getTravelZipCode());

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
        travelEntity = TravelEntity.builder()
                .idx(1L)
                .travelCode(1)
                .travelTitle("여행지 테스트").travelDescription("여행지 테스트")
                .travelAddress("인천광역시 서구").travelZipCode("123-456").visible("Y")
                .build();

        travelDTO = TravelMapper.INSTANCE.toDto(travelEntity);

        // when
        when(mockTravelRepository.findOneTravel(1L)).thenReturn(travelDTO);
        TravelDTO newTravelInfo = mockTravelRepository.findOneTravel(1L);

        // then
        assertThat(newTravelInfo.getIdx()).isEqualTo(travelEntity.getIdx());
        assertThat(newTravelInfo.getTravelCode()).isEqualTo(travelEntity.getTravelCode());
        assertThat(newTravelInfo.getTravelTitle()).isEqualTo(travelEntity.getTravelTitle());
        assertThat(newTravelInfo.getTravelDescription()).isEqualTo(travelEntity.getTravelDescription());
        assertThat(newTravelInfo.getTravelAddress()).isEqualTo(travelEntity.getTravelAddress());
        assertThat(newTravelInfo.getTravelZipCode()).isEqualTo(travelEntity.getTravelZipCode());

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
        TravelDTO newTravelInfo = mockTravelRepository.findOneTravel(travelEntity.getIdx());

        // then
        assertThat(newTravelInfo.getTravelCode()).isEqualTo(travelEntity.getTravelCode());
        assertThat(newTravelInfo.getTravelTitle()).isEqualTo(travelEntity.getTravelTitle());
        assertThat(newTravelInfo.getTravelDescription()).isEqualTo(travelEntity.getTravelDescription());
        assertThat(newTravelInfo.getTravelAddress()).isEqualTo(travelEntity.getTravelAddress());
        assertThat(newTravelInfo.getTravelZipCode()).isEqualTo(travelEntity.getTravelZipCode());

        // verify
        verify(mockTravelRepository, times(1)).findOneTravel(newTravelInfo.getIdx());
        verify(mockTravelRepository, atLeastOnce()).findOneTravel(newTravelInfo.getIdx());
        verifyNoMoreInteractions(mockTravelRepository);

        InOrder inOrder = inOrder(mockTravelRepository);
        inOrder.verify(mockTravelRepository).findOneTravel(newTravelInfo.getIdx());
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
        TravelDTO newTravelInfo = mockTravelRepository.findOneTravel(travelEntity.getIdx());

        // then
        assertThat(newTravelInfo.getTravelCode()).isEqualTo(travelEntity.getTravelCode());
        assertThat(newTravelInfo.getTravelTitle()).isEqualTo(travelEntity.getTravelTitle());
        assertThat(newTravelInfo.getTravelDescription()).isEqualTo(travelEntity.getTravelDescription());
        assertThat(newTravelInfo.getTravelAddress()).isEqualTo(travelEntity.getTravelAddress());
        assertThat(newTravelInfo.getTravelZipCode()).isEqualTo(travelEntity.getTravelZipCode());

        // verify
        then(mockTravelRepository).should(times(1)).findOneTravel(newTravelInfo.getIdx());
        then(mockTravelRepository).should(atLeastOnce()).findOneTravel(newTravelInfo.getIdx());
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

        TravelDTO newTravelInfo = travelRepository.insertTravel(travelEntity);

        TravelEntity newTravelEntity = TravelEntity.builder()
                .idx(newTravelInfo.getIdx())
                .travelCode(1)
                .travelTitle("여행지 수정 테스트").travelDescription("여행지 수정 테스트").favoriteCount(0).viewCount(0)
                .travelAddress("서울특별시 강남구").travelZipCode("123-456")
                .visible("Y").build();

        TravelDTO updateTravelEntity = travelRepository.updateTravel(newTravelEntity);

        // when
        when(mockTravelRepository.findOneTravel(newTravelEntity.getIdx())).thenReturn(updateTravelEntity);
        TravelDTO travelInfo = mockTravelRepository.findOneTravel(updateTravelEntity.getIdx());

        // then
        assertThat(travelInfo.getIdx()).isEqualTo(updateTravelEntity.getIdx());
        assertThat(travelInfo.getTravelCode()).isEqualTo(updateTravelEntity.getTravelCode());
        assertThat(travelInfo.getTravelTitle()).isEqualTo(updateTravelEntity.getTravelTitle());

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

        TravelDTO newTravelDTO = travelRepository.insertTravel(travelEntity);

        TravelEntity newAdminTravelEntity = TravelEntity.builder()
                .idx(newTravelDTO.getIdx())
                .travelCode(1)
                .travelTitle("여행지 수정 테스트").travelDescription("여행지 수정 테스트").favoriteCount(0).viewCount(0)
                .travelAddress("서울특별시 강남구").travelZipCode("123-456")
                .visible("Y").build();

        TravelDTO updateTravelDTO = travelRepository.updateTravel(newAdminTravelEntity);

        // when
        given(mockTravelRepository.findOneTravel(newAdminTravelEntity.getIdx())).willReturn(updateTravelDTO);
        TravelDTO travelInfo = mockTravelRepository.findOneTravel(newAdminTravelEntity.getIdx());

        // then
        assertThat(travelInfo.getIdx()).isEqualTo(newAdminTravelEntity.getIdx());
        assertThat(travelInfo.getTravelCode()).isEqualTo(newAdminTravelEntity.getTravelCode());
        assertThat(travelInfo.getTravelTitle()).isEqualTo(newAdminTravelEntity.getTravelTitle());

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
        when(mockTravelRepository.findOneTravel(travelEntity.getIdx())).thenReturn(travelDTO);
        Long deleteIdx = travelRepository.deleteTravel(travelEntity.getIdx());

        // then
        assertThat(mockTravelRepository.findOneTravel(travelEntity.getIdx()).getIdx()).isEqualTo(deleteIdx);

        // verify
        verify(mockTravelRepository, times(1)).findOneTravel(travelEntity.getIdx());
        verify(mockTravelRepository, atLeastOnce()).findOneTravel(travelEntity.getIdx());
        verifyNoMoreInteractions(mockTravelRepository);

        InOrder inOrder = inOrder(mockTravelRepository);
        inOrder.verify(mockTravelRepository).findOneTravel(travelEntity.getIdx());
    }

    @Test
    @DisplayName("여행지 삭제 BDD 테스트")
    void 여행지삭제BDD테스트() {
        // given
        em.persist(travelEntity);
        travelDTO = TravelMapper.INSTANCE.toDto(travelEntity);

        // when
        given(mockTravelRepository.findOneTravel(travelEntity.getIdx())).willReturn(travelDTO);
        Long deleteIdx = travelRepository.deleteTravel(travelEntity.getIdx());

        // then
        assertThat(mockTravelRepository.findOneTravel(travelEntity.getIdx()).getIdx()).isEqualTo(deleteIdx);

        // verify
        then(mockTravelRepository).should(times(1)).findOneTravel(travelEntity.getIdx());
        then(mockTravelRepository).should(atLeastOnce()).findOneTravel(travelEntity.getIdx());
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

        Boolean popular = travelRepository.togglePopular(idx);

        travelEntity = TravelEntity.builder()
                .travelCode(1)
                .travelTitle("여행지 테스트").travelDescription("여행지 테스트").favoriteCount(1).viewCount(0)
                .travelAddress("인천광역시 서구").travelZipCode("123-456").visible("Y").popular(popular)
                .build();

        travelDTO = TravelMapper.INSTANCE.toDto(travelEntity);

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

        Boolean popular = travelRepository.togglePopular(idx);

        travelEntity = TravelEntity.builder()
                .travelCode(1)
                .travelTitle("여행지 테스트").travelDescription("여행지 테스트").favoriteCount(1).viewCount(0)
                .travelAddress("인천광역시 서구").travelZipCode("123-456").visible("Y").popular(popular)
                .build();

        travelDTO = TravelMapper.INSTANCE.toDto(travelEntity);

        // when
        given(mockTravelRepository.findOneTravel(travelEntity.getIdx())).willReturn(travelDTO);
        TravelDTO travelInfo = mockTravelRepository.findOneTravel(travelEntity.getIdx());

        // then
        assertThat(travelInfo.getPopular()).isTrue();

        // verify
        then(mockTravelRepository).should(times(1)).findOneTravel(travelDTO.getIdx());
        then(mockTravelRepository).should(atLeastOnce()).findOneTravel(travelDTO.getIdx());
        then(mockTravelRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("여행지댓글등록Mockito테스트")
    void 여행지댓글등록Mockito테스트() {
        // given
        TravelDTO travelInfo = travelRepository.insertTravel(travelEntity);

        TravelReviewEntity travelReviewEntity = TravelReviewEntity.builder()
                .travelIdx(travelInfo.getIdx())
                .reviewTitle("리뷰등록테스트")
                .reviewDescription("리뷰등록테스트")
                .viewCount(0)
                .favoriteCount(0)
                .popular(false)
                .visible("Y")
                .build();

        TravelReviewDTO travelReviewDTO = travelRepository.replyTravel(travelReviewEntity);
        List<TravelReviewDTO> reviewList = new ArrayList<>();
        reviewList.add(travelReviewDTO);

        travelDTO = TravelDTO.builder()
                .travelCode(1)
                .travelTitle("여행지 테스트").travelDescription("여행지 테스트").favoriteCount(1).viewCount(0)
                .travelAddress("인천광역시 서구").travelZipCode("123-456").visible("Y").popular(false)
                .reviewList(TravelReviewMapper.INSTANCE.toEntityList(reviewList))
                .build();

        // when
        when(mockTravelRepository.findOneTravel(travelInfo.getIdx())).thenReturn(travelDTO);
        TravelDTO newTravelInfo = mockTravelRepository.findOneTravel(travelInfo.getIdx());

        // then
        assertThat(newTravelInfo.getReviewList().get(0).getReviewTitle()).isEqualTo(reviewList.get(0).getReviewTitle());

        // verify
        verify(mockTravelRepository, times(1)).findOneTravel(travelInfo.getIdx());
        verify(mockTravelRepository, atLeastOnce()).findOneTravel(travelInfo.getIdx());
        verifyNoMoreInteractions(mockTravelRepository);

        InOrder inOrder = inOrder(mockTravelRepository);
        inOrder.verify(mockTravelRepository).findOneTravel(travelInfo.getIdx());
    }

    @Test
    @DisplayName("여행지댓글수정Mockito테스트")
    void 여행지댓글수정Mockito테스트() {
        // given
        TravelDTO travelInfo = travelRepository.insertTravel(travelEntity);

        // 댓글 등록
        TravelReviewEntity travelReviewEntity = TravelReviewEntity.builder()
                .travelIdx(travelInfo.getIdx())
                .reviewTitle("리뷰등록테스트")
                .reviewDescription("리뷰등록테스트")
                .viewCount(0)
                .favoriteCount(0)
                .popular(false)
                .visible("Y")
                .build();

        TravelReviewDTO reviewDTO = travelRepository.replyTravel(travelReviewEntity);

        // 댓글 수정
        travelReviewEntity = TravelReviewEntity.builder()
                .idx(reviewDTO.getIdx())
                .travelIdx(travelInfo.getIdx())
                .reviewTitle("리뷰수정테스트")
                .reviewDescription("리뷰수정테스트")
                .viewCount(0)
                .favoriteCount(0)
                .popular(false)
                .visible("Y")
                .build();

        TravelReviewDTO travelReviewDTO = travelRepository.updateReplyTravel(travelReviewEntity);
        List<TravelReviewDTO> reviewList = new ArrayList<>();
        reviewList.add(travelReviewDTO);

        travelDTO = TravelDTO.builder()
                .travelCode(1)
                .travelTitle("여행지 테스트").travelDescription("여행지 테스트").favoriteCount(1).viewCount(0)
                .travelAddress("인천광역시 서구").travelZipCode("123-456").visible("Y").popular(false)
                .reviewList(TravelReviewMapper.INSTANCE.toEntityList(reviewList))
                .build();

        // when
        when(mockTravelRepository.replyTravelReview(travelDTO.getIdx())).thenReturn(reviewList);
        List<TravelReviewDTO> reviewDTOList = mockTravelRepository.replyTravelReview(travelDTO.getIdx());

        // then
        assertThat(reviewDTOList.get(0).getReviewTitle()).isEqualTo("리뷰수정테스트");
        assertThat(reviewDTOList.get(0).getReviewDescription()).isEqualTo("리뷰수정테스트");

        // verify
        verify(mockTravelRepository, times(1)).replyTravelReview(travelDTO.getIdx());
        verify(mockTravelRepository,  atLeastOnce()).replyTravelReview(travelDTO.getIdx());
        verifyNoMoreInteractions(mockTravelRepository);

        InOrder inOrder = inOrder(mockTravelRepository);
        inOrder.verify(mockTravelRepository).replyTravelReview(travelDTO.getIdx());
    }

    @Test
    @DisplayName("여행지 댓글 삭제 Mockito 테스트")
    void 여행지댓글삭제Mockito테스트() {
        // given
        // 여행지 등록
        TravelDTO travelInfo = travelRepository.insertTravel(travelEntity);
        // 여행지 댓글 등록
        TravelReviewEntity travelReviewEntity = TravelReviewEntity.builder()
                .travelIdx(travelInfo.getIdx())
                .reviewTitle("리뷰등록테스트")
                .reviewDescription("리뷰등록테스트")
                .viewCount(0)
                .favoriteCount(0)
                .popular(false)
                .visible("Y")
                .build();
        TravelReviewDTO travelReviewDTO = travelRepository.replyTravel(travelReviewEntity);
        // 여행지 댓글 삭제
        Long deleteIdx = travelRepository.deleteReplyTravel(travelReviewDTO.getIdx());

        // then
        assertThat(travelReviewDTO.getIdx()).isEqualTo(deleteIdx);
    }

    @Test
    @DisplayName("여행지 댓글 리스트 조회 Mockito 테스트")
    void 여행지댓글리스트조회Mockito테스트() {
        // given
        TravelDTO travelInfo = travelRepository.insertTravel(travelEntity);

        TravelReviewEntity travelReviewEntity = TravelReviewEntity.builder()
                .travelIdx(travelInfo.getIdx())
                .reviewTitle("리뷰등록테스트")
                .reviewDescription("리뷰등록테스트")
                .viewCount(0)
                .favoriteCount(0)
                .popular(false)
                .visible("Y")
                .build();

        travelRepository.replyTravel(travelReviewEntity);
        List<TravelReviewDTO> reviewList = travelRepository.replyTravelReview(travelInfo.getIdx());

        travelDTO = TravelDTO.builder()
                .travelCode(1)
                .travelTitle("여행지 테스트").travelDescription("여행지 테스트").favoriteCount(1).viewCount(0)
                .travelAddress("인천광역시 서구").travelZipCode("123-456").visible("Y").popular(false)
                .reviewList(TravelReviewMapper.INSTANCE.toEntityList(reviewList))
                .build();

        // when
        when(mockTravelRepository.replyTravelReview(travelInfo.getIdx())).thenReturn(reviewList);

        // then
        assertThat(mockTravelRepository.replyTravelReview(travelInfo.getIdx()).get(0).getTravelIdx()).isEqualTo(travelInfo.getIdx());
        assertThat(mockTravelRepository.replyTravelReview(travelInfo.getIdx()).get(0).getReviewTitle()).isEqualTo("리뷰등록테스트");
        assertThat(mockTravelRepository.replyTravelReview(travelInfo.getIdx()).get(0).getReviewDescription()).isEqualTo("리뷰등록테스트");
    }

    @Test
    @DisplayName("여행지 댓글 상세 조회 Mockito 테스트")
    void 여행지댓글상세조회Mockito테스트() {
        // given
        TravelDTO travelInfo = travelRepository.insertTravel(travelEntity);

        TravelReviewEntity travelReviewEntity = TravelReviewEntity.builder()
                .travelIdx(travelInfo.getIdx())
                .reviewTitle("리뷰등록테스트")
                .reviewDescription("리뷰등록테스트")
                .viewCount(0)
                .favoriteCount(0)
                .popular(false)
                .visible("Y")
                .build();

        TravelReviewDTO travelReviewDTO = travelRepository.replyTravel(travelReviewEntity);

        // when
        when(mockTravelRepository.detailReplyTravelReview(travelReviewDTO.getIdx())).thenReturn(travelReviewDTO);
        TravelReviewDTO travelReviewInfo = mockTravelRepository.detailReplyTravelReview(travelReviewDTO.getIdx());

        // then
        assertThat(travelReviewDTO.getTravelIdx()).isEqualTo(travelInfo.getIdx());
        assertThat(travelReviewInfo.getReviewTitle()).isEqualTo("리뷰등록테스트");
        assertThat(travelReviewInfo.getReviewDescription()).isEqualTo("리뷰등록테스트");

        // verify
        verify(mockTravelRepository, times(1)).detailReplyTravelReview(travelReviewInfo.getIdx());
        verify(mockTravelRepository,  atLeastOnce()).detailReplyTravelReview(travelReviewInfo.getIdx());
        verifyNoMoreInteractions(mockTravelRepository);

        InOrder inOrder = inOrder(mockTravelRepository);
        inOrder.verify(mockTravelRepository).detailReplyTravelReview(travelReviewInfo.getIdx());
    }

    @Test
    @DisplayName("여행지 그룹 리스트 Mockito 조회 테스트")
    void 여행지그룹리스트Mockito조회테스트() {
        // given
        Map<String, Object> groupMap = new HashMap<>();
        groupMap.put("jpaStartPage", 1);
        groupMap.put("size", 3);

        List<TravelGroupDTO> travelGroupList = new ArrayList<>();
        travelGroupList.add(TravelGroupDTO.builder().travelIdx(1L).groupName("서울모임")
                .groupDescription("서울모임").visible("Y").build());

        // when
        when(mockTravelRepository.findTravelGroupList(groupMap)).thenReturn(travelGroupList);
        List<TravelGroupDTO> newTravelGroupList = mockTravelRepository.findTravelGroupList(groupMap);

        // then
        assertThat(newTravelGroupList.get(0).getIdx()).isEqualTo(travelGroupList.get(0).getIdx());
        assertThat(newTravelGroupList.get(0).getTravelIdx()).isEqualTo(travelGroupList.get(0).getTravelIdx());
        assertThat(newTravelGroupList.get(0).getGroupName()).isEqualTo(travelGroupList.get(0).getGroupName());
        assertThat(newTravelGroupList.get(0).getGroupDescription()).isEqualTo(travelGroupList.get(0).getGroupDescription());

        // verify
        verify(mockTravelRepository, times(1)).findTravelGroupList(groupMap);
        verify(mockTravelRepository, atLeastOnce()).findTravelGroupList(groupMap);
        verifyNoMoreInteractions(mockTravelRepository);

        InOrder inOrder = inOrder(mockTravelRepository);
        inOrder.verify(mockTravelRepository).findTravelGroupList(groupMap);
    }

    @Test
    @DisplayName("여행지 그룹 상세 Mockito 테스트")
    void 여행지그룹상세Mockito테스트() {
        // given
        TravelGroupDTO travelGroupDTO = TravelGroupDTO.builder()
                .idx(1L).travelIdx(1L).groupName("서울모임").groupDescription("서울모임").visible("Y").build();

        // when
        given(mockTravelRepository.findOneTravelGroup(1L)).willReturn(travelGroupDTO);
        TravelGroupDTO newTravelGroupDTO = mockTravelRepository.findOneTravelGroup(1L);

        // then
        assertThat(newTravelGroupDTO.getIdx()).isEqualTo(1L);
        assertThat(newTravelGroupDTO.getTravelIdx()).isEqualTo(1L);
        assertThat(newTravelGroupDTO.getGroupName()).isEqualTo("서울모임");
        assertThat(newTravelGroupDTO.getGroupDescription()).isEqualTo("서울모임");

        // verify
        verify(mockTravelRepository, times(1)).findOneTravelGroup(1L);
        verify(mockTravelRepository, atLeastOnce()).findOneTravelGroup(1L);
        verifyNoMoreInteractions(mockTravelRepository);

        InOrder inOrder = inOrder(mockTravelRepository);
        inOrder.verify(mockTravelRepository).findOneTravelGroup(1L);
    }

    @Test
    @DisplayName("여행지 그룹 등록 Mockito 테스트")
    void 여행지그룹등록Mockito테스트() {
        TravelGroupEntity travelGroupEntity = TravelGroupEntity.builder()
                .travelIdx(1L).groupName("서울모임").groupDescription("서울모임").visible("Y").build();

        TravelGroupDTO travelGroupDTO = travelRepository.insertTravelGroup(travelGroupEntity);

        // when
        when(mockTravelRepository.findOneTravelGroup(travelGroupDTO.getIdx())).thenReturn(travelGroupDTO);
        TravelGroupDTO newTravelGroupDTO = mockTravelRepository.findOneTravelGroup(travelGroupDTO.getIdx());

        // then
        assertThat(newTravelGroupDTO.getIdx()).isEqualTo(travelGroupDTO.getIdx());
        assertThat(newTravelGroupDTO.getTravelIdx()).isEqualTo(travelGroupDTO.getTravelIdx());
        assertThat(newTravelGroupDTO.getGroupName()).isEqualTo(travelGroupDTO.getGroupName());
        assertThat(newTravelGroupDTO.getGroupDescription()).isEqualTo(travelGroupDTO.getGroupDescription());

        // verify
        verify(mockTravelRepository, times(1)).findOneTravelGroup(newTravelGroupDTO.getIdx());
        verify(mockTravelRepository, atLeastOnce()).findOneTravelGroup(newTravelGroupDTO.getIdx());
        verifyNoMoreInteractions(mockTravelRepository);

        InOrder inOrder = inOrder(mockTravelRepository);
        inOrder.verify(mockTravelRepository).findOneTravelGroup(newTravelGroupDTO.getIdx());
    }

    @Test
    @DisplayName("여행지 그룹 수정 Mockito 테스트")
    void 여행지그룹수정Mockito테스트() {
        // given
        TravelGroupEntity travelGroupEntity = TravelGroupEntity.builder()
                .travelIdx(1L).groupName("서울모임").groupDescription("서울모임").visible("Y").build();

        TravelGroupDTO travelGroupDTO = travelRepository.insertTravelGroup(travelGroupEntity);

        TravelGroupEntity newTravelGroupEntity = TravelGroupEntity.builder()
                .idx(travelGroupDTO.getIdx())
                .travelIdx(1L).groupName("인천모임").groupDescription("인천모임")
                .visible("Y").build();

        travelRepository.updateTravelGroup(newTravelGroupEntity);

        TravelGroupDTO newTravelGroupDTO = TravelGroupMapper.INSTANCE.toDto(newTravelGroupEntity);

        // when
        when(mockTravelRepository.findOneTravelGroup(newTravelGroupEntity.getIdx())).thenReturn(newTravelGroupDTO);
        TravelGroupDTO travelGroupInfo = mockTravelRepository.findOneTravelGroup(newTravelGroupDTO.getIdx());

        // then
        assertThat(travelGroupInfo.getIdx()).isEqualTo(newTravelGroupDTO.getIdx());
        assertThat(travelGroupInfo.getGroupName()).isEqualTo(newTravelGroupDTO.getGroupName());
        assertThat(travelGroupInfo.getGroupDescription()).isEqualTo(newTravelGroupDTO.getGroupDescription());

        // verify
        verify(mockTravelRepository, times(1)).findOneTravelGroup(travelGroupInfo.getIdx());
        verify(mockTravelRepository, atLeastOnce()).findOneTravelGroup(travelGroupInfo.getIdx());
        verifyNoMoreInteractions(mockTravelRepository);

        InOrder inOrder = inOrder(mockTravelRepository);
        inOrder.verify(mockTravelRepository).findOneTravelGroup(travelGroupInfo.getIdx());
    }

    @Test
    @DisplayName("여행지 그룹 삭제 Mockito 테스트")
    void 여행지그룹삭제Mockito테스트() {
        // given
        TravelGroupEntity travelGroupEntity = TravelGroupEntity.builder()
                .travelIdx(1L).groupName("서울모임").groupDescription("서울모임").visible("Y").build();
        em.persist(travelGroupEntity);

        TravelGroupDTO travelGroupDTO = TravelGroupMapper.INSTANCE.toDto(travelGroupEntity);

        // when
        when(mockTravelRepository.findOneTravelGroup(travelGroupDTO.getIdx())).thenReturn(travelGroupDTO);
        Long deleteIdx = travelRepository.deleteTravelGroup(travelGroupDTO.getIdx());

        // then
        assertThat(mockTravelRepository.findOneTravelGroup(travelGroupDTO.getIdx()).getIdx()).isEqualTo(deleteIdx);

        // verify
        verify(mockTravelRepository, times(1)).findOneTravelGroup(travelGroupDTO.getIdx());
        verify(mockTravelRepository, atLeastOnce()).findOneTravelGroup(travelGroupDTO.getIdx());
        verifyNoMoreInteractions(mockTravelRepository);

        InOrder inOrder = inOrder(mockTravelRepository);
        inOrder.verify(mockTravelRepository).findOneTravelGroup(travelGroupDTO.getIdx());
    }

    @Test
    @DisplayName("유저 여행 그룹 등록 Mockito 테스트")
    void 유저여행그룹등록Mockito테스트() {
        // given
        TravelGroupEntity travelGroupEntity = TravelGroupEntity.builder()
                .travelIdx(1L).groupName("서울모임").groupDescription("서울모임").visible("Y").build();
        em.persist(travelGroupEntity);

        TravelGroupDTO travelGroupDTO = TravelGroupMapper.INSTANCE.toDto(travelGroupEntity);

        TravelGroupUserEntity travelGroupUserEntity = TravelGroupUserEntity.builder()
                .userIdx(1L).groupIdx(travelGroupDTO.getIdx()).build();

        TravelGroupUserDTO travelGroupUserInfo = travelRepository.insertTravelGroupUser(travelGroupUserEntity);

        // then
        assertThat(travelGroupUserInfo.getGroupIdx()).isEqualTo(travelGroupDTO.getIdx());
        assertThat(travelGroupUserInfo.getUserIdx()).isEqualTo(1L);
    }

    @Test
    @DisplayName("유저 여행 그룹 삭제 Mockito 테스트")
    void 유저여행그룹삭제Mockito테스트() {
        // given
        TravelGroupEntity travelGroupEntity = TravelGroupEntity.builder()
                .travelIdx(1L).groupName("서울모임").groupDescription("서울모임").visible("Y").build();
        em.persist(travelGroupEntity);

        TravelGroupDTO travelGroupDTO = TravelGroupMapper.INSTANCE.toDto(travelGroupEntity);

        TravelGroupUserEntity travelGroupUserEntity = TravelGroupUserEntity.builder()
                .userIdx(1L).groupIdx(travelGroupDTO.getIdx()).build();

        TravelGroupUserDTO travelGroupUserInfo = travelRepository.insertTravelGroupUser(travelGroupUserEntity);

        Long deleteIdx = travelRepository.deleteTravelGroupUser(travelGroupUserInfo.getIdx());

        // then
        assertThat(deleteIdx).isEqualTo(travelGroupUserInfo.getIdx());
    }
}
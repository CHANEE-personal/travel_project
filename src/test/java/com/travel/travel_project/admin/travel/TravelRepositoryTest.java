package com.travel.travel_project.admin.travel;

import com.travel.travel_project.domain.travel.TravelDTO;
import com.travel.travel_project.domain.travel.TravelEntity;
import com.travel.travel_project.api.travel.TravelRepository;
import com.travel.travel_project.domain.travel.festival.TravelFestivalDTO;
import com.travel.travel_project.domain.travel.festival.TravelFestivalEntity;
import com.travel.travel_project.domain.travel.group.TravelGroupDTO;
import com.travel.travel_project.domain.travel.group.TravelGroupEntity;
import com.travel.travel_project.domain.travel.group.TravelGroupUserDTO;
import com.travel.travel_project.domain.travel.group.TravelGroupUserEntity;
import com.travel.travel_project.domain.travel.recommend.TravelRecommendDTO;
import com.travel.travel_project.domain.travel.recommend.TravelRecommendEntity;
import com.travel.travel_project.domain.travel.review.TravelReviewDTO;
import com.travel.travel_project.domain.travel.review.TravelReviewEntity;
import com.travel.travel_project.domain.travel.schedule.TravelScheduleDTO;
import com.travel.travel_project.domain.travel.schedule.TravelScheduleEntity;
import com.travel.travel_project.domain.travel.search.SearchEntity;
import com.travel.travel_project.exception.TravelException;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
@DisplayName("????????? ?????? Repository Test")
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
                .travelTitle("????????? ?????????").travelDescription("????????? ?????????").favoriteCount(1).viewCount(0)
                .travelAddress("??????????????? ??????").travelZipCode("123-456").visible("Y").popular(false)
                .build();

        travelDTO = TravelEntity.toDto(travelEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createTravel();
    }

    @Test
    @DisplayName("???????????????????????????????????????")
    void ???????????????????????????????????????() {
        // given
        Map<String, Object> travelMap = new HashMap<>();
        travelMap.put("jpaStartPage", 0);
        travelMap.put("size", 3);
        travelMap.put("searchKeyword", "??????");

        // then
        assertThat(travelRepository.findTravelList(travelMap)).isEmpty();
        travelRepository.findTravelList(travelMap);
        travelRepository.findTravelList(travelMap);

        travelMap.put("searchKeyword", "??????");
        travelRepository.findTravelList(travelMap);

        assertThat(travelRepository.rankingTravelKeyword().get(0).getSearchKeyword()).isEqualTo("??????");
        assertThat(travelRepository.rankingTravelKeyword().get(1).getSearchKeyword()).isEqualTo("??????");
    }

    @Test
    @DisplayName("??????????????????????????????")
    void ??????????????????????????????() {
        TravelDTO existTravel = travelRepository.findOneTravel(1L);
        assertThat(existTravel.getIdx()).isEqualTo(1L);
        assertThat(existTravel.getTravelCode()).isEqualTo(1);
        assertThat(existTravel.getTravelTitle()).isEqualTo("?????? ?????????");

        assertThatThrownBy(() -> travelRepository.findOneTravel(3L))
                .isInstanceOf(TravelException.class).hasMessage("?????? ?????? ??????");
    }

    @Test
    @DisplayName("????????? ?????? ????????? Mockito ?????? ?????? ?????????")
    void ????????????????????????Mockito?????????????????????() {
        // given
        Map<String, Object> travelMap = new HashMap<>();
        travelMap.put("jpaStartPage", 1);
        travelMap.put("size", 3);
        travelMap.put("searchCode", 1);

        List<TravelDTO> travelList = new ArrayList<>();
        travelList.add(TravelDTO.builder().idx(1L).travelCode(1)
                .travelTitle("????????? ??????").travelDescription("????????? ??????")
                .travelAddress("??????????????? ??????").travelZipCode("123-456").visible("Y").build());

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
    @DisplayName("????????? ?????? ????????? Mockito ?????? ?????????")
    void ????????????????????????Mockito???????????????() {
        // given
        Map<String, Object> travelMap = new HashMap<>();
        travelMap.put("jpaStartPage", 1);
        travelMap.put("size", 3);

        List<TravelDTO> travelList = new ArrayList<>();
        travelList.add(TravelDTO.builder().idx(1L).travelCode(1)
                .travelTitle("????????? ??????").travelDescription("????????? ??????")
                .travelAddress("??????????????? ??????").travelZipCode("123-456").visible("Y").build());

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
    @DisplayName("????????? ?????? ????????? BDD ?????? ?????????")
    void ????????????????????????BDD???????????????() {
        // given
        Map<String, Object> travelMap = new HashMap<>();
        travelMap.put("jpaStartPage", 1);
        travelMap.put("size", 3);

        List<TravelDTO> travelList = new ArrayList<>();
        travelList.add(TravelDTO.builder().idx(1L).travelCode(1)
                .travelTitle("????????? ??????").travelDescription("????????? ??????")
                .travelAddress("??????????????? ??????").travelZipCode("123-456").visible("Y").build());

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
    @DisplayName("????????? ?????? ?????? Mockito ?????????")
    void ?????????????????????Mockito?????????() {
        // given
        travelEntity = TravelEntity.builder()
                .idx(1L)
                .travelCode(1)
                .travelTitle("????????? ?????????").travelDescription("????????? ?????????")
                .travelAddress("??????????????? ??????").travelZipCode("123-456").visible("Y")
                .build();

        travelDTO = TravelEntity.toDto(travelEntity);

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
    @DisplayName("????????? ?????? ?????? BDD ?????????")
    void ?????????????????????BDD?????????() {
        // given
        travelEntity = TravelEntity.builder()
                .idx(1L)
                .travelCode(1)
                .travelTitle("????????? ?????????").travelDescription("????????? ?????????")
                .travelAddress("??????????????? ??????").travelZipCode("123-456").visible("Y")
                .build();

        travelDTO = TravelEntity.toDto(travelEntity);

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
    @DisplayName("?????? ????????? ?????? ?????? Mockito ?????????")
    void ???????????????????????????Mockito?????????() {
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
    @DisplayName("?????? ????????? ?????? ?????? BDD ?????????")
    void ???????????????????????????BDD?????????() {
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
    @DisplayName("?????? ????????? ?????? ?????? Mockito ?????????")
    void ???????????????????????????Mockito?????????() {
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
    @DisplayName("????????? ?????? Mockito ?????????")
    void ???????????????Mockito?????????() {
        // given
        travelEntity = TravelEntity.builder()
                .travelCode(1)
                .travelTitle("????????? ?????????").travelDescription("????????? ?????????").favoriteCount(0).viewCount(0)
                .travelAddress("??????????????? ??????").travelZipCode("123-456").visible("Y")
                .build();

        TravelDTO travelInfo = travelRepository.insertTravel(travelEntity);
        travelRepository.findOneTravel(travelEntity.getIdx());

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
    @DisplayName("????????? ?????? BDD ?????????")
    void ???????????????BDD?????????() {
        // given
        travelEntity = TravelEntity.builder()
                .travelCode(1)
                .travelTitle("????????? ?????????").travelDescription("????????? ?????????").favoriteCount(0).viewCount(0)
                .travelAddress("??????????????? ??????").travelZipCode("123-456").visible("Y")
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
    @DisplayName("????????? ?????? Mockito ?????????")
    void ???????????????Mockito?????????() {
        // given
        travelEntity = TravelEntity.builder()
                .travelCode(1)
                .travelTitle("????????? ?????????").travelDescription("????????? ?????????").favoriteCount(0).viewCount(0)
                .travelAddress("??????????????? ??????").travelZipCode("123-456").visible("Y")
                .build();

        TravelDTO newTravelInfo = travelRepository.insertTravel(travelEntity);

        TravelEntity newTravelEntity = TravelEntity.builder()
                .idx(newTravelInfo.getIdx())
                .travelCode(1)
                .travelTitle("????????? ?????? ?????????").travelDescription("????????? ?????? ?????????").favoriteCount(0).viewCount(0)
                .travelAddress("??????????????? ?????????").travelZipCode("123-456")
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
    @DisplayName("????????? ?????? BDD ?????????")
    void ???????????????BDD?????????() {
        // given
        travelEntity = TravelEntity.builder()
                .travelCode(1)
                .travelTitle("????????? ?????????").travelDescription("????????? ?????????").favoriteCount(0).viewCount(0)
                .travelAddress("??????????????? ??????").travelZipCode("123-456").visible("Y")
                .build();

        TravelDTO newTravelDTO = travelRepository.insertTravel(travelEntity);

        TravelEntity newAdminTravelEntity = TravelEntity.builder()
                .idx(newTravelDTO.getIdx())
                .travelCode(1)
                .travelTitle("????????? ?????? ?????????").travelDescription("????????? ?????? ?????????").favoriteCount(0).viewCount(0)
                .travelAddress("??????????????? ?????????").travelZipCode("123-456")
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
    @DisplayName("????????? ?????? Mockito ?????????")
    void ???????????????Mockito?????????() {
        // given
        em.persist(travelEntity);
        travelDTO = TravelEntity.toDto(travelEntity);

        // when
        when(mockTravelRepository.findOneTravel(travelEntity.getIdx())).thenReturn(travelDTO);
        Long deleteIdx = travelRepository.deleteTravel(travelEntity.getIdx());

        System.out.println("===deleteIdx===");
        System.out.println(deleteIdx);

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
    @DisplayName("????????? ?????? BDD ?????????")
    void ???????????????BDD?????????() {
        // given
        em.persist(travelEntity);
        travelDTO = TravelEntity.toDto(travelEntity);

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
    @DisplayName("??????????????????Mockito?????????")
    void ??????????????????Mockito?????????() {
        // given
        travelEntity = TravelEntity.builder()
                .travelCode(1)
                .travelTitle("????????? ?????????").travelDescription("????????? ?????????").favoriteCount(0).viewCount(0)
                .travelAddress("??????????????? ??????").travelZipCode("123-456").visible("Y")
                .build();

        TravelDTO travelDTO = travelRepository.insertTravel(travelEntity);

        // when
        // ????????? ??? ??????
        Integer favoriteCount = travelRepository.favoriteTravel(travelDTO.getIdx());
        System.out.println("===favoriteCount===");
        System.out.println(favoriteCount);
        when(mockTravelRepository.favoriteTravel(travelDTO.getIdx())).thenReturn(favoriteCount);

        // then
        // ????????? ????????? ??? ??????
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
    @DisplayName("??????????????????BDD?????????")
    void ??????????????????BDD?????????() {
        // given
        travelEntity = TravelEntity.builder()
                .travelCode(1)
                .travelTitle("????????? ?????????").travelDescription("????????? ?????????").favoriteCount(1).viewCount(0)
                .travelAddress("??????????????? ??????").travelZipCode("123-456").visible("Y")
                .build();

        TravelDTO travelDTO = travelRepository.insertTravel(travelEntity);

        // when
        // ????????? ??? ??????
        Integer favoriteCount = travelRepository.favoriteTravel(travelDTO.getIdx());
        given(mockTravelRepository.favoriteTravel(travelDTO.getIdx())).willReturn(favoriteCount);

        // then
        // ????????? ????????? ??? ??????
        Integer addFavoriteCount = mockTravelRepository.favoriteTravel(travelDTO.getIdx());
        assertThat(favoriteCount).isEqualTo(addFavoriteCount);

        // verify
        then(mockTravelRepository).should(times(1)).favoriteTravel(travelDTO.getIdx());
        then(mockTravelRepository).should(atLeastOnce()).favoriteTravel(travelDTO.getIdx());
        then(mockTravelRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("?????????????????????Mockito?????????")
    void ?????????????????????Mockito?????????() {
        // given
        Long idx = travelRepository.insertTravel(travelEntity).getIdx();

        Boolean popular = travelRepository.togglePopular(idx);

        travelEntity = TravelEntity.builder()
                .travelCode(1)
                .travelTitle("????????? ?????????").travelDescription("????????? ?????????").favoriteCount(1).viewCount(0)
                .travelAddress("??????????????? ??????").travelZipCode("123-456").visible("Y").popular(popular)
                .build();

        travelDTO = TravelEntity.toDto(travelEntity);

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
    @DisplayName("?????????????????????BDD?????????")
    void ?????????????????????BDD?????????() {
        // given
        Long idx = travelRepository.insertTravel(travelEntity).getIdx();

        Boolean popular = travelRepository.togglePopular(idx);

        travelEntity = TravelEntity.builder()
                .travelCode(1)
                .travelTitle("????????? ?????????").travelDescription("????????? ?????????").favoriteCount(1).viewCount(0)
                .travelAddress("??????????????? ??????").travelZipCode("123-456").visible("Y").popular(popular)
                .build();

        travelDTO = TravelEntity.toDto(travelEntity);

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
    @DisplayName("?????????????????????Mockito?????????")
    void ?????????????????????Mockito?????????() {
        // given
        TravelDTO travelInfo = travelRepository.insertTravel(travelEntity);

        TravelReviewEntity travelReviewEntity = TravelReviewEntity.builder()
                .travelIdx(travelInfo.getIdx())
                .reviewTitle("?????????????????????")
                .reviewDescription("?????????????????????")
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
                .travelTitle("????????? ?????????").travelDescription("????????? ?????????").favoriteCount(1).viewCount(0)
                .travelAddress("??????????????? ??????").travelZipCode("123-456").visible("Y").popular(false)
                .reviewList(reviewList)
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
    @DisplayName("?????????????????????Mockito?????????")
    void ?????????????????????Mockito?????????() {
        // given
        TravelDTO travelInfo = travelRepository.insertTravel(travelEntity);

        // ?????? ??????
        TravelReviewEntity travelReviewEntity = TravelReviewEntity.builder()
                .travelIdx(travelInfo.getIdx())
                .reviewTitle("?????????????????????")
                .reviewDescription("?????????????????????")
                .viewCount(0)
                .favoriteCount(0)
                .popular(false)
                .visible("Y")
                .build();

        TravelReviewDTO reviewDTO = travelRepository.replyTravel(travelReviewEntity);

        // ?????? ??????
        travelReviewEntity = TravelReviewEntity.builder()
                .idx(reviewDTO.getIdx())
                .travelIdx(travelInfo.getIdx())
                .reviewTitle("?????????????????????")
                .reviewDescription("?????????????????????")
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
                .travelTitle("????????? ?????????").travelDescription("????????? ?????????").favoriteCount(1).viewCount(0)
                .travelAddress("??????????????? ??????").travelZipCode("123-456").visible("Y").popular(false)
                .reviewList(reviewList)
                .build();

        // when
        when(mockTravelRepository.replyTravelReview(travelDTO.getIdx())).thenReturn(reviewList);
        List<TravelReviewDTO> reviewDTOList = mockTravelRepository.replyTravelReview(travelDTO.getIdx());

        // then
        assertThat(reviewDTOList.get(0).getReviewTitle()).isEqualTo("?????????????????????");
        assertThat(reviewDTOList.get(0).getReviewDescription()).isEqualTo("?????????????????????");

        // verify
        verify(mockTravelRepository, times(1)).replyTravelReview(travelDTO.getIdx());
        verify(mockTravelRepository, atLeastOnce()).replyTravelReview(travelDTO.getIdx());
        verifyNoMoreInteractions(mockTravelRepository);

        InOrder inOrder = inOrder(mockTravelRepository);
        inOrder.verify(mockTravelRepository).replyTravelReview(travelDTO.getIdx());
    }

    @Test
    @DisplayName("????????? ?????? ?????? Mockito ?????????")
    void ?????????????????????Mockito?????????() {
        // given
        // ????????? ??????
        TravelDTO travelInfo = travelRepository.insertTravel(travelEntity);
        // ????????? ?????? ??????
        TravelReviewEntity travelReviewEntity = TravelReviewEntity.builder()
                .travelIdx(travelInfo.getIdx())
                .reviewTitle("?????????????????????")
                .reviewDescription("?????????????????????")
                .viewCount(0)
                .favoriteCount(0)
                .popular(false)
                .visible("Y")
                .build();
        TravelReviewDTO travelReviewDTO = travelRepository.replyTravel(travelReviewEntity);
        // ????????? ?????? ??????
        Long deleteIdx = travelRepository.deleteReplyTravel(travelReviewDTO.getIdx());

        // then
        assertThat(travelReviewDTO.getIdx()).isEqualTo(deleteIdx);
    }

    @Test
    @DisplayName("????????? ?????? ????????? ?????? Mockito ?????????")
    void ??????????????????????????????Mockito?????????() {
        // given
        TravelDTO travelInfo = travelRepository.insertTravel(travelEntity);

        TravelReviewEntity travelReviewEntity = TravelReviewEntity.builder()
                .travelIdx(travelInfo.getIdx())
                .reviewTitle("?????????????????????")
                .reviewDescription("?????????????????????")
                .viewCount(0)
                .favoriteCount(0)
                .popular(false)
                .visible("Y")
                .build();

        travelRepository.replyTravel(travelReviewEntity);
        List<TravelReviewDTO> reviewList = travelRepository.replyTravelReview(travelInfo.getIdx());

        travelDTO = TravelDTO.builder()
                .travelCode(1)
                .travelTitle("????????? ?????????").travelDescription("????????? ?????????").favoriteCount(1).viewCount(0)
                .travelAddress("??????????????? ??????").travelZipCode("123-456").visible("Y").popular(false)
                .reviewList(reviewList)
                .build();

        // when
        when(mockTravelRepository.replyTravelReview(travelInfo.getIdx())).thenReturn(reviewList);

        // then
        assertThat(mockTravelRepository.replyTravelReview(travelInfo.getIdx()).get(0).getTravelIdx()).isEqualTo(travelInfo.getIdx());
        assertThat(mockTravelRepository.replyTravelReview(travelInfo.getIdx()).get(0).getReviewTitle()).isEqualTo("?????????????????????");
        assertThat(mockTravelRepository.replyTravelReview(travelInfo.getIdx()).get(0).getReviewDescription()).isEqualTo("?????????????????????");
    }

    @Test
    @DisplayName("????????? ?????? ?????? ?????? Mockito ?????????")
    void ???????????????????????????Mockito?????????() {
        // given
        TravelDTO travelInfo = travelRepository.insertTravel(travelEntity);

        TravelReviewEntity travelReviewEntity = TravelReviewEntity.builder()
                .travelIdx(travelInfo.getIdx())
                .reviewTitle("?????????????????????")
                .reviewDescription("?????????????????????")
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
        assertThat(travelReviewInfo.getReviewTitle()).isEqualTo("?????????????????????");
        assertThat(travelReviewInfo.getReviewDescription()).isEqualTo("?????????????????????");

        // verify
        verify(mockTravelRepository, times(1)).detailReplyTravelReview(travelReviewInfo.getIdx());
        verify(mockTravelRepository, atLeastOnce()).detailReplyTravelReview(travelReviewInfo.getIdx());
        verifyNoMoreInteractions(mockTravelRepository);

        InOrder inOrder = inOrder(mockTravelRepository);
        inOrder.verify(mockTravelRepository).detailReplyTravelReview(travelReviewInfo.getIdx());
    }

    @Test
    @DisplayName("????????? ?????? ????????? Mockito ?????? ?????????")
    void ????????????????????????Mockito???????????????() {
        // given
        Map<String, Object> groupMap = new HashMap<>();
        groupMap.put("jpaStartPage", 1);
        groupMap.put("size", 3);

        List<TravelGroupDTO> travelGroupList = new ArrayList<>();
        travelGroupList.add(TravelGroupDTO.builder().travelIdx(1L).groupName("????????????")
                .groupDescription("????????????").visible("Y").build());

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
    @DisplayName("????????? ?????? ?????? Mockito ?????????")
    void ?????????????????????Mockito?????????() {
        // given
        TravelGroupDTO travelGroupDTO = TravelGroupDTO.builder()
                .idx(1L).travelIdx(1L).groupName("????????????").groupDescription("????????????").visible("Y").build();

        // when
        given(mockTravelRepository.findOneTravelGroup(1L)).willReturn(travelGroupDTO);
        TravelGroupDTO newTravelGroupDTO = mockTravelRepository.findOneTravelGroup(1L);

        // then
        assertThat(newTravelGroupDTO.getIdx()).isEqualTo(1L);
        assertThat(newTravelGroupDTO.getTravelIdx()).isEqualTo(1L);
        assertThat(newTravelGroupDTO.getGroupName()).isEqualTo("????????????");
        assertThat(newTravelGroupDTO.getGroupDescription()).isEqualTo("????????????");

        // verify
        verify(mockTravelRepository, times(1)).findOneTravelGroup(1L);
        verify(mockTravelRepository, atLeastOnce()).findOneTravelGroup(1L);
        verifyNoMoreInteractions(mockTravelRepository);

        InOrder inOrder = inOrder(mockTravelRepository);
        inOrder.verify(mockTravelRepository).findOneTravelGroup(1L);
    }

    @Test
    @DisplayName("????????? ?????? ?????? Mockito ?????????")
    void ?????????????????????Mockito?????????() {
        TravelGroupEntity travelGroupEntity = TravelGroupEntity.builder()
                .travelIdx(1L).groupName("????????????").groupDescription("????????????").visible("Y").build();

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
    @DisplayName("????????? ?????? ?????? Mockito ?????????")
    void ?????????????????????Mockito?????????() {
        // given
        TravelGroupEntity travelGroupEntity = TravelGroupEntity.builder()
                .travelIdx(1L).groupName("????????????").groupDescription("????????????").visible("Y").build();

        TravelGroupDTO travelGroupDTO = travelRepository.insertTravelGroup(travelGroupEntity);

        TravelGroupEntity newTravelGroupEntity = TravelGroupEntity.builder()
                .idx(travelGroupDTO.getIdx())
                .travelIdx(1L).groupName("????????????").groupDescription("????????????")
                .visible("Y").build();

        travelRepository.updateTravelGroup(newTravelGroupEntity);

        TravelGroupDTO newTravelGroupDTO = TravelGroupEntity.toDto(newTravelGroupEntity);

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
    @DisplayName("????????? ?????? ?????? Mockito ?????????")
    void ?????????????????????Mockito?????????() {
        // given
        TravelGroupEntity travelGroupEntity = TravelGroupEntity.builder()
                .travelIdx(1L).groupName("????????????").groupDescription("????????????").visible("Y").build();
        em.persist(travelGroupEntity);

        TravelGroupDTO travelGroupDTO = TravelGroupEntity.toDto(travelGroupEntity);

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
    @DisplayName("?????? ?????? ?????? ?????? Mockito ?????????")
    void ????????????????????????Mockito?????????() {
        // given
        TravelGroupEntity travelGroupEntity = TravelGroupEntity.builder()
                .travelIdx(1L).groupName("????????????").groupDescription("????????????").visible("Y").build();
        em.persist(travelGroupEntity);

        TravelGroupDTO travelGroupDTO = TravelGroupEntity.toDto(travelGroupEntity);

        TravelGroupUserEntity travelGroupUserEntity = TravelGroupUserEntity.builder()
                .userIdx(1L).groupIdx(travelGroupDTO.getIdx()).build();

        TravelGroupUserDTO travelGroupUserInfo = travelRepository.insertTravelGroupUser(travelGroupUserEntity);

        // then
        assertThat(travelGroupUserInfo.getGroupIdx()).isEqualTo(travelGroupDTO.getIdx());
        assertThat(travelGroupUserInfo.getUserIdx()).isEqualTo(1L);
    }

    @Test
    @DisplayName("?????? ?????? ?????? ?????? Mockito ?????????")
    void ????????????????????????Mockito?????????() {
        // given
        TravelGroupEntity travelGroupEntity = TravelGroupEntity.builder()
                .travelIdx(1L).groupName("????????????").groupDescription("????????????").visible("Y").build();
        em.persist(travelGroupEntity);

        TravelGroupDTO travelGroupDTO = TravelGroupEntity.toDto(travelGroupEntity);

        TravelGroupUserEntity travelGroupUserEntity = TravelGroupUserEntity.builder()
                .userIdx(1L).groupIdx(travelGroupDTO.getIdx()).build();

        TravelGroupUserDTO travelGroupUserInfo = travelRepository.insertTravelGroupUser(travelGroupUserEntity);

        Long deleteIdx = travelRepository.deleteTravelGroupUser(travelGroupUserInfo.getIdx());

        // then
        assertThat(deleteIdx).isEqualTo(travelGroupUserInfo.getIdx());
    }

    @Test
    @DisplayName("?????? ?????? ????????? ?????? ?????????")
    void ????????????????????????????????????() {
        TravelScheduleEntity travelScheduleEntity = TravelScheduleEntity.builder()
                .travelIdx(1L)
                .userIdx(1L)
                .scheduleDescription("????????? ?????????")
                .scheduleTime(LocalDateTime.now())
                .build();

        TravelScheduleDTO travelScheduleDTO = travelRepository.insertTravelSchedule(travelScheduleEntity);

        assertThat(travelScheduleDTO.getTravelIdx()).isEqualTo(1L);
        assertThat(travelScheduleDTO.getUserIdx()).isEqualTo(1L);
        assertThat(travelScheduleDTO.getScheduleDescription()).isEqualTo("????????? ?????????");
    }

    @Test
    @DisplayName("?????? ?????? ????????? ?????? ?????????")
    void ????????????????????????????????????() {
        TravelScheduleEntity travelScheduleEntity = TravelScheduleEntity.builder()
                .travelIdx(1L)
                .userIdx(1L)
                .scheduleDescription("????????? ?????????")
                .scheduleTime(LocalDateTime.now())
                .build();

        TravelScheduleDTO travelScheduleDTO = travelRepository.insertTravelSchedule(travelScheduleEntity);

        TravelScheduleEntity updateTravelScheduleEntity = TravelScheduleEntity.builder()
                .idx(travelScheduleDTO.getIdx())
                .travelIdx(travelScheduleDTO.getTravelIdx())
                .userIdx(travelScheduleDTO.getUserIdx())
                .scheduleDescription("????????? ?????? ?????????")
                .scheduleTime(LocalDateTime.now())
                .build();

        TravelScheduleDTO updateScheduleDTO = travelRepository.updateTravelSchedule(updateTravelScheduleEntity);

        assertThat(updateScheduleDTO.getScheduleDescription()).isEqualTo("????????? ?????? ?????????");
    }

    @Test
    @DisplayName("?????? ?????? ????????? ?????? ?????????")
    void ????????????????????????????????????() {
        TravelScheduleEntity travelScheduleEntity = TravelScheduleEntity.builder()
                .travelIdx(1L)
                .userIdx(1L)
                .scheduleDescription("????????? ?????????")
                .scheduleTime(LocalDateTime.now())
                .build();

        TravelScheduleDTO travelScheduleDTO = travelRepository.insertTravelSchedule(travelScheduleEntity);

        Long deleteIdx = travelRepository.deleteTravelSchedule(travelScheduleDTO.getIdx());

        assertThat(deleteIdx).isEqualTo(travelScheduleDTO.getIdx());
    }

    @Test
    @DisplayName("????????? ?????? ????????? ????????? ?????? ?????????")
    void ????????????????????????????????????????????????() {
        Map<String, Object> travelRecommendMap = new HashMap<>();
        travelRecommendMap.put("jpaStartPage", 0);
        travelRecommendMap.put("size", 3);
        List<String> list = new ArrayList<>();
        list.add("??????");
        list.add("??????");

        TravelRecommendEntity recommendEntity = TravelRecommendEntity.builder()
                .recommendName(list)
                .build();

        travelRepository.changeTravelRecommend(recommendEntity);

        assertThat(travelRepository.findTravelRecommendList(travelRecommendMap)).isNotEmpty();
    }

    @Test
    @DisplayName("????????? ?????? ????????? ?????? ?????? ?????????")
    void ?????????????????????????????????????????????() {
        List<String> list = new ArrayList<>();
        list.add("??????");
        list.add("??????");

        TravelRecommendEntity recommendEntity = TravelRecommendEntity.builder()
                .recommendName(list)
                .build();

        TravelRecommendDTO travelRecommendDTO = travelRepository.changeTravelRecommend(recommendEntity);

        TravelRecommendDTO oneTravelRecommend = travelRepository.findOneTravelRecommend(travelRecommendDTO.getIdx());
        assertThat(oneTravelRecommend.getRecommendName()).isEqualTo(list);
    }

    @Test
    @DisplayName("????????? ?????? ????????? ?????? ?????????")
    void ???????????????????????????????????????() throws JSONException {
        List<String> list = new ArrayList<>();
        list.add("??????");
        list.add("??????");

        TravelRecommendEntity recommendEntity = TravelRecommendEntity.builder()
                .recommendName(list)
                .build();

        TravelRecommendDTO travelRecommendDTO = travelRepository.changeTravelRecommend(recommendEntity);

        JSONArray jsonArray = new JSONArray();
        jsonArray.put(travelRecommendDTO.getRecommendName());
        Object insertObject = jsonArray.get(0);

        assertThat(travelRecommendDTO.getRecommendName()).isEqualTo(insertObject);
    }

    @Test
    @DisplayName("????????? ?????? ????????? ?????? ?????????")
    void ???????????????????????????????????????() throws JSONException {
        List<String> list = new ArrayList<>();
        list.add("??????");
        list.add("??????");

        TravelRecommendEntity recommendEntity = TravelRecommendEntity.builder()
                .recommendName(list)
                .build();

        TravelRecommendDTO travelRecommendDTO = travelRepository.changeTravelRecommend(recommendEntity);

        list.add("??????");
        recommendEntity = TravelRecommendEntity.builder()
                .idx(travelRecommendDTO.getIdx())
                .recommendName(list)
                .build();
        em.flush();
        em.clear();

        TravelRecommendDTO updateRecommendDTO = travelRepository.changeTravelRecommend(recommendEntity);

        JSONArray jsonArray = new JSONArray();
        jsonArray.put(updateRecommendDTO.getRecommendName());
        Object insertObject = jsonArray.get(0);

        assertThat(updateRecommendDTO.getRecommendName()).isEqualTo(insertObject);
    }

    @Test
    @DisplayName("????????? ?????? ????????? ?????? ?????????")
    void ???????????????????????????????????????() {
        List<String> list = new ArrayList<>();
        list.add("??????");
        list.add("??????");

        TravelRecommendEntity recommendEntity = TravelRecommendEntity.builder()
                .recommendName(list)
                .build();

        TravelRecommendDTO travelRecommendDTO = travelRepository.changeTravelRecommend(recommendEntity);

        Long deleteIdx = travelRepository.deleteTravelRecommend(travelRecommendDTO.getIdx());
        em.flush();
        em.clear();

        assertThat(deleteIdx).isEqualTo(travelRecommendDTO.getIdx());
    }

    @Test
    @DisplayName("????????? ?????? ????????? ?????? ?????????")
    void ???????????????????????????????????????() {
        em.persist(SearchEntity.builder().searchKeyword("??????").build());
        em.persist(SearchEntity.builder().searchKeyword("??????").build());
        em.persist(SearchEntity.builder().searchKeyword("??????").build());
        em.persist(SearchEntity.builder().searchKeyword("??????").build());

        assertThat(travelRepository.rankingTravelKeyword().get(0).getSearchKeyword()).isEqualTo("??????");
        assertThat(travelRepository.rankingTravelKeyword().get(1).getSearchKeyword()).isEqualTo("??????");
    }

    @Test
    @DisplayName("?????? ????????? or ????????? ????????? ?????? ????????? ?????? ??????")
    void ???????????????or?????????????????????????????????????????????() {
        assertThat(travelRepository.findTravelKeyword("??????").get(0).getTravelTitle()).isEqualTo("?????? ?????????");
    }

    @Test
    @DisplayName("?????? ????????? ?????? ?????? ??????")
    void ?????????????????????????????????() {
        // ??????
        LocalDateTime dateTime = LocalDateTime.now();

        TravelFestivalEntity travelFestivalEntity = TravelFestivalEntity.builder()
                .travelCode(1)
                .festivalTitle("?????? ??????")
                .festivalDescription("?????? ??????")
                .festivalMonth(dateTime.getMonthValue())
                .festivalDay(dateTime.getDayOfMonth())
                .festivalTime(dateTime)
                .build();

        em.persist(travelFestivalEntity);

        TravelFestivalEntity travelFestivalEntity1 = TravelFestivalEntity.builder()
                .travelCode(2)
                .festivalTitle("?????? ??????")
                .festivalDescription("?????? ??????")
                .festivalMonth(dateTime.getMonthValue())
                .festivalDay(dateTime.getDayOfMonth())
                .festivalTime(dateTime)
                .build();

        em.persist(travelFestivalEntity1);

        TravelFestivalEntity travelFestivalEntity2 = TravelFestivalEntity.builder()
                .travelCode(2)
                .festivalTitle("?????? ??????")
                .festivalDescription("?????? ??????")
                .festivalMonth(dateTime.getMonthValue())
                .festivalDay(dateTime.getDayOfMonth()+1)
                .festivalTime(dateTime)
                .build();

        em.persist(travelFestivalEntity2);

        em.flush();
        em.clear();

        assertThat(travelRepository.findTravelFestivalGroup(dateTime.getMonthValue())).isNotEmpty();
    }

    @Test
    @DisplayName("?????????????????????")
    void ?????????????????????() {
        // ??????
        LocalDateTime dateTime = LocalDateTime.now();

        TravelFestivalEntity travelFestivalEntity = TravelFestivalEntity.builder()
                .travelCode(1)
                .festivalTitle("?????? ??????")
                .festivalDescription("?????? ??????")
                .festivalMonth(dateTime.getMonthValue())
                .festivalDay(dateTime.getDayOfMonth())
                .festivalTime(dateTime)
                .build();

        em.persist(travelFestivalEntity);

        TravelFestivalEntity travelFestivalEntity1 = TravelFestivalEntity.builder()
                .travelCode(2)
                .festivalTitle("?????? ??????")
                .festivalDescription("?????? ??????")
                .festivalMonth(dateTime.getMonthValue())
                .festivalDay(dateTime.getDayOfMonth())
                .festivalTime(dateTime)
                .build();

        em.persist(travelFestivalEntity1);

        em.flush();
        em.clear();

        assertThat(travelRepository.findTravelFestivalList(travelFestivalEntity)).isNotEmpty();
    }

    @Test
    @DisplayName("?????? ?????? ?????? ?????????")
    void ???????????????????????????() {
        // ??????
        LocalDateTime dateTime = LocalDateTime.now();

        TravelFestivalEntity travelFestivalEntity = TravelFestivalEntity.builder()
                .travelCode(1)
                .festivalTitle("?????? ??????")
                .festivalDescription("?????? ??????")
                .festivalMonth(dateTime.getMonthValue())
                .festivalDay(dateTime.getDayOfMonth())
                .festivalTime(dateTime)
                .build();

        TravelFestivalDTO travelFestivalDTO = travelRepository.changeTravelFestival(travelFestivalEntity);

        assertThat(travelRepository.findOneTravelFestival(travelFestivalDTO.getIdx()).getFestivalTitle()).isEqualTo("?????? ??????");
    }

    @Test
    @DisplayName("?????? ?????? or ?????? ?????????")
    void ????????????or???????????????() {
        // ??????
        LocalDateTime dateTime = LocalDateTime.now();

        TravelFestivalEntity travelFestivalEntity = TravelFestivalEntity.builder()
                .travelCode(1)
                .festivalTitle("?????? ??????")
                .festivalDescription("?????? ??????")
                .festivalMonth(dateTime.getMonthValue())
                .festivalDay(dateTime.getDayOfMonth())
                .festivalTime(dateTime)
                .build();

        TravelFestivalDTO travelFestivalDTO = travelRepository.changeTravelFestival(travelFestivalEntity);
        assertThat(travelFestivalDTO.getFestivalTitle()).isEqualTo("?????? ??????");

        travelFestivalEntity = TravelFestivalEntity.builder()
                .idx(travelFestivalDTO.getIdx())
                .travelCode(1)
                .festivalTitle("?????? ?????? ??????")
                .festivalDescription("?????? ?????? ??????")
                .festivalMonth(dateTime.getMonthValue())
                .festivalDay(dateTime.getDayOfMonth())
                .festivalTime(dateTime)
                .build();

        TravelFestivalDTO updateFestival = travelRepository.changeTravelFestival(travelFestivalEntity);
        assertThat(updateFestival.getFestivalTitle()).isEqualTo("?????? ?????? ??????");
    }

    @Test
    @DisplayName("?????? ?????? ?????????")
    void ?????????????????????() {
        // ??????
        LocalDateTime dateTime = LocalDateTime.now();

        TravelFestivalEntity travelFestivalEntity = TravelFestivalEntity.builder()
                .travelCode(1)
                .festivalTitle("?????? ??????")
                .festivalDescription("?????? ??????")
                .festivalMonth(dateTime.getMonthValue())
                .festivalDay(dateTime.getDayOfMonth())
                .festivalTime(dateTime)
                .build();

        TravelFestivalDTO travelFestivalDTO = travelRepository.changeTravelFestival(travelFestivalEntity);

        // ??????
        Long deleteIdx = travelRepository.deleteTravelFestival(travelFestivalDTO.getIdx());
        assertThat(deleteIdx).isEqualTo(travelFestivalDTO.getIdx());
    }
}
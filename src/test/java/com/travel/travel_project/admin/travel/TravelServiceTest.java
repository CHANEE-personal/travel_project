package com.travel.travel_project.admin.travel;

import com.travel.travel_project.domain.travel.TravelDTO;
import com.travel.travel_project.domain.travel.TravelEntity;
import com.travel.travel_project.api.travel.TravelService;
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

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@DisplayName("????????? Service Test")
class TravelServiceTest {
    @Mock
    private TravelService mockTravelService;
    private final TravelService travelService;
    private final EntityManager em;

    private TravelEntity travelEntity;
    private TravelDTO travelDTO;

    void createTravel() {
        travelEntity = TravelEntity.builder()
                .travelCode(1)
                .travelTitle("????????? ??????")
                .travelDescription("????????? ??????")
                .travelAddress("??????????????? ??????")
                .travelZipCode("123-456")
                .favoriteCount(1)
                .viewCount(0)
                .popular(false)
                .visible("Y")
                .build();

        travelDTO = TravelEntity.toDto(travelEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createTravel();
    }

    @Test
    @Disabled
    @DisplayName("???????????????????????????????????????")
    void ???????????????????????????????????????() {
        // given
        Map<String, Object> travelMap = new HashMap<>();
        travelMap.put("jpaStartPage", 1);
        travelMap.put("size", 3);

        // then
        assertThat(travelService.findTravelList(travelMap)).isNotEmpty();
    }

    @Test
    @DisplayName("????????????????????????Mockito???????????????")
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
        when(mockTravelService.findTravelList(travelMap)).thenReturn(travelList);
        List<TravelDTO> newTravelList = mockTravelService.findTravelList(travelMap);

        // then
        assertThat(newTravelList.get(0).getIdx()).isEqualTo(travelList.get(0).getIdx());
        assertThat(newTravelList.get(0).getTravelCode()).isEqualTo(travelList.get(0).getTravelCode());
        assertThat(newTravelList.get(0).getTravelTitle()).isEqualTo(travelList.get(0).getTravelTitle());
        assertThat(newTravelList.get(0).getTravelDescription()).isEqualTo(travelList.get(0).getTravelDescription());
        assertThat(newTravelList.get(0).getTravelAddress()).isEqualTo(travelList.get(0).getTravelAddress());
        assertThat(newTravelList.get(0).getTravelZipCode()).isEqualTo(travelList.get(0).getTravelZipCode());

        // verify
        verify(mockTravelService, times(1)).findTravelList(travelMap);
        verify(mockTravelService, atLeastOnce()).findTravelList(travelMap);
        verifyNoMoreInteractions(mockTravelService);

        InOrder inOrder = inOrder(mockTravelService);
        inOrder.verify(mockTravelService).findTravelList(travelMap);
    }

    @Test
    @DisplayName("????????????????????????BDD???????????????")
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
        given(mockTravelService.findTravelList(travelMap)).willReturn(travelList);
        List<TravelDTO> newTravelList = mockTravelService.findTravelList(travelMap);

        // then
        assertThat(newTravelList.get(0).getIdx()).isEqualTo(travelList.get(0).getIdx());
        assertThat(newTravelList.get(0).getTravelCode()).isEqualTo(travelList.get(0).getTravelCode());
        assertThat(newTravelList.get(0).getTravelTitle()).isEqualTo(travelList.get(0).getTravelTitle());
        assertThat(newTravelList.get(0).getTravelDescription()).isEqualTo(travelList.get(0).getTravelDescription());
        assertThat(newTravelList.get(0).getTravelAddress()).isEqualTo(travelList.get(0).getTravelAddress());
        assertThat(newTravelList.get(0).getTravelZipCode()).isEqualTo(travelList.get(0).getTravelZipCode());

        // verify
        then(mockTravelService).should(times(1)).findTravelList(travelMap);
        then(mockTravelService).should(atLeastOnce()).findTravelList(travelMap);
        then(mockTravelService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("??????????????????????????????")
    void ??????????????????????????????() {
        TravelDTO existTravel = travelService.findOneTravel(1L);
        assertThat(existTravel.getIdx()).isEqualTo(1L);
        assertThat(existTravel.getTravelCode()).isEqualTo(1);
        assertThat(existTravel.getTravelTitle()).isEqualTo("?????? ?????????");

        assertThatThrownBy(() -> travelService.findOneTravel(3L))
                .isInstanceOf(TravelException.class).hasMessage("?????? ?????? ??????");
    }

    @Test
    @DisplayName("?????????????????????Mockito?????????")
    void ?????????????????????Mockito?????????() {
        // given
        TravelDTO newTravel = travelService.insertTravel(travelEntity);

        // ?????? ??? ?????? ?????????
        TravelDTO oneTravel = travelService.findOneTravel(newTravel.getIdx());
        assertThat(newTravel.getViewCount() + 1).isEqualTo(oneTravel.getViewCount());

        // when
        when(mockTravelService.findOneTravel(1L)).thenReturn(travelDTO);
        TravelDTO newAdminTravel = mockTravelService.findOneTravel(1L);

        // then
        assertThat(newAdminTravel.getIdx()).isEqualTo(travelDTO.getIdx());
        assertThat(newAdminTravel.getTravelCode()).isEqualTo(travelDTO.getTravelCode());
        assertThat(newAdminTravel.getTravelTitle()).isEqualTo(travelDTO.getTravelTitle());
        assertThat(newAdminTravel.getTravelDescription()).isEqualTo(travelDTO.getTravelDescription());
        assertThat(newAdminTravel.getTravelAddress()).isEqualTo(travelDTO.getTravelAddress());
        assertThat(newAdminTravel.getTravelZipCode()).isEqualTo(travelDTO.getTravelZipCode());

        // verify
        verify(mockTravelService, times(1)).findOneTravel(1L);
        verify(mockTravelService, atLeastOnce()).findOneTravel(1L);
        verifyNoMoreInteractions(mockTravelService);

        InOrder inOrder = inOrder(mockTravelService);
        inOrder.verify(mockTravelService).findOneTravel(1L);
    }

    @Test
    @DisplayName("?????????????????????BDD?????????")
    void ?????????????????????BDD?????????() {
        // given
        travelDTO = TravelDTO.builder()
                .idx(1L)
                .travelCode(1)
                .travelTitle("????????? ?????????").travelDescription("????????? ?????????")
                .travelAddress("??????????????? ??????").travelZipCode("123-456").visible("Y")
                .build();

        // when
        when(mockTravelService.findOneTravel(1L)).thenReturn(travelDTO);
        TravelDTO newAdminTravel = mockTravelService.findOneTravel(1L);

        // then
        assertThat(newAdminTravel.getIdx()).isEqualTo(travelDTO.getIdx());
        assertThat(newAdminTravel.getTravelCode()).isEqualTo(travelDTO.getTravelCode());
        assertThat(newAdminTravel.getTravelTitle()).isEqualTo(travelDTO.getTravelTitle());
        assertThat(newAdminTravel.getTravelDescription()).isEqualTo(travelDTO.getTravelDescription());
        assertThat(newAdminTravel.getTravelAddress()).isEqualTo(travelDTO.getTravelAddress());
        assertThat(newAdminTravel.getTravelZipCode()).isEqualTo(travelDTO.getTravelZipCode());

        // verify
        then(mockTravelService).should(times(1)).findOneTravel(1L);
        then(mockTravelService).should(atLeastOnce()).findOneTravel(1L);
        then(mockTravelService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("???????????????Mockito?????????")
    void ???????????????Mockito?????????() {
        // given
        travelEntity = TravelEntity.builder()
                .travelCode(1)
                .travelTitle("????????? ?????????").travelDescription("????????? ?????????").favoriteCount(0).viewCount(0)
                .travelAddress("??????????????? ??????").travelZipCode("123-456").visible("Y")
                .build();

        TravelDTO travelInfo = travelService.insertTravel(travelEntity);

        // when
        when(mockTravelService.findOneTravel(travelEntity.getIdx())).thenReturn(travelInfo);
        TravelDTO newAdminTravel = mockTravelService.findOneTravel(travelEntity.getIdx());

        // then
        assertThat(newAdminTravel.getTravelCode()).isEqualTo(travelEntity.getTravelCode());
        assertThat(newAdminTravel.getTravelTitle()).isEqualTo(travelEntity.getTravelTitle());
        assertThat(newAdminTravel.getTravelDescription()).isEqualTo(travelEntity.getTravelDescription());
        assertThat(newAdminTravel.getTravelAddress()).isEqualTo(travelEntity.getTravelAddress());
        assertThat(newAdminTravel.getTravelZipCode()).isEqualTo(travelEntity.getTravelZipCode());

        // verify
        verify(mockTravelService, times(1)).findOneTravel(newAdminTravel.getIdx());
        verify(mockTravelService, atLeastOnce()).findOneTravel(newAdminTravel.getIdx());
        verifyNoMoreInteractions(mockTravelService);

        InOrder inOrder = inOrder(mockTravelService);
        inOrder.verify(mockTravelService).findOneTravel(newAdminTravel.getIdx());
    }

    @Test
    @DisplayName("???????????????BDD?????????")
    void ???????????????BDD?????????() {
        // given
        travelEntity = TravelEntity.builder()
                .travelCode(1)
                .travelTitle("????????? ?????????").travelDescription("????????? ?????????").favoriteCount(0).viewCount(0)
                .travelAddress("??????????????? ??????").travelZipCode("123-456").visible("Y")
                .build();

        TravelDTO travelInfo = travelService.insertTravel(travelEntity);

        // when
        given(mockTravelService.findOneTravel(travelEntity.getIdx())).willReturn(travelInfo);
        TravelDTO newAdminTravel = mockTravelService.findOneTravel(travelEntity.getIdx());

        // then
        assertThat(newAdminTravel.getTravelCode()).isEqualTo(travelEntity.getTravelCode());
        assertThat(newAdminTravel.getTravelTitle()).isEqualTo(travelEntity.getTravelTitle());
        assertThat(newAdminTravel.getTravelDescription()).isEqualTo(travelEntity.getTravelDescription());
        assertThat(newAdminTravel.getTravelAddress()).isEqualTo(travelEntity.getTravelAddress());
        assertThat(newAdminTravel.getTravelZipCode()).isEqualTo(travelEntity.getTravelZipCode());

        // verify
        then(mockTravelService).should(times(1)).findOneTravel(newAdminTravel.getIdx());
        then(mockTravelService).should(atLeastOnce()).findOneTravel(newAdminTravel.getIdx());
        then(mockTravelService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("????????????????????????")
    void ????????????????????????() {
        // given
        travelEntity = TravelEntity.builder()
                .travelCode(1)
                .travelTitle("????????? ?????????").travelDescription("????????? ?????????").favoriteCount(0).viewCount(0)
                .travelAddress("??????????????? ??????").travelZipCode("123-456").visible("Y")
                .build();

        TravelEntity travelEntity1 = TravelEntity.builder()
                .travelCode(1)
                .travelTitle("????????? ?????????1").travelDescription("????????? ?????????1").favoriteCount(0).viewCount(0)
                .travelAddress("??????????????? ?????????").travelZipCode("123-456").visible("Y")
                .build();

        TravelDTO adminTravelDTO = travelService.insertTravel(travelEntity);
        travelService.insertTravel(travelEntity1);

        TravelEntity newAdminTravelEntity = TravelEntity.builder()
                .idx(adminTravelDTO.getIdx())
                .travelCode(1)
                .travelTitle("????????? ?????? ?????????").travelDescription("????????? ?????? ?????????").favoriteCount(0).viewCount(0)
                .travelAddress("??????????????? ?????????").travelZipCode("123-456")
                .visible("Y").build();

        travelService.updateTravel(newAdminTravelEntity);

        TravelDTO oneTravel = travelService.findOneTravel(newAdminTravelEntity.getIdx());
        TravelDTO secondTravel = travelService.findOneTravel(travelEntity1.getIdx());

        assertThat(oneTravel.getTravelAddress()).isEqualTo("??????????????? ?????????");
        assertThat(secondTravel.getTravelAddress()).isEqualTo("??????????????? ?????????");
    }

    @Test
    @DisplayName("???????????????Mockito?????????")
    void ???????????????Mockito?????????() {
        // given
        travelEntity = TravelEntity.builder()
                .travelCode(1)
                .travelTitle("????????? ?????????").travelDescription("????????? ?????????").favoriteCount(0).viewCount(0)
                .travelAddress("??????????????? ??????").travelZipCode("123-456").visible("Y")
                .build();

        TravelDTO adminTravelDTO = travelService.insertTravel(travelEntity);

        TravelEntity newAdminTravelEntity = TravelEntity.builder()
                .idx(adminTravelDTO.getIdx())
                .travelCode(1)
                .travelTitle("????????? ?????? ?????????").travelDescription("????????? ?????? ?????????").favoriteCount(0).viewCount(0)
                .travelAddress("??????????????? ?????????").travelZipCode("123-456")
                .visible("Y").build();

        travelService.updateTravel(newAdminTravelEntity);

        TravelDTO newAdminTravelDTO = TravelEntity.toDto(newAdminTravelEntity);

        // when
        when(mockTravelService.findOneTravel(newAdminTravelEntity.getIdx())).thenReturn(newAdminTravelDTO);
        TravelDTO travelInfo = mockTravelService.findOneTravel(newAdminTravelDTO.getIdx());

        // then
        assertThat(travelInfo.getIdx()).isEqualTo(newAdminTravelDTO.getIdx());
        assertThat(travelInfo.getTravelCode()).isEqualTo(newAdminTravelDTO.getTravelCode());
        assertThat(travelInfo.getTravelTitle()).isEqualTo(newAdminTravelDTO.getTravelTitle());

        // verify
        verify(mockTravelService, times(1)).findOneTravel(travelInfo.getIdx());
        verify(mockTravelService, atLeastOnce()).findOneTravel(travelInfo.getIdx());
        verifyNoMoreInteractions(mockTravelService);

        InOrder inOrder = inOrder(mockTravelService);
        inOrder.verify(mockTravelService).findOneTravel(travelInfo.getIdx());
    }

    @Test
    @DisplayName("???????????????BDD?????????")
    void ???????????????BDD?????????() {
        // given
        travelEntity = TravelEntity.builder()
                .travelCode(1)
                .travelTitle("????????? ?????????").travelDescription("????????? ?????????").favoriteCount(0).viewCount(0)
                .travelAddress("??????????????? ??????").travelZipCode("123-456").visible("Y")
                .build();

        TravelDTO adminTravelDTO = travelService.insertTravel(travelEntity);

        TravelEntity newAdminTravelEntity = TravelEntity.builder()
                .idx(adminTravelDTO.getIdx())
                .travelCode(1)
                .travelTitle("????????? ?????? ?????????").travelDescription("????????? ?????? ?????????").favoriteCount(0).viewCount(0)
                .travelAddress("??????????????? ?????????").travelZipCode("123-456")
                .visible("Y").build();

        travelService.updateTravel(newAdminTravelEntity);

        TravelDTO newAdminTravelDTO = TravelEntity.toDto(newAdminTravelEntity);

        // when
        given(mockTravelService.findOneTravel(newAdminTravelEntity.getIdx())).willReturn(newAdminTravelDTO);
        TravelDTO travelInfo = mockTravelService.findOneTravel(newAdminTravelDTO.getIdx());

        // then
        assertThat(travelInfo.getIdx()).isEqualTo(newAdminTravelDTO.getIdx());
        assertThat(travelInfo.getTravelCode()).isEqualTo(newAdminTravelDTO.getTravelCode());
        assertThat(travelInfo.getTravelTitle()).isEqualTo(newAdminTravelDTO.getTravelTitle());

        // verify
        then(mockTravelService).should(times(1)).findOneTravel(travelInfo.getIdx());
        then(mockTravelService).should(atLeastOnce()).findOneTravel(travelInfo.getIdx());
        then(mockTravelService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("???????????????Mockito?????????")
    void ???????????????Mockito?????????() {
        // given
        em.persist(travelEntity);
        travelDTO = TravelEntity.toDto(travelEntity);

        // when
        when(mockTravelService.findOneTravel(travelDTO.getIdx())).thenReturn(travelDTO);
        Long deleteIdx = travelService.deleteTravel(travelDTO.getIdx());

        // then
        assertThat(mockTravelService.findOneTravel(travelDTO.getIdx()).getIdx()).isEqualTo(deleteIdx);

        // verify
        verify(mockTravelService, times(1)).findOneTravel(travelDTO.getIdx());
        verify(mockTravelService, atLeastOnce()).findOneTravel(travelDTO.getIdx());
        verifyNoMoreInteractions(mockTravelService);

        InOrder inOrder = inOrder(mockTravelService);
        inOrder.verify(mockTravelService).findOneTravel(travelDTO.getIdx());
    }

    @Test
    @DisplayName("???????????????BDD?????????")
    void ???????????????BDD?????????() {
        // given
        em.persist(travelEntity);
        travelDTO = TravelEntity.toDto(travelEntity);

        // when
        given(mockTravelService.findOneTravel(travelDTO.getIdx())).willReturn(travelDTO);
        Long deleteIdx = travelService.deleteTravel(travelDTO.getIdx());

        // then
        assertThat(mockTravelService.findOneTravel(travelDTO.getIdx()).getIdx()).isEqualTo(deleteIdx);

        // verify
        then(mockTravelService).should(times(1)).findOneTravel(travelDTO.getIdx());
        then(mockTravelService).should(atLeastOnce()).findOneTravel(travelDTO.getIdx());
        then(mockTravelService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("???????????????????????????")
    void ???????????????????????????() {
        TravelDTO oneTravel = travelService.insertTravel(travelEntity);
        int favoriteCount = travelService.favoriteTravel(oneTravel.getIdx());
        assertThat(favoriteCount).isEqualTo(oneTravel.getFavoriteCount() + 1);
    }

    @Test
    @DisplayName("??????????????????????????????")
    void ??????????????????????????????() {
        TravelDTO oneTravel = travelService.insertTravel(travelEntity);
        Boolean popular = travelService.togglePopular(oneTravel.getIdx());
        assertThat(popular).isTrue();
    }

    @Test
    @DisplayName("?????????????????????Mockito?????????")
    void ?????????????????????Mockito?????????() {
        // given
        Long idx = travelService.insertTravel(travelEntity).getIdx();

        Boolean popular = travelService.togglePopular(idx);

        travelEntity = TravelEntity.builder()
                .travelCode(1)
                .travelTitle("????????? ?????????").travelDescription("????????? ?????????").favoriteCount(1).viewCount(0)
                .travelAddress("??????????????? ??????").travelZipCode("123-456").visible("Y").popular(popular)
                .build();

        TravelDTO travelDTO = TravelEntity.toDto(travelEntity);

        // when
        when(mockTravelService.findOneTravel(travelEntity.getIdx())).thenReturn(travelDTO);
        TravelDTO travelInfo = mockTravelService.findOneTravel(travelEntity.getIdx());

        // then
        assertThat(travelInfo.getPopular()).isTrue();

        // verify
        verify(mockTravelService, times(1)).findOneTravel(travelEntity.getIdx());
        verify(mockTravelService, atLeastOnce()).findOneTravel(travelEntity.getIdx());
        verifyNoMoreInteractions(mockTravelService);

        InOrder inOrder = inOrder(mockTravelService);
        inOrder.verify(mockTravelService).findOneTravel(travelEntity.getIdx());
    }

    @Test
    @DisplayName("?????????????????????BDD?????????")
    void ?????????????????????BDD?????????() {
        // given
        Long idx = travelService.insertTravel(travelEntity).getIdx();

        Boolean popular = travelService.togglePopular(idx);

        travelEntity = TravelEntity.builder()
                .travelCode(1)
                .travelTitle("????????? ?????????").travelDescription("????????? ?????????").favoriteCount(1).viewCount(0)
                .travelAddress("??????????????? ??????").travelZipCode("123-456").visible("Y").popular(popular)
                .build();

        TravelDTO travelDTO = TravelEntity.toDto(travelEntity);

        // when
        given(mockTravelService.findOneTravel(idx)).willReturn(travelDTO);
        TravelDTO travelInfo = mockTravelService.findOneTravel(idx);

        // then
        assertThat(travelInfo.getPopular()).isTrue();

        // verify
        then(mockTravelService).should(times(1)).findOneTravel(idx);
        then(mockTravelService).should(atLeastOnce()).findOneTravel(idx);
        then(mockTravelService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("?????????????????????Mockito?????????")
    void ?????????????????????Mockito?????????() {
        // given
        TravelDTO travelInfo = travelService.insertTravel(travelEntity);

        TravelReviewEntity travelReviewEntity = TravelReviewEntity.builder()
                .travelIdx(travelInfo.getIdx())
                .reviewTitle("?????????????????????")
                .reviewDescription("?????????????????????")
                .viewCount(0)
                .favoriteCount(0)
                .popular(false)
                .visible("Y")
                .build();

        TravelReviewDTO travelReviewDTO = travelService.replyTravel(travelReviewEntity);
        List<TravelReviewDTO> reviewList = new ArrayList<>();
        reviewList.add(travelReviewDTO);

        travelDTO = TravelDTO.builder()
                .travelCode(1)
                .travelTitle("????????? ?????????").travelDescription("????????? ?????????").favoriteCount(1).viewCount(0)
                .travelAddress("??????????????? ??????").travelZipCode("123-456").visible("Y").popular(false)
                .reviewList(reviewList)
                .build();

        // when
        when(mockTravelService.findOneTravel(travelInfo.getIdx())).thenReturn(travelDTO);
        TravelDTO newTravelInfo = mockTravelService.findOneTravel(travelInfo.getIdx());

        // then
        assertThat(newTravelInfo.getReviewList().get(0).getReviewTitle()).isEqualTo(reviewList.get(0).getReviewTitle());

        // verify
        verify(mockTravelService, times(1)).findOneTravel(travelInfo.getIdx());
        verify(mockTravelService, atLeastOnce()).findOneTravel(travelInfo.getIdx());
        verifyNoMoreInteractions(mockTravelService);

        InOrder inOrder = inOrder(mockTravelService);
        inOrder.verify(mockTravelService).findOneTravel(travelInfo.getIdx());
    }

    @Test
    @DisplayName("?????????????????????Mockito?????????")
    void ?????????????????????Mockito?????????() {
        // given
        TravelDTO travelInfo = travelService.insertTravel(travelEntity);

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

        TravelReviewDTO reviewDTO = travelService.replyTravel(travelReviewEntity);

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

        TravelReviewDTO travelReviewDTO = travelService.updateReplyTravel(travelReviewEntity);
        List<TravelReviewDTO> reviewList = new ArrayList<>();
        reviewList.add(travelReviewDTO);

        travelDTO = TravelDTO.builder()
                .travelCode(1)
                .travelTitle("????????? ?????????").travelDescription("????????? ?????????").favoriteCount(1).viewCount(0)
                .travelAddress("??????????????? ??????").travelZipCode("123-456").visible("Y").popular(false)
                .reviewList(reviewList)
                .build();

        // when
        when(mockTravelService.replyTravelReview(travelDTO.getIdx())).thenReturn(reviewList);
        List<TravelReviewDTO> reviewDTOList = mockTravelService.replyTravelReview(travelDTO.getIdx());

        // then
        assertThat(reviewDTOList.get(0).getReviewTitle()).isEqualTo("?????????????????????");
        assertThat(reviewDTOList.get(0).getReviewDescription()).isEqualTo("?????????????????????");

        // verify
        verify(mockTravelService, times(1)).replyTravelReview(travelDTO.getIdx());
        verify(mockTravelService,  atLeastOnce()).replyTravelReview(travelDTO.getIdx());
        verifyNoMoreInteractions(mockTravelService);

        InOrder inOrder = inOrder(mockTravelService);
        inOrder.verify(mockTravelService).replyTravelReview(travelDTO.getIdx());
    }

    @Test
    @DisplayName("????????? ?????? ?????? Mockito ?????????")
    void ?????????????????????Mockito?????????() {
        // given
        // ????????? ??????
        TravelDTO travelInfo = travelService.insertTravel(travelEntity);
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
        TravelReviewDTO travelReviewDTO = travelService.replyTravel(travelReviewEntity);
        // ????????? ?????? ??????
        Long deleteIdx = travelService.deleteReplyTravel(travelReviewDTO.getIdx());

        // then
        assertThat(travelReviewDTO.getIdx()).isEqualTo(deleteIdx);
    }

    @Test
    @DisplayName("????????? ?????? ????????? ?????? Mockito ?????????")
    void ??????????????????????????????Mockito?????????() {
        // given
        TravelDTO travelInfo = travelService.insertTravel(travelEntity);

        TravelReviewEntity travelReviewEntity = TravelReviewEntity.builder()
                .travelIdx(travelInfo.getIdx())
                .reviewTitle("?????????????????????")
                .reviewDescription("?????????????????????")
                .viewCount(0)
                .favoriteCount(0)
                .popular(false)
                .visible("Y")
                .build();

        travelService.replyTravel(travelReviewEntity);
        List<TravelReviewDTO> reviewList = travelService.replyTravelReview(travelInfo.getIdx());

        travelDTO = TravelDTO.builder()
                .travelCode(1)
                .travelTitle("????????? ?????????").travelDescription("????????? ?????????").favoriteCount(1).viewCount(0)
                .travelAddress("??????????????? ??????").travelZipCode("123-456").visible("Y").popular(false)
                .reviewList(reviewList)
                .build();

        // when
        when(mockTravelService.replyTravelReview(travelInfo.getIdx())).thenReturn(reviewList);

        // then
        assertThat(mockTravelService.replyTravelReview(travelInfo.getIdx()).get(0).getTravelIdx()).isEqualTo(travelInfo.getIdx());
        assertThat(mockTravelService.replyTravelReview(travelInfo.getIdx()).get(0).getReviewTitle()).isEqualTo("?????????????????????");
        assertThat(mockTravelService.replyTravelReview(travelInfo.getIdx()).get(0).getReviewDescription()).isEqualTo("?????????????????????");
    }

    @Test
    @DisplayName("????????? ?????? ?????? ?????? Mockito ?????????")
    void ???????????????????????????Mockito?????????() {
        // given
        TravelDTO travelInfo = travelService.insertTravel(travelEntity);

        TravelReviewEntity travelReviewEntity = TravelReviewEntity.builder()
                .travelIdx(travelInfo.getIdx())
                .reviewTitle("?????????????????????")
                .reviewDescription("?????????????????????")
                .viewCount(0)
                .favoriteCount(0)
                .popular(false)
                .visible("Y")
                .build();

        TravelReviewDTO travelReviewDTO = travelService.replyTravel(travelReviewEntity);

        // when
        when(mockTravelService.detailReplyTravelReview(travelReviewDTO.getIdx())).thenReturn(travelReviewDTO);
        TravelReviewDTO travelReviewInfo = mockTravelService.detailReplyTravelReview(travelReviewDTO.getIdx());

        // then
        assertThat(travelReviewDTO.getTravelIdx()).isEqualTo(travelInfo.getIdx());
        assertThat(travelReviewInfo.getReviewTitle()).isEqualTo("?????????????????????");
        assertThat(travelReviewInfo.getReviewDescription()).isEqualTo("?????????????????????");

        // verify
        verify(mockTravelService, times(1)).detailReplyTravelReview(travelReviewInfo.getIdx());
        verify(mockTravelService,  atLeastOnce()).detailReplyTravelReview(travelReviewInfo.getIdx());
        verifyNoMoreInteractions(mockTravelService);

        InOrder inOrder = inOrder(mockTravelService);
        inOrder.verify(mockTravelService).detailReplyTravelReview(travelReviewInfo.getIdx());
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
        when(mockTravelService.findTravelGroupList(groupMap)).thenReturn(travelGroupList);
        List<TravelGroupDTO> newTravelGroupList = mockTravelService.findTravelGroupList(groupMap);

        // then
        assertThat(newTravelGroupList.get(0).getIdx()).isEqualTo(travelGroupList.get(0).getIdx());
        assertThat(newTravelGroupList.get(0).getTravelIdx()).isEqualTo(travelGroupList.get(0).getTravelIdx());
        assertThat(newTravelGroupList.get(0).getGroupName()).isEqualTo(travelGroupList.get(0).getGroupName());
        assertThat(newTravelGroupList.get(0).getGroupDescription()).isEqualTo(travelGroupList.get(0).getGroupDescription());

        // verify
        verify(mockTravelService, times(1)).findTravelGroupList(groupMap);
        verify(mockTravelService, atLeastOnce()).findTravelGroupList(groupMap);
        verifyNoMoreInteractions(mockTravelService);

        InOrder inOrder = inOrder(mockTravelService);
        inOrder.verify(mockTravelService).findTravelGroupList(groupMap);
    }

    @Test
    @DisplayName("????????? ?????? ?????? Mockito ?????????")
    void ?????????????????????Mockito?????????() {
        // given
        TravelGroupDTO travelGroupDTO = TravelGroupDTO.builder()
                .idx(1L).travelIdx(1L).groupName("????????????").groupDescription("????????????").visible("Y").build();

        // when
        given(mockTravelService.findOneTravelGroup(1L)).willReturn(travelGroupDTO);
        TravelGroupDTO newTravelGroupDTO = mockTravelService.findOneTravelGroup(1L);

        // then
        assertThat(newTravelGroupDTO.getIdx()).isEqualTo(1L);
        assertThat(newTravelGroupDTO.getTravelIdx()).isEqualTo(1L);
        assertThat(newTravelGroupDTO.getGroupName()).isEqualTo("????????????");
        assertThat(newTravelGroupDTO.getGroupDescription()).isEqualTo("????????????");

        // verify
        verify(mockTravelService, times(1)).findOneTravelGroup(1L);
        verify(mockTravelService, atLeastOnce()).findOneTravelGroup(1L);
        verifyNoMoreInteractions(mockTravelService);

        InOrder inOrder = inOrder(mockTravelService);
        inOrder.verify(mockTravelService).findOneTravelGroup(1L);
    }

    @Test
    @DisplayName("????????? ?????? ?????? Mockito ?????????")
    void ?????????????????????Mockito?????????() {
        TravelGroupEntity travelGroupEntity = TravelGroupEntity.builder()
                .travelIdx(1L).groupName("????????????").groupDescription("????????????").visible("Y").build();

        TravelGroupDTO travelGroupDTO = travelService.insertTravelGroup(travelGroupEntity);

        // when
        when(mockTravelService.findOneTravelGroup(travelGroupDTO.getIdx())).thenReturn(travelGroupDTO);
        TravelGroupDTO newTravelGroupDTO = mockTravelService.findOneTravelGroup(travelGroupDTO.getIdx());

        // then
        assertThat(newTravelGroupDTO.getIdx()).isEqualTo(travelGroupDTO.getIdx());
        assertThat(newTravelGroupDTO.getTravelIdx()).isEqualTo(travelGroupDTO.getTravelIdx());
        assertThat(newTravelGroupDTO.getGroupName()).isEqualTo(travelGroupDTO.getGroupName());
        assertThat(newTravelGroupDTO.getGroupDescription()).isEqualTo(travelGroupDTO.getGroupDescription());

        // verify
        verify(mockTravelService, times(1)).findOneTravelGroup(newTravelGroupDTO.getIdx());
        verify(mockTravelService, atLeastOnce()).findOneTravelGroup(newTravelGroupDTO.getIdx());
        verifyNoMoreInteractions(mockTravelService);

        InOrder inOrder = inOrder(mockTravelService);
        inOrder.verify(mockTravelService).findOneTravelGroup(newTravelGroupDTO.getIdx());
    }

    @Test
    @DisplayName("????????? ?????? ?????? Mockito ?????????")
    void ?????????????????????Mockito?????????() {
        // given
        TravelGroupEntity travelGroupEntity = TravelGroupEntity.builder()
                .travelIdx(1L).groupName("????????????").groupDescription("????????????").visible("Y").build();

        TravelGroupDTO travelGroupDTO = travelService.insertTravelGroup(travelGroupEntity);

        TravelGroupEntity newTravelGroupEntity = TravelGroupEntity.builder()
                .idx(travelGroupDTO.getIdx())
                .travelIdx(1L).groupName("????????????").groupDescription("????????????")
                .visible("Y").build();

        travelService.updateTravelGroup(newTravelGroupEntity);

        TravelGroupDTO newTravelGroupDTO = TravelGroupEntity.toDto(newTravelGroupEntity);

        // when
        when(mockTravelService.findOneTravelGroup(newTravelGroupEntity.getIdx())).thenReturn(newTravelGroupDTO);
        TravelGroupDTO travelGroupInfo = mockTravelService.findOneTravelGroup(newTravelGroupDTO.getIdx());

        // then
        assertThat(travelGroupInfo.getIdx()).isEqualTo(newTravelGroupDTO.getIdx());
        assertThat(travelGroupInfo.getGroupName()).isEqualTo(newTravelGroupDTO.getGroupName());
        assertThat(travelGroupInfo.getGroupDescription()).isEqualTo(newTravelGroupDTO.getGroupDescription());

        // verify
        verify(mockTravelService, times(1)).findOneTravelGroup(travelGroupInfo.getIdx());
        verify(mockTravelService, atLeastOnce()).findOneTravelGroup(travelGroupInfo.getIdx());
        verifyNoMoreInteractions(mockTravelService);

        InOrder inOrder = inOrder(mockTravelService);
        inOrder.verify(mockTravelService).findOneTravelGroup(travelGroupInfo.getIdx());
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
        when(mockTravelService.findOneTravelGroup(travelGroupDTO.getIdx())).thenReturn(travelGroupDTO);
        Long deleteIdx = travelService.deleteTravelGroup(travelGroupDTO.getIdx());

        // then
        assertThat(mockTravelService.findOneTravelGroup(travelGroupDTO.getIdx()).getIdx()).isEqualTo(deleteIdx);

        // verify
        verify(mockTravelService, times(1)).findOneTravelGroup(travelGroupDTO.getIdx());
        verify(mockTravelService, atLeastOnce()).findOneTravelGroup(travelGroupDTO.getIdx());
        verifyNoMoreInteractions(mockTravelService);

        InOrder inOrder = inOrder(mockTravelService);
        inOrder.verify(mockTravelService).findOneTravelGroup(travelGroupDTO.getIdx());
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

        TravelGroupUserDTO travelGroupUserInfo = travelService.insertTravelGroupUser(travelGroupUserEntity);

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

        TravelGroupUserDTO travelGroupUserInfo = travelService.insertTravelGroupUser(travelGroupUserEntity);

        Long deleteIdx = travelService.deleteTravelGroupUser(travelGroupUserInfo.getIdx());

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

        TravelScheduleDTO travelScheduleDTO = travelService.insertTravelSchedule(travelScheduleEntity);

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

        TravelScheduleDTO travelScheduleDTO = travelService.insertTravelSchedule(travelScheduleEntity);

        TravelScheduleEntity updateTravelScheduleEntity = TravelScheduleEntity.builder()
                .idx(travelScheduleDTO.getIdx())
                .travelIdx(travelScheduleDTO.getTravelIdx())
                .userIdx(travelScheduleDTO.getUserIdx())
                .scheduleDescription("????????? ?????? ?????????")
                .scheduleTime(LocalDateTime.now())
                .build();

        TravelScheduleDTO updateScheduleDTO = travelService.updateTravelSchedule(updateTravelScheduleEntity);

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

        TravelScheduleDTO travelScheduleDTO = travelService.insertTravelSchedule(travelScheduleEntity);

        Long deleteIdx = travelService.deleteTravelSchedule(travelScheduleDTO.getIdx());

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

        travelService.insertTravelRecommend(recommendEntity);

        assertThat(travelService.findTravelRecommendList(travelRecommendMap)).isNotEmpty();
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

        TravelRecommendDTO travelRecommendDTO = travelService.insertTravelRecommend(recommendEntity);

        TravelRecommendDTO oneTravelRecommend = travelService.findOneTravelRecommend(travelRecommendDTO.getIdx());
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

        TravelRecommendDTO travelRecommendDTO = travelService.insertTravelRecommend(recommendEntity);

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

        TravelRecommendDTO travelRecommendDTO = travelService.insertTravelRecommend(recommendEntity);

        list.add("??????");
        recommendEntity = TravelRecommendEntity.builder()
                .idx(travelRecommendDTO.getIdx())
                .recommendName(list)
                .build();
        em.flush();
        em.clear();

        TravelRecommendDTO updateRecommendDTO = travelService.updateTravelRecommend(recommendEntity);

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

        TravelRecommendDTO travelRecommendDTO = travelService.insertTravelRecommend(recommendEntity);

        Long deleteIdx = travelService.deleteTravelRecommend(travelRecommendDTO.getIdx());
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

        assertThat(travelService.rankingTravelKeyword().get(0).getSearchKeyword()).isEqualTo("??????");
        assertThat(travelService.rankingTravelKeyword().get(1).getSearchKeyword()).isEqualTo("??????");
    }
    @Test
    @DisplayName("?????? ????????? or ????????? ????????? ?????? ????????? ?????? ??????")
    void ???????????????or?????????????????????????????????????????????() {
        assertThat(travelService.findTravelKeyword("??????").get(0).getTravelTitle()).isEqualTo("?????? ?????????");
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

        assertThat(travelService.findTravelFestivalGroup(dateTime.getMonthValue())).isNotEmpty();
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

        assertThat(travelService.findTravelFestivalList(travelFestivalEntity)).isNotEmpty();
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

        TravelFestivalDTO travelFestivalDTO = travelService.insertTravelFestival(travelFestivalEntity);

        assertThat(travelService.findOneTravelFestival(travelFestivalDTO.getIdx()).getFestivalTitle()).isEqualTo("?????? ??????");
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

        TravelFestivalDTO travelFestivalDTO = travelService.insertTravelFestival(travelFestivalEntity);
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

        TravelFestivalDTO updateFestival = travelService.updateTravelFestival(travelFestivalEntity);
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

        TravelFestivalDTO travelFestivalDTO = travelService.insertTravelFestival(travelFestivalEntity);

        // ??????
        Long deleteIdx = travelService.deleteTravelFestival(travelFestivalDTO.getIdx());
        assertThat(deleteIdx).isEqualTo(travelFestivalDTO.getIdx());
    }
}
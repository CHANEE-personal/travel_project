package com.travel.travel_project.admin.travel;

import com.travel.travel_project.domain.travel.TravelDTO;
import com.travel.travel_project.domain.travel.TravelEntity;
import com.travel.travel_project.api.travel.TravelService;
import com.travel.travel_project.domain.travel.group.TravelGroupDTO;
import com.travel.travel_project.domain.travel.group.TravelGroupEntity;
import com.travel.travel_project.domain.travel.group.TravelGroupUserDTO;
import com.travel.travel_project.domain.travel.group.TravelGroupUserEntity;
import com.travel.travel_project.domain.travel.review.TravelReviewDTO;
import com.travel.travel_project.domain.travel.review.TravelReviewEntity;
import com.travel.travel_project.domain.travel.schedule.TravelScheduleDTO;
import com.travel.travel_project.domain.travel.schedule.TravelScheduleEntity;
import com.travel.travel_project.exception.TravelException;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
@DisplayName("여행지 Service Test")
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
                .travelTitle("여행지 소개")
                .travelDescription("여행지 소개")
                .travelAddress("인천광역시 서구")
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
    @DisplayName("여행지소개리스트조회테스트")
    void 여행지소개리스트조회테스트() {
        // given
        Map<String, Object> travelMap = new HashMap<>();
        travelMap.put("jpaStartPage", 1);
        travelMap.put("size", 3);

        // then
        assertThat(travelService.findTravelList(travelMap)).isNotEmpty();
    }

    @Test
    @DisplayName("여행지소개리스트Mockito조회테스트")
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
    @DisplayName("여행지소개리스트BDD조회테스트")
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
    @DisplayName("여행지상세조회테스트")
    void 여행지상세조회테스트() {
        TravelDTO existTravel = travelService.findOneTravel(1L);
        assertThat(existTravel.getIdx()).isEqualTo(1L);
        assertThat(existTravel.getTravelCode()).isEqualTo(1);
        assertThat(existTravel.getTravelTitle()).isEqualTo("서울 여행지");

        assertThatThrownBy(() -> travelService.findOneTravel(3L))
                .isInstanceOf(TravelException.class).hasMessage("여행 상세 없음");
    }

    @Test
    @DisplayName("여행지소개상세Mockito테스트")
    void 여행지소개상세Mockito테스트() {
        // given
        TravelDTO newTravel = travelService.insertTravel(travelEntity);

        // 조회 수 관련 테스트
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
    @DisplayName("여행지소개상세BDD테스트")
    void 여행지소개상세BDD테스트() {
        // given
        travelDTO = TravelDTO.builder()
                .idx(1L)
                .travelCode(1)
                .travelTitle("여행지 테스트").travelDescription("여행지 테스트")
                .travelAddress("인천광역시 서구").travelZipCode("123-456").visible("Y")
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
    @DisplayName("여행지등록Mockito테스트")
    void 여행지등록Mockito테스트() {
        // given
        travelEntity = TravelEntity.builder()
                .travelCode(1)
                .travelTitle("여행지 테스트").travelDescription("여행지 테스트").favoriteCount(0).viewCount(0)
                .travelAddress("인천광역시 서구").travelZipCode("123-456").visible("Y")
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
    @DisplayName("여행지등록BDD테스트")
    void 여행지등록BDD테스트() {
        // given
        travelEntity = TravelEntity.builder()
                .travelCode(1)
                .travelTitle("여행지 테스트").travelDescription("여행지 테스트").favoriteCount(0).viewCount(0)
                .travelAddress("인천광역시 서구").travelZipCode("123-456").visible("Y")
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
    @DisplayName("여행지수정Mockito테스트")
    void 여행지수정Mockito테스트() {
        // given
        travelEntity = TravelEntity.builder()
                .travelCode(1)
                .travelTitle("여행지 테스트").travelDescription("여행지 테스트").favoriteCount(0).viewCount(0)
                .travelAddress("인천광역시 서구").travelZipCode("123-456").visible("Y")
                .build();

        TravelDTO adminTravelDTO = travelService.insertTravel(travelEntity);

        TravelEntity newAdminTravelEntity = TravelEntity.builder()
                .idx(adminTravelDTO.getIdx())
                .travelCode(1)
                .travelTitle("여행지 수정 테스트").travelDescription("여행지 수정 테스트").favoriteCount(0).viewCount(0)
                .travelAddress("서울특별시 강남구").travelZipCode("123-456")
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
    @DisplayName("여행지수정BDD테스트")
    void 여행지수정BDD테스트() {
        // given
        travelEntity = TravelEntity.builder()
                .travelCode(1)
                .travelTitle("여행지 테스트").travelDescription("여행지 테스트").favoriteCount(0).viewCount(0)
                .travelAddress("인천광역시 서구").travelZipCode("123-456").visible("Y")
                .build();

        TravelDTO adminTravelDTO = travelService.insertTravel(travelEntity);

        TravelEntity newAdminTravelEntity = TravelEntity.builder()
                .idx(adminTravelDTO.getIdx())
                .travelCode(1)
                .travelTitle("여행지 수정 테스트").travelDescription("여행지 수정 테스트").favoriteCount(0).viewCount(0)
                .travelAddress("서울특별시 강남구").travelZipCode("123-456")
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
    @DisplayName("여행지삭제Mockito테스트")
    void 여행지삭제Mockito테스트() {
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
    @DisplayName("여행지삭제BDD테스트")
    void 여행지삭제BDD테스트() {
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
    @DisplayName("여행지좋아요테스트")
    void 여행지좋아요테스트() {
        TravelDTO oneTravel = travelService.insertTravel(travelEntity);
        int favoriteCount = travelService.favoriteTravel(oneTravel.getIdx());
        assertThat(favoriteCount).isEqualTo(oneTravel.getFavoriteCount() + 1);
    }

    @Test
    @DisplayName("인기여행지선정테스트")
    void 인기여행지선정테스트() {
        TravelDTO oneTravel = travelService.insertTravel(travelEntity);
        Boolean popular = travelService.togglePopular(oneTravel.getIdx());
        assertThat(popular).isTrue();
    }

    @Test
    @DisplayName("인기여행지선정Mockito테스트")
    void 인기여행지선정Mockito테스트() {
        // given
        Long idx = travelService.insertTravel(travelEntity).getIdx();

        Boolean popular = travelService.togglePopular(idx);

        travelEntity = TravelEntity.builder()
                .travelCode(1)
                .travelTitle("여행지 테스트").travelDescription("여행지 테스트").favoriteCount(1).viewCount(0)
                .travelAddress("인천광역시 서구").travelZipCode("123-456").visible("Y").popular(popular)
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
    @DisplayName("인기여행지선정BDD테스트")
    void 인기여행지선정BDD테스트() {
        // given
        Long idx = travelService.insertTravel(travelEntity).getIdx();

        Boolean popular = travelService.togglePopular(idx);

        travelEntity = TravelEntity.builder()
                .travelCode(1)
                .travelTitle("여행지 테스트").travelDescription("여행지 테스트").favoriteCount(1).viewCount(0)
                .travelAddress("인천광역시 서구").travelZipCode("123-456").visible("Y").popular(popular)
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
    @DisplayName("여행지댓글등록Mockito테스트")
    void 여행지댓글등록Mockito테스트() {
        // given
        TravelDTO travelInfo = travelService.insertTravel(travelEntity);

        TravelReviewEntity travelReviewEntity = TravelReviewEntity.builder()
                .travelIdx(travelInfo.getIdx())
                .reviewTitle("리뷰등록테스트")
                .reviewDescription("리뷰등록테스트")
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
                .travelTitle("여행지 테스트").travelDescription("여행지 테스트").favoriteCount(1).viewCount(0)
                .travelAddress("인천광역시 서구").travelZipCode("123-456").visible("Y").popular(false)
                .reviewList(reviewList.stream().map(TravelReviewEntity::toEntity).collect(Collectors.toList()))
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
    @DisplayName("여행지댓글수정Mockito테스트")
    void 여행지댓글수정Mockito테스트() {
        // given
        TravelDTO travelInfo = travelService.insertTravel(travelEntity);

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

        TravelReviewDTO reviewDTO = travelService.replyTravel(travelReviewEntity);

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

        TravelReviewDTO travelReviewDTO = travelService.updateReplyTravel(travelReviewEntity);
        List<TravelReviewDTO> reviewList = new ArrayList<>();
        reviewList.add(travelReviewDTO);

        travelDTO = TravelDTO.builder()
                .travelCode(1)
                .travelTitle("여행지 테스트").travelDescription("여행지 테스트").favoriteCount(1).viewCount(0)
                .travelAddress("인천광역시 서구").travelZipCode("123-456").visible("Y").popular(false)
                .reviewList(reviewList.stream().map(TravelReviewEntity::toEntity).collect(Collectors.toList()))
                .build();

        // when
        when(mockTravelService.replyTravelReview(travelDTO.getIdx())).thenReturn(reviewList);
        List<TravelReviewDTO> reviewDTOList = mockTravelService.replyTravelReview(travelDTO.getIdx());

        // then
        assertThat(reviewDTOList.get(0).getReviewTitle()).isEqualTo("리뷰수정테스트");
        assertThat(reviewDTOList.get(0).getReviewDescription()).isEqualTo("리뷰수정테스트");

        // verify
        verify(mockTravelService, times(1)).replyTravelReview(travelDTO.getIdx());
        verify(mockTravelService,  atLeastOnce()).replyTravelReview(travelDTO.getIdx());
        verifyNoMoreInteractions(mockTravelService);

        InOrder inOrder = inOrder(mockTravelService);
        inOrder.verify(mockTravelService).replyTravelReview(travelDTO.getIdx());
    }

    @Test
    @DisplayName("여행지 댓글 삭제 Mockito 테스트")
    void 여행지댓글삭제Mockito테스트() {
        // given
        // 여행지 등록
        TravelDTO travelInfo = travelService.insertTravel(travelEntity);
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
        TravelReviewDTO travelReviewDTO = travelService.replyTravel(travelReviewEntity);
        // 여행지 댓글 삭제
        Long deleteIdx = travelService.deleteReplyTravel(travelReviewDTO.getIdx());

        // then
        assertThat(travelReviewDTO.getIdx()).isEqualTo(deleteIdx);
    }

    @Test
    @DisplayName("여행지 댓글 리스트 조회 Mockito 테스트")
    void 여행지댓글리스트조회Mockito테스트() {
        // given
        TravelDTO travelInfo = travelService.insertTravel(travelEntity);

        TravelReviewEntity travelReviewEntity = TravelReviewEntity.builder()
                .travelIdx(travelInfo.getIdx())
                .reviewTitle("리뷰등록테스트")
                .reviewDescription("리뷰등록테스트")
                .viewCount(0)
                .favoriteCount(0)
                .popular(false)
                .visible("Y")
                .build();

        travelService.replyTravel(travelReviewEntity);
        List<TravelReviewDTO> reviewList = travelService.replyTravelReview(travelInfo.getIdx());

        travelDTO = TravelDTO.builder()
                .travelCode(1)
                .travelTitle("여행지 테스트").travelDescription("여행지 테스트").favoriteCount(1).viewCount(0)
                .travelAddress("인천광역시 서구").travelZipCode("123-456").visible("Y").popular(false)
                .reviewList(reviewList.stream().map(TravelReviewEntity::toEntity).collect(Collectors.toList()))
                .build();

        // when
        when(mockTravelService.replyTravelReview(travelInfo.getIdx())).thenReturn(reviewList);

        // then
        assertThat(mockTravelService.replyTravelReview(travelInfo.getIdx()).get(0).getTravelIdx()).isEqualTo(travelInfo.getIdx());
        assertThat(mockTravelService.replyTravelReview(travelInfo.getIdx()).get(0).getReviewTitle()).isEqualTo("리뷰등록테스트");
        assertThat(mockTravelService.replyTravelReview(travelInfo.getIdx()).get(0).getReviewDescription()).isEqualTo("리뷰등록테스트");
    }

    @Test
    @DisplayName("여행지 댓글 상세 조회 Mockito 테스트")
    void 여행지댓글상세조회Mockito테스트() {
        // given
        TravelDTO travelInfo = travelService.insertTravel(travelEntity);

        TravelReviewEntity travelReviewEntity = TravelReviewEntity.builder()
                .travelIdx(travelInfo.getIdx())
                .reviewTitle("리뷰등록테스트")
                .reviewDescription("리뷰등록테스트")
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
        assertThat(travelReviewInfo.getReviewTitle()).isEqualTo("리뷰등록테스트");
        assertThat(travelReviewInfo.getReviewDescription()).isEqualTo("리뷰등록테스트");

        // verify
        verify(mockTravelService, times(1)).detailReplyTravelReview(travelReviewInfo.getIdx());
        verify(mockTravelService,  atLeastOnce()).detailReplyTravelReview(travelReviewInfo.getIdx());
        verifyNoMoreInteractions(mockTravelService);

        InOrder inOrder = inOrder(mockTravelService);
        inOrder.verify(mockTravelService).detailReplyTravelReview(travelReviewInfo.getIdx());
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
    @DisplayName("여행지 그룹 상세 Mockito 테스트")
    void 여행지그룹상세Mockito테스트() {
        // given
        TravelGroupDTO travelGroupDTO = TravelGroupDTO.builder()
                .idx(1L).travelIdx(1L).groupName("서울모임").groupDescription("서울모임").visible("Y").build();

        // when
        given(mockTravelService.findOneTravelGroup(1L)).willReturn(travelGroupDTO);
        TravelGroupDTO newTravelGroupDTO = mockTravelService.findOneTravelGroup(1L);

        // then
        assertThat(newTravelGroupDTO.getIdx()).isEqualTo(1L);
        assertThat(newTravelGroupDTO.getTravelIdx()).isEqualTo(1L);
        assertThat(newTravelGroupDTO.getGroupName()).isEqualTo("서울모임");
        assertThat(newTravelGroupDTO.getGroupDescription()).isEqualTo("서울모임");

        // verify
        verify(mockTravelService, times(1)).findOneTravelGroup(1L);
        verify(mockTravelService, atLeastOnce()).findOneTravelGroup(1L);
        verifyNoMoreInteractions(mockTravelService);

        InOrder inOrder = inOrder(mockTravelService);
        inOrder.verify(mockTravelService).findOneTravelGroup(1L);
    }

    @Test
    @DisplayName("여행지 그룹 등록 Mockito 테스트")
    void 여행지그룹등록Mockito테스트() {
        TravelGroupEntity travelGroupEntity = TravelGroupEntity.builder()
                .travelIdx(1L).groupName("서울모임").groupDescription("서울모임").visible("Y").build();

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
    @DisplayName("여행지 그룹 수정 Mockito 테스트")
    void 여행지그룹수정Mockito테스트() {
        // given
        TravelGroupEntity travelGroupEntity = TravelGroupEntity.builder()
                .travelIdx(1L).groupName("서울모임").groupDescription("서울모임").visible("Y").build();

        TravelGroupDTO travelGroupDTO = travelService.insertTravelGroup(travelGroupEntity);

        TravelGroupEntity newTravelGroupEntity = TravelGroupEntity.builder()
                .idx(travelGroupDTO.getIdx())
                .travelIdx(1L).groupName("인천모임").groupDescription("인천모임")
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
    @DisplayName("여행지 그룹 삭제 Mockito 테스트")
    void 여행지그룹삭제Mockito테스트() {
        // given
        TravelGroupEntity travelGroupEntity = TravelGroupEntity.builder()
                .travelIdx(1L).groupName("서울모임").groupDescription("서울모임").visible("Y").build();
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
    @DisplayName("유저 여행 그룹 등록 Mockito 테스트")
    void 유저여행그룹등록Mockito테스트() {
        // given
        TravelGroupEntity travelGroupEntity = TravelGroupEntity.builder()
                .travelIdx(1L).groupName("서울모임").groupDescription("서울모임").visible("Y").build();
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
    @DisplayName("유저 여행 그룹 삭제 Mockito 테스트")
    void 유저여행그룹삭제Mockito테스트() {
        // given
        TravelGroupEntity travelGroupEntity = TravelGroupEntity.builder()
                .travelIdx(1L).groupName("서울모임").groupDescription("서울모임").visible("Y").build();
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
    @DisplayName("유저 여행 스케줄 등록 테스트")
    void 유저여행스케줄등록테스트() {
        TravelScheduleEntity travelScheduleEntity = TravelScheduleEntity.builder()
                .travelIdx(1L)
                .userIdx(1L)
                .scheduleDescription("스케줄 테스트")
                .scheduleTime(LocalDateTime.now())
                .build();

        TravelScheduleDTO travelScheduleDTO = travelService.insertTravelSchedule(travelScheduleEntity);

        assertThat(travelScheduleDTO.getTravelIdx()).isEqualTo(1L);
        assertThat(travelScheduleDTO.getUserIdx()).isEqualTo(1L);
        assertThat(travelScheduleDTO.getScheduleDescription()).isEqualTo("스케줄 테스트");
    }

    @Test
    @DisplayName("유저 여행 스케줄 수정 테스트")
    void 유저여행스케줄수정테스트() {
        TravelScheduleEntity travelScheduleEntity = TravelScheduleEntity.builder()
                .travelIdx(1L)
                .userIdx(1L)
                .scheduleDescription("스케줄 테스트")
                .scheduleTime(LocalDateTime.now())
                .build();

        TravelScheduleDTO travelScheduleDTO = travelService.insertTravelSchedule(travelScheduleEntity);

        TravelScheduleEntity updateTravelScheduleEntity = TravelScheduleEntity.builder()
                .idx(travelScheduleDTO.getIdx())
                .travelIdx(travelScheduleDTO.getTravelIdx())
                .userIdx(travelScheduleDTO.getUserIdx())
                .scheduleDescription("스케줄 수정 테스트")
                .scheduleTime(LocalDateTime.now())
                .build();

        TravelScheduleDTO updateScheduleDTO = travelService.updateTravelSchedule(updateTravelScheduleEntity);

        assertThat(updateScheduleDTO.getScheduleDescription()).isEqualTo("스케줄 수정 테스트");
    }

    @Test
    @DisplayName("유저 여행 스케줄 삭제 테스트")
    void 유저여행스케줄삭제테스트() {
        TravelScheduleEntity travelScheduleEntity = TravelScheduleEntity.builder()
                .travelIdx(1L)
                .userIdx(1L)
                .scheduleDescription("스케줄 테스트")
                .scheduleTime(LocalDateTime.now())
                .build();

        TravelScheduleDTO travelScheduleDTO = travelService.insertTravelSchedule(travelScheduleEntity);

        Long deleteIdx = travelService.deleteTravelSchedule(travelScheduleDTO.getIdx());

        assertThat(deleteIdx).isEqualTo(travelScheduleDTO.getIdx());
    }
}
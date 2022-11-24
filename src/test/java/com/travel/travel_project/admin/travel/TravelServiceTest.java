package com.travel.travel_project.admin.travel;

import com.travel.travel_project.api.travel.mapper.review.TravelReviewMapper;
import com.travel.travel_project.domain.travel.TravelDTO;
import com.travel.travel_project.domain.travel.TravelEntity;
import com.travel.travel_project.api.travel.TravelService;
import com.travel.travel_project.api.travel.mapper.TravelMapper;
import com.travel.travel_project.domain.travel.review.TravelReviewDTO;
import com.travel.travel_project.domain.travel.review.TravelReviewEntity;
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
                .favoriteCount(0)
                .viewCount(0)
                .visible("Y")
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
        assertThat(travelService.findTravelsList(travelMap)).isNotEmpty();
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
        when(mockTravelService.findTravelsList(travelMap)).thenReturn(travelList);
        List<TravelDTO> newTravelList = mockTravelService.findTravelsList(travelMap);

        // then
        assertThat(newTravelList.get(0).getIdx()).isEqualTo(travelList.get(0).getIdx());
        assertThat(newTravelList.get(0).getTravelCode()).isEqualTo(travelList.get(0).getTravelCode());
        assertThat(newTravelList.get(0).getTravelTitle()).isEqualTo(travelList.get(0).getTravelTitle());
        assertThat(newTravelList.get(0).getTravelDescription()).isEqualTo(travelList.get(0).getTravelDescription());
        assertThat(newTravelList.get(0).getTravelAddress()).isEqualTo(travelList.get(0).getTravelAddress());
        assertThat(newTravelList.get(0).getTravelZipCode()).isEqualTo(travelList.get(0).getTravelZipCode());

        // verify
        verify(mockTravelService, times(1)).findTravelsList(travelMap);
        verify(mockTravelService, atLeastOnce()).findTravelsList(travelMap);
        verifyNoMoreInteractions(mockTravelService);

        InOrder inOrder = inOrder(mockTravelService);
        inOrder.verify(mockTravelService).findTravelsList(travelMap);
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
        given(mockTravelService.findTravelsList(travelMap)).willReturn(travelList);
        List<TravelDTO> newTravelList = mockTravelService.findTravelsList(travelMap);

        // then
        assertThat(newTravelList.get(0).getIdx()).isEqualTo(travelList.get(0).getIdx());
        assertThat(newTravelList.get(0).getTravelCode()).isEqualTo(travelList.get(0).getTravelCode());
        assertThat(newTravelList.get(0).getTravelTitle()).isEqualTo(travelList.get(0).getTravelTitle());
        assertThat(newTravelList.get(0).getTravelDescription()).isEqualTo(travelList.get(0).getTravelDescription());
        assertThat(newTravelList.get(0).getTravelAddress()).isEqualTo(travelList.get(0).getTravelAddress());
        assertThat(newTravelList.get(0).getTravelZipCode()).isEqualTo(travelList.get(0).getTravelZipCode());

        // verify
        then(mockTravelService).should(times(1)).findTravelsList(travelMap);
        then(mockTravelService).should(atLeastOnce()).findTravelsList(travelMap);
        then(mockTravelService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("여행지소개상세Mockito테스트")
    void 여행지소개상세Mockito테스트() {
        // given
        travelDTO = TravelDTO.builder()
                .idx(1L)
                .travelCode(1)
                .travelTitle("여행지 테스트").travelDescription("여행지 테스트")
                .travelAddress("인천광역시 서구").travelZipCode("123-456").visible("Y")
                .build();

        // when
        given(mockTravelService.findOneTravel(1L)).willReturn(travelDTO);
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

        TravelDTO newAdminTravelDTO = TravelMapper.INSTANCE.toDto(newAdminTravelEntity);

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

        TravelDTO newAdminTravelDTO = TravelMapper.INSTANCE.toDto(newAdminTravelEntity);

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
        travelDTO = TravelMapper.INSTANCE.toDto(travelEntity);

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
        travelDTO = TravelMapper.INSTANCE.toDto(travelEntity);

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
    @DisplayName("인기여행지선정Mockito테스트")
    void 인기여행지선정Mockito테스트() {
        // given
        Long idx = travelService.insertTravel(travelEntity).getIdx();

        Boolean popular = travelService.togglePopular(idx).getPopular();

        travelEntity = TravelEntity.builder()
                .travelCode(1)
                .travelTitle("여행지 테스트").travelDescription("여행지 테스트").favoriteCount(1).viewCount(0)
                .travelAddress("인천광역시 서구").travelZipCode("123-456").visible("Y").popular(popular)
                .build();

        TravelDTO travelDTO = TravelMapper.INSTANCE.toDto(travelEntity);

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

        Boolean popular = travelService.togglePopular(idx).getPopular();

        travelEntity = TravelEntity.builder()
                .travelCode(1)
                .travelTitle("여행지 테스트").travelDescription("여행지 테스트").favoriteCount(1).viewCount(0)
                .travelAddress("인천광역시 서구").travelZipCode("123-456").visible("Y").popular(popular)
                .build();

        TravelDTO travelDTO = TravelMapper.INSTANCE.toDto(travelEntity);

        // when
        given(mockTravelService.findOneTravel(travelEntity.getIdx())).willReturn(travelDTO);
        TravelDTO travelInfo = mockTravelService.findOneTravel(travelEntity.getIdx());

        // then
        assertThat(travelInfo.getPopular()).isTrue();

        // verify
        then(mockTravelService).should(times(1)).favoriteTravel(travelDTO.getIdx());
        then(mockTravelService).should(atLeastOnce()).favoriteTravel(travelDTO.getIdx());
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
                .reviewList(TravelReviewMapper.INSTANCE.toEntityList(reviewList))
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
                .reviewList(TravelReviewMapper.INSTANCE.toEntityList(reviewList))
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
                .reviewList(TravelReviewMapper.INSTANCE.toEntityList(reviewList))
                .build();

        // when
        when(mockTravelService.replyTravelReview(travelInfo.getIdx())).thenReturn(reviewList);

        // then
        assertThat(mockTravelService.replyTravelReview(travelInfo.getIdx()).get(0).getTravelIdx()).isEqualTo(travelInfo.getIdx());
        assertThat(mockTravelService.replyTravelReview(travelInfo.getIdx()).get(0).getReviewTitle()).isEqualTo("리뷰등록테스트");
        assertThat(mockTravelService.replyTravelReview(travelInfo.getIdx()).get(0).getReviewDescription()).isEqualTo("리뷰등록테스트");
    }
}
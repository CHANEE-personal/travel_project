package com.travel.api.travel;

import com.travel.api.AdminCommonServiceTest;
import com.travel.api.common.domain.repository.CommonRepository;
import com.travel.api.travel.domain.TravelDto;
import com.travel.api.travel.domain.TravelEntity;
import com.travel.api.travel.domain.festival.TravelFestivalDto;
import com.travel.api.travel.domain.festival.TravelFestivalEntity;
import com.travel.api.travel.domain.group.TravelGroupDto;
import com.travel.api.travel.domain.group.TravelGroupEntity;
import com.travel.api.travel.domain.group.repository.GroupRepository;
import com.travel.api.travel.domain.recommend.TravelRecommendDto;
import com.travel.api.travel.domain.recommend.TravelRecommendEntity;
import com.travel.api.travel.domain.reservation.TravelReservationDto;
import com.travel.api.travel.domain.reservation.TravelReservationEntity;
import com.travel.api.travel.domain.review.TravelReviewDto;
import com.travel.api.travel.domain.review.TravelReviewEntity;
import com.travel.api.travel.domain.review.repository.ReviewRepository;
import com.travel.api.travel.domain.search.SearchEntity;
import com.travel.exception.TravelException;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.*;
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
class TravelServiceTest extends AdminCommonServiceTest {

    @Mock private CommonRepository commonRepository;
    @Mock private ReviewRepository reviewRepository;
    @Mock private TravelRepository travelRepository;
    @Mock private GroupRepository groupRepository;
    @Mock private TravelQueryRepository travelQueryRepository;
    @InjectMocks private TravelService mockTravelService;
    private final TravelService travelService;
    private final EntityManager em;

    @Test
    @DisplayName("여행지소개리스트조회테스트")
    void 여행지소개리스트조회테스트() {
        // given
        Map<String, Object> travelMap = new HashMap<>();
        travelMap.put("searchKeyword", "서울");

        PageRequest pageRequest = PageRequest.of(0, 3);

        // then
        assertThat(travelService.findTravelList(travelMap, pageRequest)).isEmpty();
        travelService.findTravelList(travelMap, pageRequest);
        travelService.findTravelList(travelMap, pageRequest);

        travelMap.put("searchKeyword", "인천");
        travelService.findTravelList(travelMap, pageRequest);

        assertThat(travelService.rankingTravelKeyword().get(0).getSearchKeyword()).isEqualTo("서울");
        assertThat(travelService.rankingTravelKeyword().get(1).getSearchKeyword()).isEqualTo("인천");
    }

    @Test
    @DisplayName("여행지 소개 리스트 Mockito 검색 조회 테스트")
    void 여행지소개리스트Mockito검색조회테스트() {
        // given
        Map<String, Object> travelMap = new HashMap<>();
        travelMap.put("searchCode", 1);
        travelMap.put("searchKeyword", "여행지");

        PageRequest pageRequest = PageRequest.of(0, 3);

        List<TravelDto> travelList = new ArrayList<>();
        travelList.add(TravelDto.builder().idx(1L).commonCode(commonDTO.getCommonCode())
                .travelTitle("여행지 소개").travelDescription("여행지 소개")
                .travelAddress("인천광역시 서구").travelZipCode("123-456").visible("Y").build());

        Page<TravelDto> resultPage = new PageImpl<>(travelList, pageRequest, travelList.size());

        // when
        when(travelQueryRepository.findTravelList(travelMap, pageRequest)).thenReturn(resultPage);
        Page<TravelDto> newTravelList = mockTravelService.findTravelList(travelMap, pageRequest);

        List<TravelDto> findTravelList = newTravelList.stream().collect(Collectors.toList());

        // then
        assertThat(findTravelList.get(0).getIdx()).isEqualTo(travelList.get(0).getIdx());
        assertThat(findTravelList.get(0).getCommonCode()).isEqualTo(travelList.get(0).getCommonCode());
        assertThat(findTravelList.get(0).getTravelTitle()).isEqualTo(travelList.get(0).getTravelTitle());
        assertThat(findTravelList.get(0).getTravelDescription()).isEqualTo(travelList.get(0).getTravelDescription());
        assertThat(findTravelList.get(0).getTravelAddress()).isEqualTo(travelList.get(0).getTravelAddress());
        assertThat(findTravelList.get(0).getTravelZipCode()).isEqualTo(travelList.get(0).getTravelZipCode());

        // verify
        verify(travelQueryRepository, times(1)).findTravelList(travelMap, pageRequest);
        verify(travelQueryRepository, atLeastOnce()).findTravelList(travelMap, pageRequest);
        verifyNoMoreInteractions(travelQueryRepository);

        InOrder inOrder = inOrder(travelQueryRepository);
        inOrder.verify(travelQueryRepository).findTravelList(travelMap, pageRequest);
    }

    @Test
    @DisplayName("여행지 소개 리스트 Mockito 조회 테스트")
    void 여행지소개리스트Mockito조회테스트() {
        // given
        Map<String, Object> travelMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(0, 3);

        List<TravelDto> travelList = new ArrayList<>();
        travelList.add(TravelDto.builder().idx(1L).commonCode(commonDTO.getCommonCode())
                .travelTitle("여행지 소개").travelDescription("여행지 소개")
                .travelAddress("인천광역시 서구").travelZipCode("123-456").visible("Y").build());

        Page<TravelDto> resultPage = new PageImpl<>(travelList, pageRequest, travelList.size());

        // when
        when(travelQueryRepository.findTravelList(travelMap, pageRequest)).thenReturn(resultPage);
        Page<TravelDto> newTravelList = mockTravelService.findTravelList(travelMap, pageRequest);

        List<TravelDto> findTravelList = newTravelList.stream().collect(Collectors.toList());

        // then
        assertThat(findTravelList.get(0).getIdx()).isEqualTo(travelList.get(0).getIdx());
        assertThat(findTravelList.get(0).getCommonCode()).isEqualTo(travelList.get(0).getCommonCode());
        assertThat(findTravelList.get(0).getTravelTitle()).isEqualTo(travelList.get(0).getTravelTitle());
        assertThat(findTravelList.get(0).getTravelDescription()).isEqualTo(travelList.get(0).getTravelDescription());
        assertThat(findTravelList.get(0).getTravelAddress()).isEqualTo(travelList.get(0).getTravelAddress());
        assertThat(findTravelList.get(0).getTravelZipCode()).isEqualTo(travelList.get(0).getTravelZipCode());

        // verify
        verify(travelQueryRepository, times(1)).findTravelList(travelMap, pageRequest);
        verify(travelQueryRepository, atLeastOnce()).findTravelList(travelMap, pageRequest);
        verifyNoMoreInteractions(travelQueryRepository);

        InOrder inOrder = inOrder(travelQueryRepository);
        inOrder.verify(travelQueryRepository).findTravelList(travelMap, pageRequest);
    }

    @Test
    @DisplayName("여행지 소개 리스트 BDD 조회 테스트")
    void 여행지소개리스트BDD조회테스트() {
        Map<String, Object> travelMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(0, 3);

        List<TravelDto> travelList = new ArrayList<>();
        travelList.add(TravelDto.builder().idx(1L).commonCode(commonDTO.getCommonCode())
                .travelTitle("여행지 소개").travelDescription("여행지 소개")
                .travelAddress("인천광역시 서구").travelZipCode("123-456").visible("Y").build());

        Page<TravelDto> resultPage = new PageImpl<>(travelList, pageRequest, travelList.size());

        // when
        given(travelQueryRepository.findTravelList(travelMap, pageRequest)).willReturn(resultPage);
        Page<TravelDto> newTravelList = mockTravelService.findTravelList(travelMap, pageRequest);

        List<TravelDto> findTravelList = newTravelList.stream().collect(Collectors.toList());

        // then
        assertThat(findTravelList.get(0).getIdx()).isEqualTo(travelList.get(0).getIdx());
        assertThat(findTravelList.get(0).getCommonCode()).isEqualTo(travelList.get(0).getCommonCode());
        assertThat(findTravelList.get(0).getTravelTitle()).isEqualTo(travelList.get(0).getTravelTitle());
        assertThat(findTravelList.get(0).getTravelDescription()).isEqualTo(travelList.get(0).getTravelDescription());
        assertThat(findTravelList.get(0).getTravelAddress()).isEqualTo(travelList.get(0).getTravelAddress());
        assertThat(findTravelList.get(0).getTravelZipCode()).isEqualTo(travelList.get(0).getTravelZipCode());


        // verify
        then(travelQueryRepository).should(times(1)).findTravelList(travelMap, pageRequest);
        then(travelQueryRepository).should(atLeastOnce()).findTravelList(travelMap, pageRequest);
        then(travelQueryRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("여행지상세조회테스트")
    void 여행지상세조회테스트() {
        TravelDto existTravel = travelService.findOneTravel(travelDTO.getIdx());
        assertThat(existTravel.getIdx()).isEqualTo(travelDTO.getIdx());
        assertThat(existTravel.getCommonCode()).isEqualTo(travelDTO.getCommonCode());
        assertThat(existTravel.getTravelTitle()).isEqualTo(travelDTO.getTravelTitle());

        assertThatThrownBy(() -> travelService.findOneTravel(3L))
                .isInstanceOf(TravelException.class).hasMessage("여행 상세 없음");
    }

    @Test
    @DisplayName("이전여행지조회테스트")
    void 이전여행지조회테스트() {
        travelService.findPrevOneTravel(3L);
    }

    @Test
    @DisplayName("여행지소개상세Mockito테스트")
    void 여행지소개상세Mockito테스트() {
        // 조회 수 관련 테스트
        TravelDto oneTravel = travelService.findOneTravel(travelEntity.getIdx());
        assertThat(travelEntity.getViewCount()).isEqualTo(oneTravel.getViewCount());

        // when
        when(travelRepository.findByIdx(travelEntity.getIdx())).thenReturn(Optional.ofNullable(travelEntity));
        TravelDto newAdminTravel = mockTravelService.findOneTravel(travelEntity.getIdx());

        // then
        assertThat(newAdminTravel.getIdx()).isEqualTo(travelEntity.getIdx());
        assertThat(newAdminTravel.getCommonCode()).isEqualTo(travelEntity.getNewTravelCode().getCommonCode());
        assertThat(newAdminTravel.getTravelTitle()).isEqualTo(travelEntity.getTravelTitle());
        assertThat(newAdminTravel.getTravelDescription()).isEqualTo(travelEntity.getTravelDescription());
        assertThat(newAdminTravel.getTravelAddress()).isEqualTo(travelEntity.getTravelAddress());
        assertThat(newAdminTravel.getTravelZipCode()).isEqualTo(travelEntity.getTravelZipCode());

        // verify
        verify(travelRepository, times(1)).findByIdx(travelEntity.getIdx());
        verify(travelRepository, atLeastOnce()).findByIdx(travelEntity.getIdx());
        verifyNoMoreInteractions(travelRepository);

        InOrder inOrder = inOrder(travelRepository);
        inOrder.verify(travelRepository).findByIdx(travelEntity.getIdx());
    }

    @Test
    @DisplayName("여행지소개상세BDD테스트")
    void 여행지소개상세BDD테스트() {
        /// 조회 수 관련 테스트
        TravelDto oneTravel = travelService.findOneTravel(travelEntity.getIdx());
        assertThat(travelEntity.getViewCount()).isEqualTo(oneTravel.getViewCount());

        // when
        given(travelRepository.findByIdx(travelEntity.getIdx())).willReturn(Optional.ofNullable(travelEntity));
        TravelDto newAdminTravel = mockTravelService.findOneTravel(travelEntity.getIdx());

        // then
        assertThat(newAdminTravel.getIdx()).isEqualTo(travelEntity.getIdx());
        assertThat(newAdminTravel.getCommonCode()).isEqualTo(travelEntity.getNewTravelCode().getCommonCode());
        assertThat(newAdminTravel.getTravelTitle()).isEqualTo(travelEntity.getTravelTitle());
        assertThat(newAdminTravel.getTravelDescription()).isEqualTo(travelEntity.getTravelDescription());
        assertThat(newAdminTravel.getTravelAddress()).isEqualTo(travelEntity.getTravelAddress());
        assertThat(newAdminTravel.getTravelZipCode()).isEqualTo(travelEntity.getTravelZipCode());

        // verify
        then(travelRepository).should(times(1)).findByIdx(travelEntity.getIdx());
        then(travelRepository).should(atLeastOnce()).findByIdx(travelEntity.getIdx());
        then(travelRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("여행지등록Mockito테스트")
    void 여행지등록Mockito테스트() {
        // when
        when(commonRepository.findByCommonCode(travelEntity.getNewTravelCode().getCommonCode())).thenReturn(Optional.ofNullable(commonEntity));
        when(travelRepository.save(travelEntity)).thenReturn(travelEntity);
        TravelDto newAdminTravel = mockTravelService.insertTravel(travelEntity);

        // then
        assertThat(newAdminTravel.getCommonCode()).isEqualTo(travelEntity.getNewTravelCode().getCommonCode());
        assertThat(newAdminTravel.getTravelTitle()).isEqualTo(travelEntity.getTravelTitle());
        assertThat(newAdminTravel.getTravelDescription()).isEqualTo(travelEntity.getTravelDescription());
        assertThat(newAdminTravel.getTravelAddress()).isEqualTo(travelEntity.getTravelAddress());
        assertThat(newAdminTravel.getTravelZipCode()).isEqualTo(travelEntity.getTravelZipCode());

        // verify
        verify(travelRepository, times(1)).save(travelEntity);
        verify(travelRepository, atLeastOnce()).save(travelEntity);
        verifyNoMoreInteractions(travelRepository);

        InOrder inOrder = inOrder(travelRepository);
        inOrder.verify(travelRepository).save(travelEntity);
    }

    @Test
    @DisplayName("여행지등록BDD테스트")
    void 여행지등록BDD테스트() {
        // when
        given(commonRepository.findByCommonCode(travelEntity.getNewTravelCode().getCommonCode())).willReturn(Optional.ofNullable(commonEntity));
        given(travelRepository.save(travelEntity)).willReturn(travelEntity);
        TravelDto newAdminTravel = mockTravelService.insertTravel(travelEntity);

        // then
        assertThat(newAdminTravel.getCommonCode()).isEqualTo(travelEntity.getNewTravelCode().getCommonCode());
        assertThat(newAdminTravel.getTravelTitle()).isEqualTo(travelEntity.getTravelTitle());
        assertThat(newAdminTravel.getTravelDescription()).isEqualTo(travelEntity.getTravelDescription());
        assertThat(newAdminTravel.getTravelAddress()).isEqualTo(travelEntity.getTravelAddress());
        assertThat(newAdminTravel.getTravelZipCode()).isEqualTo(travelEntity.getTravelZipCode());

        // verify
        then(travelRepository).should(times(1)).save(travelEntity);
        then(travelRepository).should(atLeastOnce()).save(travelEntity);
        then(travelRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("여행지수정테스트")
    void 여행지수정테스트() {
        // given
        TravelEntity newAdminTravelEntity = TravelEntity.builder()
                .idx(travelEntity.getIdx())
                .newTravelCode(commonEntity)
                .travelTitle("여행지 수정 테스트").travelDescription("여행지 수정 테스트").favoriteCount(0).viewCount(0)
                .travelAddress("서울특별시 강남구").travelZipCode("123-456")
                .visible("Y").build();

        travelService.updateTravel(newAdminTravelEntity.getIdx(), newAdminTravelEntity);

        TravelDto oneTravel = travelService.findOneTravel(newAdminTravelEntity.getIdx());

        assertThat(oneTravel.getTravelAddress()).isEqualTo("서울특별시 강남구");
    }

    @Test
    @DisplayName("여행지수정Mockito테스트")
    void 여행지수정Mockito테스트() {
        // given
        TravelEntity newAdminTravelEntity = TravelEntity.builder()
                .idx(travelEntity.getIdx())
                .newTravelCode(commonEntity)
                .travelTitle("여행지 수정 테스트").travelDescription("여행지 수정 테스트").favoriteCount(0).viewCount(0)
                .travelAddress("서울특별시 강남구").travelZipCode("123-456")
                .visible("Y").build();

        // when
        when(travelRepository.findById(newAdminTravelEntity.getIdx())).thenReturn(Optional.of(newAdminTravelEntity));
        when(travelRepository.save(newAdminTravelEntity)).thenReturn(newAdminTravelEntity);
        TravelDto travelInfo = mockTravelService.updateTravel(newAdminTravelEntity.getIdx(), newAdminTravelEntity);

        // then
        assertThat(travelInfo.getIdx()).isEqualTo(newAdminTravelEntity.getIdx());
        assertThat(travelInfo.getCommonCode()).isEqualTo(newAdminTravelEntity.getNewTravelCode().getCommonCode());
        assertThat(travelInfo.getTravelTitle()).isEqualTo(newAdminTravelEntity.getTravelTitle());

        // verify
        verify(travelRepository, times(1)).findById(newAdminTravelEntity.getIdx());
        verify(travelRepository, atLeastOnce()).findById(newAdminTravelEntity.getIdx());
        verifyNoMoreInteractions(travelRepository);

        InOrder inOrder = inOrder(travelRepository);
        inOrder.verify(travelRepository).findById(newAdminTravelEntity.getIdx());
    }

    @Test
    @DisplayName("여행지수정BDD테스트")
    void 여행지수정BDD테스트() {
        // given
        TravelEntity newAdminTravelEntity = TravelEntity.builder()
                .idx(travelEntity.getIdx())
                .newTravelCode(commonEntity)
                .travelTitle("여행지 수정 테스트").travelDescription("여행지 수정 테스트").favoriteCount(0).viewCount(0)
                .travelAddress("서울특별시 강남구").travelZipCode("123-456")
                .visible("Y").build();

        // when
        given(travelRepository.findById(newAdminTravelEntity.getIdx())).willReturn(Optional.of(newAdminTravelEntity));
        given(travelRepository.save(newAdminTravelEntity)).willReturn(newAdminTravelEntity);
        TravelDto travelInfo = mockTravelService.updateTravel(newAdminTravelEntity.getIdx(), newAdminTravelEntity);

        // then
        assertThat(travelInfo.getIdx()).isEqualTo(newAdminTravelEntity.getIdx());
        assertThat(travelInfo.getCommonCode()).isEqualTo(newAdminTravelEntity.getNewTravelCode().getCommonCode());
        assertThat(travelInfo.getTravelTitle()).isEqualTo(newAdminTravelEntity.getTravelTitle());

        // verify
        then(travelRepository).should(times(1)).findById(newAdminTravelEntity.getIdx());
        then(travelRepository).should(atLeastOnce()).findById(newAdminTravelEntity.getIdx());
        then(travelRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("여행지삭제테스트")
    void 여행지삭제테스트() {
        // when
        Long deleteIdx = travelService.deleteTravel(travelEntity.getIdx());

        // then
        assertThat(travelEntity.getIdx()).isEqualTo(deleteIdx);
    }

    @Test
    @DisplayName("인기여행지선정테스트")
    void 인기여행지선정테스트() {
        Boolean popular = travelService.togglePopular(travelDTO.getIdx());
        assertThat(popular).isTrue();
    }

    @Test
    @DisplayName("인기여행지선정Mockito테스트")
    void 인기여행지선정Mockito테스트() {
        // given
        travelService.togglePopular(travelDTO.getIdx());

        // when
        when(travelRepository.findByIdx(travelEntity.getIdx())).thenReturn(Optional.ofNullable(travelEntity));
        TravelDto travelInfo = mockTravelService.findOneTravel(travelEntity.getIdx());

        // then
        assertThat(travelInfo.getPopular()).isTrue();

        // verify
        verify(travelRepository, times(1)).findByIdx(travelEntity.getIdx());
        verify(travelRepository, atLeastOnce()).findByIdx(travelEntity.getIdx());
        verifyNoMoreInteractions(travelRepository);

        InOrder inOrder = inOrder(travelRepository);
        inOrder.verify(travelRepository).findByIdx(travelEntity.getIdx());
    }

    @Test
    @DisplayName("인기여행지선정BDD테스트")
    void 인기여행지선정BDD테스트() {
        // given
        travelService.togglePopular(travelDTO.getIdx());

        // when
        given(travelRepository.findByIdx(travelEntity.getIdx())).willReturn(Optional.ofNullable(travelEntity));
        TravelDto travelInfo = mockTravelService.findOneTravel(travelEntity.getIdx());

        // then
        assertThat(travelInfo.getPopular()).isTrue();

        // verify
        then(travelRepository).should(times(1)).findByIdx(travelEntity.getIdx());
        then(travelRepository).should(atLeastOnce()).findByIdx(travelEntity.getIdx());
        then(travelRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("여행지리뷰등록Mockito테스트")
    void 여행지리뷰등록Mockito테스트() {
        // given
        TravelReviewEntity travelReviewEntity = TravelReviewEntity.builder()
                .reviewTitle("리뷰등록테스트")
                .reviewDescription("리뷰등록테스트")
                .viewCount(0)
                .favoriteCount(0)
                .popular(false)
                .visible("Y")
                .build();

        // when
        when(travelRepository.findById(travelEntity.getIdx())).thenReturn(Optional.ofNullable(travelEntity));
        when(reviewRepository.save(travelReviewEntity)).thenReturn(travelReviewEntity);
        TravelReviewDto insertReview = mockTravelService.reviewTravel(travelEntity.getIdx(), travelReviewEntity);

        // then
        assertThat(insertReview.getReviewTitle()).isEqualTo(travelReviewEntity.getReviewTitle());

        // verify
        verify(reviewRepository, times(1)).save(travelReviewEntity);
        verify(reviewRepository, atLeastOnce()).save(travelReviewEntity);
        verifyNoMoreInteractions(reviewRepository);

        InOrder inOrder = inOrder(reviewRepository);
        inOrder.verify(reviewRepository).save(travelReviewEntity);
    }

    @Test
    @DisplayName("여행지댓글수정Mockito테스트")
    void 여행지리뷰수정Mockito테스트() {
        // 댓글 등록
        TravelReviewEntity travelReviewEntity = TravelReviewEntity.builder()
                .reviewTitle("리뷰등록테스트")
                .reviewDescription("리뷰등록테스트")
                .viewCount(0)
                .favoriteCount(0)
                .popular(false)
                .visible("Y")
                .build();

        travelService.reviewTravel(travelEntity.getIdx(), travelReviewEntity);

        // 댓글 수정
        TravelReviewEntity updateTravelReviewEntity = TravelReviewEntity.builder()
                .idx(travelReviewEntity.getIdx())
                .reviewTitle("리뷰수정테스트")
                .reviewDescription("리뷰수정테스트")
                .viewCount(0)
                .favoriteCount(0)
                .newTravelEntity(travelEntity)
                .popular(false)
                .visible("Y")
                .build();

        // when
        when(reviewRepository.findById(updateTravelReviewEntity.getIdx())).thenReturn(Optional.of(updateTravelReviewEntity));
        TravelReviewDto travelReviewDTO = mockTravelService.updateReviewTravel(updateTravelReviewEntity.getIdx(), updateTravelReviewEntity);

        // then
        assertThat(travelReviewDTO.getReviewTitle()).isEqualTo("리뷰수정테스트");
        assertThat(travelReviewDTO.getReviewDescription()).isEqualTo("리뷰수정테스트");

        // verify
        verify(reviewRepository, times(1)).findById(updateTravelReviewEntity.getIdx());
        verify(reviewRepository,  atLeastOnce()).findById(updateTravelReviewEntity.getIdx());
        verifyNoMoreInteractions(reviewRepository);

        InOrder inOrder = inOrder(reviewRepository);
        inOrder.verify(reviewRepository).findById(updateTravelReviewEntity.getIdx());
    }

    @Test
    @DisplayName("여행지 리뷰 삭제 Mockito 테스트")
    void 여행지리뷰삭제Mockito테스트() {
        // given
        // 여행지 리뷰 등록
        TravelReviewEntity travelReviewEntity = TravelReviewEntity.builder()
                .reviewTitle("리뷰등록테스트")
                .reviewDescription("리뷰등록테스트")
                .viewCount(0)
                .favoriteCount(0)
                .popular(false)
                .visible("Y")
                .build();

        travelService.reviewTravel(travelEntity.getIdx(), travelReviewEntity);
        // 여행지 리뷰 삭제
        Long deleteIdx = travelService.deleteReviewTravel(travelReviewEntity.getIdx());

        // then
        assertThat(travelReviewEntity.getIdx()).isEqualTo(deleteIdx);
    }

    @Test
    @DisplayName("여행지 리뷰 리스트 조회 테스트")
    void 여행지리뷰리스트조회테스트() {
        // given
        em.persist(travelEntity);

        TravelReviewEntity travelReviewEntity = TravelReviewEntity.builder()
                .reviewTitle("리뷰등록테스트")
                .reviewDescription("리뷰등록테스트")
                .viewCount(0)
                .favoriteCount(0)
                .popular(false)
                .visible("Y")
                .build();

        travelService.reviewTravel(travelEntity.getIdx(), travelReviewEntity);

        travelService.travelReviewList(travelEntity.getIdx());
    }

    @Test
    @DisplayName("여행지 리뷰 리스트 조회 Mockito 테스트")
    void 여행지리뷰리스트조회Mockito테스트() {
        // given
        em.persist(travelEntity);

        TravelReviewEntity travelReviewEntity = TravelReviewEntity.builder()
                .reviewTitle("리뷰등록테스트")
                .reviewDescription("리뷰등록테스트")
                .viewCount(0)
                .favoriteCount(0)
                .popular(false)
                .visible("Y")
                .build();

        travelService.reviewTravel(travelEntity.getIdx(), travelReviewEntity);
        List<TravelReviewEntity> reviewList = new ArrayList<>();
        reviewList.add(travelReviewEntity);

        // when
        when(reviewRepository.findByNewTravelEntityIdx(travelEntity.getIdx())).thenReturn(reviewList);
        List<TravelReviewDto> findReviewList = mockTravelService.travelReviewList(travelEntity.getIdx());

        // then
        assertThat(findReviewList.get(0).getReviewTitle()).isEqualTo("리뷰등록테스트");
        assertThat(findReviewList.get(0).getReviewDescription()).isEqualTo("리뷰등록테스트");
    }

    @Test
    @DisplayName("여행지 리뷰 상세 조회 Mockito 테스트")
    void 여행지리뷰상세조회Mockito테스트() {
        TravelReviewEntity travelReviewEntity = TravelReviewEntity.builder()
                .reviewTitle("리뷰등록테스트")
                .reviewDescription("리뷰등록테스트")
                .viewCount(0)
                .favoriteCount(0)
                .popular(false)
                .visible("Y")
                .newTravelEntity(travelEntity)
                .build();

        em.persist(travelReviewEntity);

        // when
        when(reviewRepository.findById(travelReviewEntity.getIdx())).thenReturn(Optional.of(travelReviewEntity));
        TravelReviewDto travelReviewInfo = mockTravelService.detailTravelReview(travelReviewEntity.getIdx());

        // then
        assertThat(travelReviewInfo.getReviewTitle()).isEqualTo("리뷰등록테스트");
        assertThat(travelReviewInfo.getReviewDescription()).isEqualTo("리뷰등록테스트");

        // verify
        verify(reviewRepository, times(1)).findById(travelReviewEntity.getIdx());
        verify(reviewRepository,  atLeastOnce()).findById(travelReviewEntity.getIdx());
        verifyNoMoreInteractions(reviewRepository);

        InOrder inOrder = inOrder(reviewRepository);
        inOrder.verify(reviewRepository).findById(travelReviewEntity.getIdx());
    }

    @Test
    @DisplayName("여행지 그룹 리스트 Mockito 조회 테스트")
    void 여행지그룹리스트Mockito조회테스트() {
        // given
        Map<String, Object> groupMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by("idx").descending());

        List<TravelGroupEntity> travelGroupList = new ArrayList<>();
        travelGroupList.add(TravelGroupEntity.builder().travelEntity(travelEntity).groupName("서울모임")
                .groupDescription("서울모임").visible("Y").build());

        Page<TravelGroupEntity> resultPage = new PageImpl<>(travelGroupList, pageRequest, travelGroupList.size());

        // when
        when(groupRepository.findAll(pageRequest)).thenReturn(resultPage);
        List<TravelGroupDto> newTravelGroupList = mockTravelService.findTravelGroupList(groupMap, pageRequest);

        // then
        assertThat(newTravelGroupList.get(0).getIdx()).isEqualTo(travelGroupList.get(0).getIdx());
        assertThat(newTravelGroupList.get(0).getTravelTitle()).isEqualTo(travelGroupList.get(0).getTravelEntity().getTravelTitle());
        assertThat(newTravelGroupList.get(0).getGroupName()).isEqualTo(travelGroupList.get(0).getGroupName());
        assertThat(newTravelGroupList.get(0).getGroupDescription()).isEqualTo(travelGroupList.get(0).getGroupDescription());

        // verify
        verify(groupRepository, times(1)).findAll(pageRequest);
        verify(groupRepository, atLeastOnce()).findAll(pageRequest);
        verifyNoMoreInteractions(groupRepository);

        InOrder inOrder = inOrder(groupRepository);
        inOrder.verify(groupRepository).findAll(pageRequest);
    }

    @Test
    @DisplayName("여행지 그룹 상세 Mockito 테스트")
    void 여행지그룹상세Mockito테스트() {
        // given
        TravelGroupEntity groupEntity = TravelGroupEntity.builder().travelEntity(travelEntity)
                .groupName("서울모임").groupDescription("서울모임").visible("Y").build();

        em.persist(groupEntity);

        // when
        when(groupRepository.findById(groupEntity.getIdx())).thenReturn(Optional.of(groupEntity));
        TravelGroupDto newTravelGroupDTO = mockTravelService.findOneTravelGroup(groupEntity.getIdx());

        // then
        assertThat(newTravelGroupDTO.getIdx()).isEqualTo(groupEntity.getIdx());
        assertThat(newTravelGroupDTO.getTravelTitle()).isEqualTo(groupEntity.getTravelEntity().getTravelTitle());
        assertThat(newTravelGroupDTO.getGroupName()).isEqualTo(groupEntity.getGroupName());
        assertThat(newTravelGroupDTO.getGroupDescription()).isEqualTo(groupEntity.getGroupDescription());

        // verify
        verify(groupRepository, times(1)).findById(groupEntity.getIdx());
        verify(groupRepository, atLeastOnce()).findById(groupEntity.getIdx());
        verifyNoMoreInteractions(groupRepository);

        InOrder inOrder = inOrder(groupRepository);
        inOrder.verify(groupRepository).findById(groupEntity.getIdx());
    }

    @Test
    @DisplayName("여행지 그룹 등록 Mockito 테스트")
    void 여행지그룹등록Mockito테스트() {
        TravelGroupEntity travelGroupEntity = TravelGroupEntity.builder()
                .travelEntity(travelEntity)
                .groupName("서울모임").groupDescription("서울모임").visible("Y").build();

        // when
        when(travelRepository.findById(travelEntity.getIdx())).thenReturn(Optional.ofNullable(travelEntity));
        when(groupRepository.save(travelGroupEntity)).thenReturn(travelGroupEntity);
        TravelGroupDto newTravelGroupDTO = mockTravelService.insertTravelGroup(travelEntity.getIdx(), travelGroupEntity);

        // then
        assertThat(newTravelGroupDTO.getIdx()).isEqualTo(travelGroupEntity.getIdx());
        assertThat(newTravelGroupDTO.getTravelTitle()).isEqualTo(travelGroupEntity.getTravelEntity().getTravelTitle());
        assertThat(newTravelGroupDTO.getGroupName()).isEqualTo(travelGroupEntity.getGroupName());
        assertThat(newTravelGroupDTO.getGroupDescription()).isEqualTo(travelGroupEntity.getGroupDescription());

        // verify
        verify(groupRepository, times(1)).save(travelGroupEntity);
        verify(groupRepository, atLeastOnce()).save(travelGroupEntity);
        verifyNoMoreInteractions(groupRepository);

        InOrder inOrder = inOrder(groupRepository);
        inOrder.verify(groupRepository).save(travelGroupEntity);
    }

    @Test
    @DisplayName("여행지 그룹 수정 Mockito 테스트")
    void 여행지그룹수정Mockito테스트() {
        // given
        TravelGroupEntity travelGroupEntity = TravelGroupEntity.builder()
                .travelEntity(travelEntity)
                .groupName("서울모임").groupDescription("서울모임").visible("Y").build();

        em.persist(travelGroupEntity);

        TravelGroupEntity newTravelGroupEntity = TravelGroupEntity.builder()
                .idx(travelGroupEntity.getIdx())
                .travelEntity(travelEntity)
                .groupName("인천모임").groupDescription("인천모임")
                .visible("Y").build();

        // when
        when(groupRepository.findById(travelGroupEntity.getIdx())).thenReturn(Optional.ofNullable(travelGroupEntity));
        when(groupRepository.save(newTravelGroupEntity)).thenReturn(newTravelGroupEntity);
        TravelGroupDto travelGroupInfo = mockTravelService.updateTravelGroup(newTravelGroupEntity.getIdx(), newTravelGroupEntity);

        // then
        assertThat(travelGroupInfo.getIdx()).isEqualTo(newTravelGroupEntity.getIdx());
        assertThat(travelGroupInfo.getGroupName()).isEqualTo(newTravelGroupEntity.getGroupName());
        assertThat(travelGroupInfo.getGroupDescription()).isEqualTo(newTravelGroupEntity.getGroupDescription());

        // verify
        verify(groupRepository, times(1)).findById(newTravelGroupEntity.getIdx());
        verify(groupRepository, atLeastOnce()).findById(newTravelGroupEntity.getIdx());
        verifyNoMoreInteractions(groupRepository);

        InOrder inOrder = inOrder(groupRepository);
        inOrder.verify(groupRepository).findById(newTravelGroupEntity.getIdx());
    }

    @Test
    @DisplayName("여행지 그룹 삭제 Mockito 테스트")
    void 여행지그룹삭제Mockito테스트() {
        // given
        TravelGroupEntity travelGroupEntity = TravelGroupEntity.builder()
                .travelEntity(travelEntity)
                .groupName("서울모임").groupDescription("서울모임").visible("Y").build();

        em.persist(travelGroupEntity);

        // when
        Long deleteIdx = travelService.deleteTravelGroup(travelGroupEntity.getIdx());

        // then
        assertThat(travelGroupEntity.getIdx()).isEqualTo(deleteIdx);
    }

    @Test
    @DisplayName("여행지 추천 검색어 리스트 조회 테스트")
    void 여행지추천검색어리스트조회테스트() {
        Map<String, Object> travelRecommendMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(0, 3);
        List<String> list = new ArrayList<>();
        list.add("서울");
        list.add("인천");

        TravelRecommendEntity recommendEntity = TravelRecommendEntity.builder()
                .recommendName(list)
                .build();

        travelService.insertTravelRecommend(recommendEntity);

        assertThat(travelService.findTravelRecommendList(travelRecommendMap, pageRequest)).isNotEmpty();
    }

    @Test
    @DisplayName("여행지 추천 검색어 상세 조회 테스트")
    void 여행지추천검색어상세조회테스트() {
        List<String> list = new ArrayList<>();
        list.add("서울");
        list.add("인천");

        TravelRecommendEntity recommendEntity = TravelRecommendEntity.builder()
                .recommendName(list)
                .build();

        TravelRecommendDto travelRecommendDTO = travelService.insertTravelRecommend(recommendEntity);

        TravelRecommendDto oneTravelRecommend = travelService.findOneTravelRecommend(travelRecommendDTO.getIdx());
        assertThat(oneTravelRecommend.getRecommendName()).isEqualTo(list);
    }

    @Test
    @DisplayName("여행지 추천 검색어 등록 테스트")
    void 여행지추천검색어등록테스트() throws JSONException {
        List<String> list = new ArrayList<>();
        list.add("서울");
        list.add("인천");

        TravelRecommendEntity recommendEntity = TravelRecommendEntity.builder()
                .recommendName(list)
                .build();

        TravelRecommendDto travelRecommendDTO = travelService.insertTravelRecommend(recommendEntity);

        JSONArray jsonArray = new JSONArray();
        jsonArray.put(travelRecommendDTO.getRecommendName());
        Object insertObject = jsonArray.get(0);

        assertThat(travelRecommendDTO.getRecommendName()).isEqualTo(insertObject);
    }

    @Test
    @DisplayName("여행지 추천 검색어 수정 테스트")
    void 여행지추천검색어수정테스트() throws JSONException {
        List<String> list = new ArrayList<>();
        list.add("서울");
        list.add("인천");

        TravelRecommendEntity recommendEntity = TravelRecommendEntity.builder()
                .recommendName(list)
                .build();

        TravelRecommendDto travelRecommendDTO = travelService.insertTravelRecommend(recommendEntity);

        list.add("대구");
        recommendEntity = TravelRecommendEntity.builder()
                .idx(travelRecommendDTO.getIdx())
                .recommendName(list)
                .build();
        em.flush();
        em.clear();

        TravelRecommendDto updateRecommendDTO = travelService.updateTravelRecommend(travelRecommendDTO.getIdx(), recommendEntity);

        JSONArray jsonArray = new JSONArray();
        jsonArray.put(updateRecommendDTO.getRecommendName());
        Object insertObject = jsonArray.get(0);

        assertThat(updateRecommendDTO.getRecommendName()).isEqualTo(insertObject);
    }

    @Test
    @DisplayName("여행지 추천 검색어 삭제 테스트")
    void 여행지추천검색어삭제테스트() {
        List<String> list = new ArrayList<>();
        list.add("서울");
        list.add("인천");

        TravelRecommendEntity recommendEntity = TravelRecommendEntity.builder()
                .recommendName(list)
                .build();

        TravelRecommendDto travelRecommendDTO = travelService.insertTravelRecommend(recommendEntity);

        Long deleteIdx = travelService.deleteTravelRecommend(travelRecommendDTO.getIdx());
        em.flush();
        em.clear();

        assertThat(deleteIdx).isEqualTo(travelRecommendDTO.getIdx());
    }

    @Test
    @DisplayName("검색어 랭킹 리스트 조회 테스트")
    void 검색어랭킹리스트조회테스트() {
        em.persist(SearchEntity.builder().searchKeyword("서울").build());
        em.persist(SearchEntity.builder().searchKeyword("서울").build());
        em.persist(SearchEntity.builder().searchKeyword("서울").build());
        em.persist(SearchEntity.builder().searchKeyword("인천").build());

        assertThat(travelService.rankingTravelKeyword().get(0).getSearchKeyword()).isEqualTo("서울");
        assertThat(travelService.rankingTravelKeyword().get(1).getSearchKeyword()).isEqualTo("인천");
    }

    @Test
    @DisplayName("축제 리스트 갯수 그룹 조회")
    void 축제리스트갯수그룹조회() {
        // 등록
        LocalDateTime dateTime = LocalDateTime.now();

        TravelFestivalEntity travelFestivalEntity = TravelFestivalEntity.builder()
                .newFestivalCode(commonEntity)
                .festivalTitle("축제 제목")
                .festivalDescription("축제 내용")
                .festivalMonth(dateTime.getMonthValue())
                .festivalDay(dateTime.getDayOfMonth())
                .festivalTime(dateTime)
                .build();

        em.persist(travelFestivalEntity);
        em.flush();
        em.clear();

        assertThat(travelService.findTravelFestivalGroup(dateTime.getMonthValue())).isNotEmpty();
    }

    @Test
    @DisplayName("축제리스트조회")
    void 축제리스트조회() {
        // 등록
        LocalDateTime dateTime = LocalDateTime.now();

        TravelFestivalEntity travelFestivalEntity = TravelFestivalEntity.builder()
                .newFestivalCode(commonEntity)
                .festivalTitle("축제 제목")
                .festivalDescription("축제 내용")
                .festivalMonth(dateTime.getMonthValue())
                .festivalDay(dateTime.getDayOfMonth())
                .festivalTime(dateTime)
                .build();

        em.persist(travelFestivalEntity);
        em.flush();
        em.clear();

        assertThat(travelService.findTravelFestivalList(travelFestivalEntity)).isNotEmpty();
    }

    @Test
    @DisplayName("축제 상세 조회 테스트")
    void 축제상세조회테스트() {
        // 등록
        LocalDateTime dateTime = LocalDateTime.now();

        TravelFestivalEntity travelFestivalEntity = TravelFestivalEntity.builder()
                .newFestivalCode(commonEntity)
                .festivalTitle("축제 제목")
                .festivalDescription("축제 내용")
                .festivalMonth(dateTime.getMonthValue())
                .festivalDay(dateTime.getDayOfMonth())
                .festivalTime(dateTime)
                .build();

        TravelFestivalDto travelFestivalDTO = travelService.insertTravelFestival(travelFestivalEntity);

        assertThat(travelService.findOneTravelFestival(travelFestivalDTO.getIdx()).getFestivalTitle()).isEqualTo("축제 제목");
    }

    @Test
    @DisplayName("축제 등록 or 수정 테스트")
    void 축제등록or수정테스트() {
        // 등록
        LocalDateTime dateTime = LocalDateTime.now();

        TravelFestivalEntity travelFestivalEntity = TravelFestivalEntity.builder()
                .newFestivalCode(commonEntity)
                .festivalTitle("축제 제목")
                .festivalDescription("축제 내용")
                .festivalMonth(dateTime.getMonthValue())
                .festivalDay(dateTime.getDayOfMonth())
                .festivalTime(dateTime)
                .build();

        TravelFestivalDto travelFestivalDTO = travelService.insertTravelFestival(travelFestivalEntity);
        assertThat(travelFestivalDTO.getFestivalTitle()).isEqualTo("축제 제목");

        travelFestivalEntity = TravelFestivalEntity.builder()
                .idx(travelFestivalDTO.getIdx())
                .newFestivalCode(commonEntity)
                .festivalTitle("축제 수정 제목")
                .festivalDescription("축제 수정 내용")
                .festivalMonth(dateTime.getMonthValue())
                .festivalDay(dateTime.getDayOfMonth())
                .festivalTime(dateTime)
                .build();

        TravelFestivalDto updateFestival = travelService.updateTravelFestival(travelFestivalDTO.getIdx(), travelFestivalEntity);
        assertThat(updateFestival.getFestivalTitle()).isEqualTo("축제 수정 제목");
    }

    @Test
    @DisplayName("축제 삭제 테스트")
    void 축제삭제테스트() {
        // 등록
        LocalDateTime dateTime = LocalDateTime.now();

        TravelFestivalEntity travelFestivalEntity = TravelFestivalEntity.builder()
                .newFestivalCode(commonEntity)
                .festivalTitle("축제 제목")
                .festivalDescription("축제 내용")
                .festivalMonth(dateTime.getMonthValue())
                .festivalDay(dateTime.getDayOfMonth())
                .festivalTime(dateTime)
                .build();

        TravelFestivalDto travelFestivalDTO = travelService.insertTravelFestival(travelFestivalEntity);

        // 삭제
        Long deleteIdx = travelService.deleteTravelFestival(travelFestivalDTO.getIdx());
        assertThat(deleteIdx).isEqualTo(travelFestivalDTO.getIdx());
    }

    @Test
    @DisplayName("여행 예약 리스트 조회 테스트")
    void 여행예약리스트조회테스트() {
        assertThat(travelService.findTravelReservationList()).isNotEmpty();
        assertThat(travelService.findTravelReservationList().get(0).getTitle()).isEqualTo("예약 등록지");
        assertThat(travelService.findTravelReservationList().get(0).getDescription()).isEqualTo("예약 등록지");
        assertThat(travelService.findTravelReservationList().get(0).getPrice()).isEqualTo(50000);
    }

    @Test
    @DisplayName("여행 예약 상세 조회")
    void 여행예약상세조회테스트() {
        TravelReservationDto oneTravelReservation = travelService.findOneTravelReservation(travelReservationDTO.getIdx());
        assertThat(oneTravelReservation.getTitle()).isEqualTo("예약 등록지");
        assertThat(oneTravelReservation.getDescription()).isEqualTo("예약 등록지");
        assertThat(oneTravelReservation.getPrice()).isEqualTo(50000);
        assertThat(oneTravelReservation.getAddress()).isEqualTo("서울 강남구");
        assertThat(oneTravelReservation.getZipCode()).isEqualTo("123-456");
    }

    @Test
    @DisplayName("여행 예약 등록 테스트")
    void 여행예약등록테스트() {
        // given
        TravelReservationEntity insertReservation = TravelReservationEntity.builder()
                .commonEntity(commonEntity)
                .title("예약 등록지")
                .description("예약 등록지")
                .address("서울 강남구")
                .zipCode("123-456")
                .price(50000)
                .possibleCount(10)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .status(true)
                .popular(false)
                .build();

        // when
        TravelReservationDto reservationDTO = travelService.insertTravelReservation(insertReservation);

        // then
        assertThat(reservationDTO.getTitle()).isEqualTo("예약 등록지");
        assertThat(reservationDTO.getDescription()).isEqualTo("예약 등록지");
        assertThat(reservationDTO.getPrice()).isEqualTo(50000);
    }

    @Test
    @DisplayName("여행 예약 수정 테스트")
    void 여행예약수정테스트() {
        // given
        TravelReservationDto oneTravelReservation = travelService.findOneTravelReservation(travelReservationDTO.getIdx());

        TravelReservationEntity updateReservation = TravelReservationEntity.builder()
                .idx(oneTravelReservation.getIdx())
                .commonEntity(commonEntity)
                .title("예약 수정지")
                .description("예약 수정지")
                .address("서울 강남구")
                .zipCode("123-456")
                .price(40000)
                .possibleCount(10)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .status(true)
                .popular(false)
                .build();

        // when
        TravelReservationDto updateTravelReservation = travelService.updateTravelReservation(oneTravelReservation.getIdx(), updateReservation);

        // then
        assertThat(updateTravelReservation.getTitle()).isEqualTo("예약 수정지");
        assertThat(updateTravelReservation.getDescription()).isEqualTo("예약 수정지");
        assertThat(updateTravelReservation.getPrice()).isEqualTo(40000);
    }

    @Test
    @DisplayName("여행 예약 삭제 테스트")
    void 여행예약삭제테스트() {
        Long deleteIdx = travelService.deleteTravelReservation(travelReservationDTO.getIdx());
        assertThat(deleteIdx).isEqualTo(travelReservationDTO.getIdx());
    }
}

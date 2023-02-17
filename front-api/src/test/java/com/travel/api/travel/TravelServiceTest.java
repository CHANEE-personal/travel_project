package com.travel.api.travel;

import com.travel.api.FrontCommonServiceTest;
import com.travel.api.travel.domain.TravelDTO;
import com.travel.api.travel.domain.festival.TravelFestivalEntity;
import com.travel.api.travel.domain.recommend.TravelRecommendDTO;
import com.travel.api.travel.domain.recommend.TravelRecommendEntity;
import com.travel.api.travel.domain.review.TravelReviewDTO;
import com.travel.api.travel.domain.review.TravelReviewEntity;
import com.travel.api.travel.domain.review.repository.ReviewRepository;
import com.travel.api.travel.domain.search.SearchEntity;
import com.travel.exception.TravelException;
import lombok.RequiredArgsConstructor;
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
class TravelServiceTest extends FrontCommonServiceTest {

    @Mock
    private TravelRepository travelRepository;
    @Mock
    private TravelQueryRepository travelQueryRepository;
    @Mock
    private ReviewRepository reviewRepository;
    @InjectMocks
    private TravelService mockTravelService;
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

        List<TravelDTO> travelList = new ArrayList<>();
        travelList.add(TravelDTO.builder().idx(1L).commonCode(commonDTO.getCommonCode())
                .commonName(commonDTO.getCommonName())
                .travelTitle("여행지 소개").travelDescription("여행지 소개")
                .travelAddress("인천광역시 서구").travelZipCode("123-456").visible("Y").build());

        Page<TravelDTO> resultPage = new PageImpl<>(travelList, pageRequest, travelList.size());

        // when
        when(travelQueryRepository.findTravelList(travelMap, pageRequest)).thenReturn(resultPage);
        Page<TravelDTO> newTravelList = mockTravelService.findTravelList(travelMap, pageRequest);

        List<TravelDTO> findTravelList = newTravelList.stream().collect(Collectors.toList());

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

        List<TravelDTO> travelList = new ArrayList<>();
        travelList.add(TravelDTO.builder().idx(1L)
                .commonCode(commonDTO.getCommonCode())
                .commonName(commonDTO.getCommonName())
                .travelTitle("여행지 소개").travelDescription("여행지 소개")
                .travelAddress("인천광역시 서구").travelZipCode("123-456").visible("Y").build());

        Page<TravelDTO> resultPage = new PageImpl<>(travelList, pageRequest, travelList.size());

        // when
        when(travelQueryRepository.findTravelList(travelMap, pageRequest)).thenReturn(resultPage);
        Page<TravelDTO> newTravelList = mockTravelService.findTravelList(travelMap, pageRequest);

        List<TravelDTO> findTravelList = newTravelList.stream().collect(Collectors.toList());

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

        List<TravelDTO> travelList = new ArrayList<>();
        travelList.add(TravelDTO.builder().idx(1L).commonCode(commonDTO.getCommonCode())
                .commonName(commonDTO.getCommonName())
                .travelTitle("여행지 소개").travelDescription("여행지 소개")
                .travelAddress("인천광역시 서구").travelZipCode("123-456").visible("Y").build());

        Page<TravelDTO> resultPage = new PageImpl<>(travelList, pageRequest, travelList.size());

        // when
        given(travelQueryRepository.findTravelList(travelMap, pageRequest)).willReturn(resultPage);
        Page<TravelDTO> newTravelList = mockTravelService.findTravelList(travelMap, pageRequest);

        List<TravelDTO> findTravelList = newTravelList.stream().collect(Collectors.toList());

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
        TravelDTO existTravel = travelService.findOneTravel(travelEntity.getIdx());
        assertThat(existTravel.getIdx()).isEqualTo(travelEntity.getIdx());
        assertThat(existTravel.getCommonCode()).isEqualTo(travelEntity.getNewTravelCode().getCommonCode());
        assertThat(existTravel.getTravelTitle()).isEqualTo(travelEntity.getTravelTitle());

        assertThatThrownBy(() -> travelService.findOneTravel(3L))
                .isInstanceOf(TravelException.class).hasMessage("여행 상세 없음");
    }

    @Test
    @DisplayName("여행지소개상세Mockito테스트")
    void 여행지소개상세Mockito테스트() {
        // given
        // 조회 수 관련 테스트
        TravelDTO oneTravel = travelService.findOneTravel(travelEntity.getIdx());
        assertThat(travelEntity.getViewCount()).isEqualTo(oneTravel.getViewCount());

        // when
        when(travelRepository.findByIdx(travelEntity.getIdx())).thenReturn(Optional.ofNullable(travelEntity));
        TravelDTO newAdminTravel = mockTravelService.findOneTravel(travelEntity.getIdx());

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
        // given
        // 조회 수 관련 테스트
        TravelDTO oneTravel = travelService.findOneTravel(travelEntity.getIdx());
        assertThat(travelEntity.getViewCount()).isEqualTo(oneTravel.getViewCount());

        // when
        given(travelRepository.findByIdx(travelEntity.getIdx())).willReturn(Optional.ofNullable(travelEntity));
        TravelDTO newAdminTravel = mockTravelService.findOneTravel(travelEntity.getIdx());

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
    @DisplayName("여행지좋아요테스트")
    void 여행지좋아요테스트() {
        em.persist(travelEntity);
        em.flush();
        em.clear();
        int favoriteCount = travelService.favoriteTravel(travelEntity.getIdx());
        assertThat(favoriteCount).isEqualTo(travelEntity.getFavoriteCount() + 1);
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
        TravelReviewDTO insertReview = mockTravelService.reviewTravel(travelEntity.getIdx(), travelReviewEntity);

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
        TravelReviewDTO travelReviewDTO = mockTravelService.updateReviewTravel(updateTravelReviewEntity.getIdx(), updateTravelReviewEntity);

        // then
        assertThat(travelReviewDTO.getReviewTitle()).isEqualTo("리뷰수정테스트");
        assertThat(travelReviewDTO.getReviewDescription()).isEqualTo("리뷰수정테스트");

        // verify
        verify(reviewRepository, times(1)).findById(updateTravelReviewEntity.getIdx());
        verify(reviewRepository, atLeastOnce()).findById(updateTravelReviewEntity.getIdx());
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
        List<TravelReviewDTO> findReviewList = mockTravelService.travelReviewList(travelEntity.getIdx());

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
        TravelReviewDTO travelReviewInfo = mockTravelService.detailTravelReview(travelReviewEntity.getIdx());

        // then
        assertThat(travelReviewInfo.getReviewTitle()).isEqualTo("리뷰등록테스트");
        assertThat(travelReviewInfo.getReviewDescription()).isEqualTo("리뷰등록테스트");

        // verify
        verify(reviewRepository, times(1)).findById(travelReviewEntity.getIdx());
        verify(reviewRepository, atLeastOnce()).findById(travelReviewEntity.getIdx());
        verifyNoMoreInteractions(reviewRepository);

        InOrder inOrder = inOrder(reviewRepository);
        inOrder.verify(reviewRepository).findById(travelReviewEntity.getIdx());
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

        em.persist(recommendEntity);
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

        em.persist(recommendEntity);
        TravelRecommendDTO oneTravelRecommend = travelService.findOneTravelRecommend(recommendEntity.getIdx());
        assertThat(oneTravelRecommend.getRecommendName()).isEqualTo(list);
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
    @DisplayName("추천 검색어 or 검색어 랭킹을 통한 여행지 검색 조회")
    void 추천검색어or검색어랭킹을통한여행지검색조회() {
        assertThat(travelService.findTravelKeyword("서울").get(0).getTravelTitle()).isEqualTo("서울 여행지");
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

        em.persist(travelFestivalEntity);

        assertThat(travelService.findOneTravelFestival(travelFestivalEntity.getIdx()).getFestivalTitle()).isEqualTo("축제 제목");
    }
}

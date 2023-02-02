package com.travel.api.travel;

import com.travel.api.AdminCommonServiceTest;
import com.travel.api.travel.domain.TravelDto;
import com.travel.api.travel.domain.TravelEntity;
import com.travel.api.travel.domain.festival.TravelFestivalDto;
import com.travel.api.travel.domain.festival.TravelFestivalEntity;
import com.travel.api.travel.domain.group.TravelGroupDto;
import com.travel.api.travel.domain.group.TravelGroupEntity;
import com.travel.api.travel.domain.recommend.TravelRecommendDto;
import com.travel.api.travel.domain.recommend.TravelRecommendEntity;
import com.travel.api.travel.domain.review.TravelReviewDto;
import com.travel.api.travel.domain.review.TravelReviewEntity;
import com.travel.api.travel.domain.search.SearchEntity;
import com.travel.exception.TravelException;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
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
class TravelServiceTest extends AdminCommonServiceTest {
    @Mock
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

        List<TravelDto> travelList = new ArrayList<>();
        travelList.add(TravelDto.builder().idx(1L).newTravelCode(commonDTO)
                .travelTitle("여행지 소개").travelDescription("여행지 소개")
                .travelAddress("인천광역시 서구").travelZipCode("123-456").visible("Y").build());

        Page<TravelDto> resultPage = new PageImpl<>(travelList, pageRequest, travelList.size());

        // when
        when(mockTravelService.findTravelList(travelMap, pageRequest)).thenReturn(resultPage);
        Page<TravelDto> newTravelList = mockTravelService.findTravelList(travelMap, pageRequest);

        List<TravelDto> findTravelList = newTravelList.stream().collect(Collectors.toList());

        // then
        assertThat(findTravelList.get(0).getIdx()).isEqualTo(travelList.get(0).getIdx());
        assertThat(findTravelList.get(0).getNewTravelCode().getCommonCode()).isEqualTo(travelList.get(0).getNewTravelCode().getCommonCode());
        assertThat(findTravelList.get(0).getTravelTitle()).isEqualTo(travelList.get(0).getTravelTitle());
        assertThat(findTravelList.get(0).getTravelDescription()).isEqualTo(travelList.get(0).getTravelDescription());
        assertThat(findTravelList.get(0).getTravelAddress()).isEqualTo(travelList.get(0).getTravelAddress());
        assertThat(findTravelList.get(0).getTravelZipCode()).isEqualTo(travelList.get(0).getTravelZipCode());

        // verify
        verify(mockTravelService, times(1)).findTravelList(travelMap, pageRequest);
        verify(mockTravelService, atLeastOnce()).findTravelList(travelMap, pageRequest);
        verifyNoMoreInteractions(mockTravelService);

        InOrder inOrder = inOrder(mockTravelService);
        inOrder.verify(mockTravelService).findTravelList(travelMap, pageRequest);
    }

    @Test
    @DisplayName("여행지 소개 리스트 Mockito 조회 테스트")
    void 여행지소개리스트Mockito조회테스트() {
        // given
        Map<String, Object> travelMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(0, 3);

        List<TravelDto> travelList = new ArrayList<>();
        travelList.add(TravelDto.builder().idx(1L).newTravelCode(commonDTO)
                .travelTitle("여행지 소개").travelDescription("여행지 소개")
                .travelAddress("인천광역시 서구").travelZipCode("123-456").visible("Y").build());

        Page<TravelDto> resultPage = new PageImpl<>(travelList, pageRequest, travelList.size());

        // when
        when(mockTravelService.findTravelList(travelMap, pageRequest)).thenReturn(resultPage);
        Page<TravelDto> newTravelList = mockTravelService.findTravelList(travelMap, pageRequest);

        List<TravelDto> findTravelList = newTravelList.stream().collect(Collectors.toList());

        // then
        assertThat(findTravelList.get(0).getIdx()).isEqualTo(travelList.get(0).getIdx());
        assertThat(findTravelList.get(0).getNewTravelCode().getCommonCode()).isEqualTo(travelList.get(0).getNewTravelCode().getCommonCode());
        assertThat(findTravelList.get(0).getTravelTitle()).isEqualTo(travelList.get(0).getTravelTitle());
        assertThat(findTravelList.get(0).getTravelDescription()).isEqualTo(travelList.get(0).getTravelDescription());
        assertThat(findTravelList.get(0).getTravelAddress()).isEqualTo(travelList.get(0).getTravelAddress());
        assertThat(findTravelList.get(0).getTravelZipCode()).isEqualTo(travelList.get(0).getTravelZipCode());

        // verify
        verify(mockTravelService, times(1)).findTravelList(travelMap, pageRequest);
        verify(mockTravelService, atLeastOnce()).findTravelList(travelMap, pageRequest);
        verifyNoMoreInteractions(mockTravelService);

        InOrder inOrder = inOrder(mockTravelService);
        inOrder.verify(mockTravelService).findTravelList(travelMap, pageRequest);
    }

    @Test
    @DisplayName("여행지 소개 리스트 BDD 조회 테스트")
    void 여행지소개리스트BDD조회테스트() {
        Map<String, Object> travelMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(0, 3);

        List<TravelDto> travelList = new ArrayList<>();
        travelList.add(TravelDto.builder().idx(1L).newTravelCode(commonDTO)
                .travelTitle("여행지 소개").travelDescription("여행지 소개")
                .travelAddress("인천광역시 서구").travelZipCode("123-456").visible("Y").build());

        Page<TravelDto> resultPage = new PageImpl<>(travelList, pageRequest, travelList.size());

        // when
        given(mockTravelService.findTravelList(travelMap, pageRequest)).willReturn(resultPage);
        Page<TravelDto> newTravelList = mockTravelService.findTravelList(travelMap, pageRequest);

        List<TravelDto> findTravelList = newTravelList.stream().collect(Collectors.toList());

        // then
        assertThat(findTravelList.get(0).getIdx()).isEqualTo(travelList.get(0).getIdx());
        assertThat(findTravelList.get(0).getNewTravelCode().getCommonCode()).isEqualTo(travelList.get(0).getNewTravelCode().getCommonCode());
        assertThat(findTravelList.get(0).getTravelTitle()).isEqualTo(travelList.get(0).getTravelTitle());
        assertThat(findTravelList.get(0).getTravelDescription()).isEqualTo(travelList.get(0).getTravelDescription());
        assertThat(findTravelList.get(0).getTravelAddress()).isEqualTo(travelList.get(0).getTravelAddress());
        assertThat(findTravelList.get(0).getTravelZipCode()).isEqualTo(travelList.get(0).getTravelZipCode());


        // verify
        then(mockTravelService).should(times(1)).findTravelList(travelMap, pageRequest);
        then(mockTravelService).should(atLeastOnce()).findTravelList(travelMap, pageRequest);
        then(mockTravelService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("여행지상세조회테스트")
    void 여행지상세조회테스트() {
        TravelDto existTravel = travelService.findOneTravel(travelDTO.getIdx());
        assertThat(existTravel.getIdx()).isEqualTo(travelDTO.getIdx());
        assertThat(existTravel.getNewTravelCode().getCommonCode()).isEqualTo(travelDTO.getNewTravelCode().getCommonCode());
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
        TravelDto oneTravel = travelService.findOneTravel(travelDTO.getIdx());
        assertThat(travelDTO.getViewCount() + 1).isEqualTo(oneTravel.getViewCount());

        // when
        when(mockTravelService.findOneTravel(travelDTO.getIdx())).thenReturn(travelDTO);
        TravelDto newAdminTravel = mockTravelService.findOneTravel(travelDTO.getIdx());

        // then
        assertThat(newAdminTravel.getIdx()).isEqualTo(travelDTO.getIdx());
        assertThat(newAdminTravel.getNewTravelCode().getCommonCode()).isEqualTo(travelDTO.getNewTravelCode().getCommonCode());
        assertThat(newAdminTravel.getTravelTitle()).isEqualTo(travelDTO.getTravelTitle());
        assertThat(newAdminTravel.getTravelDescription()).isEqualTo(travelDTO.getTravelDescription());
        assertThat(newAdminTravel.getTravelAddress()).isEqualTo(travelDTO.getTravelAddress());
        assertThat(newAdminTravel.getTravelZipCode()).isEqualTo(travelDTO.getTravelZipCode());

        // verify
        verify(mockTravelService, times(1)).findOneTravel(travelDTO.getIdx());
        verify(mockTravelService, atLeastOnce()).findOneTravel(travelDTO.getIdx());
        verifyNoMoreInteractions(mockTravelService);

        InOrder inOrder = inOrder(mockTravelService);
        inOrder.verify(mockTravelService).findOneTravel(travelDTO.getIdx());
    }

    @Test
    @DisplayName("여행지소개상세BDD테스트")
    void 여행지소개상세BDD테스트() {
        // when
        when(mockTravelService.findOneTravel(travelDTO.getIdx())).thenReturn(travelDTO);
        TravelDto newAdminTravel = mockTravelService.findOneTravel(travelDTO.getIdx());

        // then
        assertThat(newAdminTravel.getIdx()).isEqualTo(travelDTO.getIdx());
        assertThat(newAdminTravel.getNewTravelCode().getCommonCode()).isEqualTo(travelDTO.getNewTravelCode().getCommonCode());
        assertThat(newAdminTravel.getTravelTitle()).isEqualTo(travelDTO.getTravelTitle());
        assertThat(newAdminTravel.getTravelDescription()).isEqualTo(travelDTO.getTravelDescription());
        assertThat(newAdminTravel.getTravelAddress()).isEqualTo(travelDTO.getTravelAddress());
        assertThat(newAdminTravel.getTravelZipCode()).isEqualTo(travelDTO.getTravelZipCode());

        // verify
        then(mockTravelService).should(times(1)).findOneTravel(travelDTO.getIdx());
        then(mockTravelService).should(atLeastOnce()).findOneTravel(travelDTO.getIdx());
        then(mockTravelService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("여행지등록Mockito테스트")
    void 여행지등록Mockito테스트() {
        // given
        TravelEntity insertTravelEntity = TravelEntity.builder()
                .newTravelCode(commonEntity)
                .travelTitle("여행지 테스트").travelDescription("여행지 테스트").favoriteCount(0).viewCount(0)
                .travelAddress("인천광역시 서구").travelZipCode("123-456").visible("Y")
                .build();

        TravelDto travelInfo = travelService.insertTravel(insertTravelEntity);

        // when
        when(mockTravelService.findOneTravel(travelInfo.getIdx())).thenReturn(travelInfo);
        TravelDto newAdminTravel = mockTravelService.findOneTravel(travelInfo.getIdx());

        // then
        assertThat(newAdminTravel.getNewTravelCode().getCommonCode()).isEqualTo(insertTravelEntity.getNewTravelCode().getCommonCode());
        assertThat(newAdminTravel.getTravelTitle()).isEqualTo(travelInfo.getTravelTitle());
        assertThat(newAdminTravel.getTravelDescription()).isEqualTo(travelInfo.getTravelDescription());
        assertThat(newAdminTravel.getTravelAddress()).isEqualTo(travelInfo.getTravelAddress());
        assertThat(newAdminTravel.getTravelZipCode()).isEqualTo(travelInfo.getTravelZipCode());

        // verify
        verify(mockTravelService, times(1)).findOneTravel(travelInfo.getIdx());
        verify(mockTravelService, atLeastOnce()).findOneTravel(travelInfo.getIdx());
        verifyNoMoreInteractions(mockTravelService);

        InOrder inOrder = inOrder(mockTravelService);
        inOrder.verify(mockTravelService).findOneTravel(travelInfo.getIdx());
    }

    @Test
    @DisplayName("여행지등록BDD테스트")
    void 여행지등록BDD테스트() {
        // given
        TravelEntity insertTravelEntity = TravelEntity.builder()
                .newTravelCode(commonEntity)
                .travelTitle("여행지 테스트").travelDescription("여행지 테스트").favoriteCount(0).viewCount(0)
                .travelAddress("인천광역시 서구").travelZipCode("123-456").visible("Y")
                .build();

        TravelDto travelInfo = travelService.insertTravel(insertTravelEntity);

        // when
        given(mockTravelService.findOneTravel(travelInfo.getIdx())).willReturn(travelInfo);
        TravelDto newAdminTravel = mockTravelService.findOneTravel(travelInfo.getIdx());

        // then
        assertThat(newAdminTravel.getNewTravelCode().getCommonCode()).isEqualTo(insertTravelEntity.getNewTravelCode().getCommonCode());
        assertThat(newAdminTravel.getTravelTitle()).isEqualTo(travelInfo.getTravelTitle());
        assertThat(newAdminTravel.getTravelDescription()).isEqualTo(travelInfo.getTravelDescription());
        assertThat(newAdminTravel.getTravelAddress()).isEqualTo(travelInfo.getTravelAddress());
        assertThat(newAdminTravel.getTravelZipCode()).isEqualTo(travelInfo.getTravelZipCode());

        // verify
        then(mockTravelService).should(times(1)).findOneTravel(travelInfo.getIdx());
        then(mockTravelService).should(atLeastOnce()).findOneTravel(travelInfo.getIdx());
        then(mockTravelService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("여행지수정테스트")
    void 여행지수정테스트() {
        // given
        travelEntity = TravelEntity.builder()
                .newTravelCode(commonEntity)
                .travelTitle("여행지 테스트").travelDescription("여행지 테스트").favoriteCount(0).viewCount(0)
                .travelAddress("인천광역시 서구").travelZipCode("123-456").visible("Y")
                .build();

        TravelEntity travelEntity1 = TravelEntity.builder()
                .newTravelCode(commonEntity)
                .travelTitle("여행지 테스트1").travelDescription("여행지 테스트1").favoriteCount(0).viewCount(0)
                .travelAddress("인천광역시 계양구").travelZipCode("123-456").visible("Y")
                .build();

        TravelDto adminTravelDTO = travelService.insertTravel(travelEntity);
        travelService.insertTravel(travelEntity1);

        TravelEntity newAdminTravelEntity = TravelEntity.builder()
                .idx(adminTravelDTO.getIdx())
                .newTravelCode(commonEntity)
                .travelTitle("여행지 수정 테스트").travelDescription("여행지 수정 테스트").favoriteCount(0).viewCount(0)
                .travelAddress("서울특별시 강남구").travelZipCode("123-456")
                .visible("Y").build();

        travelService.updateTravel(adminTravelDTO.getIdx(), newAdminTravelEntity);

        TravelDto oneTravel = travelService.findOneTravel(newAdminTravelEntity.getIdx());
        TravelDto secondTravel = travelService.findOneTravel(travelEntity1.getIdx());

        assertThat(oneTravel.getTravelAddress()).isEqualTo("서울특별시 강남구");
        assertThat(secondTravel.getTravelAddress()).isEqualTo("인천광역시 계양구");
    }

    @Test
    @DisplayName("여행지수정Mockito테스트")
    void 여행지수정Mockito테스트() {
        // given
        travelEntity = TravelEntity.builder()
                .newTravelCode(commonEntity)
                .travelTitle("여행지 테스트").travelDescription("여행지 테스트").favoriteCount(0).viewCount(0)
                .travelAddress("인천광역시 서구").travelZipCode("123-456").visible("Y")
                .build();

        TravelDto adminTravelDTO = travelService.insertTravel(travelEntity);

        TravelEntity newAdminTravelEntity = TravelEntity.builder()
                .idx(adminTravelDTO.getIdx())
                .newTravelCode(commonEntity)
                .travelTitle("여행지 수정 테스트").travelDescription("여행지 수정 테스트").favoriteCount(0).viewCount(0)
                .travelAddress("서울특별시 강남구").travelZipCode("123-456")
                .visible("Y").build();

        travelService.updateTravel(adminTravelDTO.getIdx(), newAdminTravelEntity);

        TravelDto newAdminTravelDTO = TravelEntity.toDto(newAdminTravelEntity);

        // when
        when(mockTravelService.findOneTravel(newAdminTravelEntity.getIdx())).thenReturn(newAdminTravelDTO);
        TravelDto travelInfo = mockTravelService.findOneTravel(newAdminTravelDTO.getIdx());

        // then
        assertThat(travelInfo.getIdx()).isEqualTo(newAdminTravelDTO.getIdx());
        assertThat(travelInfo.getNewTravelCode().getCommonCode()).isEqualTo(newAdminTravelDTO.getNewTravelCode().getCommonCode());
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
                .newTravelCode(commonEntity)
                .travelTitle("여행지 테스트").travelDescription("여행지 테스트").favoriteCount(0).viewCount(0)
                .travelAddress("인천광역시 서구").travelZipCode("123-456").visible("Y")
                .build();

        TravelDto adminTravelDTO = travelService.insertTravel(travelEntity);

        TravelEntity newAdminTravelEntity = TravelEntity.builder()
                .idx(adminTravelDTO.getIdx())
                .newTravelCode(commonEntity)
                .travelTitle("여행지 수정 테스트").travelDescription("여행지 수정 테스트").favoriteCount(0).viewCount(0)
                .travelAddress("서울특별시 강남구").travelZipCode("123-456")
                .visible("Y").build();

        travelService.updateTravel(adminTravelDTO.getIdx(), newAdminTravelEntity);

        TravelDto newAdminTravelDTO = TravelEntity.toDto(newAdminTravelEntity);

        // when
        given(mockTravelService.findOneTravel(newAdminTravelEntity.getIdx())).willReturn(newAdminTravelDTO);
        TravelDto travelInfo = mockTravelService.findOneTravel(newAdminTravelDTO.getIdx());

        // then
        assertThat(travelInfo.getIdx()).isEqualTo(newAdminTravelDTO.getIdx());
        assertThat(travelInfo.getNewTravelCode().getCommonCode()).isEqualTo(newAdminTravelDTO.getNewTravelCode().getCommonCode());
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
    @DisplayName("인기여행지선정테스트")
    void 인기여행지선정테스트() {
        Boolean popular = travelService.togglePopular(travelDTO.getIdx());
        assertThat(popular).isTrue();
    }

    @Test
    @DisplayName("인기여행지선정Mockito테스트")
    void 인기여행지선정Mockito테스트() {
        // given
        Boolean popular = travelService.togglePopular(travelDTO.getIdx());

        travelEntity = TravelEntity.builder()
                .travelTitle("여행지 테스트").travelDescription("여행지 테스트").favoriteCount(1).viewCount(0)
                .travelAddress("인천광역시 서구").travelZipCode("123-456").visible("Y").popular(popular)
                .build();

        TravelDto travelDTO = TravelEntity.toDto(travelEntity);

        // when
        when(mockTravelService.findOneTravel(travelEntity.getIdx())).thenReturn(travelDTO);
        TravelDto travelInfo = mockTravelService.findOneTravel(travelEntity.getIdx());

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
        Boolean popular = travelService.togglePopular(travelDTO.getIdx());

        travelEntity = TravelEntity.builder()
                .travelTitle("여행지 테스트").travelDescription("여행지 테스트").favoriteCount(1).viewCount(0)
                .travelAddress("인천광역시 서구").travelZipCode("123-456").visible("Y").popular(popular)
                .build();

        TravelDto travelDTO = TravelEntity.toDto(travelEntity);

        // when
        given(mockTravelService.findOneTravel(travelDTO.getIdx())).willReturn(travelDTO);
        TravelDto travelInfo = mockTravelService.findOneTravel(travelDTO.getIdx());

        // then
        assertThat(travelInfo.getPopular()).isTrue();

        // verify
        then(mockTravelService).should(times(1)).findOneTravel(travelDTO.getIdx());
        then(mockTravelService).should(atLeastOnce()).findOneTravel(travelDTO.getIdx());
        then(mockTravelService).shouldHaveNoMoreInteractions();
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

        TravelReviewDto travelReviewDTO = travelService.reviewTravel(travelDTO.getIdx(), travelReviewEntity);
        List<TravelReviewDto> reviewList = new ArrayList<>();
        reviewList.add(travelReviewDTO);

        travelDTO = TravelDto.builder()
                .newTravelCode(commonDTO)
                .travelTitle("여행지 테스트").travelDescription("여행지 테스트").favoriteCount(1).viewCount(0)
                .travelAddress("인천광역시 서구").travelZipCode("123-456").visible("Y").popular(false)
                .reviewList(reviewList)
                .build();

        // when
        when(mockTravelService.findOneTravel(travelDTO.getIdx())).thenReturn(travelDTO);
        TravelDto newTravelInfo = mockTravelService.findOneTravel(travelDTO.getIdx());

        // then
        assertThat(newTravelInfo.getReviewList().get(0).getReviewTitle()).isEqualTo(reviewList.get(0).getReviewTitle());

        // verify
        verify(mockTravelService, times(1)).findOneTravel(travelDTO.getIdx());
        verify(mockTravelService, atLeastOnce()).findOneTravel(travelDTO.getIdx());
        verifyNoMoreInteractions(mockTravelService);

        InOrder inOrder = inOrder(mockTravelService);
        inOrder.verify(mockTravelService).findOneTravel(travelDTO.getIdx());
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

        TravelReviewDto reviewDTO = travelService.reviewTravel(travelDTO.getIdx(), travelReviewEntity);

        // 댓글 수정
        travelReviewEntity = TravelReviewEntity.builder()
                .idx(reviewDTO.getIdx())
                .reviewTitle("리뷰수정테스트")
                .reviewDescription("리뷰수정테스트")
                .viewCount(0)
                .favoriteCount(0)
                .newTravelEntity(travelEntity)
                .popular(false)
                .visible("Y")
                .build();

        TravelReviewDto travelReviewDTO = travelService.updateReviewTravel(reviewDTO.getIdx(), travelReviewEntity);
        List<TravelReviewDto> reviewList = new ArrayList<>();
        reviewList.add(travelReviewDTO);

        travelDTO = TravelDto.builder()
                .newTravelCode(commonDTO)
                .travelTitle("여행지 테스트").travelDescription("여행지 테스트").favoriteCount(1).viewCount(0)
                .travelAddress("인천광역시 서구").travelZipCode("123-456").visible("Y").popular(false)
                .reviewList(reviewList)
                .build();

        // when
        when(mockTravelService.travelReviewList(travelDTO.getIdx())).thenReturn(reviewList);
        List<TravelReviewDto> reviewDTOList = mockTravelService.travelReviewList(travelDTO.getIdx());

        // then
        assertThat(reviewDTOList.get(0).getReviewTitle()).isEqualTo("리뷰수정테스트");
        assertThat(reviewDTOList.get(0).getReviewDescription()).isEqualTo("리뷰수정테스트");

        // verify
        verify(mockTravelService, times(1)).travelReviewList(travelDTO.getIdx());
        verify(mockTravelService,  atLeastOnce()).travelReviewList(travelDTO.getIdx());
        verifyNoMoreInteractions(mockTravelService);

        InOrder inOrder = inOrder(mockTravelService);
        inOrder.verify(mockTravelService).travelReviewList(travelDTO.getIdx());
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

        TravelReviewDto travelReviewDTO = travelService.reviewTravel(travelDTO.getIdx(), travelReviewEntity);
        // 여행지 리뷰 삭제
        Long deleteIdx = travelService.deleteReviewTravel(travelReviewDTO.getIdx());

        // then
        assertThat(travelReviewDTO.getIdx()).isEqualTo(deleteIdx);
    }

    @Test
    @DisplayName("여행지 리뷰 리스트 조회 테스트")
    void 여행지리뷰리스트조회테스트() {
        // given
        TravelReviewEntity travelReviewEntity = TravelReviewEntity.builder()
                .reviewTitle("리뷰등록테스트")
                .reviewDescription("리뷰등록테스트")
                .viewCount(0)
                .favoriteCount(0)
                .popular(false)
                .visible("Y")
                .build();

        travelService.reviewTravel(travelDTO.getIdx(), travelReviewEntity);

        travelService.travelReviewList(travelDTO.getIdx());
    }

    @Test
    @DisplayName("여행지 리뷰 리스트 조회 Mockito 테스트")
    void 여행지리뷰리스트조회Mockito테스트() {
        // given
        TravelReviewEntity travelReviewEntity = TravelReviewEntity.builder()
                .reviewTitle("리뷰등록테스트")
                .reviewDescription("리뷰등록테스트")
                .viewCount(0)
                .favoriteCount(0)
                .popular(false)
                .visible("Y")
                .build();

        List<TravelReviewDto> reviewList = travelService.travelReviewList(travelDTO.getIdx());

        TravelDto reviewTravelDTO = TravelDto.builder()
                .newTravelCode(commonDTO)
                .travelTitle("여행지 테스트").travelDescription("여행지 테스트").favoriteCount(1).viewCount(0)
                .travelAddress("인천광역시 서구").travelZipCode("123-456").visible("Y").popular(false)
                .reviewList(reviewList)
                .build();

        // when
        when(mockTravelService.travelReviewList(reviewTravelDTO.getIdx())).thenReturn(reviewList);

        // then
        assertThat(mockTravelService.travelReviewList(reviewTravelDTO.getIdx()).get(0).getTravelDTO().getIdx()).isEqualTo(travelDTO.getIdx());
        assertThat(mockTravelService.travelReviewList(reviewTravelDTO.getIdx()).get(0).getReviewTitle()).isEqualTo("리뷰등록테스트");
        assertThat(mockTravelService.travelReviewList(reviewTravelDTO.getIdx()).get(0).getReviewDescription()).isEqualTo("리뷰등록테스트");
    }

    @Test
    @DisplayName("여행지 리뷰 상세 조회 Mockito 테스트")
    void 여행지리뷰상세조회Mockito테스트() {
        // given
        TravelReviewEntity travelReviewEntity = TravelReviewEntity.builder()
                .reviewTitle("리뷰등록테스트")
                .reviewDescription("리뷰등록테스트")
                .viewCount(0)
                .favoriteCount(0)
                .popular(false)
                .visible("Y")
                .build();

        TravelReviewDto travelReviewDTO = travelService.reviewTravel(travelDTO.getIdx(), travelReviewEntity);

        // when
        when(mockTravelService.detailTravelReview(travelReviewDTO.getIdx())).thenReturn(travelReviewDTO);
        TravelReviewDto travelReviewInfo = mockTravelService.detailTravelReview(travelReviewDTO.getIdx());

        // then
        assertThat(travelReviewDTO.getTravelDTO().getIdx()).isEqualTo(travelDTO.getIdx());
        assertThat(travelReviewInfo.getReviewTitle()).isEqualTo("리뷰등록테스트");
        assertThat(travelReviewInfo.getReviewDescription()).isEqualTo("리뷰등록테스트");

        // verify
        verify(mockTravelService, times(1)).detailTravelReview(travelReviewInfo.getIdx());
        verify(mockTravelService,  atLeastOnce()).detailTravelReview(travelReviewInfo.getIdx());
        verifyNoMoreInteractions(mockTravelService);

        InOrder inOrder = inOrder(mockTravelService);
        inOrder.verify(mockTravelService).detailTravelReview(travelReviewInfo.getIdx());
    }

    @Test
    @DisplayName("여행지 그룹 리스트 Mockito 조회 테스트")
    void 여행지그룹리스트Mockito조회테스트() {
        // given
        Map<String, Object> groupMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by("idx").descending());

        List<TravelGroupDto> travelGroupList = new ArrayList<>();
        travelGroupList.add(TravelGroupDto.builder().travelIdx(1L).groupName("서울모임")
                .groupDescription("서울모임").visible("Y").build());

        travelService.findTravelGroupList(groupMap, pageRequest);
        // when
        when(mockTravelService.findTravelGroupList(groupMap, pageRequest)).thenReturn(travelGroupList);
        List<TravelGroupDto> newTravelGroupList = mockTravelService.findTravelGroupList(groupMap, pageRequest);

        // then
        assertThat(newTravelGroupList.get(0).getIdx()).isEqualTo(travelGroupList.get(0).getIdx());
        assertThat(newTravelGroupList.get(0).getTravelIdx()).isEqualTo(travelGroupList.get(0).getTravelIdx());
        assertThat(newTravelGroupList.get(0).getGroupName()).isEqualTo(travelGroupList.get(0).getGroupName());
        assertThat(newTravelGroupList.get(0).getGroupDescription()).isEqualTo(travelGroupList.get(0).getGroupDescription());

        // verify
        verify(mockTravelService, times(1)).findTravelGroupList(groupMap, pageRequest);
        verify(mockTravelService, atLeastOnce()).findTravelGroupList(groupMap, pageRequest);
        verifyNoMoreInteractions(mockTravelService);

        InOrder inOrder = inOrder(mockTravelService);
        inOrder.verify(mockTravelService).findTravelGroupList(groupMap, pageRequest);
    }

    @Test
    @DisplayName("여행지 그룹 상세 Mockito 테스트")
    void 여행지그룹상세Mockito테스트() {
        // given
        TravelGroupDto travelGroupDTO = TravelGroupDto.builder()
                .idx(1L).travelIdx(1L).groupName("서울모임").groupDescription("서울모임").visible("Y").build();

        // when
        given(mockTravelService.findOneTravelGroup(1L)).willReturn(travelGroupDTO);
        TravelGroupDto newTravelGroupDTO = mockTravelService.findOneTravelGroup(1L);

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
        // 여행지 등록
        em.persist(travelEntity);
        travelDTO = TravelEntity.toDto(travelEntity);

        TravelGroupEntity travelGroupEntity = TravelGroupEntity.builder()
                .groupName("서울모임").groupDescription("서울모임").visible("Y").build();

        TravelGroupDto travelGroupDTO = travelService.insertTravelGroup(travelDTO.getIdx(), travelGroupEntity);

        // when
        when(mockTravelService.findOneTravelGroup(travelGroupDTO.getIdx())).thenReturn(travelGroupDTO);
        TravelGroupDto newTravelGroupDTO = mockTravelService.findOneTravelGroup(travelGroupDTO.getIdx());

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
        // 여행지 등록
        em.persist(travelEntity);
        travelDTO = TravelEntity.toDto(travelEntity);

        TravelGroupEntity travelGroupEntity = TravelGroupEntity.builder()
                .groupName("서울모임").groupDescription("서울모임").visible("Y").build();

        TravelGroupDto travelGroupDTO = travelService.insertTravelGroup(travelDTO.getIdx(), travelGroupEntity);

        TravelGroupEntity newTravelGroupEntity = TravelGroupEntity.builder()
                .idx(travelGroupDTO.getIdx())
                .travelEntity(travelEntity)
                .groupName("인천모임").groupDescription("인천모임")
                .visible("Y").build();

        travelService.updateTravelGroup(travelGroupDTO.getIdx(), newTravelGroupEntity);

        TravelGroupDto newTravelGroupDTO = TravelGroupEntity.toDto(newTravelGroupEntity);

        // when
        when(mockTravelService.findOneTravelGroup(newTravelGroupEntity.getIdx())).thenReturn(newTravelGroupDTO);
        TravelGroupDto travelGroupInfo = mockTravelService.findOneTravelGroup(newTravelGroupDTO.getIdx());

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
        em.persist(travelEntity);
        TravelGroupEntity travelGroupEntity = TravelGroupEntity.builder()
                .groupName("서울모임").groupDescription("서울모임").visible("Y").build();
        travelService.insertTravelGroup(travelEntity.getIdx(), travelGroupEntity);

        TravelGroupDto travelGroupDTO = TravelGroupEntity.toDto(travelGroupEntity);

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
}

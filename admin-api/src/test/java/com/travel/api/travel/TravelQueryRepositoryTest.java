package com.travel.api.travel;

import com.travel.api.common.domain.CommonDto;
import com.travel.api.common.domain.CommonEntity;
import com.travel.api.travel.domain.TravelDto;
import com.travel.api.travel.domain.TravelEntity;
import com.travel.api.travel.domain.festival.TravelFestivalEntity;
import com.travel.api.travel.domain.group.TravelGroupDto;
import com.travel.api.travel.domain.search.SearchEntity;
import com.travel.exception.TravelException;
import lombok.RequiredArgsConstructor;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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

@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@ExtendWith(MockitoExtension.class)
@DisplayName("여행지 소개 Repository Test")
class TravelQueryRepositoryTest {
    @Mock
    private TravelQueryRepository mockTravelQueryRepository;
    private final TravelQueryRepository travelQueryRepository;
    private final EntityManager em;

    private TravelEntity travelEntity;
    private TravelDto travelDTO;
    private CommonEntity commonEntity;
    private CommonDto commonDto;

    void createTravel() {
        commonEntity = CommonEntity.builder()
                .commonCode(999)
                .commonName("서울")
                .visible("Y")
                .build();

        em.persist(commonEntity);

        commonDto = CommonEntity.toDto(commonEntity);

        travelEntity = TravelEntity.builder()
                .newTravelCode(commonEntity)
                .travelTitle("여행지 테스트").travelDescription("여행지 테스트").favoriteCount(1).viewCount(0)
                .travelAddress("인천광역시 서구").travelZipCode("123-456").visible("Y").popular(false)
                .build();

        travelDTO = TravelEntity.toDto(travelEntity);
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
        travelMap.put("searchKeyword", "서울");

        PageRequest pageRequest = PageRequest.of(0, 3);

        // then
        assertThat(travelQueryRepository.findTravelList(travelMap, pageRequest)).isEmpty();
        travelQueryRepository.findTravelList(travelMap, pageRequest);
        travelQueryRepository.findTravelList(travelMap, pageRequest);

        travelMap.put("searchKeyword", "인천");
        travelQueryRepository.findTravelList(travelMap, pageRequest);

        assertThat(travelQueryRepository.rankingTravelKeyword().get(0).getSearchKeyword()).isEqualTo("서울");
        assertThat(travelQueryRepository.rankingTravelKeyword().get(1).getSearchKeyword()).isEqualTo("인천");
    }

    @Test
    @DisplayName("여행지상세조회테스트")
    void 여행지상세조회테스트() {
        TravelDto existTravel = travelQueryRepository.findOneTravel(1L);
        assertThat(existTravel.getIdx()).isEqualTo(1L);
        assertThat(existTravel.getCommonCode()).isEqualTo(1);
        assertThat(existTravel.getTravelTitle()).isEqualTo("서울 여행지");

        assertThatThrownBy(() -> travelQueryRepository.findOneTravel(3L))
                .isInstanceOf(TravelException.class).hasMessage("여행 상세 없음");
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
        travelList.add(TravelDto.builder().idx(1L).commonCode(commonDto.getCommonCode())
                .travelTitle("여행지 소개").travelDescription("여행지 소개")
                .travelAddress("인천광역시 서구").travelZipCode("123-456").visible("Y").build());

        Page<TravelDto> resultPage = new PageImpl<>(travelList, pageRequest, travelList.size());

        // when
        when(mockTravelQueryRepository.findTravelList(travelMap, pageRequest)).thenReturn(resultPage);
        Page<TravelDto> newTravelList = mockTravelQueryRepository.findTravelList(travelMap, pageRequest);

        List<TravelDto> findTravelList = newTravelList.stream().collect(Collectors.toList());

        // then
        assertThat(findTravelList.get(0).getIdx()).isEqualTo(travelList.get(0).getIdx());
        assertThat(findTravelList.get(0).getCommonCode()).isEqualTo(travelList.get(0).getCommonCode());
        assertThat(findTravelList.get(0).getTravelTitle()).isEqualTo(travelList.get(0).getTravelTitle());
        assertThat(findTravelList.get(0).getTravelDescription()).isEqualTo(travelList.get(0).getTravelDescription());
        assertThat(findTravelList.get(0).getTravelAddress()).isEqualTo(travelList.get(0).getTravelAddress());
        assertThat(findTravelList.get(0).getTravelZipCode()).isEqualTo(travelList.get(0).getTravelZipCode());

        // verify
        verify(mockTravelQueryRepository, times(1)).findTravelList(travelMap, pageRequest);
        verify(mockTravelQueryRepository, atLeastOnce()).findTravelList(travelMap, pageRequest);
        verifyNoMoreInteractions(mockTravelQueryRepository);

        InOrder inOrder = inOrder(mockTravelQueryRepository);
        inOrder.verify(mockTravelQueryRepository).findTravelList(travelMap, pageRequest);
    }

    @Test
    @DisplayName("여행지 소개 리스트 Mockito 조회 테스트")
    void 여행지소개리스트Mockito조회테스트() {
        // given
        Map<String, Object> travelMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(0, 3);

        List<TravelDto> travelList = new ArrayList<>();
        travelList.add(TravelDto.builder().idx(1L).commonCode(commonDto.getCommonCode())
                .travelTitle("여행지 소개").travelDescription("여행지 소개")
                .travelAddress("인천광역시 서구").travelZipCode("123-456").visible("Y").build());

        Page<TravelDto> resultPage = new PageImpl<>(travelList, pageRequest, travelList.size());

        // when
        when(mockTravelQueryRepository.findTravelList(travelMap, pageRequest)).thenReturn(resultPage);
        Page<TravelDto> newTravelList = mockTravelQueryRepository.findTravelList(travelMap, pageRequest);

        List<TravelDto> findTravelList = newTravelList.stream().collect(Collectors.toList());

        // then
        assertThat(findTravelList.get(0).getIdx()).isEqualTo(travelList.get(0).getIdx());
        assertThat(findTravelList.get(0).getCommonCode()).isEqualTo(travelList.get(0).getCommonCode());
        assertThat(findTravelList.get(0).getTravelTitle()).isEqualTo(travelList.get(0).getTravelTitle());
        assertThat(findTravelList.get(0).getTravelDescription()).isEqualTo(travelList.get(0).getTravelDescription());
        assertThat(findTravelList.get(0).getTravelAddress()).isEqualTo(travelList.get(0).getTravelAddress());
        assertThat(findTravelList.get(0).getTravelZipCode()).isEqualTo(travelList.get(0).getTravelZipCode());

        // verify
        verify(mockTravelQueryRepository, times(1)).findTravelList(travelMap, pageRequest);
        verify(mockTravelQueryRepository, atLeastOnce()).findTravelList(travelMap, pageRequest);
        verifyNoMoreInteractions(mockTravelQueryRepository);

        InOrder inOrder = inOrder(mockTravelQueryRepository);
        inOrder.verify(mockTravelQueryRepository).findTravelList(travelMap, pageRequest);
    }

    @Test
    @DisplayName("여행지 소개 리스트 BDD 조회 테스트")
    void 여행지소개리스트BDD조회테스트() {
        Map<String, Object> travelMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(0, 3);

        List<TravelDto> travelList = new ArrayList<>();
        travelList.add(TravelDto.builder().idx(1L).commonCode(commonDto.getCommonCode())
                .travelTitle("여행지 소개").travelDescription("여행지 소개")
                .travelAddress("인천광역시 서구").travelZipCode("123-456").visible("Y").build());

        Page<TravelDto> resultPage = new PageImpl<>(travelList, pageRequest, travelList.size());

        // when
        given(mockTravelQueryRepository.findTravelList(travelMap, pageRequest)).willReturn(resultPage);
        Page<TravelDto> newTravelList = mockTravelQueryRepository.findTravelList(travelMap, pageRequest);

        List<TravelDto> findTravelList = newTravelList.stream().collect(Collectors.toList());

        // then
        assertThat(findTravelList.get(0).getIdx()).isEqualTo(travelList.get(0).getIdx());
        assertThat(findTravelList.get(0).getCommonCode()).isEqualTo(travelList.get(0).getCommonCode());
        assertThat(findTravelList.get(0).getTravelTitle()).isEqualTo(travelList.get(0).getTravelTitle());
        assertThat(findTravelList.get(0).getTravelDescription()).isEqualTo(travelList.get(0).getTravelDescription());
        assertThat(findTravelList.get(0).getTravelAddress()).isEqualTo(travelList.get(0).getTravelAddress());
        assertThat(findTravelList.get(0).getTravelZipCode()).isEqualTo(travelList.get(0).getTravelZipCode());


        // verify
        then(mockTravelQueryRepository).should(times(1)).findTravelList(travelMap, pageRequest);
        then(mockTravelQueryRepository).should(atLeastOnce()).findTravelList(travelMap, pageRequest);
        then(mockTravelQueryRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("여행지 소개 상세 Mockito 테스트")
    void 여행지소개상세Mockito테스트() {
        // given
        travelEntity = TravelEntity.builder()
                .idx(1L)
                .travelTitle("여행지 테스트").travelDescription("여행지 테스트")
                .travelAddress("인천광역시 서구").travelZipCode("123-456").visible("Y")
                .build();

        travelDTO = TravelEntity.toDto(travelEntity);

        // when
        when(mockTravelQueryRepository.findOneTravel(1L)).thenReturn(travelDTO);
        TravelDto newTravelInfo = mockTravelQueryRepository.findOneTravel(1L);

        // then
        assertThat(newTravelInfo.getIdx()).isEqualTo(travelEntity.getIdx());
//        assertThat(newTravelInfo.getTravelCode()).isEqualTo(travelEntity.getTravelCode());
        assertThat(newTravelInfo.getTravelTitle()).isEqualTo(travelEntity.getTravelTitle());
        assertThat(newTravelInfo.getTravelDescription()).isEqualTo(travelEntity.getTravelDescription());
        assertThat(newTravelInfo.getTravelAddress()).isEqualTo(travelEntity.getTravelAddress());
        assertThat(newTravelInfo.getTravelZipCode()).isEqualTo(travelEntity.getTravelZipCode());

        // verify
        verify(mockTravelQueryRepository, times(1)).findOneTravel(1L);
        verify(mockTravelQueryRepository, atLeastOnce()).findOneTravel(1L);
        verifyNoMoreInteractions(mockTravelQueryRepository);

        InOrder inOrder = inOrder(mockTravelQueryRepository);
        inOrder.verify(mockTravelQueryRepository).findOneTravel(1L);
    }

    @Test
    @DisplayName("여행지 소개 상세 BDD 테스트")
    void 여행지소개상세BDD테스트() {
        // given
        travelEntity = TravelEntity.builder()
                .idx(1L)
                .travelTitle("여행지 테스트").travelDescription("여행지 테스트")
                .travelAddress("인천광역시 서구").travelZipCode("123-456").visible("Y")
                .build();

        travelDTO = TravelEntity.toDto(travelEntity);

        // when
        when(mockTravelQueryRepository.findOneTravel(1L)).thenReturn(travelDTO);
        TravelDto newTravelInfo = mockTravelQueryRepository.findOneTravel(1L);

        // then
        assertThat(newTravelInfo.getIdx()).isEqualTo(travelEntity.getIdx());
//        assertThat(newTravelInfo.getTravelCode()).isEqualTo(travelEntity.getTravelCode());
        assertThat(newTravelInfo.getTravelTitle()).isEqualTo(travelEntity.getTravelTitle());
        assertThat(newTravelInfo.getTravelDescription()).isEqualTo(travelEntity.getTravelDescription());
        assertThat(newTravelInfo.getTravelAddress()).isEqualTo(travelEntity.getTravelAddress());
        assertThat(newTravelInfo.getTravelZipCode()).isEqualTo(travelEntity.getTravelZipCode());

        // verify
        then(mockTravelQueryRepository).should(times(1)).findOneTravel(1L);
        then(mockTravelQueryRepository).should(atLeastOnce()).findOneTravel(1L);
        then(mockTravelQueryRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("이전 여행지 상세 조회 Mockito 테스트")
    void 이전여행지상세조회Mockito테스트() {
        // given
        travelEntity = TravelEntity.builder().idx(2L).build();

        // when
        travelDTO = travelQueryRepository.findOnePrevTravel(travelDTO.getIdx());

        when(mockTravelQueryRepository.findOnePrevTravel(travelDTO.getIdx())).thenReturn(travelDTO);
        TravelDto travelInfo = mockTravelQueryRepository.findOnePrevTravel(travelDTO.getIdx());

        // then
        assertThat(travelInfo.getIdx()).isEqualTo(1L);

        // verify
        verify(mockTravelQueryRepository, times(1)).findOnePrevTravel(travelDTO.getIdx());
        verify(mockTravelQueryRepository, atLeastOnce()).findOnePrevTravel(travelDTO.getIdx());
        verifyNoMoreInteractions(mockTravelQueryRepository);

        InOrder inOrder = inOrder(mockTravelQueryRepository);
        inOrder.verify(mockTravelQueryRepository).findOnePrevTravel(travelDTO.getIdx());
    }

    @Test
    @DisplayName("이전 여행지 상세 조회 BDD 테스트")
    void 이전여행지상세조회BDD테스트() {
        // given
        travelEntity = TravelEntity.builder().idx(2L).build();

        // when
        travelDTO = travelQueryRepository.findOnePrevTravel(travelDTO.getIdx());

        given(mockTravelQueryRepository.findOnePrevTravel(travelDTO.getIdx())).willReturn(travelDTO);
        TravelDto travelInfo = mockTravelQueryRepository.findOnePrevTravel(travelDTO.getIdx());

        // then
        assertThat(travelInfo.getIdx()).isEqualTo(1L);

        // verify
        then(mockTravelQueryRepository).should(times(1)).findOnePrevTravel(2L);
        then(mockTravelQueryRepository).should(atLeastOnce()).findOnePrevTravel(2L);
        then(mockTravelQueryRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("다음 여행지 상세 조회 Mockito 테스트")
    void 다음여행지상세조회Mockito테스트() {
        // given
        travelEntity = TravelEntity.builder().idx(2L).build();

        // when
        travelDTO = travelQueryRepository.findOneNextTravel(travelEntity.getIdx());

        when(mockTravelQueryRepository.findOneNextTravel(travelEntity.getIdx())).thenReturn(travelDTO);
        TravelDto travelInfo = mockTravelQueryRepository.findOneNextTravel(travelEntity.getIdx());

        // then
        assertThat(travelInfo.getIdx()).isEqualTo(1L);

        // verify
        verify(mockTravelQueryRepository, times(1)).findOneNextTravel(travelEntity.getIdx());
        verify(mockTravelQueryRepository, atLeastOnce()).findOneNextTravel(travelEntity.getIdx());
        verifyNoMoreInteractions(mockTravelQueryRepository);

        InOrder inOrder = inOrder(mockTravelQueryRepository);
        inOrder.verify(mockTravelQueryRepository).findOneNextTravel(travelEntity.getIdx());
    }

    @Test
    @DisplayName("여행지좋아요Mockito테스트")
    void 여행지좋아요Mockito테스트() {
        // given
        travelEntity = TravelEntity.builder()
                .newTravelCode(commonEntity)
                .travelTitle("여행지 테스트").travelDescription("여행지 테스트").favoriteCount(0).viewCount(0)
                .travelAddress("인천광역시 서구").travelZipCode("123-456").visible("Y")
                .build();

        em.persist(travelEntity);

        travelDTO = TravelEntity.toDto(travelEntity);

        // when
        // 좋아요 수 증가
        Integer favoriteCount = travelQueryRepository.favoriteTravel(travelDTO.getIdx());

        when(mockTravelQueryRepository.favoriteTravel(travelDTO.getIdx())).thenReturn(favoriteCount);

        // then
        // 증가한 좋아요 수 조회
        Integer addFavoriteCount = mockTravelQueryRepository.favoriteTravel(travelDTO.getIdx());
        assertThat(favoriteCount).isEqualTo(addFavoriteCount);

        // verify
        verify(mockTravelQueryRepository, times(1)).favoriteTravel(travelDTO.getIdx());
        verify(mockTravelQueryRepository, atLeastOnce()).favoriteTravel(travelDTO.getIdx());
        verifyNoMoreInteractions(mockTravelQueryRepository);

        InOrder inOrder = inOrder(mockTravelQueryRepository);
        inOrder.verify(mockTravelQueryRepository).favoriteTravel(travelDTO.getIdx());
    }

    @Test
    @DisplayName("여행지좋아요BDD테스트")
    void 여행지좋아요BDD테스트() {
        // given
        travelEntity = TravelEntity.builder()
                .newTravelCode(commonEntity)
                .travelTitle("여행지 테스트").travelDescription("여행지 테스트").favoriteCount(1).viewCount(0)
                .travelAddress("인천광역시 서구").travelZipCode("123-456").visible("Y")
                .build();

        em.persist(travelEntity);

        travelDTO = TravelEntity.toDto(travelEntity);

        // when
        // 좋아요 수 증가
        Integer favoriteCount = travelQueryRepository.favoriteTravel(travelDTO.getIdx());
        given(mockTravelQueryRepository.favoriteTravel(travelDTO.getIdx())).willReturn(favoriteCount);

        // then
        // 증가한 좋아요 수 조회
        Integer addFavoriteCount = mockTravelQueryRepository.favoriteTravel(travelDTO.getIdx());
        assertThat(favoriteCount).isEqualTo(addFavoriteCount);

        // verify
        then(mockTravelQueryRepository).should(times(1)).favoriteTravel(travelDTO.getIdx());
        then(mockTravelQueryRepository).should(atLeastOnce()).favoriteTravel(travelDTO.getIdx());
        then(mockTravelQueryRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("인기여행지선정Mockito테스트")
    void 인기여행지선정Mockito테스트() {
        // given
        em.persist(travelEntity);

        travelDTO = TravelEntity.toDto(travelEntity);
        Long idx = travelDTO.getIdx();

        Boolean popular = travelQueryRepository.togglePopular(idx);

        travelEntity = TravelEntity.builder()
                .travelTitle("여행지 테스트").travelDescription("여행지 테스트").favoriteCount(1).viewCount(0)
                .travelAddress("인천광역시 서구").travelZipCode("123-456").visible("Y").popular(popular)
                .build();

        travelDTO = TravelEntity.toDto(travelEntity);

        // when
        when(mockTravelQueryRepository.findOneTravel(travelEntity.getIdx())).thenReturn(travelDTO);
        TravelDto travelInfo = mockTravelQueryRepository.findOneTravel(travelEntity.getIdx());

        // then
        assertThat(travelInfo.getPopular()).isTrue();

        // verify
        verify(mockTravelQueryRepository, times(1)).findOneTravel(travelEntity.getIdx());
        verify(mockTravelQueryRepository, atLeastOnce()).findOneTravel(travelEntity.getIdx());
        verifyNoMoreInteractions(mockTravelQueryRepository);

        InOrder inOrder = inOrder(mockTravelQueryRepository);
        inOrder.verify(mockTravelQueryRepository).findOneTravel(travelEntity.getIdx());
    }

    @Test
    @DisplayName("인기여행지선정BDD테스트")
    void 인기여행지선정BDD테스트() {
        // given
        em.persist(travelEntity);

        travelDTO = TravelEntity.toDto(travelEntity);
        Long idx = travelDTO.getIdx();

        Boolean popular = travelQueryRepository.togglePopular(idx);

        travelEntity = TravelEntity.builder()
                .travelTitle("여행지 테스트").travelDescription("여행지 테스트").favoriteCount(1).viewCount(0)
                .travelAddress("인천광역시 서구").travelZipCode("123-456").visible("Y").popular(popular)
                .build();

        travelDTO = TravelEntity.toDto(travelEntity);

        // when
        given(mockTravelQueryRepository.findOneTravel(travelEntity.getIdx())).willReturn(travelDTO);
        TravelDto travelInfo = mockTravelQueryRepository.findOneTravel(travelEntity.getIdx());

        // then
        assertThat(travelInfo.getPopular()).isTrue();

        // verify
        then(mockTravelQueryRepository).should(times(1)).findOneTravel(travelDTO.getIdx());
        then(mockTravelQueryRepository).should(atLeastOnce()).findOneTravel(travelDTO.getIdx());
        then(mockTravelQueryRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("여행지 그룹 리스트 Mockito 조회 테스트")
    void 여행지그룹리스트Mockito조회테스트() {
        // given
        Map<String, Object> groupMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(0, 3);

        List<TravelGroupDto> travelGroupList = new ArrayList<>();
        travelGroupList.add(TravelGroupDto.builder()
                .commonCode(travelEntity.getNewTravelCode().getCommonCode())
                .commonName(travelEntity.getNewTravelCode().getCommonName())
                .travelTitle(travelEntity.getTravelTitle()).travelDescription(travelEntity.getTravelDescription())
                .groupName("서울모임").groupDescription("서울모임").visible("Y").build());

        Page<TravelGroupDto> resultPage = new PageImpl<>(travelGroupList, pageRequest, travelGroupList.size());

        // when
        when(mockTravelQueryRepository.findTravelGroupList(groupMap, pageRequest)).thenReturn(resultPage);
        Page<TravelGroupDto> newTravelGroupList = mockTravelQueryRepository.findTravelGroupList(groupMap, pageRequest);

        List<TravelGroupDto> findTravelGroupList = newTravelGroupList.stream().collect(Collectors.toList());
        // then
        assertThat(findTravelGroupList.get(0).getIdx()).isEqualTo(travelGroupList.get(0).getIdx());
        assertThat(findTravelGroupList.get(0).getTravelTitle()).isEqualTo(travelGroupList.get(0).getTravelTitle());
        assertThat(findTravelGroupList.get(0).getGroupName()).isEqualTo(travelGroupList.get(0).getGroupName());
        assertThat(findTravelGroupList.get(0).getGroupDescription()).isEqualTo(travelGroupList.get(0).getGroupDescription());

        // verify
        verify(mockTravelQueryRepository, times(1)).findTravelGroupList(groupMap, pageRequest);
        verify(mockTravelQueryRepository, atLeastOnce()).findTravelGroupList(groupMap, pageRequest);
        verifyNoMoreInteractions(mockTravelQueryRepository);

        InOrder inOrder = inOrder(mockTravelQueryRepository);
        inOrder.verify(mockTravelQueryRepository).findTravelGroupList(groupMap, pageRequest);
    }

    @Test
    @DisplayName("여행지 그룹 상세 Mockito 테스트")
    void 여행지그룹상세Mockito테스트() {
        // given
        TravelGroupDto travelGroupDTO = TravelGroupDto.builder()
                .commonCode(travelEntity.getNewTravelCode().getCommonCode())
                .commonName(travelEntity.getNewTravelCode().getCommonName())
                .travelTitle(travelEntity.getTravelTitle()).travelDescription(travelEntity.getTravelDescription())
                .groupName("서울모임").groupDescription("서울모임").visible("Y").build();

        // when
        given(mockTravelQueryRepository.findOneTravelGroup(1L)).willReturn(travelGroupDTO);
        TravelGroupDto newTravelGroupDTO = mockTravelQueryRepository.findOneTravelGroup(1L);

        // then
        assertThat(newTravelGroupDTO.getIdx()).isEqualTo(1L);
        assertThat(newTravelGroupDTO.getTravelTitle()).isEqualTo(travelEntity.getTravelTitle());
        assertThat(newTravelGroupDTO.getGroupName()).isEqualTo("서울모임");
        assertThat(newTravelGroupDTO.getGroupDescription()).isEqualTo("서울모임");

        // verify
        verify(mockTravelQueryRepository, times(1)).findOneTravelGroup(1L);
        verify(mockTravelQueryRepository, atLeastOnce()).findOneTravelGroup(1L);
        verifyNoMoreInteractions(mockTravelQueryRepository);

        InOrder inOrder = inOrder(mockTravelQueryRepository);
        inOrder.verify(mockTravelQueryRepository).findOneTravelGroup(1L);
    }

    @Test
    @DisplayName("검색어 랭킹 리스트 조회 테스트")
    void 검색어랭킹리스트조회테스트() {
        em.persist(SearchEntity.builder().searchKeyword("서울").build());
        em.persist(SearchEntity.builder().searchKeyword("서울").build());
        em.persist(SearchEntity.builder().searchKeyword("서울").build());
        em.persist(SearchEntity.builder().searchKeyword("인천").build());

        assertThat(travelQueryRepository.rankingTravelKeyword().get(0).getSearchKeyword()).isEqualTo("서울");
        assertThat(travelQueryRepository.rankingTravelKeyword().get(1).getSearchKeyword()).isEqualTo("인천");
    }

    @Test
    @DisplayName("추천 검색어 or 검색어 랭킹을 통한 여행지 검색 조회")
    void 추천검색어or검색어랭킹을통한여행지검색조회() {
        assertThat(travelQueryRepository.findTravelKeyword("서울").get(0).getTravelTitle()).isEqualTo("서울 여행지");
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

        assertThat(travelQueryRepository.findTravelFestivalGroup(dateTime.getMonthValue())).isNotEmpty();
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

        assertThat(travelQueryRepository.findTravelFestivalList(travelFestivalEntity)).isNotEmpty();
    }
}

package com.travel.travel_project.api.common;

import com.travel.travel_project.api.common.mapper.CommonMapper;
import com.travel.travel_project.domain.common.CommonDTO;
import com.travel.travel_project.domain.common.CommonEntity;
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
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
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
@DisplayName("공통 코드 Repository Test")
class CommonRepositoryTest {

    @Mock
    private CommonRepository mockCommonRepository;
    private final CommonRepository commonRepository;
    private final EntityManager em;

    private CommonEntity commonEntity;
    private CommonDTO commonDTO;

    void createCommonCode() {
        commonEntity = CommonEntity.builder()
                .commonCode(1)
                .commonName("서울")
                .visible("Y")
                .build();

        commonDTO = CommonMapper.INSTANCE.toDto(commonEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() { createCommonCode(); }

    @Test
    @DisplayName("공통 코드 리스트 Mockito 조회 테스트")
    void 공통코드리스트Mockito조회테스트() {
        // given
        Map<String, Object> commonMap = new HashMap<>();
        commonMap.put("jpaStartPage", 1);
        commonMap.put("size", 3);
        commonMap.put("searchKeyword", "서울");

        List<CommonDTO> commonList = new ArrayList<>();
        commonList.add(CommonDTO.builder().idx(1L).commonCode(1).commonName("서울").visible("Y").build());

        // when
        given(mockCommonRepository.findCommonList(commonMap)).willReturn(commonList);
        List<CommonDTO> newCommonList = mockCommonRepository.findCommonList(commonMap);

        // then
        assertThat(newCommonList.get(0).getIdx()).isEqualTo(commonList.get(0).getIdx());
        assertThat(newCommonList.get(0).getCommonCode()).isEqualTo(commonList.get(0).getCommonCode());
        assertThat(newCommonList.get(0).getCommonName()).isEqualTo(commonList.get(0).getCommonName());

        // verify
        verify(mockCommonRepository, times(1)).findCommonList(commonMap);
        verify(mockCommonRepository, atLeastOnce()).findCommonList(commonMap);
        verifyNoMoreInteractions(mockCommonRepository);

        InOrder inOrder = inOrder(mockCommonRepository);
        inOrder.verify(mockCommonRepository).findCommonList(commonMap);
    }

    @Test
    @DisplayName("공통 코드 상세 Mockito 테스트")
    void 공통코드상세Mockito테스트() {
        // given
        commonDTO = CommonDTO.builder()
                .idx(1L)
                .commonCode(1)
                .commonName("서울")
                .visible("Y")
                .build();

        // when
        given(mockCommonRepository.findOneCommon(1L)).willReturn(commonDTO);
        CommonDTO newCommonCode = mockCommonRepository.findOneCommon(1L);

        // then
        assertThat(newCommonCode.getIdx()).isEqualTo(commonDTO.getIdx());
        assertThat(newCommonCode.getCommonCode()).isEqualTo(commonDTO.getCommonCode());
        assertThat(newCommonCode.getCommonName()).isEqualTo(commonDTO.getCommonName());

        // verify
        verify(mockCommonRepository, times(1)).findOneCommon(1L);
        verify(mockCommonRepository, atLeastOnce()).findOneCommon(1L);
        verifyNoMoreInteractions(mockCommonRepository);

        InOrder inOrder = inOrder(mockCommonRepository);
        inOrder.verify(mockCommonRepository).findOneCommon(1L);
    }

    @Test
    @DisplayName("공통 코드 등록 Mockito 테스트")
    void 공통코드등록Mockito테스트() {
        // given
        CommonDTO commonInfo = commonRepository.insertCommonCode(commonEntity);

        // when
        given(mockCommonRepository.findOneCommon(commonInfo.getIdx())).willReturn(commonInfo);
        CommonDTO newCommonInfo = mockCommonRepository.findOneCommon(commonInfo.getIdx());

        // then
        assertThat(newCommonInfo.getCommonCode()).isEqualTo(commonEntity.getCommonCode());
        assertThat(newCommonInfo.getCommonName()).isEqualTo(commonEntity.getCommonName());

        // verify
        verify(mockCommonRepository, times(1)).findOneCommon(newCommonInfo.getIdx());
        verify(mockCommonRepository, atLeastOnce()).findOneCommon(newCommonInfo.getIdx());
        verifyNoMoreInteractions(mockCommonRepository);

        InOrder inOrder = inOrder(mockCommonRepository);
        inOrder.verify(mockCommonRepository).findOneCommon(newCommonInfo.getIdx());
    }

    @Test
    @DisplayName("공통 코드 수정 Mockito 테스트")
    void 공통코드수정Mockito테스트() {
        // given
        CommonDTO commonDTO = commonRepository.insertCommonCode(commonEntity);
        CommonEntity newCommonEntity = CommonEntity.builder()
                .idx(commonDTO.getIdx())
                .commonCode(2)
                .commonName("인천")
                .visible("Y").build();

        commonRepository.updateCommonCode(newCommonEntity);

        CommonDTO newCommonInfo = CommonMapper.INSTANCE.toDto(newCommonEntity);

        // when
        given(mockCommonRepository.findOneCommon(newCommonInfo.getIdx())).willReturn(newCommonInfo);
        CommonDTO commonInfo = mockCommonRepository.findOneCommon(newCommonInfo.getIdx());

        // then
        assertThat(commonInfo.getIdx()).isEqualTo(newCommonInfo.getIdx());
        assertThat(commonInfo.getCommonCode()).isEqualTo(newCommonInfo.getCommonCode());
        assertThat(commonInfo.getCommonName()).isEqualTo(newCommonInfo.getCommonName());

        // verify
        verify(mockCommonRepository, times(1)).findOneCommon(commonInfo.getIdx());
        verify(mockCommonRepository, atLeastOnce()).findOneCommon(commonInfo.getIdx());
        verifyNoMoreInteractions(mockCommonRepository);

        InOrder inOrder = inOrder(mockCommonRepository);
        inOrder.verify(mockCommonRepository).findOneCommon(commonInfo.getIdx());
    }

    @Test
    @DisplayName("공통 코드 삭제 Mockito 테스트")
    void 공통코드삭제Mockito테스트() {
        // given
        em.persist(commonEntity);
        commonDTO = CommonMapper.INSTANCE.toDto(commonEntity);

        // when
        given(mockCommonRepository.findOneCommon(commonDTO.getIdx())).willReturn(commonDTO);
        Long deleteIdx = commonRepository.deleteCommonCode(commonDTO.getIdx());

        // then
        assertThat(mockCommonRepository.findOneCommon(commonDTO.getIdx()).getIdx()).isEqualTo(deleteIdx);

        // verify
        verify(mockCommonRepository, times(1)).findOneCommon(commonDTO.getIdx());
        verify(mockCommonRepository, atLeastOnce()).findOneCommon(commonDTO.getIdx());
        verifyNoMoreInteractions(mockCommonRepository);

        InOrder inOrder = inOrder(mockCommonRepository);
        inOrder.verify(mockCommonRepository).findOneCommon(commonDTO.getIdx());
    }
}
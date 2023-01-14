package com.travel.travel_project.api.common;

import com.travel.travel_project.domain.common.CommonDTO;
import com.travel.travel_project.domain.common.CommonEntity;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
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
@DisplayName("공통 코드 Service Test")
class CommonServiceTest {
    @Mock private CommonService mockCommonService;
    private final CommonService commonService;
    private final EntityManager em;

    private CommonEntity commonEntity;
    private CommonDTO commonDTO;

    void createCommon() {
        commonEntity = CommonEntity.builder()
                .commonCode(1)
                .commonName("서울")
                .visible("Y")
                .build();

        commonDTO = CommonEntity.toDto(commonEntity);
    }

    @BeforeEach
    @EventListener
    public void init() {
        createCommon();
    }

    @Test
    @DisplayName("공통 코드 리스트 조회 Mockito 테스트")
    void 공통코드리스트조회Mockito테스트() {
        // given
        Map<String, Object> commonMap = new HashMap<>();
        commonMap.put("searchKeyword", "서울");

        PageRequest pageRequest = PageRequest.of(0, 3);

        List<CommonDTO> commonList = new ArrayList<>();
        commonList.add(CommonDTO.builder().idx(1L).commonCode(1).commonName("서울").visible("Y").build());

        Page<CommonDTO> resultPage = new PageImpl<>(commonList, pageRequest, commonList.size());

        // when
        when(mockCommonService.findCommonList(commonMap, pageRequest)).thenReturn(resultPage);
        Page<CommonDTO> newCommonList = mockCommonService.findCommonList(commonMap, pageRequest);

        List<CommonDTO> commonDTOList = newCommonList.stream().collect(Collectors.toList());

        // then
        assertThat(commonDTOList.get(0).getIdx()).isEqualTo(commonList.get(0).getIdx());
        assertThat(commonDTOList.get(0).getCommonCode()).isEqualTo(commonList.get(0).getCommonCode());
        assertThat(commonDTOList.get(0).getCommonName()).isEqualTo(commonList.get(0).getCommonName());

        // verify
        verify(mockCommonService, times(1)).findCommonList(commonMap, pageRequest);
        verify(mockCommonService, atLeastOnce()).findCommonList(commonMap, pageRequest);
        verifyNoMoreInteractions(mockCommonService);

        InOrder inOrder = inOrder(mockCommonService);
        inOrder.verify(mockCommonService).findCommonList(commonMap, pageRequest);
    }

    @Test
    @DisplayName("공통 코드 상세 조회 Mockito 테스트")
    void 공통코드상세조회Mockito테스트() {
        // given
        CommonDTO newCommonDTO = commonService.insertCommonCode(commonEntity);

        // when
        when(mockCommonService.findOneCommon(newCommonDTO.getIdx())).thenReturn(newCommonDTO);
        CommonDTO commonInfo = mockCommonService.findOneCommon(newCommonDTO.getIdx());

        // then
        assertThat(commonInfo.getCommonCode()).isEqualTo(newCommonDTO.getCommonCode());
        assertThat(commonInfo.getCommonName()).isEqualTo(newCommonDTO.getCommonName());

        // verify
        verify(mockCommonService, times(1)).findOneCommon(newCommonDTO.getIdx());
        verify(mockCommonService, atLeastOnce()).findOneCommon(newCommonDTO.getIdx());
        verifyNoMoreInteractions(mockCommonService);

        InOrder inOrder = inOrder(mockCommonService);
        inOrder.verify(mockCommonService).findOneCommon(newCommonDTO.getIdx());
    }

    @Test
    @DisplayName("공통 코드 등록 Mockito 테스트")
    void 공통코드등록Mockito테스트() {
        // given
        CommonDTO newCommonDTO = commonService.insertCommonCode(commonEntity);

        // when
        when(mockCommonService.findOneCommon(newCommonDTO.getIdx())).thenReturn(newCommonDTO);
        CommonDTO commonInfo = mockCommonService.findOneCommon(newCommonDTO.getIdx());

        // then
        assertThat(commonInfo.getCommonCode()).isEqualTo(newCommonDTO.getCommonCode());
        assertThat(commonInfo.getCommonName()).isEqualTo(newCommonDTO.getCommonName());

        // verify
        verify(mockCommonService, times(1)).findOneCommon(newCommonDTO.getIdx());
        verify(mockCommonService, atLeastOnce()).findOneCommon(newCommonDTO.getIdx());
        verifyNoMoreInteractions(mockCommonService);

        InOrder inOrder = inOrder(mockCommonService);
        inOrder.verify(mockCommonService).findOneCommon(newCommonDTO.getIdx());
    }

    @Test
    @DisplayName("공통 코드 수정 Mockito 테스트")
    void 공통코드수정Mockito테스트() {
        // given
        CommonDTO commonDTO = commonService.insertCommonCode(commonEntity);
        CommonEntity newCommonEntity = CommonEntity.builder()
                .idx(commonDTO.getIdx())
                .commonCode(2).commonName("인천").visible("Y").build();
        commonService.updateCommonCode(commonDTO.getIdx(), newCommonEntity);

        CommonDTO newCommonInfo = CommonEntity.toDto(newCommonEntity);

        // when
        when(mockCommonService.findOneCommon(newCommonInfo.getIdx())).thenReturn(newCommonInfo);
        CommonDTO commonInfo = mockCommonService.findOneCommon(newCommonInfo.getIdx());

        // then
        assertThat(commonInfo.getIdx()).isEqualTo(newCommonInfo.getIdx());
        assertThat(commonInfo.getCommonCode()).isEqualTo(newCommonInfo.getCommonCode());
        assertThat(commonInfo.getCommonName()).isEqualTo(newCommonInfo.getCommonName());

        // verify
        verify(mockCommonService, times(1)).findOneCommon(commonInfo.getIdx());
        verify(mockCommonService, atLeastOnce()).findOneCommon(commonInfo.getIdx());
        verifyNoMoreInteractions(mockCommonService);

        InOrder inOrder = inOrder(mockCommonService);
        inOrder.verify(mockCommonService).findOneCommon(commonInfo.getIdx());
    }

    @Test
    @DisplayName("공통 코드 삭제 Mockito 테스트")
    void 공통코드삭제Mockito테스트() {
        // given
        em.persist(commonEntity);
        commonDTO = CommonEntity.toDto(commonEntity);

        // when
        when(mockCommonService.findOneCommon(commonDTO.getIdx())).thenReturn(commonDTO);
        Long deleteIdx = commonService.deleteCommonCode(commonDTO.getIdx());

        // then
        assertThat(mockCommonService.findOneCommon(commonDTO.getIdx()).getIdx()).isEqualTo(deleteIdx);

        // verify
        verify(mockCommonService, times(1)).findOneCommon(commonDTO.getIdx());
        verify(mockCommonService, atLeastOnce()).findOneCommon(commonDTO.getIdx());
        verifyNoMoreInteractions(mockCommonService);

        InOrder inOrder = inOrder(mockCommonService);
        inOrder.verify(mockCommonService).findOneCommon(commonDTO.getIdx());
    }
}
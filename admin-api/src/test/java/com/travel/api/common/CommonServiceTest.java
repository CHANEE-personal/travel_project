package com.travel.api.common;

import com.travel.api.AdminCommonServiceTest;
import com.travel.api.common.domain.CommonDto;
import com.travel.api.common.domain.CommonEntity;
import lombok.RequiredArgsConstructor;
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
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

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
class CommonServiceTest extends AdminCommonServiceTest {
    @Mock
    private CommonService mockCommonService;
    private final CommonService commonService;

    @Test
    @DisplayName("공통 코드 리스트 조회 Mockito 테스트")
    void 공통코드리스트조회Mockito테스트() {
        // given
        Map<String, Object> commonMap = new HashMap<>();
        commonMap.put("searchKeyword", "서울");

        PageRequest pageRequest = PageRequest.of(0, 3);

        List<CommonDto> commonList = new ArrayList<>();
        commonList.add(CommonDto.builder().idx(1L).commonCode(1).commonName("서울").visible("Y").build());

        Page<CommonDto> resultPage = new PageImpl<>(commonList, pageRequest, commonList.size());

        // when
        when(mockCommonService.findCommonList(commonMap, pageRequest)).thenReturn(resultPage);
        Page<CommonDto> newCommonList = mockCommonService.findCommonList(commonMap, pageRequest);

        List<CommonDto> commonDtoList = newCommonList.stream().collect(Collectors.toList());

        // then
        assertThat(commonDtoList.get(0).getIdx()).isEqualTo(commonList.get(0).getIdx());
        assertThat(commonDtoList.get(0).getCommonCode()).isEqualTo(commonList.get(0).getCommonCode());
        assertThat(commonDtoList.get(0).getCommonName()).isEqualTo(commonList.get(0).getCommonName());

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
        CommonDto newCommonDto = commonService.insertCommonCode(commonEntity);

        // when
        when(mockCommonService.findOneCommon(newCommonDto.getIdx())).thenReturn(newCommonDto);
        CommonDto commonInfo = mockCommonService.findOneCommon(newCommonDto.getIdx());

        // then
        assertThat(commonInfo.getCommonCode()).isEqualTo(newCommonDto.getCommonCode());
        assertThat(commonInfo.getCommonName()).isEqualTo(newCommonDto.getCommonName());

        // verify
        verify(mockCommonService, times(1)).findOneCommon(newCommonDto.getIdx());
        verify(mockCommonService, atLeastOnce()).findOneCommon(newCommonDto.getIdx());
        verifyNoMoreInteractions(mockCommonService);

        InOrder inOrder = inOrder(mockCommonService);
        inOrder.verify(mockCommonService).findOneCommon(newCommonDto.getIdx());
    }

    @Test
    @DisplayName("공통 코드 등록 Mockito 테스트")
    void 공통코드등록Mockito테스트() {
        // given
        CommonDto newCommonDto = commonService.insertCommonCode(commonEntity);

        // when
        when(mockCommonService.findOneCommon(newCommonDto.getIdx())).thenReturn(newCommonDto);
        CommonDto commonInfo = mockCommonService.findOneCommon(newCommonDto.getIdx());

        // then
        assertThat(commonInfo.getCommonCode()).isEqualTo(newCommonDto.getCommonCode());
        assertThat(commonInfo.getCommonName()).isEqualTo(newCommonDto.getCommonName());

        // verify
        verify(mockCommonService, times(1)).findOneCommon(newCommonDto.getIdx());
        verify(mockCommonService, atLeastOnce()).findOneCommon(newCommonDto.getIdx());
        verifyNoMoreInteractions(mockCommonService);

        InOrder inOrder = inOrder(mockCommonService);
        inOrder.verify(mockCommonService).findOneCommon(newCommonDto.getIdx());
    }

    @Test
    @DisplayName("공통 코드 수정 Mockito 테스트")
    void 공통코드수정Mockito테스트() {
        // given
        commonService.insertCommonCode(commonEntity);
        CommonEntity newCommonEntity = CommonEntity.builder()
                .idx(commonEntity.getIdx())
                .commonCode(2).commonName("인천").visible("Y").build();

        commonService.updateCommonCode(commonEntity.getIdx(), newCommonEntity);

        CommonDto newCommonInfo = CommonEntity.toDto(newCommonEntity);

        // when
        when(mockCommonService.findOneCommon(newCommonEntity.getIdx())).thenReturn(newCommonInfo);
        CommonDto commonInfo = mockCommonService.findOneCommon(newCommonEntity.getIdx());

        // then
        assertThat(commonInfo.getCommonCode()).isEqualTo(newCommonEntity.getCommonCode());
        assertThat(commonInfo.getCommonName()).isEqualTo(newCommonEntity.getCommonName());

        // verify
        verify(mockCommonService, times(1)).findOneCommon(newCommonEntity.getIdx());
        verify(mockCommonService, atLeastOnce()).findOneCommon(newCommonEntity.getIdx());
        verifyNoMoreInteractions(mockCommonService);

        InOrder inOrder = inOrder(mockCommonService);
        inOrder.verify(mockCommonService).findOneCommon(newCommonEntity.getIdx());
    }

    @Test
    @DisplayName("공통 코드 삭제 테스트")
    void 공통코드삭제테스트() {
        // when
        when(mockCommonService.findOneCommon(commonDTO.getIdx())).thenReturn(commonDTO);
        Long deleteIdx = commonService.deleteCommonCode(commonDTO.getIdx());

        // then
        assertThat(commonDTO.getIdx()).isEqualTo(deleteIdx);
    }
}

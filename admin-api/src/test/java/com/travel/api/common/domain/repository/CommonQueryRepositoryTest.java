package com.travel.api.common.domain.repository;

import com.travel.api.common.domain.CommonDto;
import com.travel.api.common.domain.CommonEntity;
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

@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@ExtendWith(MockitoExtension.class)
@DisplayName("공통 코드 Repository Test")
class CommonQueryRepositoryTest {

    @Mock
    private CommonQueryRepository mockCommonQueryRepository;
    private final CommonQueryRepository commonQueryRepository;

    private CommonEntity commonEntity;
    private CommonDto commonDTO;

    void createCommonCode() {
        commonEntity = CommonEntity.builder()
                .commonCode(1)
                .commonName("서울")
                .visible("Y")
                .build();

        commonDTO = CommonEntity.toDto(commonEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() { createCommonCode(); }

    @Test
    @DisplayName("공통 코드 리스트 조회 테스트")
    void 공통코드리스트조회테스트() {
        Map<String, Object> commonMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(0, 3);
        Page<CommonDto> commonList = commonQueryRepository.findCommonList(commonMap, pageRequest);

        assertThat(commonList.getTotalElements()).isGreaterThan(0);
    }

    @Test
    @DisplayName("공통 코드 리스트 Mockito 조회 테스트")
    void 공통코드리스트Mockito조회테스트() {
        // given
        Map<String, Object> commonMap = new HashMap<>();
        commonMap.put("searchKeyword", "서울");

        PageRequest pageRequest = PageRequest.of(0, 3);

        List<CommonDto> commonList = new ArrayList<>();
        commonList.add(CommonDto.builder().idx(1L).commonCode(1).commonName("서울").visible("Y").build());

        Page<CommonDto> resultPage = new PageImpl<>(commonList, pageRequest, commonList.size());

        // when
        when(mockCommonQueryRepository.findCommonList(commonMap, pageRequest)).thenReturn(resultPage);
        Page<CommonDto> newCommonList = mockCommonQueryRepository.findCommonList(commonMap, pageRequest);

        List<CommonDto> commonDtoList = newCommonList.stream().collect(Collectors.toList());

        // then
        assertThat(commonDtoList.get(0).getIdx()).isEqualTo(commonList.get(0).getIdx());
        assertThat(commonDtoList.get(0).getCommonCode()).isEqualTo(commonList.get(0).getCommonCode());
        assertThat(commonDtoList.get(0).getCommonName()).isEqualTo(commonList.get(0).getCommonName());

        // verify
        verify(mockCommonQueryRepository, times(1)).findCommonList(commonMap, pageRequest);
        verify(mockCommonQueryRepository, atLeastOnce()).findCommonList(commonMap, pageRequest);
        verifyNoMoreInteractions(mockCommonQueryRepository);

        InOrder inOrder = inOrder(mockCommonQueryRepository);
        inOrder.verify(mockCommonQueryRepository).findCommonList(commonMap, pageRequest);
    }
}

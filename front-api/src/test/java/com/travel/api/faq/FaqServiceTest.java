package com.travel.api.faq;

import com.travel.api.FrontCommonServiceTest;
import com.travel.api.faq.domain.FaqDTO;
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
@DisplayName("FAQ Service Test")
class FaqServiceTest extends FrontCommonServiceTest {
    @Mock private FaqService mockFaqService;

    @Test
    @DisplayName("FAQ 리스트 조회 Mockito 테스트")
    void FAQ리스트조회Mockito테스트() {
        Map<String, Object> faqMap = new HashMap<>();

        PageRequest pageRequest = PageRequest.of(0, 3);

        List<FaqDTO> faqList = new ArrayList<>();
        faqList.add(faqDTO);

        Page<FaqDTO> resultPage = new PageImpl<>(faqList, pageRequest, faqList.size());

        // when
        when(mockFaqService.findFaqList(faqMap, pageRequest)).thenReturn(resultPage);
        Page<FaqDTO> newFaqList = mockFaqService.findFaqList(faqMap, pageRequest);

        List<FaqDTO> findFaqList = newFaqList.stream().collect(Collectors.toList());

        // then
        assertThat(findFaqList.get(0).getIdx()).isEqualTo(faqList.get(0).getIdx());
        assertThat(findFaqList.get(0).getNewFaqCode().getCommonCode()).isEqualTo(faqList.get(0).getNewFaqCode().getCommonCode());
        assertThat(findFaqList.get(0).getTitle()).isEqualTo(faqList.get(0).getTitle());
        assertThat(findFaqList.get(0).getDescription()).isEqualTo(faqList.get(0).getDescription());

        // verify
        verify(mockFaqService, times(1)).findFaqList(faqMap, pageRequest);
        verify(mockFaqService, atLeastOnce()).findFaqList(faqMap, pageRequest);
        verifyNoMoreInteractions(mockFaqService);

        InOrder inOrder = inOrder(mockFaqService);
        inOrder.verify(mockFaqService).findFaqList(faqMap, pageRequest);
    }

    @Test
    @DisplayName("FAQ 상세 조회 Mockito 테스트")
    void FAQ상세조회Mockito테스트() {
        // when
        when(mockFaqService.findOneFaq(faqDTO.getIdx())).thenReturn(faqDTO);
        FaqDTO faqInfo = mockFaqService.findOneFaq(faqDTO.getIdx());

        // then
        assertThat(faqInfo.getIdx()).isEqualTo(faqDTO.getIdx());
        assertThat(faqInfo.getTitle()).isEqualTo(faqDTO.getTitle());
        assertThat(faqInfo.getDescription()).isEqualTo(faqDTO.getDescription());

        // verify
        verify(mockFaqService, times(1)).findOneFaq(faqDTO.getIdx());
        verify(mockFaqService, atLeastOnce()).findOneFaq(faqDTO.getIdx());
        verifyNoMoreInteractions(mockFaqService);

        InOrder inOrder = inOrder(mockFaqService);
        inOrder.verify(mockFaqService).findOneFaq(faqDTO.getIdx());
    }
}

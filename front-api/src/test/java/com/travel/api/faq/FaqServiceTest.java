package com.travel.api.faq;

import com.travel.api.FrontCommonServiceTest;
import com.travel.api.faq.domain.FaqDTO;
import com.travel.api.faq.domain.repository.FaqQueryRepository;
import com.travel.api.faq.domain.repository.FaqRepository;
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

import javax.transaction.Transactional;

import java.util.*;
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

    @Mock private FaqRepository faqRepository;
    @Mock private FaqQueryRepository faqQueryRepository;
    @InjectMocks private FaqService mockFaqService;

    @Test
    @DisplayName("FAQ 리스트 조회 Mockito 테스트")
    void FAQ리스트조회Mockito테스트() {
        Map<String, Object> faqMap = new HashMap<>();

        PageRequest pageRequest = PageRequest.of(0, 3);

        List<FaqDTO> faqList = new ArrayList<>();
        faqList.add(faqDTO);

        Page<FaqDTO> resultPage = new PageImpl<>(faqList, pageRequest, faqList.size());

        // when
        when(faqQueryRepository.findFaqList(faqMap, pageRequest)).thenReturn(resultPage);
        Page<FaqDTO> newFaqList = mockFaqService.findFaqList(faqMap, pageRequest);

        List<FaqDTO> findFaqList = newFaqList.stream().collect(Collectors.toList());

        // then
        assertThat(findFaqList.get(0).getIdx()).isEqualTo(faqList.get(0).getIdx());
        assertThat(findFaqList.get(0).getNewFaqCode().getCommonCode()).isEqualTo(faqList.get(0).getNewFaqCode().getCommonCode());
        assertThat(findFaqList.get(0).getTitle()).isEqualTo(faqList.get(0).getTitle());
        assertThat(findFaqList.get(0).getDescription()).isEqualTo(faqList.get(0).getDescription());

        // verify
        verify(faqQueryRepository, times(1)).findFaqList(faqMap, pageRequest);
        verify(faqQueryRepository, atLeastOnce()).findFaqList(faqMap, pageRequest);
        verifyNoMoreInteractions(faqQueryRepository);

        InOrder inOrder = inOrder(faqQueryRepository);
        inOrder.verify(faqQueryRepository).findFaqList(faqMap, pageRequest);
    }

    @Test
    @DisplayName("FAQ 상세 조회 Mockito 테스트")
    void FAQ상세조회Mockito테스트() {
        // when
        when(faqRepository.findByIdx(faqEntity.getIdx())).thenReturn(Optional.ofNullable(faqEntity));
        FaqDTO faqInfo = mockFaqService.findOneFaq(faqEntity.getIdx());

        // then
        assertThat(faqInfo.getIdx()).isEqualTo(faqEntity.getIdx());
        assertThat(faqInfo.getTitle()).isEqualTo(faqEntity.getTitle());
        assertThat(faqInfo.getDescription()).isEqualTo(faqEntity.getDescription());

        // verify
        verify(faqRepository, times(1)).findByIdx(faqEntity.getIdx());
        verify(faqRepository, atLeastOnce()).findByIdx(faqEntity.getIdx());
        verifyNoMoreInteractions(faqRepository);

        InOrder inOrder = inOrder(faqRepository);
        inOrder.verify(faqRepository).findByIdx(faqEntity.getIdx());
    }
}

package com.travel.api.faq;

import com.travel.api.common.domain.CommonEntity;
import com.travel.api.faq.domain.FaqDTO;
import com.travel.api.faq.domain.FaqEntity;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.springframework.boot.context.event.ApplicationReadyEvent;
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
@DisplayName("FAQ Service Test")
class FaqServiceTest {
    @Mock private FaqService mockFaqService;

    private FaqEntity faqEntity;
    private FaqDTO faqDTO;

    private CommonEntity commonEntity;
    private final EntityManager em;

    void createFaq() {
        commonEntity = CommonEntity.builder()
                .commonCode(999)
                .commonName("서울")
                .visible("Y")
                .build();

        em.persist(commonEntity);

        faqEntity = FaqEntity.builder()
                .title("FAQ 등록 테스트")
                .description("FAQ 등록 테스트")
                .viewCount(1)
                .visible("Y")
                .newFaqCode(commonEntity)
                .build();

        em.persist(faqEntity);

        faqDTO = FaqEntity.toDto(faqEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createFaq();
    }

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
        assertThat(findFaqList.get(0).getFaqCode()).isEqualTo(faqList.get(0).getFaqCode());
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
        when(mockFaqService.findOneFaq(faqEntity.getIdx())).thenReturn(faqDTO);
        FaqDTO faqInfo = mockFaqService.findOneFaq(faqEntity.getIdx());

        // then
        assertThat(faqInfo.getIdx()).isEqualTo(faqEntity.getIdx());
        assertThat(faqInfo.getTitle()).isEqualTo(faqEntity.getTitle());
        assertThat(faqInfo.getDescription()).isEqualTo(faqEntity.getDescription());

        // verify
        verify(mockFaqService, times(1)).findOneFaq(faqEntity.getIdx());
        verify(mockFaqService, atLeastOnce()).findOneFaq(faqEntity.getIdx());
        verifyNoMoreInteractions(mockFaqService);

        InOrder inOrder = inOrder(mockFaqService);
        inOrder.verify(mockFaqService).findOneFaq(faqEntity.getIdx());
    }
}

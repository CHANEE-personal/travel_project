package com.travel.travel_project.api.faq;

import com.travel.travel_project.domain.faq.FaqEntity;
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
@DisplayName("FAQ Repository Test")
class FaqRepositoryTest {

    @Mock private FaqRepository mockFaqRepository;
    private final FaqRepository faqRepository;
    private final EntityManager em;

    private FaqEntity faqEntity;

    void createFaq() {
        faqEntity = FaqEntity.builder()
                .faqCode(3L)
                .title("FAQ 등록 테스트")
                .description("FAQ 등록 테스트")
                .viewCount(1)
                .visible("Y")
                .build();
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() { createFaq(); }

    @Test
    @DisplayName("FAQ 리스트 조회 Mockito 테스트")
    void FAQ리스트조회Mockito테스트() {
        Map<String, Object> faqMap = new HashMap<>();
        faqMap.put("jpaStartPage", 1);
        faqMap.put("size", 3);

        List<FaqEntity> faqList = new ArrayList<>();
        faqList.add(faqEntity);

        // when
        when(mockFaqRepository.findFaqList(faqMap)).thenReturn(faqList);
        List<FaqEntity> newFaqList = mockFaqRepository.findFaqList(faqMap);

        // then
        assertThat(newFaqList.get(0).getIdx()).isEqualTo(faqList.get(0).getIdx());
        assertThat(newFaqList.get(0).getFaqCode()).isEqualTo(faqList.get(0).getFaqCode());
        assertThat(newFaqList.get(0).getTitle()).isEqualTo(faqList.get(0).getTitle());
        assertThat(newFaqList.get(0).getDescription()).isEqualTo(faqList.get(0).getDescription());

        // verify
        verify(mockFaqRepository, times(1)).findFaqList(faqMap);
        verify(mockFaqRepository, atLeastOnce()).findFaqList(faqMap);
        verifyNoMoreInteractions(mockFaqRepository);

        InOrder inOrder = inOrder(mockFaqRepository);
        inOrder.verify(mockFaqRepository).findFaqList(faqMap);
    }

    @Test
    @DisplayName("FAQ 상세 조회 Mockito 테스트")
    void FAQ상세조회Mockito테스트() {
        // when
        when(mockFaqRepository.findOneFaq(faqEntity.getIdx())).thenReturn(faqEntity);
        FaqEntity faqInfo = mockFaqRepository.findOneFaq(faqEntity.getIdx());

        // then
        assertThat(faqInfo.getIdx()).isEqualTo(faqEntity.getIdx());
        assertThat(faqInfo.getFaqCode()).isEqualTo(faqEntity.getFaqCode());
        assertThat(faqInfo.getTitle()).isEqualTo(faqEntity.getTitle());
        assertThat(faqInfo.getDescription()).isEqualTo(faqEntity.getDescription());

        // verify
        verify(mockFaqRepository, times(1)).findOneFaq(faqEntity.getIdx());
        verify(mockFaqRepository, atLeastOnce()).findOneFaq(faqEntity.getIdx());
        verifyNoMoreInteractions(mockFaqRepository);

        InOrder inOrder = inOrder(mockFaqRepository);
        inOrder.verify(mockFaqRepository).findOneFaq(faqEntity.getIdx());
    }

    @Test
    @DisplayName("FAQ등록Mockito테스트")
    void FAQ등록Mockito테스트() {
        // given
        FaqEntity faqInfo = faqRepository.insertFaq(faqEntity);

        // when
        when(mockFaqRepository.findOneFaq(faqInfo.getIdx())).thenReturn(faqInfo);
        FaqEntity newFaqInfo = mockFaqRepository.findOneFaq(faqInfo.getIdx());

        // then
        assertThat(newFaqInfo.getIdx()).isEqualTo(newFaqInfo.getIdx());
        assertThat(newFaqInfo.getFaqCode()).isEqualTo(newFaqInfo.getFaqCode());
        assertThat(newFaqInfo.getTitle()).isEqualTo(newFaqInfo.getTitle());
        assertThat(newFaqInfo.getDescription()).isEqualTo(newFaqInfo.getDescription());

        // verify
        verify(mockFaqRepository, times(1)).findOneFaq(faqInfo.getIdx());
        verify(mockFaqRepository, atLeastOnce()).findOneFaq(faqInfo.getIdx());
        verifyNoMoreInteractions(mockFaqRepository);

        InOrder inOrder = inOrder(mockFaqRepository);
        inOrder.verify(mockFaqRepository).findOneFaq(faqInfo.getIdx());
    }

    @Test
    @DisplayName("FAQ 수정 Mockito 테스트")
    void FAQ수정Mockito테스트() {
        // given
        FaqEntity faqInfo = faqRepository.insertFaq(faqEntity);

        FaqEntity updateFaqEntity = FaqEntity.builder()
                .idx(faqInfo.getIdx())
                .faqCode(3L)
                .title("FAQ 수정 테스트")
                .description("FAQ 수정 테스트")
                .viewCount(1)
                .visible("Y")
                .build();

        FaqEntity newFaqInfo = faqRepository.updateFaq(updateFaqEntity);

        // when
        when(mockFaqRepository.findOneFaq(newFaqInfo.getIdx())).thenReturn(newFaqInfo);
        FaqEntity findFaqInfo = mockFaqRepository.findOneFaq(newFaqInfo.getIdx());

        // then
        assertThat(findFaqInfo.getTitle()).isEqualTo(updateFaqEntity.getTitle());
        assertThat(findFaqInfo.getDescription()).isEqualTo(updateFaqEntity.getDescription());

        // verify
        verify(mockFaqRepository, times(1)).findOneFaq(newFaqInfo.getIdx());
        verify(mockFaqRepository, atLeastOnce()).findOneFaq(newFaqInfo.getIdx());
        verifyNoMoreInteractions(mockFaqRepository);

        InOrder inOrder = inOrder(mockFaqRepository);
        inOrder.verify(mockFaqRepository).findOneFaq(newFaqInfo.getIdx());
    }

    @Test
    @DisplayName("FAQ 삭제 Mockito 테스트")
    void FAQ삭제Mockito테스트() {
        // given
        FaqEntity faqInfo = faqRepository.insertFaq(faqEntity);

        // when
        when(mockFaqRepository.findOneFaq(faqInfo.getIdx())).thenReturn(faqInfo);
        Long deleteIdx = faqRepository.deleteFaq(faqInfo.getIdx());

        // then
        assertThat(mockFaqRepository.findOneFaq(faqInfo.getIdx()).getIdx()).isEqualTo(deleteIdx);

        // verify
        verify(mockFaqRepository, times(1)).findOneFaq(faqInfo.getIdx());
        verify(mockFaqRepository, atLeastOnce()).findOneFaq(faqInfo.getIdx());
        verifyNoMoreInteractions(mockFaqRepository);

        InOrder inOrder = inOrder(mockFaqRepository);
        inOrder.verify(mockFaqRepository).findOneFaq(faqInfo.getIdx());
    }
}
package com.travel.api.faq.domain.repository;

import com.travel.api.common.domain.CommonEntity;
import com.travel.api.faq.domain.FaqDTO;
import com.travel.api.faq.domain.FaqEntity;
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
@DisplayName("FAQ Repository Test")
class FaqQueryRepositoryTest {

    @Mock private FaqQueryRepository mockFaqQueryRepository;
    private final FaqQueryRepository faqQueryRepository;
    private final EntityManager em;

    private FaqEntity faqEntity;

    private CommonEntity commonEntity;

    void createFaq() {
        commonEntity = CommonEntity.builder()
                .commonCode(999)
                .commonName("서울")
                .visible("Y")
                .build();

        em.persist(commonEntity);

        faqEntity = FaqEntity.builder()
                .newFaqCode(commonEntity)
                .title("FAQ 등록 테스트")
                .description("FAQ 등록 테스트")
                .viewCount(1)
                .visible("Y")
                .build();

        em.persist(faqEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() { createFaq(); }

    @Test
    @DisplayName("FAQ 리스트 조회 테스트")
    void FAQ리스트조회테스트() {
        Map<String, Object> faqMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(0, 3);
        Page<FaqDTO> faqList = faqQueryRepository.findFaqList(faqMap, pageRequest);

        assertThat(faqList.getTotalElements()).isGreaterThan(0);
    }

    @Test
    @DisplayName("FAQ 리스트 조회 Mockito 테스트")
    void FAQ리스트조회Mockito테스트() {
        Map<String, Object> faqMap = new HashMap<>();

        PageRequest pageRequest = PageRequest.of(0, 3);

        List<FaqDTO> faqList = new ArrayList<>();
        faqList.add(FaqEntity.toDto(faqEntity));

        Page<FaqDTO> resultPage = new PageImpl<>(faqList, pageRequest, faqList.size());

        // when
        when(mockFaqQueryRepository.findFaqList(faqMap, pageRequest)).thenReturn(resultPage);
        Page<FaqDTO> newFaqList = mockFaqQueryRepository.findFaqList(faqMap, pageRequest);

        List<FaqDTO> findFaqList = newFaqList.stream().collect(Collectors.toList());

        // then
        assertThat(findFaqList.get(0).getIdx()).isEqualTo(faqList.get(0).getIdx());
        assertThat(findFaqList.get(0).getNewFaqCode().getCommonCode()).isEqualTo(faqList.get(0).getNewFaqCode().getCommonCode());
        assertThat(findFaqList.get(0).getTitle()).isEqualTo(faqList.get(0).getTitle());
        assertThat(findFaqList.get(0).getDescription()).isEqualTo(faqList.get(0).getDescription());

        // verify
        verify(mockFaqQueryRepository, times(1)).findFaqList(faqMap, pageRequest);
        verify(mockFaqQueryRepository, atLeastOnce()).findFaqList(faqMap, pageRequest);
        verifyNoMoreInteractions(mockFaqQueryRepository);

        InOrder inOrder = inOrder(mockFaqQueryRepository);
        inOrder.verify(mockFaqQueryRepository).findFaqList(faqMap, pageRequest);
    }

    @Test
    @DisplayName("FAQ 상세 조회 Mockito 테스트")
    void FAQ상세조회Mockito테스트() {
        // when
        when(mockFaqQueryRepository.findOneFaq(faqEntity.getIdx())).thenReturn(FaqEntity.toDto(faqEntity));
        FaqDTO faqInfo = mockFaqQueryRepository.findOneFaq(faqEntity.getIdx());

        // then
        assertThat(faqInfo.getTitle()).isEqualTo(faqEntity.getTitle());
        assertThat(faqInfo.getDescription()).isEqualTo(faqEntity.getDescription());

        // verify
        verify(mockFaqQueryRepository, times(1)).findOneFaq(faqEntity.getIdx());
        verify(mockFaqQueryRepository, atLeastOnce()).findOneFaq(faqEntity.getIdx());
        verifyNoMoreInteractions(mockFaqQueryRepository);

        InOrder inOrder = inOrder(mockFaqQueryRepository);
        inOrder.verify(mockFaqQueryRepository).findOneFaq(faqEntity.getIdx());
    }
}

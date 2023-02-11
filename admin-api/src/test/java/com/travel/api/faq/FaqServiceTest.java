package com.travel.api.faq;

import com.travel.api.AdminCommonServiceTest;
import com.travel.api.common.domain.repository.CommonRepository;
import com.travel.api.faq.domain.FaqDto;
import com.travel.api.faq.domain.FaqEntity;
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
class FaqServiceTest extends AdminCommonServiceTest {
    @Mock private FaqRepository faqRepository;
    @Mock private CommonRepository commonRepository;
    @Mock private FaqQueryRepository faqQueryRepository;
    @InjectMocks private FaqService mockFaqService;
    private final FaqService faqService;

    @Test
    @DisplayName("FAQ 리스트 조회 Mockito 테스트")
    void FAQ리스트조회Mockito테스트() {
        Map<String, Object> faqMap = new HashMap<>();

        PageRequest pageRequest = PageRequest.of(0, 3);

        List<FaqDto> faqList = new ArrayList<>();
        faqList.add(faqDTO);

        Page<FaqDto> resultPage = new PageImpl<>(faqList, pageRequest, faqList.size());

        // when
        when(faqQueryRepository.findFaqList(faqMap, pageRequest)).thenReturn(resultPage);
        Page<FaqDto> newFaqList = mockFaqService.findFaqList(faqMap, pageRequest);

        List<FaqDto> findFaqList = newFaqList.stream().collect(Collectors.toList());

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
        when(faqQueryRepository.findOneFaq(faqEntity.getIdx())).thenReturn(faqDTO);
        FaqDto faqInfo = mockFaqService.findOneFaq(faqEntity.getIdx());

        // then
        assertThat(faqInfo.getIdx()).isEqualTo(faqEntity.getIdx());
        assertThat(faqInfo.getTitle()).isEqualTo(faqEntity.getTitle());
        assertThat(faqInfo.getDescription()).isEqualTo(faqEntity.getDescription());

        // verify
        verify(faqQueryRepository, times(1)).findOneFaq(faqEntity.getIdx());
        verify(faqQueryRepository, atLeastOnce()).findOneFaq(faqEntity.getIdx());
        verifyNoMoreInteractions(faqQueryRepository);

        InOrder inOrder = inOrder(faqQueryRepository);
        inOrder.verify(faqQueryRepository).findOneFaq(faqEntity.getIdx());
    }

    @Test
    @DisplayName("FAQ등록Mockito테스트")
    void FAQ등록Mockito테스트() {
        // when
        when(commonRepository.findByCommonCode(commonEntity.getCommonCode())).thenReturn(Optional.ofNullable(commonEntity));
        when(faqRepository.save(faqEntity)).thenReturn(faqEntity);
        FaqDto newFaqInfo = mockFaqService.insertFaq(faqEntity);

        // then
        assertThat(newFaqInfo.getIdx()).isEqualTo(newFaqInfo.getIdx());
        assertThat(newFaqInfo.getNewFaqCode().getCommonCode()).isEqualTo(newFaqInfo.getNewFaqCode().getCommonCode());
        assertThat(newFaqInfo.getTitle()).isEqualTo(newFaqInfo.getTitle());
        assertThat(newFaqInfo.getDescription()).isEqualTo(newFaqInfo.getDescription());

        // verify
        verify(faqRepository, times(1)).save(faqEntity);
        verify(faqRepository, atLeastOnce()).save(faqEntity);
        verifyNoMoreInteractions(faqRepository);

        InOrder inOrder = inOrder(faqRepository);
        inOrder.verify(faqRepository).save(faqEntity);
    }

    @Test
    @DisplayName("FAQ 수정 Mockito 테스트")
    void FAQ수정Mockito테스트() {
        // given
        FaqEntity updateFaqEntity = FaqEntity.builder()
                .idx(faqEntity.getIdx())
                .title("FAQ 수정 테스트")
                .description("FAQ 수정 테스트")
                .viewCount(1)
                .visible("Y")
                .build();

        // when
        when(faqRepository.findById(updateFaqEntity.getIdx())).thenReturn(Optional.of(updateFaqEntity));
        when(faqRepository.save(updateFaqEntity)).thenReturn(updateFaqEntity);
        FaqDto findFaqInfo = mockFaqService.updateFaq(updateFaqEntity.getIdx(), updateFaqEntity);

        // then
        assertThat(findFaqInfo.getTitle()).isEqualTo(updateFaqEntity.getTitle());
        assertThat(findFaqInfo.getDescription()).isEqualTo(updateFaqEntity.getDescription());

        // verify
        verify(faqRepository, times(1)).findById(updateFaqEntity.getIdx());
        verify(faqRepository, atLeastOnce()).findById(updateFaqEntity.getIdx());
        verifyNoMoreInteractions(faqRepository);

        InOrder inOrder = inOrder(faqRepository);
        inOrder.verify(faqRepository).findById(updateFaqEntity.getIdx());
    }

    @Test
    @DisplayName("FAQ 삭제 테스트")
    void FAQ삭제테스트() {
        // when
        Long deleteIdx = faqService.deleteFaq(faqEntity.getIdx());

        // then
        assertThat(faqEntity.getIdx()).isEqualTo(deleteIdx);
    }
}

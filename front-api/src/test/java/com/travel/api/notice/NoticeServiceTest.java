package com.travel.api.notice;

import com.travel.api.FrontCommonServiceTest;
import com.travel.api.notice.domain.NoticeDTO;
import com.travel.api.notice.domain.repository.NoticeQueryRepository;
import com.travel.api.notice.domain.repository.NoticeRepository;
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
@DisplayName("공지사항 Service Test")
class NoticeServiceTest extends FrontCommonServiceTest {
    @Mock private NoticeRepository noticeRepository;
    @Mock private NoticeQueryRepository noticeQueryRepository;
    @InjectMocks private NoticeService mockNoticeService;
    private final NoticeService noticeService;

    @Test
    @DisplayName("공지사항 리스트 조회 Mockito 테스트")
    void 공지사항리스트조회Mockito테스트() {
        // given
        Map<String, Object> noticeMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(0, 3);

        List<NoticeDTO> noticeList = new ArrayList<>();
        noticeList.add(noticeDTO);

        Page<NoticeDTO> resultPage = new PageImpl<>(noticeList, pageRequest, noticeList.size());

        // when
        when(noticeQueryRepository.findNoticeList(noticeMap, pageRequest)).thenReturn(resultPage);
        Page<NoticeDTO> newNoticeList = mockNoticeService.findNoticeList(noticeMap, pageRequest);

        List<NoticeDTO> findNoticeList = newNoticeList.stream().collect(Collectors.toList());
        // then
        assertThat(findNoticeList.get(0).getIdx()).isEqualTo(noticeList.get(0).getIdx());
        assertThat(findNoticeList.get(0).getTitle()).isEqualTo(noticeList.get(0).getTitle());
        assertThat(findNoticeList.get(0).getDescription()).isEqualTo(noticeList.get(0).getDescription());

        // verify
        verify(noticeQueryRepository, times(1)).findNoticeList(noticeMap, pageRequest);
        verify(noticeQueryRepository, atLeastOnce()).findNoticeList(noticeMap, pageRequest);
        verifyNoMoreInteractions(noticeQueryRepository);

        InOrder inOrder = inOrder(noticeQueryRepository);
        inOrder.verify(noticeQueryRepository).findNoticeList(noticeMap, pageRequest);
    }

    @Test
    @DisplayName("공지사항 상세 조회 Mockito 테스트")
    void 공지사항상세조회Mockito테스트() {
        // 조회 수 관련 테스트
        NoticeDTO oneNotice = noticeService.findOneNotice(noticeEntity.getIdx());
        assertThat(noticeEntity.getViewCount()).isEqualTo(oneNotice.getViewCount());

        // when
        when(noticeRepository.findById(noticeEntity.getIdx())).thenReturn(Optional.ofNullable(noticeEntity));
        NoticeDTO noticeInfo = mockNoticeService.findOneNotice(noticeEntity.getIdx());

        // then
        assertThat(noticeInfo.getIdx()).isEqualTo(noticeEntity.getIdx());
        assertThat(noticeInfo.getTitle()).isEqualTo(noticeEntity.getTitle());
        assertThat(noticeInfo.getDescription()).isEqualTo(noticeEntity.getDescription());

        // verify
        verify(noticeRepository, times(1)).findById(noticeEntity.getIdx());
        verify(noticeRepository, atLeastOnce()).findById(noticeEntity.getIdx());
        verifyNoMoreInteractions(noticeRepository);

        InOrder inOrder = inOrder(noticeRepository);
        inOrder.verify(noticeRepository).findById(noticeEntity.getIdx());
    }
}

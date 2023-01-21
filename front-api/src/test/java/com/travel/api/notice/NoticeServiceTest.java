package com.travel.api.notice;

import com.travel.api.notice.domain.NoticeDTO;
import com.travel.api.notice.domain.NoticeEntity;
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
@DisplayName("공지사항 Service Test")
class NoticeServiceTest {
    @Mock private NoticeService mockNoticeService;
    private final NoticeService noticeService;
    private final EntityManager em;

    private NoticeEntity noticeEntity;
    private NoticeDTO noticeDTO;

    void createNotice() {
        noticeEntity = NoticeEntity.builder()
                .title("공지사항 등록 테스트")
                .description("공지사항 등록 테스트")
                .topFixed(false)
                .visible("Y").viewCount(1)
                .build();

        noticeDTO = NoticeEntity.toDto(noticeEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createNotice();
    }

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
        when(mockNoticeService.findNoticeList(noticeMap, pageRequest)).thenReturn(resultPage);
        Page<NoticeDTO> newNoticeList = mockNoticeService.findNoticeList(noticeMap, pageRequest);

        List<NoticeDTO> findNoticeList = newNoticeList.stream().collect(Collectors.toList());
        // then
        assertThat(findNoticeList.get(0).getIdx()).isEqualTo(noticeList.get(0).getIdx());
        assertThat(findNoticeList.get(0).getTitle()).isEqualTo(noticeList.get(0).getTitle());
        assertThat(findNoticeList.get(0).getDescription()).isEqualTo(noticeList.get(0).getDescription());

        // verify
        verify(mockNoticeService, times(1)).findNoticeList(noticeMap, pageRequest);
        verify(mockNoticeService, atLeastOnce()).findNoticeList(noticeMap, pageRequest);
        verifyNoMoreInteractions(mockNoticeService);

        InOrder inOrder = inOrder(mockNoticeService);
        inOrder.verify(mockNoticeService).findNoticeList(noticeMap, pageRequest);
    }

    @Test
    @DisplayName("공지사항 상세 조회 Mockito 테스트")
    void 공지사항상세조회Mockito테스트() {
        em.persist(noticeEntity);
        NoticeDTO newNotice = NoticeEntity.toDto(noticeEntity);

        // 조회 수 관련 테스트
        NoticeDTO oneNotice = noticeService.findOneNotice(newNotice.getIdx());
        assertThat(newNotice.getViewCount() + 1).isEqualTo(oneNotice.getViewCount());
        // when
        when(mockNoticeService.findOneNotice(noticeDTO.getIdx())).thenReturn(noticeDTO);
        NoticeDTO noticeInfo = mockNoticeService.findOneNotice(noticeDTO.getIdx());

        // then
        assertThat(noticeInfo.getIdx()).isEqualTo(noticeDTO.getIdx());
        assertThat(noticeInfo.getTitle()).isEqualTo(noticeDTO.getTitle());
        assertThat(noticeInfo.getDescription()).isEqualTo(noticeDTO.getDescription());

        // verify
        verify(mockNoticeService, times(1)).findOneNotice(noticeDTO.getIdx());
        verify(mockNoticeService, atLeastOnce()).findOneNotice(noticeDTO.getIdx());
        verifyNoMoreInteractions(mockNoticeService);

        InOrder inOrder = inOrder(mockNoticeService);
        inOrder.verify(mockNoticeService).findOneNotice(noticeDTO.getIdx());
    }
}

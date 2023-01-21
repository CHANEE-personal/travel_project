package com.travel.api.notice;

import com.travel.api.notice.domain.NoticeDto;
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
    private NoticeDto noticeDTO;

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

        List<NoticeDto> noticeList = new ArrayList<>();
        noticeList.add(noticeDTO);

        Page<NoticeDto> resultPage = new PageImpl<>(noticeList, pageRequest, noticeList.size());

        // when
        when(mockNoticeService.findNoticeList(noticeMap, pageRequest)).thenReturn(resultPage);
        Page<NoticeDto> newNoticeList = mockNoticeService.findNoticeList(noticeMap, pageRequest);

        List<NoticeDto> findNoticeList = newNoticeList.stream().collect(Collectors.toList());
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
        NoticeDto newNotice = noticeService.insertNotice(noticeEntity);

        // 조회 수 관련 테스트
        NoticeDto oneNotice = noticeService.findOneNotice(newNotice.getIdx());
        assertThat(newNotice.getViewCount() + 1).isEqualTo(oneNotice.getViewCount());
        // when
        when(mockNoticeService.findOneNotice(noticeDTO.getIdx())).thenReturn(noticeDTO);
        NoticeDto noticeInfo = mockNoticeService.findOneNotice(noticeDTO.getIdx());

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

    @Test
    @DisplayName("공지사항 등록 Mockito 테스트")
    void 공지사항등록Mockito테스트() {
        NoticeDto noticeInfo = noticeService.insertNotice(noticeEntity);

        // when
        when(mockNoticeService.findOneNotice(noticeInfo.getIdx())).thenReturn(noticeInfo);
        NoticeDto newNoticeDTO = mockNoticeService.findOneNotice(noticeInfo.getIdx());

        // then
        assertThat(newNoticeDTO.getIdx()).isEqualTo(noticeInfo.getIdx());
        assertThat(newNoticeDTO.getTitle()).isEqualTo(noticeInfo.getTitle());
        assertThat(newNoticeDTO.getDescription()).isEqualTo(noticeInfo.getDescription());

        // verify
        verify(mockNoticeService, times(1)).findOneNotice(noticeInfo.getIdx());
        verify(mockNoticeService, atLeastOnce()).findOneNotice(noticeInfo.getIdx());
        verifyNoMoreInteractions(mockNoticeService);

        InOrder inOrder = inOrder(mockNoticeService);
        inOrder.verify(mockNoticeService).findOneNotice(noticeInfo.getIdx());
    }

    @Test
    @DisplayName("공지사항 수정 Mockito 테스트")
    void 공지사항수정Mockito테스트() {
        // given
        noticeEntity = NoticeEntity.builder()
                .title("공지사항 등록 테스트")
                .description("공지사항 등록 테스트")
                .viewCount(0)
                .visible("Y")
                .build();

        NoticeDto newNoticeDTO = noticeService.insertNotice(noticeEntity);

        NoticeEntity updateNoticeEntity = NoticeEntity.builder()
                .idx(newNoticeDTO.getIdx())
                .title("공지사항 수정 테스트")
                .description("공지사항 수정 테스트")
                .viewCount(0)
                .visible("Y")
                .build();

        NoticeDto updateNoticeDTO = noticeService.updateNotice(newNoticeDTO.getIdx(), updateNoticeEntity);

        // when
        when(mockNoticeService.findOneNotice(updateNoticeDTO.getIdx())).thenReturn(updateNoticeDTO);
        NoticeDto noticeInfo = mockNoticeService.findOneNotice(updateNoticeDTO.getIdx());

        // then
        assertThat(noticeInfo.getIdx()).isEqualTo(updateNoticeDTO.getIdx());
        assertThat(noticeInfo.getTitle()).isEqualTo(updateNoticeDTO.getTitle());
        assertThat(noticeInfo.getDescription()).isEqualTo(updateNoticeDTO.getDescription());

        // verify
        verify(mockNoticeService, times(1)).findOneNotice(noticeInfo.getIdx());
        verify(mockNoticeService, atLeastOnce()).findOneNotice(noticeInfo.getIdx());
        verifyNoMoreInteractions(mockNoticeService);

        InOrder inOrder = inOrder(mockNoticeService);
        inOrder.verify(mockNoticeService).findOneNotice(noticeInfo.getIdx());
    }

    @Test
    @DisplayName("공지사항 삭제 Mockito 테스트")
    void 공지사항삭제Mockito테스트() {
        // given
        em.persist(noticeEntity);
        noticeDTO = NoticeEntity.toDto(noticeEntity);

        // when
        when(mockNoticeService.findOneNotice(noticeDTO.getIdx())).thenReturn(noticeDTO);
        Long deleteIdx = noticeService.deleteNotice(noticeDTO.getIdx());

        // then
        assertThat(mockNoticeService.findOneNotice(noticeDTO.getIdx()).getIdx()).isEqualTo(deleteIdx);

        // verify
        verify(mockNoticeService, times(1)).findOneNotice(noticeDTO.getIdx());
        verify(mockNoticeService, atLeastOnce()).findOneNotice(noticeDTO.getIdx());
        verifyNoMoreInteractions(mockNoticeService);

        InOrder inOrder = inOrder(mockNoticeService);
        inOrder.verify(mockNoticeService).findOneNotice(noticeDTO.getIdx());
    }

    @Test
    @DisplayName("공지사항 고정글 테스트")
    void 공지사항고정글테스트() {
        NoticeDto oneNotice = noticeService.insertNotice(noticeEntity);
        Boolean trueFixed = noticeService.toggleTopFixed(oneNotice.getIdx());
        em.flush();
        em.clear();
        assertThat(trueFixed).isEqualTo(true);

        Boolean falseFixed = noticeService.toggleTopFixed(oneNotice.getIdx());
        assertThat(falseFixed).isEqualTo(false);
    }
}

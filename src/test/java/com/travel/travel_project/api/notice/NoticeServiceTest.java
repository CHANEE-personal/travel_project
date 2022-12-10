package com.travel.travel_project.api.notice;

import com.travel.travel_project.api.notice.mapper.NoticeMapper;
import com.travel.travel_project.domain.notice.NoticeDTO;
import com.travel.travel_project.domain.notice.NoticeEntity;
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

        noticeDTO = NoticeMapper.INSTANCE.toDto(noticeEntity);
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
        noticeMap.put("jpaStartPage", 1);
        noticeMap.put("size", 3);

        List<NoticeEntity> noticeList = new ArrayList<>();
        noticeList.add(noticeEntity);

        // when
        when(mockNoticeService.findNoticeList(noticeMap)).thenReturn(NoticeMapper.INSTANCE.toDtoList(noticeList));
        List<NoticeDTO> newNoticeList = mockNoticeService.findNoticeList(noticeMap);

        // then
        assertThat(newNoticeList.get(0).getIdx()).isEqualTo(noticeList.get(0).getIdx());
        assertThat(newNoticeList.get(0).getTitle()).isEqualTo(noticeList.get(0).getTitle());
        assertThat(newNoticeList.get(0).getDescription()).isEqualTo(noticeList.get(0).getDescription());

        // verify
        verify(mockNoticeService, times(1)).findNoticeList(noticeMap);
        verify(mockNoticeService, atLeastOnce()).findNoticeList(noticeMap);
        verifyNoMoreInteractions(mockNoticeService);

        InOrder inOrder = inOrder(mockNoticeService);
        inOrder.verify(mockNoticeService).findNoticeList(noticeMap);
    }

    @Test
    @DisplayName("공지사항 상세 조회 Mockito 테스트")
    void 공지사항상세조회Mockito테스트() {
        NoticeDTO newNotice = noticeService.insertNotice(noticeEntity);

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

    @Test
    @DisplayName("공지사항 등록 Mockito 테스트")
    void 공지사항등록Mockito테스트() {
        NoticeDTO noticeInfo = noticeService.insertNotice(noticeEntity);

        // when
        when(mockNoticeService.findOneNotice(noticeInfo.getIdx())).thenReturn(noticeInfo);
        NoticeDTO newNoticeDTO = mockNoticeService.findOneNotice(noticeInfo.getIdx());

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

        NoticeDTO newNoticeDTO = noticeService.insertNotice(noticeEntity);

        NoticeEntity updateNoticeEntity = NoticeEntity.builder()
                .idx(newNoticeDTO.getIdx())
                .title("공지사항 수정 테스트")
                .description("공지사항 수정 테스트")
                .viewCount(0)
                .visible("Y")
                .build();

        NoticeDTO updateNoticeDTO = noticeService.updateNotice(updateNoticeEntity);

        // when
        when(mockNoticeService.findOneNotice(updateNoticeDTO.getIdx())).thenReturn(updateNoticeDTO);
        NoticeDTO noticeInfo = mockNoticeService.findOneNotice(updateNoticeDTO.getIdx());

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
        noticeDTO = NoticeMapper.INSTANCE.toDto(noticeEntity);

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
        NoticeDTO oneNotice = noticeService.insertNotice(noticeEntity);
        Boolean trueFixed = noticeService.toggleTopFixed(oneNotice.getIdx());
        assertThat(trueFixed).isEqualTo(true);

        Boolean falseFixed = noticeService.toggleTopFixed(oneNotice.getIdx());
        assertThat(falseFixed).isEqualTo(false);
    }
}
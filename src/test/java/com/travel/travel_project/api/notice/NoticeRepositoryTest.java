package com.travel.travel_project.api.notice;

import com.travel.travel_project.api.notice.mapper.NoticeMapper;
import com.travel.travel_project.domain.notice.NoticeDTO;
import com.travel.travel_project.domain.notice.NoticeEntity;

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
import static org.mockito.BDDMockito.given;
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
@DisplayName("공지사항 Repository Test")
class NoticeRepositoryTest {
    @Mock
    private NoticeRepository mockNoticeRepository;
    private final NoticeRepository noticeRepository;
    private final EntityManager em;

    private NoticeEntity noticeEntity;
    private NoticeDTO noticeDTO;

    void createNotice() {
        noticeEntity = NoticeEntity.builder()
                .title("공지사항 등록 테스트")
                .description("공지사항 등록 테스트")
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

        List<NoticeDTO> noticeList = new ArrayList<>();
        noticeList.add(noticeDTO);

        // when
        when(mockNoticeRepository.findNoticeList(noticeMap)).thenReturn(noticeList);
        List<NoticeDTO> newNoticeList = mockNoticeRepository.findNoticeList(noticeMap);

        // then
        assertThat(newNoticeList.get(0).getIdx()).isEqualTo(noticeList.get(0).getIdx());
        assertThat(newNoticeList.get(0).getTitle()).isEqualTo(noticeList.get(0).getTitle());
        assertThat(newNoticeList.get(0).getDescription()).isEqualTo(noticeList.get(0).getDescription());

        // verify
        verify(mockNoticeRepository, times(1)).findNoticeList(noticeMap);
        verify(mockNoticeRepository, atLeastOnce()).findNoticeList(noticeMap);
        verifyNoMoreInteractions(mockNoticeRepository);

        InOrder inOrder = inOrder(mockNoticeRepository);
        inOrder.verify(mockNoticeRepository).findNoticeList(noticeMap);
    }

    @Test
    @DisplayName("공지사항 상세 조회 Mockito 테스트")
    void 공지사항상세조회Mockito테스트() {
        // when
        when(mockNoticeRepository.findOneNotice(noticeDTO.getIdx())).thenReturn(noticeDTO);
        NoticeDTO noticeInfo = mockNoticeRepository.findOneNotice(noticeDTO.getIdx());

        // then
        assertThat(noticeInfo.getIdx()).isEqualTo(noticeDTO.getIdx());
        assertThat(noticeInfo.getTitle()).isEqualTo(noticeDTO.getTitle());
        assertThat(noticeInfo.getDescription()).isEqualTo(noticeDTO.getDescription());

        // verify
        verify(mockNoticeRepository, times(1)).findOneNotice(noticeDTO.getIdx());
        verify(mockNoticeRepository, atLeastOnce()).findOneNotice(noticeDTO.getIdx());
        verifyNoMoreInteractions(mockNoticeRepository);

        InOrder inOrder = inOrder(mockNoticeRepository);
        inOrder.verify(mockNoticeRepository).findOneNotice(noticeDTO.getIdx());
    }

    @Test
    @DisplayName("공지사항 등록 Mockito 테스트")
    void 공지사항등록Mockito테스트() {
        NoticeDTO noticeInfo = noticeRepository.insertNotice(noticeEntity);

        // when
        given(mockNoticeRepository.findOneNotice(noticeInfo.getIdx())).willReturn(noticeInfo);
        NoticeDTO newNoticeDTO = mockNoticeRepository.findOneNotice(noticeInfo.getIdx());

        // then
        assertThat(newNoticeDTO.getIdx()).isEqualTo(noticeEntity.getIdx());
        assertThat(newNoticeDTO.getTitle()).isEqualTo(noticeEntity.getTitle());
        assertThat(newNoticeDTO.getDescription()).isEqualTo(noticeEntity.getDescription());

        // verify
        verify(mockNoticeRepository, times(1)).findOneNotice(noticeEntity.getIdx());
        verify(mockNoticeRepository, atLeastOnce()).findOneNotice(noticeEntity.getIdx());
        verifyNoMoreInteractions(mockNoticeRepository);

        InOrder inOrder = inOrder(mockNoticeRepository);
        inOrder.verify(mockNoticeRepository).findOneNotice(noticeEntity.getIdx());
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

        NoticeDTO newNoticeEntity = noticeRepository.insertNotice(noticeEntity);

        NoticeEntity updateNoticeEntity = NoticeEntity.builder()
                .idx(newNoticeEntity.getIdx())
                .title("공지사항 수정 테스트")
                .description("공지사항 수정 테스트")
                .viewCount(0)
                .visible("Y")
                .build();

        NoticeDTO updateNotice = noticeRepository.updateNotice(updateNoticeEntity);

        // when
        when(mockNoticeRepository.findOneNotice(updateNotice.getIdx())).thenReturn(updateNotice);
        NoticeDTO noticeInfo = mockNoticeRepository.findOneNotice(updateNotice.getIdx());

        // then
        assertThat(noticeInfo.getIdx()).isEqualTo(updateNotice.getIdx());
        assertThat(noticeInfo.getTitle()).isEqualTo(updateNotice.getTitle());
        assertThat(noticeInfo.getDescription()).isEqualTo(updateNotice.getDescription());

        // verify
        verify(mockNoticeRepository, times(1)).findOneNotice(noticeInfo.getIdx());
        verify(mockNoticeRepository, atLeastOnce()).findOneNotice(noticeInfo.getIdx());
        verifyNoMoreInteractions(mockNoticeRepository);

        InOrder inOrder = inOrder(mockNoticeRepository);
        inOrder.verify(mockNoticeRepository).findOneNotice(noticeInfo.getIdx());
    }

    @Test
    @DisplayName("공지사항 삭제 Mockito 테스트")
    void 공지사항삭제Mockito테스트() {
        // given
        em.persist(noticeEntity);
        noticeDTO = NoticeMapper.INSTANCE.toDto(noticeEntity);

        // when
        when(mockNoticeRepository.findOneNotice(noticeDTO.getIdx())).thenReturn(noticeDTO);
        Long deleteIdx = noticeRepository.deleteNotice(noticeDTO.getIdx());

        // then
        assertThat(mockNoticeRepository.findOneNotice(noticeDTO.getIdx()).getIdx()).isEqualTo(deleteIdx);

        // verify
        verify(mockNoticeRepository, times(1)).findOneNotice(noticeDTO.getIdx());
        verify(mockNoticeRepository, atLeastOnce()).findOneNotice(noticeDTO.getIdx());
        verifyNoMoreInteractions(mockNoticeRepository);

        InOrder inOrder = inOrder(mockNoticeRepository);
        inOrder.verify(mockNoticeRepository).findOneNotice(noticeDTO.getIdx());
    }
}
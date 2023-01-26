package com.travel.api.notice.domain.repository;

import com.travel.api.notice.domain.NoticeDto;

import com.travel.api.notice.domain.NoticeEntity;
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
@DisplayName("공지사항 Repository Test")
class NoticeQueryRepositoryTest {

    @Mock
    private NoticeQueryRepository mockNoticeQueryRepository;

    private NoticeEntity noticeEntity;
    private NoticeDto noticeDTO;

    private final EntityManager em;

    void createNotice() {
        noticeEntity = NoticeEntity.builder()
                .title("공지사항 등록 테스트")
                .description("공지사항 등록 테스트")
                .visible("Y").viewCount(1)
                .build();

        em.persist(noticeEntity);

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
        when(mockNoticeQueryRepository.findNoticeList(noticeMap, pageRequest)).thenReturn(resultPage);
        Page<NoticeDto> newNoticeList = mockNoticeQueryRepository.findNoticeList(noticeMap, pageRequest);

        List<NoticeDto> findNoticeList = newNoticeList.stream().collect(Collectors.toList());

        // then
        assertThat(findNoticeList.get(0).getIdx()).isEqualTo(noticeList.get(0).getIdx());
        assertThat(findNoticeList.get(0).getTitle()).isEqualTo(noticeList.get(0).getTitle());
        assertThat(findNoticeList.get(0).getDescription()).isEqualTo(noticeList.get(0).getDescription());

        // verify
        verify(mockNoticeQueryRepository, times(1)).findNoticeList(noticeMap, pageRequest);
        verify(mockNoticeQueryRepository, atLeastOnce()).findNoticeList(noticeMap, pageRequest);
        verifyNoMoreInteractions(mockNoticeQueryRepository);

        InOrder inOrder = inOrder(mockNoticeQueryRepository);
        inOrder.verify(mockNoticeQueryRepository).findNoticeList(noticeMap, pageRequest);
    }
}

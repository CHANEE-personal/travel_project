package com.travel.api.notice;

import com.travel.api.AdminCommonServiceTest;
import com.travel.api.notice.domain.NoticeDto;
import com.travel.api.notice.domain.NoticeEntity;
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

import javax.persistence.EntityManager;
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
class NoticeServiceTest extends AdminCommonServiceTest {
    @Mock private NoticeRepository noticeRepository;
    @Mock private NoticeQueryRepository noticeQueryRepository;
    @InjectMocks private NoticeService mockNoticeService;
    private final NoticeService noticeService;
    private final EntityManager em;

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
        when(noticeQueryRepository.findNoticeList(noticeMap, pageRequest)).thenReturn(resultPage);
        Page<NoticeDto> newNoticeList = mockNoticeService.findNoticeList(noticeMap, pageRequest);

        List<NoticeDto> findNoticeList = newNoticeList.stream().collect(Collectors.toList());

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
        // when
        when(noticeRepository.findById(noticeEntity.getIdx())).thenReturn(Optional.ofNullable(noticeEntity));
        NoticeDto noticeInfo = mockNoticeService.findOneNotice(noticeEntity.getIdx());

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

    @Test
    @DisplayName("공지사항 등록 Mockito 테스트")
    void 공지사항등록Mockito테스트() {
        // when
        when(noticeRepository.save(noticeEntity)).thenReturn(noticeEntity);
        NoticeDto newNoticeDTO = mockNoticeService.insertNotice(noticeEntity);

        // then
        assertThat(newNoticeDTO.getIdx()).isEqualTo(noticeEntity.getIdx());
        assertThat(newNoticeDTO.getTitle()).isEqualTo(noticeEntity.getTitle());
        assertThat(newNoticeDTO.getDescription()).isEqualTo(noticeEntity.getDescription());

        // verify
        verify(noticeRepository, times(1)).save(noticeEntity);
        verify(noticeRepository, atLeastOnce()).save(noticeEntity);
        verifyNoMoreInteractions(noticeRepository);

        InOrder inOrder = inOrder(noticeRepository);
        inOrder.verify(noticeRepository).save(noticeEntity);
    }

    @Test
    @DisplayName("공지사항 수정 Mockito 테스트")
    void 공지사항수정Mockito테스트() {
        // given
        NoticeEntity updateNoticeEntity = NoticeEntity.builder()
                .idx(noticeEntity.getIdx())
                .title("공지사항 수정 테스트")
                .description("공지사항 수정 테스트")
                .viewCount(0)
                .visible("Y")
                .build();

        // when
        when(noticeRepository.findById(updateNoticeEntity.getIdx())).thenReturn(Optional.of(updateNoticeEntity));
        when(noticeRepository.save(updateNoticeEntity)).thenReturn(updateNoticeEntity);
        NoticeDto noticeInfo = mockNoticeService.updateNotice(updateNoticeEntity.getIdx(), updateNoticeEntity);

        // then
        assertThat(noticeInfo.getIdx()).isEqualTo(updateNoticeEntity.getIdx());
        assertThat(noticeInfo.getTitle()).isEqualTo(updateNoticeEntity.getTitle());
        assertThat(noticeInfo.getDescription()).isEqualTo(updateNoticeEntity.getDescription());

        // verify
        verify(noticeRepository, times(1)).findById(updateNoticeEntity.getIdx());
        verify(noticeRepository, atLeastOnce()).findById(updateNoticeEntity.getIdx());
        verifyNoMoreInteractions(noticeRepository);

        InOrder inOrder = inOrder(noticeRepository);
        inOrder.verify(noticeRepository).findById(updateNoticeEntity.getIdx());
    }

    @Test
    @DisplayName("공지사항 삭제 테스트")
    void 공지사항삭제테스트() {
        Long deleteIdx = noticeService.deleteNotice(noticeEntity.getIdx());

        // then
        assertThat(noticeEntity.getIdx()).isEqualTo(deleteIdx);
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

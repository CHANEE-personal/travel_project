package com.travel.api.notice;

import com.travel.api.notice.domain.NoticeDTO;
import com.travel.api.notice.domain.NoticeEntity;
import com.travel.api.notice.domain.repository.NoticeQueryRepository;
import com.travel.api.notice.domain.repository.NoticeRepository;
import com.travel.exception.TravelException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.travel.exception.ApiExceptionType.NOT_FOUND_NOTICE;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeQueryRepository noticeQueryRepository;
    private final NoticeRepository noticeRepository;

    private NoticeEntity oneNotice(Long idx) {
        return noticeRepository.findById(idx)
                .orElseThrow(() -> new TravelException(NOT_FOUND_NOTICE));
    }

    /**
     * <pre>
     * 1. MethodName : findNoticeList
     * 2. ClassName  : NoticeService.java
     * 3. Comment    : 공지사항 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 28.
     * </pre>
     */
    @Transactional(readOnly = true)
    public Page<NoticeDTO> findNoticeList(Map<String, Object> noticeMap, PageRequest pageRequest) {
        return noticeQueryRepository.findNoticeList(noticeMap, pageRequest);
    }

    /**
     * <pre>
     * 1. MethodName : findOneNotice
     * 2. ClassName  : NoticeService.java
     * 3. Comment    : 공지사항 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 28.
     * </pre>
     */
    @Transactional
    public NoticeDTO findOneNotice(Long idx) {
        oneNotice(idx).updateViewCount();
        return NoticeEntity.toDto(oneNotice(idx));
    }
}

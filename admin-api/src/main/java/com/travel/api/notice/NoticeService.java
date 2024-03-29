package com.travel.api.notice;

import com.travel.api.notice.domain.NoticeDto;
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
import java.util.Optional;

import static com.travel.exception.ApiExceptionType.*;

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
    public Page<NoticeDto> findNoticeList(Map<String, Object> noticeMap, PageRequest pageRequest) {
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
    public NoticeDto findOneNotice(Long idx) {
        return NoticeEntity.toDto(oneNotice(idx));
    }

    /**
     * <pre>
     * 1. MethodName : insertNotice
     * 2. ClassName  : NoticeService.java
     * 3. Comment    : 공지사항 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 28.
     * </pre>
     */
    @Transactional
    public NoticeDto insertNotice(NoticeEntity noticeEntity) {
        try {
            return NoticeEntity.toDto(noticeRepository.save(noticeEntity));
        } catch (Exception e) {
            throw new TravelException(ERROR_NOTICE);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updateNotice
     * 2. ClassName  : NoticeService.java
     * 3. Comment    : 공지사항 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 28.
     * </pre>
     */
    @Transactional
    public NoticeDto updateNotice(Long idx, NoticeEntity noticeEntity) {
        try {
            oneNotice(idx).update(noticeEntity);
            return NoticeEntity.toDto(noticeEntity);
        } catch (Exception e) {
            throw new TravelException(ERROR_UPDATE_NOTICE);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteNotice
     * 2. ClassName  : NoticeService.java
     * 3. Comment    : 공지사항 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 28.
     * </pre>
     */
    @Transactional
    public Long deleteNotice(Long idx) {
        try {
            noticeRepository.deleteById(idx);
            return idx;
        } catch (Exception e) {
            throw new TravelException(ERROR_DELETE_NOTICE);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updateTopFixed
     * 2. ClassName  : NoticeService.java
     * 3. Comment    : 공지사항 고정글
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 28.
     * </pre>
     */
    @Transactional
    public Boolean toggleTopFixed(Long idx) {
        try {
            NoticeEntity oneNotice = oneNotice(idx);
            Optional.ofNullable(oneNotice)
                    .ifPresent(noticeEntity -> noticeEntity.toggleTopFixed(oneNotice.getTopFixed()));

            assert oneNotice != null;
            return oneNotice.getTopFixed();
        } catch (Exception e) {
            throw new TravelException(ERROR_UPDATE_NOTICE);
        }
    }
}

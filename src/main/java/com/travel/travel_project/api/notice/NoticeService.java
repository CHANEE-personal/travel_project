package com.travel.travel_project.api.notice;

import com.travel.travel_project.domain.notice.NoticeDTO;
import com.travel.travel_project.domain.notice.NoticeEntity;
import com.travel.travel_project.exception.TravelException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.travel.travel_project.exception.ApiExceptionType.*;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;

    /**
     * <pre>
     * 1. MethodName : findNoticeCount
     * 2. ClassName  : NoticeService.java
     * 3. Comment    : 공지사항 리스트 갯수 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 11. 28.
     * </pre>
     */
    @Transactional
    public int findNoticeCount(Map<String, Object> noticeMap) {
        try {
            return noticeRepository.findNoticeCount(noticeMap);
        } catch (Exception e) {
            throw new TravelException(NOT_FOUND_NOTICE_LIST, e);
        }
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
    @Cacheable(value = "notice", key = "#noticeMap")
    @Transactional(readOnly = true)
    public List<NoticeDTO> findNoticeList(Map<String, Object> noticeMap) {
        return noticeRepository.findNoticeList(noticeMap);
    }

    /**
     * <pre>
     * 1. MethodName : findOneNotice
     * 2. ClassName  : NoticeService.java
     * 3. Comment    : 공지사항 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 11. 28.
     * </pre>
     */
    @Cacheable(value = "notice", key = "#idx")
    @Transactional(readOnly = true)
    public NoticeDTO findOneNotice(Long idx) {
        return noticeRepository.findOneNotice(idx);
    }

    /**
     * <pre>
     * 1. MethodName : insertNotice
     * 2. ClassName  : NoticeService.java
     * 3. Comment    : 공지사항 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 11. 28.
     * </pre>
     */
    @CachePut("notice")
    @Transactional
    public NoticeDTO insertNotice(NoticeEntity noticeEntity) {
        try {
            return noticeRepository.insertNotice(noticeEntity);
        } catch (Exception e) {
            throw new TravelException(ERROR_NOTICE, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updateNotice
     * 2. ClassName  : NoticeService.java
     * 3. Comment    : 공지사항 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 11. 28.
     * </pre>
     */
    @CachePut(value = "notice", key = "#noticeEntity.idx")
    @Transactional
    public NoticeDTO updateNotice(NoticeEntity noticeEntity) {
        try {
            return noticeRepository.updateNotice(noticeEntity);
        } catch (Exception e) {
            throw new TravelException(ERROR_UPDATE_NOTICE, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteNotice
     * 2. ClassName  : NoticeService.java
     * 3. Comment    : 공지사항 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 11. 28.
     * </pre>
     */
    @CacheEvict(value = "notice", key = "#idx")
    @Transactional
    public Long deleteNotice(Long idx) {
        try {
            return noticeRepository.deleteNotice(idx);
        } catch (Exception e) {
            throw new TravelException(ERROR_DELETE_NOTICE, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updateTopFixed
     * 2. ClassName  : NoticeService.java
     * 3. Comment    : 공지사항 고정글
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 11. 28.
     * </pre>
     */
    @CachePut(value = "notice", key = "#idx")
    @Transactional
    public Boolean toggleTopFixed(Long idx) {
        try {
            return noticeRepository.toggleTopFixed(idx);
        } catch (Exception e) {
            throw new TravelException(NOT_FOUND_NOTICE, e);
        }
    }
}

package com.travel.travel_project.api.notice;

import com.travel.travel_project.api.notice.mapper.NoticeMapper;
import com.travel.travel_project.domain.notice.NoticeDTO;
import com.travel.travel_project.domain.notice.NoticeEntity;
import com.travel.travel_project.exception.TravelException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.jpa.repository.Modifying;
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
    public Integer findNoticeCount(Map<String, Object> noticeMap) {
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
    @Transactional(readOnly = true)
    public List<NoticeDTO> findNoticeList(Map<String, Object> noticeMap) {
        try {
            List<NoticeEntity> noticeList = noticeRepository.findNoticeList(noticeMap);
            return NoticeMapper.INSTANCE.toDtoList(noticeList);
        } catch (Exception e) {
            throw new TravelException(NOT_FOUND_NOTICE_LIST, e);
        }
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
    @Transactional(readOnly = true)
    public NoticeDTO findOneNotice(Long idx) {
        try {
            NoticeEntity noticeEntity = noticeRepository.findOneNotice(idx);
            noticeEntity.updateViewCount();
            return NoticeMapper.INSTANCE.toDto(noticeEntity);
        } catch (Exception e) {
            throw new TravelException(NOT_FOUND_NOTICE, e);
        }
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
    @Modifying(clearAutomatically = true)
    @Transactional
    public NoticeDTO insertNotice(NoticeEntity noticeEntity) {
        try {
            NoticeEntity noticeInfo = noticeRepository.insertNotice(noticeEntity);
            return NoticeMapper.INSTANCE.toDto(noticeInfo);
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
    @CachePut("notice")
    @Modifying(clearAutomatically = true)
    @Transactional
    public NoticeDTO updateNotice(NoticeEntity noticeEntity) {
        try {
            NoticeEntity noticeInfo = noticeRepository.updateNotice(noticeEntity);
            return NoticeMapper.INSTANCE.toDto(noticeInfo);
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
    @CacheEvict("notice")
    @Modifying(clearAutomatically = true)
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
    @Modifying(clearAutomatically = true)
    @Transactional
    public NoticeDTO toggleTopFixed(Long idx) {
        try {
            NoticeEntity noticeEntity = noticeRepository.findOneNotice(idx);
            noticeEntity.toggleTopFixed(noticeEntity.getTopFixed());
            return NoticeMapper.INSTANCE.toDto(noticeEntity);
        } catch (Exception e) {
            throw new TravelException(NOT_FOUND_NOTICE, e);
        }
    }
}

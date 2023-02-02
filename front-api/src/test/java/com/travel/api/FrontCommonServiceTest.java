package com.travel.api;

import com.travel.api.common.domain.CommonDTO;
import com.travel.api.common.domain.CommonEntity;
import com.travel.api.common.domain.repository.CommonRepository;
import com.travel.api.faq.domain.FaqDTO;
import com.travel.api.faq.domain.FaqEntity;
import com.travel.api.faq.domain.repository.FaqRepository;
import com.travel.api.notice.domain.NoticeDTO;
import com.travel.api.notice.domain.NoticeEntity;
import com.travel.api.notice.domain.repository.NoticeRepository;
import com.travel.api.travel.TravelRepository;
import com.travel.api.travel.domain.TravelDTO;
import com.travel.api.travel.domain.TravelEntity;
import com.travel.api.user.domain.UserDTO;
import com.travel.api.user.domain.UserEntity;
import com.travel.api.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;

import static com.travel.api.user.domain.Role.ROLE_TRAVEL_USER;

public abstract class FrontCommonServiceTest {

    @Autowired private TravelRepository travelRepository;
    @Autowired private CommonRepository commonRepository;
    @Autowired private FaqRepository faqRepository;
    @Autowired private NoticeRepository noticeRepository;
    @Autowired private UserRepository userRepository;

    protected TravelEntity travelEntity;
    protected TravelDTO travelDTO;
    protected CommonEntity commonEntity;
    protected CommonDTO commonDTO;
    protected FaqEntity faqEntity;
    protected FaqDTO faqDTO;
    protected NoticeEntity noticeEntity;
    protected NoticeDTO noticeDTO;
    protected UserEntity userEntity;
    protected UserDTO userDTO;

    void createData() {
        // 공통 코드 등록
        commonEntity = commonRepository.save(
                CommonEntity.builder()
                        .commonCode(100)
                        .commonName("서울")
                        .visible("Y")
                        .build());

        commonDTO = CommonEntity.toDto(commonEntity);

        // 여행지 등록
        travelEntity = travelRepository.save(
                TravelEntity.builder()
                        .newTravelCode(commonEntity)
                        .travelTitle("여행지 소개")
                        .travelDescription("여행지 소개")
                        .travelAddress("인천광역시 서구")
                        .travelZipCode("123-456")
                        .favoriteCount(1)
                        .viewCount(0)
                        .popular(false)
                        .visible("Y")
                        .build());

        travelDTO = TravelEntity.toDto(travelEntity);

        // FAQ 등록
        faqEntity = faqRepository.save(FaqEntity.builder()
                .title("FAQ 등록 테스트")
                .description("FAQ 등록 테스트")
                .viewCount(1)
                .visible("Y")
                .newFaqCode(commonEntity)
                .build());

        faqDTO = FaqEntity.toDto(faqEntity);

        // 공지사항 등록
        noticeEntity = noticeRepository.save(
                NoticeEntity.builder()
                        .title("공지사항 등록 테스트")
                        .description("공지사항 등록 테스트")
                        .topFixed(false)
                        .visible("Y")
                        .build());

        noticeDTO = NoticeEntity.toDto(noticeEntity);

        // 유저 등록
        userEntity = userRepository.save(
                UserEntity.builder()
                        .userId("test111")
                        .password("test111")
                        .email("test@naver.com")
                        .name("test")
                        .role(ROLE_TRAVEL_USER)
                        .visible("Y")
                        .build());

        userDTO = UserEntity.toDto(userEntity);
    }

    @BeforeEach
    @EventListener
    public void init() {
        createData();
    }
}

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
import com.travel.api.travel.domain.group.TravelGroupDTO;
import com.travel.api.travel.domain.group.TravelGroupEntity;
import com.travel.api.travel.domain.group.repository.GroupRepository;
import com.travel.api.travel.domain.reservation.TravelReservationDTO;
import com.travel.api.travel.domain.reservation.TravelReservationEntity;
import com.travel.api.travel.domain.reservation.repository.TravelReservationRepository;
import com.travel.api.user.domain.UserDTO;
import com.travel.api.user.domain.UserEntity;
import com.travel.api.user.domain.repository.UserRepository;
import com.travel.api.user.domain.reservation.UserReservationDTO;
import com.travel.api.user.domain.reservation.UserReservationEntity;
import com.travel.api.user.domain.reservation.reservation.UserReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;

import java.time.LocalDateTime;

import static com.travel.api.user.domain.Role.ROLE_TRAVEL_USER;

public abstract class FrontCommonServiceTest {

    @Autowired private TravelRepository travelRepository;
    @Autowired private CommonRepository commonRepository;
    @Autowired private FaqRepository faqRepository;
    @Autowired private NoticeRepository noticeRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private TravelReservationRepository travelReservationRepository;
    @Autowired private UserReservationRepository userReservationRepository;
    @Autowired private GroupRepository groupRepository;

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
    protected TravelReservationEntity travelReservationEntity;
    protected TravelReservationDTO travelReservationDTO;
    protected UserReservationEntity userReservationEntity;
    protected UserReservationDTO userReservationDTO;
    protected TravelGroupEntity travelGroupEntity;
    protected TravelGroupDTO travelGroupDTO;

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

        // 여행 예약 등록
        travelReservationEntity = travelReservationRepository.save(
                TravelReservationEntity.builder()
                        .commonEntity(commonEntity)
                        .title("예약 등록지")
                        .description("예약 등록지")
                        .address("서울 강남구")
                        .zipCode("123-456")
                        .price(50000)
                        .possibleCount(10)
                        .startDate(LocalDateTime.now())
                        .endDate(LocalDateTime.now())
                        .status(true)
                        .popular(false)
                        .build());

        travelReservationDTO = TravelReservationEntity.toDto(travelReservationEntity);

        // 유저 여행 예약 등록
        userReservationEntity = userReservationRepository.save(
                UserReservationEntity.builder()
                        .newUserEntity(userEntity)
                        .travelReservationEntity(travelReservationEntity)
                        .price(travelReservationEntity.getPrice())
                        .startDate(LocalDateTime.of(2022, 2, 1, 0, 0, 0))
                        .endDate(LocalDateTime.of(2022, 2, 3, 23, 59, 59))
                        .userCount(2)
                        .build());

        userReservationDTO = UserReservationEntity.toDto(userReservationEntity);

        // 여행 그룹 등록
        travelGroupEntity = groupRepository.save(
                TravelGroupEntity.builder()
                        .travelEntity(travelEntity)
                        .groupName("서울 그룹")
                        .groupDescription("서울 그룹")
                        .visible("Y")
                        .build());

        travelGroupDTO = TravelGroupEntity.toDto(travelGroupEntity);
    }

    @BeforeEach
    @EventListener
    public void init() {
        createData();
    }
}

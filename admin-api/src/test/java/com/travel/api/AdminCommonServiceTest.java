package com.travel.api;

import com.travel.api.common.domain.CommonDto;
import com.travel.api.common.domain.CommonEntity;
import com.travel.api.common.domain.EntityType;
import com.travel.api.common.domain.repository.CommonRepository;
import com.travel.api.coupon.domain.CouponDto;
import com.travel.api.coupon.domain.CouponEntity;
import com.travel.api.coupon.domain.repository.CouponRepository;
import com.travel.api.faq.domain.FaqDto;
import com.travel.api.faq.domain.FaqEntity;
import com.travel.api.faq.domain.repository.FaqRepository;
import com.travel.api.notice.domain.NoticeDto;
import com.travel.api.notice.domain.NoticeEntity;
import com.travel.api.notice.domain.repository.NoticeRepository;
import com.travel.api.post.domain.PostDto;
import com.travel.api.post.domain.PostEntity;
import com.travel.api.post.domain.repository.PostRepository;
import com.travel.api.travel.TravelImageRepository;
import com.travel.api.travel.TravelRepository;
import com.travel.api.travel.domain.TravelDto;
import com.travel.api.travel.domain.TravelEntity;
import com.travel.api.travel.domain.image.TravelImageDto;
import com.travel.api.travel.domain.image.TravelImageEntity;
import com.travel.api.travel.domain.reservation.TravelReservationDto;
import com.travel.api.travel.domain.reservation.TravelReservationEntity;
import com.travel.api.travel.domain.reservation.repository.ReservationRepository;
import com.travel.api.user.domain.UserDto;
import com.travel.api.user.domain.UserEntity;
import com.travel.api.user.domain.repository.UserRepository;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;

import java.time.LocalDateTime;

import static com.travel.api.user.domain.Role.ROLE_ADMIN;

@NoArgsConstructor
public abstract class AdminCommonServiceTest {

    @Autowired private CommonRepository commonRepository;
    @Autowired private TravelRepository travelRepository;
    @Autowired private TravelImageRepository travelImageRepository;
    @Autowired private FaqRepository faqRepository;
    @Autowired private NoticeRepository noticeRepository;
    @Autowired private PostRepository postRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ReservationRepository reservationRepository;
    @Autowired private CouponRepository couponRepository;

    protected CommonEntity commonEntity;
    protected CommonDto commonDTO;
    protected TravelEntity travelEntity;
    protected TravelDto travelDTO;
    protected TravelImageEntity travelImageEntity;
    protected FaqEntity faqEntity;
    protected FaqDto faqDTO;
    protected NoticeEntity noticeEntity;
    protected NoticeDto noticeDTO;
    protected PostEntity postEntity;
    protected PostDto postDTO;
    protected UserEntity userEntity;
    protected UserDto userDTO;
    protected TravelReservationEntity travelReservationEntity;
    protected TravelReservationDto travelReservationDTO;
    protected CouponEntity couponEntity;
    protected CouponDto couponDTO;

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

        // 여행지 이미지 등록
        travelImageEntity = TravelImageEntity.builder()
                .typeIdx(travelEntity.getIdx())
                .imageType("main")
                .fileName("test.jpg")
                .fileMask("test.jpg")
                .filePath("/test/test.jpg")
                .entityType(EntityType.TRAVEL)
                .visible("Y")
                .build();

        travelImageRepository.save(travelImageEntity);

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

        // 게시글 등록
        postEntity = postRepository.save(
                PostEntity.builder()
                        .postTitle("게시글 테스트")
                        .postDescription("게시글 테스트")
                        .popular(false)
                        .viewCount(0)
                        .favoriteCount(0)
                        .visible("Y")
                        .build());

        postDTO = PostEntity.toDto(postEntity);

        // 유저 등록
        userEntity = userRepository.save(
                UserEntity.builder()
                        .userId("test111")
                        .password("test111")
                        .email("test@naver.com")
                        .name("test")
                        .role(ROLE_ADMIN)
                        .visible("Y")
                        .build());

        userDTO = UserEntity.toDto(userEntity);

        // 여행 예약 등록
        travelReservationEntity = reservationRepository.save(
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

        // 예약 여행지 이미지 등록
        travelImageEntity = TravelImageEntity.builder()
                .typeIdx(travelReservationDTO.getIdx())
                .imageType("main")
                .fileName("test.jpg")
                .fileMask("test.jpg")
                .filePath("/test/test.jpg")
                .entityType(EntityType.RESERVATION)
                .build();
        travelImageRepository.save(travelImageEntity);

        // 쿠폰 등록
        couponEntity = couponRepository.save(
                CouponEntity.builder()
                        .title("10% 쿠폰")
                        .description("10% 쿠폰")
                        .salePrice(0)
                        .percentage(10)
                        .percentageStatus(true)
                        .count(1)
                        .startDate(LocalDateTime.of(2022, 2, 1, 0, 0, 0))
                        .endDate(LocalDateTime.of(2022, 2, 28, 23, 59, 59))
                        .status(true)
                        .build());

        couponDTO = CouponEntity.toDto(couponEntity);
    }

    @BeforeEach
    @EventListener
    public void init() {
        createData();
    }
}

package com.travel.api;

import com.travel.api.common.domain.CommonDTO;
import com.travel.api.common.domain.CommonEntity;
import com.travel.api.common.domain.EntityType;
import com.travel.api.common.domain.repository.CommonRepository;
import com.travel.api.faq.domain.FaqDTO;
import com.travel.api.faq.domain.FaqEntity;
import com.travel.api.faq.domain.repository.FaqRepository;
import com.travel.api.notice.domain.NoticeDTO;
import com.travel.api.notice.domain.NoticeEntity;
import com.travel.api.notice.domain.repository.NoticeRepository;
import com.travel.api.post.domain.PostDTO;
import com.travel.api.post.domain.PostEntity;
import com.travel.api.post.domain.reply.ReplyDTO;
import com.travel.api.post.domain.reply.ReplyEntity;
import com.travel.api.post.domain.repository.PostRepository;
import com.travel.api.post.domain.repository.ReplyRepository;
import com.travel.api.travel.TravelImageRepository;
import com.travel.api.travel.TravelRepository;
import com.travel.api.travel.domain.TravelDTO;
import com.travel.api.travel.domain.TravelEntity;
import com.travel.api.travel.domain.group.TravelGroupDTO;
import com.travel.api.travel.domain.group.TravelGroupEntity;
import com.travel.api.travel.domain.group.repository.GroupRepository;
import com.travel.api.travel.domain.image.TravelImageEntity;
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
    @Autowired private TravelImageRepository travelImageRepository;
    @Autowired private CommonRepository commonRepository;
    @Autowired private FaqRepository faqRepository;
    @Autowired private NoticeRepository noticeRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private TravelReservationRepository travelReservationRepository;
    @Autowired private UserReservationRepository userReservationRepository;
    @Autowired private GroupRepository groupRepository;
    @Autowired private PostRepository postRepository;
    @Autowired private ReplyRepository replyRepository;

    protected TravelEntity travelEntity;
    protected TravelImageEntity travelImageEntity;
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
    protected PostEntity postEntity;
    protected PostDTO postDTO;
    protected ReplyEntity replyEntity;
    protected ReplyDTO replyDTO;
    protected ReplyEntity replyEntity2;

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
                        .viewCount(1)
                        .popular(false)
                        .visible("Y")
                        .build());

        travelDTO = TravelEntity.toDto(travelEntity);

        // 여행지 이미지 등록
        travelImageEntity = TravelImageEntity.builder()
                .typeIdx(travelDTO.getIdx())
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

        // 게시글 댓글 등록
        replyEntity = replyRepository.save(
                ReplyEntity.builder()
                .commentTitle("댓글 테스트")
                .commentDescription("댓글 테스트")
                .favoriteCount(0)
                .postEntity(postEntity)
                .visible("Y")
                .build());

        replyDTO = ReplyEntity.toDto(replyEntity);

        // 게시글 대댓글 등록
        replyEntity2 = replyRepository.save(
                ReplyEntity.builder()
                        .commentTitle("대댓글 테스트")
                        .commentDescription("대댓글 테스트")
                        .favoriteCount(0)
                        .visible("Y")
                        .postEntity(postEntity)
                        .parent(replyEntity)
                        .build());
    }

    @BeforeEach
    @EventListener
    public void init() {
        createData();
    }
}

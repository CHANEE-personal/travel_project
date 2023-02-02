package com.travel.api;

import com.travel.api.common.domain.CommonDto;
import com.travel.api.common.domain.CommonEntity;
import com.travel.api.common.domain.repository.CommonRepository;
import com.travel.api.faq.domain.FaqDto;
import com.travel.api.faq.domain.FaqEntity;
import com.travel.api.faq.domain.repository.FaqRepository;
import com.travel.api.notice.domain.NoticeDto;
import com.travel.api.notice.domain.NoticeEntity;
import com.travel.api.notice.domain.repository.NoticeRepository;
import com.travel.api.post.domain.PostDto;
import com.travel.api.post.domain.PostEntity;
import com.travel.api.post.domain.repository.PostRepository;
import com.travel.api.travel.TravelRepository;
import com.travel.api.travel.domain.TravelDto;
import com.travel.api.travel.domain.TravelEntity;
import com.travel.api.travel.domain.image.TravelImageDto;
import com.travel.api.travel.domain.image.TravelImageEntity;
import com.travel.api.user.domain.UserDto;
import com.travel.api.user.domain.UserEntity;
import com.travel.api.user.domain.repository.UserRepository;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;

import static com.travel.api.user.domain.Role.ROLE_ADMIN;

@NoArgsConstructor
public abstract class AdminCommonServiceTest {

    @Autowired private CommonRepository commonRepository;
    @Autowired private TravelRepository travelRepository;
    @Autowired private FaqRepository faqRepository;
    @Autowired private NoticeRepository noticeRepository;
    @Autowired private PostRepository postRepository;
    @Autowired private UserRepository userRepository;

    protected CommonEntity commonEntity;
    protected CommonDto commonDTO;
    protected TravelEntity travelEntity;
    protected TravelDto travelDTO;
    protected FaqEntity faqEntity;
    protected FaqDto faqDTO;
    protected NoticeEntity noticeEntity;
    protected NoticeDto noticeDTO;
    protected PostEntity postEntity;
    protected PostDto postDTO;
    protected UserEntity userEntity;
    protected UserDto userDTO;

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
    }

    @BeforeEach
    @EventListener
    public void init() {
        createData();
    }
}

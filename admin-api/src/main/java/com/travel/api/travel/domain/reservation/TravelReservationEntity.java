package com.travel.api.travel.domain.reservation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.travel.api.common.domain.CommonEntity;
import com.travel.api.common.domain.NewCommonMappedClass;
import com.travel.api.travel.domain.image.TravelImageEntity;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicUpdate
@Table(name = "travel_reservation")
public class TravelReservationEntity extends NewCommonMappedClass {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "common_code", referencedColumnName = "common_code", nullable = false)
    private CommonEntity commonEntity;

    @Column(name = "title")
    @NotEmpty(message = "제목 입력은 필수입니다.")
    private String title;

    @Column(name = "description")
    @Lob
    @NotEmpty(message = "내용 입력은 필수입니다.")
    private String description;

    @Column(name = "address")
    @NotEmpty(message = "주소 입력은 필수입니다.")
    private String address;

    @Column(name = "zip_code")
    @NotEmpty(message = "우편번호 입력은 필수입니다.")
    private String zipCode;

    @Column(name = "price")
    @NotNull(message = "예약비 입력은 필수입니다.")
    private int price;

    @Column(name = "possible_count")
    @NotNull(message = "예약 가능 인원 입력은 필수입니다.")
    private int possibleCount;

    @Column(name = "start_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "popular")
    private Boolean popular;

    @Builder.Default
    @BatchSize(size = 5)
    @Where(clause = "type_name = 'reservation'")
    @OneToMany(mappedBy = "travelReservationEntity", fetch = FetchType.LAZY)
    private List<TravelImageEntity> travelImageEntityList = new ArrayList<>();

    public void update(TravelReservationEntity travelReservationEntity) {
        this.title = travelReservationEntity.title;
        this.description = travelReservationEntity.description;
        this.address = travelReservationEntity.address;
        this.zipCode = travelReservationEntity.zipCode;
        this.price = travelReservationEntity.price;
        this.possibleCount = travelReservationEntity.possibleCount;
        this.startDate = travelReservationEntity.startDate;
        this.endDate = travelReservationEntity.endDate;
        this.status = travelReservationEntity.status;
    }

    public static TravelReservationDto toDto(TravelReservationEntity entity) {
        if (entity == null) return null;
        return TravelReservationDto.builder()
                .idx(entity.getIdx())
                .commonDto(CommonEntity.toDto(entity.commonEntity))
                .title(entity.getTitle())
                .description(entity.getDescription())
                .address(entity.getAddress())
                .zipCode(entity.getZipCode())
                .price(entity.getPrice())
                .possibleCount(entity.getPossibleCount())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .status(entity.getStatus())
                .build();
    }
}

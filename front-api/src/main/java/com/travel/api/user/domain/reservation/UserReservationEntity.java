package com.travel.api.user.domain.reservation;

import com.travel.api.common.domain.NewCommonMappedClass;
import com.travel.api.travel.domain.reservation.TravelReservationEntity;
import com.travel.api.user.domain.UserEntity;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "travel_user_reservation")
public class UserReservationEntity extends NewCommonMappedClass {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @Column(name = "price")
    @NotNull(message = "가격은 필수입니다.")
    private int price;

    @Column(name = "sale_price")
    private int salePrice;

    @Column(name = "start_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;

    @Column(name = "user_count")
    @NotNull(message = "인원은 필수입니다.")
    private int userCount;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_idx", referencedColumnName = "idx", nullable = false)
    private UserEntity newUserEntity;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "reservation_idx", referencedColumnName = "idx", nullable = false)
    private TravelReservationEntity travelReservationEntity;

    public static UserReservationDTO toDto(UserReservationEntity entity) {
        if (entity == null) return null;
        return UserReservationDTO.builder()
                .idx(entity.idx)
                .price(entity.price)
                .salePrice(entity.salePrice)
                .startDate(entity.startDate)
                .endDate(entity.endDate)
                .userCount(entity.userCount)
                .userId(entity.newUserEntity.getUserId())
                .userName(entity.newUserEntity.getName())
                .title(entity.travelReservationEntity.getTitle())
                .description(entity.travelReservationEntity.getDescription())
                .address(entity.travelReservationEntity.getAddress())
                .zipCode(entity.travelReservationEntity.getZipCode())
                .possibleCount(entity.travelReservationEntity.getPossibleCount())
                .status(entity.travelReservationEntity.getStatus())
                .build();
    }
}

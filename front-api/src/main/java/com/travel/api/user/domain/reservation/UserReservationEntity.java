package com.travel.api.user.domain.reservation;

import com.travel.api.travel.domain.reservation.TravelReservationEntity;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "travel_user_reservation")
public class UserReservationEntity {

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

    @Builder.Default
    @BatchSize(size = 20)
    @OneToMany(mappedBy = "userReservation", fetch = LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<TravelReservationEntity> travelReservationList = new ArrayList<>();

    public void addReservation(TravelReservationEntity travelReservationEntity) {
        travelReservationEntity.setUserReservation(this);
        this.travelReservationList.add(travelReservationEntity);
    }

    public static UserReservationDTO toDto(UserReservationEntity entity) {
        if (entity == null) return null;
        return UserReservationDTO.builder()
                .idx(entity.getIdx())
                .price(entity.getPrice())
                .salePrice(entity.getSalePrice())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .userCount(entity.getUserCount())
                .travelReservationList(entity.getTravelReservationList())
                .build();
    }
}

package com.travel.travel_project.domain.travel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TravelEntityTest {

    private TravelEntity travelEntity;

    @BeforeEach
    void setUp() {
        travelEntity = TravelEntity.builder()
                .travelCode(1)
                .travelTitle("여행지 테스트").travelDescription("여행지 테스트").favoriteCount(1).viewCount(0)
                .travelAddress("인천광역시 서구").travelZipCode("123-456").visible("Y").popular(false)
                .build();
    }

    @Test
    @DisplayName("조회수 증가 테스트")
    void updateViewCount() {
        // when
        int beforeView = travelEntity.getViewCount();
        travelEntity.updateViewCount();

        // then
        assertThat(travelEntity.getViewCount()).isEqualTo(beforeView + 1);
    }

    @Test
    @DisplayName("좋아요수 증가 테스트")
    void updateFavoriteCount() {
        // when
        int beforeFavoriteCount = travelEntity.getFavoriteCount();
        travelEntity.updateFavoriteCount();

        // then
        assertThat(travelEntity.getFavoriteCount()).isEqualTo(beforeFavoriteCount + 1);
    }

    @Test
    @DisplayName("인기 여행지 선정 테스트")
    void togglePopular() {
        // when
        Boolean beforePopular = travelEntity.getPopular();
        travelEntity.togglePopular(beforePopular);

        // then
        assertThat(travelEntity.getPopular()).isEqualTo(!beforePopular);
    }
}
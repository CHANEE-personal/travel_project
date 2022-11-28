package com.travel.travel_project.domain.notice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NoticeEntityTest {
    private NoticeEntity noticeEntity;

    @BeforeEach
    void setUp() {
        noticeEntity = NoticeEntity.builder()
                .title("title")
                .description("description")
                .viewCount(0)
                .visible("Y")
                .topFixed(false)
                .build();
    }

    @Test
    @DisplayName("조회수 증가 테스트")
    void updateViewCount() {
        // when
        int beforeView = noticeEntity.getViewCount();
        noticeEntity.updateViewCount();

        // then
        assertThat(noticeEntity.getViewCount()).isEqualTo(beforeView + 1);
    }

    @Test
    @DisplayName("고정글 테스트")
    void updateTopFixed() {
        // when
        Boolean topFixed = noticeEntity.getTopFixed();
        noticeEntity.toggleTopFixed(topFixed);
        // then
        assertThat(noticeEntity.getTopFixed()).isEqualTo(true);
    }
}
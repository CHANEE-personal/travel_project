package com.travel.travel_project.domain.notice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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
    void increaseView() {
        // when
        int beforeView = noticeEntity.getViewCount();
        noticeEntity.updateViewCount();

        // then
        assertThat(noticeEntity.getViewCount()).isEqualTo(beforeView + 1);
    }
}
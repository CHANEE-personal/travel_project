package com.travel.travel_project.domain.faq;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class FaqEntityTest {

    private FaqEntity faqEntity;

    @BeforeEach
    void setUp() {
        faqEntity = FaqEntity.builder()
                .faqCode(1L)
                .title("FAQ")
                .description("FAQ")
                .viewCount(1)
                .visible("Y")
                .build();
    }

    @Test
    @DisplayName("조회수 증가 테스트")
    void updateViewCount() {
        int beforeViewCount = faqEntity.getViewCount();
        faqEntity.updateViewCount();

        assertThat(faqEntity.getViewCount()).isEqualTo(beforeViewCount + 1);
    }

}
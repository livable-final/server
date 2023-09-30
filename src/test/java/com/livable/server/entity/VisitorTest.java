package com.livable.server.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class VisitorTest {

    @DisplayName("Visitor.entrance 성공 테스트_1")
    @Test
    void entranceSuccessTest_1() {
        // given
        Visitor visitor = Visitor.builder()
                .build();

        // when
        visitor.entrance();

        // then
        assertThat(visitor.getFirstVisitedTime()).isNotNull();
    }

    @DisplayName("Visitor.entrance 성공 테스트_1")
    @Test
    void entranceSuccessTest_2() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Visitor visitor = Visitor.builder()
                .firstVisitedTime(now)
                .build();

        // when
        visitor.entrance();

        // then
        assertThat(visitor.getFirstVisitedTime()).isEqualTo(now);
    }
}
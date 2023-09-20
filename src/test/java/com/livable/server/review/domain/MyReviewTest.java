package com.livable.server.review.domain;

import com.livable.server.core.exception.GlobalRuntimeException;
import com.livable.server.entity.Evaluation;
import com.livable.server.review.dto.MyReviewProjection;
import com.livable.server.review.dto.MyReviewResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("나의 리뷰 쿼리 결과 래퍼 클래스 테스트")
class MyReviewTest {


    @DisplayName("정적 팩토리 메서드 테스트 (생성자 및 검증 메서드 간접 테스트)")
    @Nested
    class StaticFactoryMethod {

        @DisplayName("성공")
        @Test
        void success_Test() {
            // Given
            List<MyReviewProjection> myReviewProjections = List.of(
                    MyReviewProjection.builder()
                            .reviewTitle("TestTitle")
                            .reviewDescription("TestDescription")
                            .reviewCreatedAt(LocalDateTime.now())
                            .reviewTaste(Evaluation.GOOD)
                            .reviewImg("TestImage")
                            .location("TestLocation")
                            .build()
            );

            // When
            // Then
            MyReview.from(myReviewProjections);
        }

        @DisplayName("실패 - 비어있는 쿼리 결과로 생성")
        @Test
        void failure_Test_constructedEmptyList() {
            // Given
            List<MyReviewProjection> myReviewProjections = List.of();

            // When
            // Then
            Assertions.assertThrows(GlobalRuntimeException.class, () ->
                    MyReview.from(myReviewProjections));
        }
    }

    @DisplayName("DTO 변환 테스트 (getTopOne, getImages 간접 테스트)")
    @Nested
    class ConvertToDTO {

        @DisplayName("성공 - 싱글 이미지")
        @Test
        void success_Test_SingleImage() {
            // Given
            List<MyReviewProjection> myReviewProjections = List.of(
                    MyReviewProjection.builder()
                            .reviewTitle("TestTitle")
                            .reviewDescription("TestDescription")
                            .reviewCreatedAt(LocalDateTime.now())
                            .reviewTaste(Evaluation.GOOD)
                            .reviewImg("TestImage")
                            .location("TestLocation")
                            .build()
            );

            // When
            MyReview myReview = MyReview.from(myReviewProjections);
            MyReviewResponse.DetailDTO actual = myReview.toResponseDTO();

            // Then
            Assertions.assertAll(
                    () -> Assertions.assertEquals("TestTitle", actual.getReviewTitle()),
                    () -> Assertions.assertEquals(Evaluation.GOOD, actual.getReviewTaste()),
                    () -> Assertions.assertEquals(1, actual.getReviewImg().size())
            );
        }

        @DisplayName("성공 - 여러 이미지")
        @Test
        void success_Test_MultipleImage() {
            // Given
            List<MyReviewProjection> myReviewProjections = List.of(
                    MyReviewProjection.builder()
                            .reviewTitle("TestTitle")
                            .reviewDescription("TestDescription")
                            .reviewCreatedAt(LocalDateTime.now())
                            .reviewTaste(Evaluation.GOOD)
                            .reviewImg("TestImage1")
                            .location("TestLocation")
                            .build(),

                    MyReviewProjection.builder()
                            .reviewTitle("TestTitle")
                            .reviewDescription("TestDescription")
                            .reviewCreatedAt(LocalDateTime.now())
                            .reviewTaste(Evaluation.GOOD)
                            .reviewImg("TestImage2")
                            .location("TestLocation")
                            .build(),

                    MyReviewProjection.builder()
                            .reviewTitle("TestTitle")
                            .reviewDescription("TestDescription")
                            .reviewCreatedAt(LocalDateTime.now())
                            .reviewTaste(Evaluation.GOOD)
                            .reviewImg("TestImage3")
                            .location("TestLocation")
                            .build()
            );

            // When
            MyReview myReview = MyReview.from(myReviewProjections);
            MyReviewResponse.DetailDTO actual = myReview.toResponseDTO();

            // Then
            Assertions.assertAll(
                    () -> Assertions.assertEquals("TestTitle", actual.getReviewTitle()),
                    () -> Assertions.assertEquals(Evaluation.GOOD, actual.getReviewTaste()),
                    () -> Assertions.assertEquals(3, actual.getReviewImg().size())
            );
        }
    }
}
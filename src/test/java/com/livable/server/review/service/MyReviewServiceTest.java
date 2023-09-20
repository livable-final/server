package com.livable.server.review.service;

import com.livable.server.core.exception.GlobalRuntimeException;
import com.livable.server.review.dto.MyReviewProjection;
import com.livable.server.review.dto.MyReviewResponse;
import com.livable.server.review.repository.MyReviewRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MyReviewServiceTest {

    @Mock
    private MyReviewRepository myReviewRepository;

    @InjectMocks
    private MyReviewService myReviewService;

    @Nested
    @DisplayName("나의 레스토랑 리뷰 서비스 단위 테스트")
    class MyRestaurantReview {

        @DisplayName("성공 - DTO 변환 테스트 (싱글 이미지)")
        @Test
        void success_Test_SingleImg() {
            // Given
            Long reviewId = 1L;
            Long memberId = 1L;
            String reviewDescription = "맛있오";
            String imgUrl = "mockImage.jpg";

            List<MyReviewProjection> mockList = List.of(
                    MyReviewProjection.builder().reviewDescription(reviewDescription).reviewImg(imgUrl).build()
            );

            Mockito.when(myReviewRepository.findRestaurantReviewByReviewId(
                    ArgumentMatchers.anyLong(),
                    ArgumentMatchers.anyLong()
            )).thenReturn(mockList);


            // When
            MyReviewResponse.DetailDTO actual = myReviewService.getMyRestaurantReview(reviewId, memberId);

            // Then
            Assertions.assertAll(
                    () -> Assertions.assertEquals(reviewDescription, actual.getReviewDescription()),
                    () -> Assertions.assertEquals(1, actual.getReviewImg().size()),
                    () -> Assertions.assertEquals(imgUrl, actual.getReviewImg().get(0))
            );
        }

        @DisplayName("성공 - DTO 변환 테스트 (여러 이미지)")
        @Test
        void success_Test_MultipleImg() {
            // Given
            Long reviewId = 1L;
            Long memberId = 1L;
            String imgUrl1 = "mockImage1.jpg";
            String imgUrl2 = "mockImage2.jpg";

            List<MyReviewProjection> mockList = List.of(
                    MyReviewProjection.builder().reviewImg(imgUrl1).build(),
                    MyReviewProjection.builder().reviewImg(imgUrl2).build()
            );

            Mockito.when(myReviewRepository.findRestaurantReviewByReviewId(
                    ArgumentMatchers.anyLong(),
                    ArgumentMatchers.anyLong()
            )).thenReturn(mockList);


            // When
            MyReviewResponse.DetailDTO actual = myReviewService.getMyRestaurantReview(reviewId, memberId);

            // Then
            Assertions.assertAll(
                    () -> Assertions.assertEquals(2, actual.getReviewImg().size()),
                    () -> Assertions.assertEquals(imgUrl1, actual.getReviewImg().get(0)),
                    () -> Assertions.assertEquals(imgUrl2, actual.getReviewImg().get(1))
            );
        }

        @DisplayName("실패 - DTO변환 오류")
        @Test
        void failure_Test_FailedConvertToDTO() {
            // Given
            Long reviewId = 1L;
            Long memberId = 1L;

            Mockito.when(myReviewRepository.findRestaurantReviewByReviewId(
                    ArgumentMatchers.anyLong(),
                    ArgumentMatchers.anyLong()
            )).thenReturn(new ArrayList<>());

            // When
            // Then
            Assertions.assertThrows(GlobalRuntimeException.class, () ->
                    myReviewService.getMyRestaurantReview(reviewId, memberId));
        }
    }

    @Nested
    @DisplayName("나의 구내식당 리뷰 서비스 단위 테스트")
    class MyCafeteriaReview {

        @DisplayName("성공 - DTO 변환 테스트 (싱글 이미지)")
        @Test
        void success_Test_SingleImg() {
            // Given
            Long reviewId = 1L;
            Long memberId = 1L;
            String reviewDescription = "맛있오";
            String imgUrl = "mockImage.jpg";

            List<MyReviewProjection> mockList = List.of(
                    MyReviewProjection.builder().reviewDescription(reviewDescription).reviewImg(imgUrl).build()
            );

            Mockito.when(myReviewRepository.findCafeteriaReviewByReviewId(
                    ArgumentMatchers.anyLong(),
                    ArgumentMatchers.anyLong()
            )).thenReturn(mockList);


            // When
            MyReviewResponse.DetailDTO actual = myReviewService.getMyCafeteriaReview(reviewId, memberId);

            // Then
            Assertions.assertAll(
                    () -> Assertions.assertEquals(reviewDescription, actual.getReviewDescription()),
                    () -> Assertions.assertEquals(1, actual.getReviewImg().size()),
                    () -> Assertions.assertEquals(imgUrl, actual.getReviewImg().get(0))
            );
        }

        @DisplayName("성공 - DTO 변환 테스트 (여러 이미지)")
        @Test
        void success_Test_MultipleImg() {
            // Given
            Long reviewId = 1L;
            Long memberId = 1L;
            String imgUrl1 = "mockImage1.jpg";
            String imgUrl2 = "mockImage2.jpg";

            List<MyReviewProjection> mockList = List.of(
                    MyReviewProjection.builder().reviewImg(imgUrl1).build(),
                    MyReviewProjection.builder().reviewImg(imgUrl2).build()
            );

            Mockito.when(myReviewRepository.findCafeteriaReviewByReviewId(
                    ArgumentMatchers.anyLong(),
                    ArgumentMatchers.anyLong()
            )).thenReturn(mockList);


            // When
            MyReviewResponse.DetailDTO actual = myReviewService.getMyCafeteriaReview(reviewId, memberId);

            // Then
            Assertions.assertAll(
                    () -> Assertions.assertEquals(2, actual.getReviewImg().size()),
                    () -> Assertions.assertEquals(imgUrl1, actual.getReviewImg().get(0)),
                    () -> Assertions.assertEquals(imgUrl2, actual.getReviewImg().get(1))
            );
        }

        @DisplayName("실패 - DTO변환 오류")
        @Test
        void failure_Test_FailedConvertToDTO() {
            // Given
            Long reviewId = 1L;
            Long memberId = 1L;

            Mockito.when(myReviewRepository.findCafeteriaReviewByReviewId(
                    ArgumentMatchers.anyLong(),
                    ArgumentMatchers.anyLong()
            )).thenReturn(new ArrayList<>());

            // When
            // Then
            Assertions.assertThrows(GlobalRuntimeException.class, () ->
                    myReviewService.getMyCafeteriaReview(reviewId, memberId));
        }
    }

    @Nested
    @DisplayName("나의 도시락 리뷰 서비스 단위 테스트")
    class MyLunchBoxReview {

        @DisplayName("성공 - DTO 변환 테스트 (싱글 이미지)")
        @Test
        void success_Test_SingleImg() {
            // Given
            Long reviewId = 1L;
            Long memberId = 1L;
            String reviewDescription = "맛있오";
            String imgUrl = "mockImage.jpg";

            List<MyReviewProjection> mockList = List.of(
                    MyReviewProjection.builder().reviewDescription(reviewDescription).reviewImg(imgUrl).build()
            );

            Mockito.when(myReviewRepository.findLunchBoxReviewByReviewId(
                    ArgumentMatchers.anyLong(),
                    ArgumentMatchers.anyLong()
            )).thenReturn(mockList);


            // When
            MyReviewResponse.DetailDTO actual = myReviewService.getMyLunchBoxReview(reviewId, memberId);

            // Then
            Assertions.assertAll(
                    () -> Assertions.assertEquals(reviewDescription, actual.getReviewDescription()),
                    () -> Assertions.assertEquals(1, actual.getReviewImg().size()),
                    () -> Assertions.assertEquals(imgUrl, actual.getReviewImg().get(0))
            );
        }

        @DisplayName("성공 - DTO 변환 테스트 (여러 이미지)")
        @Test
        void success_Test_MultipleImg() {
            // Given
            Long reviewId = 1L;
            Long memberId = 1L;
            String imgUrl1 = "mockImage1.jpg";
            String imgUrl2 = "mockImage2.jpg";

            List<MyReviewProjection> mockList = List.of(
                    MyReviewProjection.builder().reviewImg(imgUrl1).build(),
                    MyReviewProjection.builder().reviewImg(imgUrl2).build()
            );

            Mockito.when(myReviewRepository.findLunchBoxReviewByReviewId(
                    ArgumentMatchers.anyLong(),
                    ArgumentMatchers.anyLong()
            )).thenReturn(mockList);


            // When
            MyReviewResponse.DetailDTO actual = myReviewService.getMyLunchBoxReview(reviewId, memberId);

            // Then
            Assertions.assertAll(
                    () -> Assertions.assertEquals(2, actual.getReviewImg().size()),
                    () -> Assertions.assertEquals(imgUrl1, actual.getReviewImg().get(0)),
                    () -> Assertions.assertEquals(imgUrl2, actual.getReviewImg().get(1))
            );
        }

        @DisplayName("실패 - DTO변환 오류")
        @Test
        void failure_Test_FailedConvertToDTO() {
            // Given
            Long reviewId = 1L;
            Long memberId = 1L;

            Mockito.when(myReviewRepository.findLunchBoxReviewByReviewId(
                    ArgumentMatchers.anyLong(),
                    ArgumentMatchers.anyLong()
            )).thenReturn(new ArrayList<>());

            // When
            // Then
            Assertions.assertThrows(GlobalRuntimeException.class, () ->
                    myReviewService.getMyLunchBoxReview(reviewId, memberId));
        }
    }
}
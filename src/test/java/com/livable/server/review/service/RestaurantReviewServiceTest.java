package com.livable.server.review.service;

import com.livable.server.core.exception.GlobalRuntimeException;
import com.livable.server.review.dto.Projection;
import com.livable.server.review.dto.RestaurantReviewResponse;
import com.livable.server.review.repository.RestaurantReviewRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static com.livable.server.review.dto.RestaurantReviewResponse.ListDTO;
import static com.livable.server.review.dto.RestaurantReviewResponse.ListForMenuDTO;

@ExtendWith(MockitoExtension.class)
class RestaurantReviewServiceTest {

    @Mock
    private RestaurantReviewRepository restaurantReviewRepository;

    @InjectMocks
    private RestaurantReviewService restaurantReviewService;

    @Nested
    @DisplayName("레스토랑 리뷰 리스트 서비스 단위 테스트")
    class list {

        @DisplayName("성공")
        @Test
        void success_Test() {
            // Given
            Long buildingId = 1L;

            List<ListDTO> mockList = List.of(
                    ListDTO.builder().reviewId(1L).build(),
                    ListDTO.builder().reviewId(2L).build(),
                    ListDTO.builder().reviewId(3L).build(),
                    ListDTO.builder().reviewId(4L).build(),
                    ListDTO.builder().reviewId(5L).build(),
                    ListDTO.builder().reviewId(6L).build(),
                    ListDTO.builder().reviewId(7L).build(),
                    ListDTO.builder().reviewId(8L).build(),
                    ListDTO.builder().reviewId(9L).build(),
                    ListDTO.builder().reviewId(10L).build()
            );
            Pageable pageable = PageRequest.of(0, 10);
            Page<ListDTO> mockPage = new PageImpl<>(mockList, pageable, 1);

            Mockito.when(restaurantReviewRepository.findRestaurantReviewByBuildingId(
                    ArgumentMatchers.anyLong(),
                    ArgumentMatchers.any(Pageable.class)
            )).thenReturn(mockPage);

            // When
            Page<ListDTO> actual =
                    restaurantReviewService.getAllList(buildingId, pageable);

            // Then
            Assertions.assertAll(
                    () -> Assertions.assertEquals(10, actual.getSize()),
                    () -> Assertions.assertEquals(1, actual.getTotalPages())
            );
        }
    }

    @Nested
    @DisplayName("특정 메뉴에 대한 레스토랑 리뷰 리스트 서비스 단위 테스트")
    class listForMenu {

        @DisplayName("성공")
        @Test
        void success_Test() {
            // Given
            Long menuId = 1L;

            List<ListForMenuDTO> mockList = List.of(
                    ListForMenuDTO.builder().reviewId(1L).build(),
                    ListForMenuDTO.builder().reviewId(2L).build(),
                    ListForMenuDTO.builder().reviewId(3L).build(),
                    ListForMenuDTO.builder().reviewId(4L).build(),
                    ListForMenuDTO.builder().reviewId(5L).build(),
                    ListForMenuDTO.builder().reviewId(6L).build(),
                    ListForMenuDTO.builder().reviewId(7L).build(),
                    ListForMenuDTO.builder().reviewId(8L).build(),
                    ListForMenuDTO.builder().reviewId(9L).build(),
                    ListForMenuDTO.builder().reviewId(10L).build()
            );
            Pageable pageable = PageRequest.of(0, 10);
            Page<ListForMenuDTO> mockPage = new PageImpl<>(mockList, pageable, 1);

            Mockito.when(restaurantReviewRepository.findRestaurantReviewByMenuId(
                    ArgumentMatchers.anyLong(),
                    ArgumentMatchers.any(Pageable.class)
            )).thenReturn(mockPage);

            // When
            Page<ListForMenuDTO> actual =
                    restaurantReviewService.getAllListForMenu(menuId, pageable);

            // Then
            Assertions.assertAll(
                    () -> Assertions.assertEquals(10, actual.getSize()),
                    () -> Assertions.assertEquals(1, actual.getTotalPages())
            );
        }
    }

    @Nested
    @DisplayName("레스토랑 리뷰 상세 정보 서비스 단위 테스트")
    class Detail {

        @DisplayName("성공 - 싱글 이미지")
        @Test
        void success_Test_SingleImage() {
            // Given
            Long reviewId = 1L;

            List<Projection.RestaurantReview> mockResult = List.of(
                    Projection.RestaurantReview.builder()
                            .reviewImg("TestImages")
                            .reviewDescription("TestDescription")
                            .build()
            );

            Mockito.when(restaurantReviewRepository.findRestaurantReviewById(ArgumentMatchers.anyLong()))
                    .thenReturn(mockResult);
            // When
            RestaurantReviewResponse.DetailDTO actual = restaurantReviewService.getDetail(reviewId);

            // Then
            Assertions.assertAll(
                    () -> Assertions.assertEquals("TestDescription", actual.getReviewDescription()),
                    () -> Assertions.assertEquals(1, actual.getReviewImages().size())
            );
        }

        @DisplayName("성공 - 여러 이미지")
        @Test
        void success_Test_MultipleImage() {
            // Given
            Long reviewId = 1L;

            List<Projection.RestaurantReview> mockResult = List.of(
                    Projection.RestaurantReview.builder()
                            .reviewImg("TestImage1")
                            .reviewDescription("TestDescription")
                            .build(),
                    Projection.RestaurantReview.builder()
                            .reviewImg("TestImage2")
                            .reviewDescription("TestDescription")
                            .build()
            );

            Mockito.when(restaurantReviewRepository.findRestaurantReviewById(ArgumentMatchers.anyLong()))
                    .thenReturn(mockResult);
            // When
            RestaurantReviewResponse.DetailDTO actual = restaurantReviewService.getDetail(reviewId);

            // Then
            Assertions.assertAll(
                    () -> Assertions.assertEquals("TestDescription", actual.getReviewDescription()),
                    () -> Assertions.assertEquals(2, actual.getReviewImages().size())
            );
        }

        @DisplayName("실패 - DTO변환 오류")
        @Test
        void failure_Test_FailedConvertToDTO() {
            // Given
            Long reviewId = 1L;

            Mockito.when(restaurantReviewRepository.findRestaurantReviewById(ArgumentMatchers.anyLong()))
                    .thenReturn(new ArrayList<>());

            // When
            // Then
            Assertions.assertThrows(GlobalRuntimeException.class, () ->
                    restaurantReviewService.getDetail(reviewId));
        }
    }
}
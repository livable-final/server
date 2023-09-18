package com.livable.server.review.service;

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

import java.util.List;

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

            List<RestaurantReviewResponse.ListDTO> mockList = List.of(
                    RestaurantReviewResponse.ListDTO.builder().reviewId(1L).build(),
                    RestaurantReviewResponse.ListDTO.builder().reviewId(2L).build(),
                    RestaurantReviewResponse.ListDTO.builder().reviewId(3L).build(),
                    RestaurantReviewResponse.ListDTO.builder().reviewId(4L).build(),
                    RestaurantReviewResponse.ListDTO.builder().reviewId(5L).build(),
                    RestaurantReviewResponse.ListDTO.builder().reviewId(6L).build(),
                    RestaurantReviewResponse.ListDTO.builder().reviewId(7L).build(),
                    RestaurantReviewResponse.ListDTO.builder().reviewId(8L).build(),
                    RestaurantReviewResponse.ListDTO.builder().reviewId(9L).build(),
                    RestaurantReviewResponse.ListDTO.builder().reviewId(10L).build()
            );
            Pageable pageable = PageRequest.of(0, 10);
            Page<RestaurantReviewResponse.ListDTO> mockPage = new PageImpl<>(mockList, pageable, 1);

            Mockito.when(restaurantReviewRepository.findRestaurantReviewByBuildingId(
                    ArgumentMatchers.anyLong(),
                    ArgumentMatchers.any(Pageable.class)
            )).thenReturn(mockPage);

            // When
            Page<RestaurantReviewResponse.ListDTO> actual =
                    restaurantReviewService.getAllList(buildingId, pageable);

            // Then
            Assertions.assertAll(
                    () -> Assertions.assertEquals(10, actual.getSize()),
                    () -> Assertions.assertEquals(1,actual.getTotalPages())
            );
        }
    }
}
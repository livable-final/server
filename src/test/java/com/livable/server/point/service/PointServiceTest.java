package com.livable.server.point.service;

import com.livable.server.core.exception.GlobalRuntimeException;
import com.livable.server.entity.Point;
import com.livable.server.point.domain.DateFactory;
import com.livable.server.point.domain.DateRange;
import com.livable.server.point.dto.PointResponse;
import com.livable.server.point.repository.PointRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class PointServiceTest {

    @Mock
    private PointRepository pointRepository;

    @Mock
    private DateFactory dateFactory;

    @InjectMocks
    private PointService pointService;

    @Nested
    @DisplayName("나의 리뷰 카운트 서비스 단위 테스트")
    class MyRestaurantReview {

        @DisplayName("성공")
        @Test
        void success_Test() {
            // Given
            Long memberId = 1L;
            LocalDateTime currentDate = LocalDateTime.now();
            PointResponse.ReviewCountDTO countDTO = new PointResponse.ReviewCountDTO(5L);
            Point point = Point.builder().id(1L).build();

            LocalDateTime startDate = LocalDateTime.of(2023, 1, 1, 0, 0, 0);
            LocalDateTime endDte = LocalDateTime.of(2023, 2, 1, 0, 0, 0);
            DateRange dateRange = new DateRange(startDate, endDte);

            Mockito.when(dateFactory.getMonthRangeOf(ArgumentMatchers.any(LocalDateTime.class)))
                    .thenReturn(dateRange);

            Mockito.when(pointRepository.findByMember_Id(ArgumentMatchers.anyLong()))
                    .thenReturn(Optional.of(point));

            Mockito.when(pointRepository.findPointCountById(
                    ArgumentMatchers.anyLong(),
                    ArgumentMatchers.any(LocalDateTime.class),
                    ArgumentMatchers.any(LocalDateTime.class),
                    ArgumentMatchers.anyList()
            )).thenReturn(countDTO);

            // When
            PointResponse.ReviewCountDTO actual = pointService.getMyReviewCount(memberId, currentDate);

            // Then
            Assertions.assertEquals(5L, actual.getCount());
        }

        @DisplayName("실패 - 유효하지 않은 포인트 정보")
        @Test
        void failure_Test_existPointData() {
            // Given
            Long memberId = 1L;
            LocalDateTime currentDate = LocalDateTime.now();

            Mockito.when(pointRepository.findByMember_Id(ArgumentMatchers.anyLong()))
                    .thenReturn(Optional.empty());

            // When
            // Then
            Assertions.assertThrows(GlobalRuntimeException.class, () ->
                    pointService.getMyReviewCount(memberId, currentDate));
        }
    }
}
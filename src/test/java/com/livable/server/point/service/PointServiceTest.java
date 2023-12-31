package com.livable.server.point.service;

import com.livable.server.core.exception.GlobalRuntimeException;
import com.livable.server.entity.Point;
import com.livable.server.entity.PointCode;
import com.livable.server.entity.PointLog;
import com.livable.server.entity.Review;
import com.livable.server.point.domain.DateFactory;
import com.livable.server.point.domain.DateRange;
import com.livable.server.point.domain.PointErrorCode;
import com.livable.server.point.dto.PointResponse;
import com.livable.server.point.repository.PointLogRepository;
import com.livable.server.point.repository.PointRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class PointServiceTest {

    @Mock
    private PointRepository pointRepository;

    @Mock
    private PointLogRepository pointLogRepository;

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

            Mockito.when(pointRepository.findByMemberId(ArgumentMatchers.anyLong()))
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

            Mockito.when(pointRepository.findByMemberId(ArgumentMatchers.anyLong()))
                    .thenReturn(Optional.empty());

            // When
            // Then
            Assertions.assertThrows(GlobalRuntimeException.class, () ->
                    pointService.getMyReviewCount(memberId, currentDate));
        }
    }

    @Nested
    @MockitoSettings(strictness = Strictness.LENIENT)
    @DisplayName("목표 달성 포인트 지급 서비스 단위 테스트")
    class GetAchievementPoint {

        @BeforeEach
        void setUp() {
            LocalDateTime startDate = LocalDateTime.of(2023, 1, 1, 0, 0, 0);
            LocalDateTime endDte = LocalDateTime.of(2023, 2, 1, 0, 0, 0);
            DateRange dateRange = new DateRange(startDate, endDte);

            Mockito.when(dateFactory.getMonthRangeOf(ArgumentMatchers.any(LocalDateTime.class)))
                    .thenReturn(dateRange);

            LocalDate pureDate = LocalDate.of(2023, 1, 1);
            Mockito.when(dateFactory.getPureDate(ArgumentMatchers.any(LocalDateTime.class)))
                    .thenReturn(pureDate);
        }

        @DisplayName("실패 - 회원 아이디로부터 포인트 테이블이 조회되지 않는 경우, 예외를 발생한다.")
        @Test
        void failure_Test_GivenInvalidMemberId_ThenThrowsErrorWithMessage() {
            // Given
            Long memberId = 1L;
            LocalDateTime requestTime = LocalDateTime.of(2023, 1, 1, 0, 0, 0);

            // When
            Mockito.when(pointRepository.findByMemberId(ArgumentMatchers.anyLong()))
                    .thenReturn(Optional.empty());

            // Then
            Assertions.assertThrows(GlobalRuntimeException.class, () -> pointService.getAchievementPoint(memberId, requestTime));
            try {
                pointService.getAchievementPoint(memberId, requestTime);
            } catch (GlobalRuntimeException exception) {
                Assertions.assertEquals(PointErrorCode.POINT_NOT_EXIST.getMessage(), exception.getMessage());
            }
        }

        @DisplayName("실패 - 포인트 데이터로부터 포인트 로그 테이블의 데이터가 존재하지 않는 경우, 예외를 발생한다.")
        @Test
        void failure_Test_GivenHaveNothingLogPoint_ThenThrowsErrorWithMessage() {
            // Given
            Long memberId = 1L;
            LocalDateTime requestTime = LocalDateTime.of(2023, 1, 1, 0, 0, 0);

            Point point = Point.builder().id(1L).balance(10).build();
            List<PointLog> mockList = List.of();

            // When
            Mockito.when(pointRepository.findByMemberId(ArgumentMatchers.anyLong()))
                            .thenReturn(Optional.of(point));

            Mockito.when(pointLogRepository.findDateRangeOfPointLogByPointId(
                    ArgumentMatchers.anyLong(),
                    ArgumentMatchers.any(LocalDateTime.class),
                    ArgumentMatchers.any(LocalDateTime.class))
            ).thenReturn(mockList);

            // Then
            Assertions.assertThrows(GlobalRuntimeException.class, () -> pointService.getAchievementPoint(memberId, requestTime));
            try {
                pointService.getAchievementPoint(memberId, requestTime);
            } catch (GlobalRuntimeException exception) {
                Assertions.assertEquals(PointErrorCode.POINT_NOT_EXIST_FOR_CURRENT_MONTH.getMessage(), exception.getMessage());
            }
        }

        @DisplayName("성공 - 7개의 리뷰 포인트 로그가 주어지는 경우, 목표 포인트를 지급한다.")
        @Test
        void success_Test_GivenSevenReviewPointLog_ThenPaidAchievementPoint() {
            // Given
            Long memberId = 1L;
            LocalDateTime requestTime = LocalDateTime.of(2023, 1, 1, 0, 0, 0);
            LocalDateTime paidTime = LocalDateTime.of(2023, 1, 1, 0, 0, 0);

            Point point = Point.builder().id(1L).balance(10).build();
            Review review = Review.builder().id(1L).build();
            List<PointLog> MockList = List.of(
                    PointLog.builder().id(1L).point(point).review(review).code(PointCode.PA00).paidAt(paidTime).build(),
                    PointLog.builder().id(2L).point(point).review(review).code(PointCode.PA01).paidAt(paidTime).build(),
                    PointLog.builder().id(3L).point(point).review(review).code(PointCode.PA00).paidAt(paidTime).build(),
                    PointLog.builder().id(4L).point(point).review(review).code(PointCode.PA00).paidAt(paidTime).build(),
                    PointLog.builder().id(5L).point(point).review(review).code(PointCode.PA02).paidAt(paidTime).build(),
                    PointLog.builder().id(6L).point(point).review(review).code(PointCode.PA00).paidAt(paidTime).build(),
                    PointLog.builder().id(7L).point(point).review(review).code(PointCode.PA01).paidAt(paidTime).build()
            );

            Mockito.when(pointRepository.findByMemberId(ArgumentMatchers.anyLong()))
                            .thenReturn(Optional.of(point));

            Mockito.when(pointLogRepository.findDateRangeOfPointLogByPointId(
                    ArgumentMatchers.anyLong(),
                    ArgumentMatchers.any(LocalDateTime.class),
                    ArgumentMatchers.any(LocalDateTime.class)
            )).thenReturn(MockList);

            // When
            // Then
            pointService.getAchievementPoint(memberId, requestTime);
            Assertions.assertEquals(110, point.getBalance());
        }

        @DisplayName("성공 - 14개의 리뷰 포인트 로그가 주어지는 경우, 목표 포인트를 지급한다.")
        @Test
        void success_Test_GivenFourTeenReviewPointLog_ThenPaidAchievementPoint() {
            // Given
            Long memberId = 1L;
            LocalDateTime requestTime = LocalDateTime.of(2023, 1, 1, 0, 0, 0);
            LocalDateTime paidTime = LocalDateTime.of(2023, 1, 1, 0, 0, 0);

            Point point = Point.builder().id(1L).balance(10).build();
            Review review = Review.builder().id(1L).build();
            List<PointLog> MockList = List.of(
                    PointLog.builder().id(1L).point(point).review(review).code(PointCode.PA00).paidAt(paidTime).build(),
                    PointLog.builder().id(2L).point(point).review(review).code(PointCode.PA01).paidAt(paidTime).build(),
                    PointLog.builder().id(3L).point(point).review(review).code(PointCode.PA00).paidAt(paidTime).build(),
                    PointLog.builder().id(4L).point(point).review(review).code(PointCode.PA00).paidAt(paidTime).build(),
                    PointLog.builder().id(5L).point(point).review(review).code(PointCode.PA02).paidAt(paidTime).build(),
                    PointLog.builder().id(6L).point(point).review(review).code(PointCode.PA00).paidAt(paidTime).build(),
                    PointLog.builder().id(7L).point(point).review(review).code(PointCode.PA01).paidAt(paidTime).build(),
                    PointLog.builder().id(8L).point(point).review(review).code(PointCode.PA01).paidAt(paidTime).build(),
                    PointLog.builder().id(9L).point(point).review(review).code(PointCode.PA01).paidAt(paidTime).build(),
                    PointLog.builder().id(10L).point(point).review(review).code(PointCode.PA01).paidAt(paidTime).build(),
                    PointLog.builder().id(11L).point(point).review(review).code(PointCode.PA01).paidAt(paidTime).build(),
                    PointLog.builder().id(12L).point(point).review(review).code(PointCode.PA01).paidAt(paidTime).build(),
                    PointLog.builder().id(13L).point(point).review(review).code(PointCode.PA01).paidAt(paidTime).build(),
                    PointLog.builder().id(14L).point(point).review(review).code(PointCode.PA01).paidAt(paidTime).build()
            );

            Mockito.when(pointRepository.findByMemberId(ArgumentMatchers.anyLong()))
                    .thenReturn(Optional.of(point));

            Mockito.when(pointLogRepository.findDateRangeOfPointLogByPointId(
                    ArgumentMatchers.anyLong(),
                    ArgumentMatchers.any(LocalDateTime.class),
                    ArgumentMatchers.any(LocalDateTime.class)
            )).thenReturn(MockList);

            // When
            // Then
            pointService.getAchievementPoint(memberId, requestTime);
            Assertions.assertEquals(110, point.getBalance());
        }

        @DisplayName("실패 - 3개의 리뷰 포인트 로그가 주어지는 경우, 목표를 달성하지 않았다는 메시지 예외를 발생한다.")
        @Test
        void failure_Test_GivenThreeReviewPointLog_ThenThrowsErrorWithMessage() {
            // Given
            Long memberId = 1L;
            LocalDateTime requestTime = LocalDateTime.of(2023, 1, 1, 0, 0, 0);
            LocalDateTime paidTime = LocalDateTime.of(2023, 1, 1, 0, 0, 0);

            Point point = Point.builder().id(1L).balance(10).build();
            Review review = Review.builder().id(1L).build();
            List<PointLog> MockList = List.of(
                    PointLog.builder().id(1L).point(point).review(review).code(PointCode.PA00).paidAt(paidTime).build(),
                    PointLog.builder().id(2L).point(point).review(review).code(PointCode.PA01).paidAt(paidTime).build(),
                    PointLog.builder().id(3L).point(point).review(review).code(PointCode.PA00).paidAt(paidTime).build()
            );

            Mockito.when(pointRepository.findByMemberId(ArgumentMatchers.anyLong()))
                    .thenReturn(Optional.of(point));

            Mockito.when(pointLogRepository.findDateRangeOfPointLogByPointId(
                    ArgumentMatchers.anyLong(),
                    ArgumentMatchers.any(LocalDateTime.class),
                    ArgumentMatchers.any(LocalDateTime.class)
            )).thenReturn(MockList);

            // When
            // Then
            Assertions.assertThrows(GlobalRuntimeException.class,
                    () -> pointService.getAchievementPoint(memberId, requestTime));

            try {
                pointService.getAchievementPoint(memberId, requestTime);
            } catch (GlobalRuntimeException exception) {
                Assertions.assertEquals(
                        PointErrorCode.ACHIEVEMENT_POINT_NOT_MATCHED.getMessage(), exception.getMessage());
            }
        }

        @DisplayName("실패 - 11개의 리뷰 포인트 로그가 주어지는 경우, 목표를 달성하지 않았다는 메시지 예외를 발생한다.")
        @Test
        void failure_Test_GivenElevenReviewPointLog_ThenThrowsErrorWithMessage() {
            // Given
            Long memberId = 1L;
            LocalDateTime requestTime = LocalDateTime.of(2023, 1, 1, 0, 0, 0);
            LocalDateTime paidTime = LocalDateTime.of(2023, 1, 1, 0, 0, 0);

            Point point = Point.builder().id(1L).balance(10).build();
            Review review = Review.builder().id(1L).build();
            List<PointLog> MockList = List.of(
                    PointLog.builder().id(1L).point(point).review(review).code(PointCode.PA00).paidAt(paidTime).build(),
                    PointLog.builder().id(2L).point(point).review(review).code(PointCode.PA01).paidAt(paidTime).build(),
                    PointLog.builder().id(3L).point(point).review(review).code(PointCode.PA00).paidAt(paidTime).build(),
                    PointLog.builder().id(4L).point(point).review(review).code(PointCode.PA00).paidAt(paidTime).build(),
                    PointLog.builder().id(5L).point(point).review(review).code(PointCode.PA00).paidAt(paidTime).build(),
                    PointLog.builder().id(6L).point(point).review(review).code(PointCode.PA00).paidAt(paidTime).build(),
                    PointLog.builder().id(7L).point(point).review(review).code(PointCode.PA00).paidAt(paidTime).build(),
                    PointLog.builder().id(8L).point(point).review(review).code(PointCode.PA00).paidAt(paidTime).build(),
                    PointLog.builder().id(9L).point(point).review(review).code(PointCode.PA00).paidAt(paidTime).build(),
                    PointLog.builder().id(10L).point(point).review(review).code(PointCode.PA00).paidAt(paidTime).build(),
                    PointLog.builder().id(11L).point(point).review(review).code(PointCode.PA00).paidAt(paidTime).build()
            );

            Mockito.when(pointRepository.findByMemberId(ArgumentMatchers.anyLong()))
                    .thenReturn(Optional.of(point));

            Mockito.when(pointLogRepository.findDateRangeOfPointLogByPointId(
                    ArgumentMatchers.anyLong(),
                    ArgumentMatchers.any(LocalDateTime.class),
                    ArgumentMatchers.any(LocalDateTime.class)
            )).thenReturn(MockList);

            // When
            // Then
            Assertions.assertThrows(GlobalRuntimeException.class,
                    () -> pointService.getAchievementPoint(memberId, requestTime));

            try {
                pointService.getAchievementPoint(memberId, requestTime);
            } catch (GlobalRuntimeException exception) {
                Assertions.assertEquals(
                        PointErrorCode.ACHIEVEMENT_POINT_NOT_MATCHED.getMessage(), exception.getMessage());
            }
        }

        @DisplayName("실패 - 목표 달성 포인트를 지급받은 이후 다시 포인트 지급을 요청하는 경우, 예외를 발생한다.")
        @Test
        void failure_Test_WhenDuplicateRequest_ThenThrowError() {
            // Given
            Long memberId = 1L;
            Point point = Point.builder().id(1L).balance(10).build();
            Review review = Review.builder().id(1L).build();

            LocalDateTime requestTime = LocalDateTime.of(2023, 1, 1, 0, 0, 0);
            LocalDateTime paidTime = LocalDateTime.of(2023, 1, 1, 0, 0, 0);
            List<PointLog> MockList = List.of(
                    PointLog.builder().id(1L).point(point).review(review).code(PointCode.PA00).paidAt(paidTime).build(),
                    PointLog.builder().id(2L).point(point).review(review).code(PointCode.PA01).paidAt(paidTime).build(),
                    PointLog.builder().id(3L).point(point).review(review).code(PointCode.PA00).paidAt(paidTime).build(),
                    PointLog.builder().id(4L).point(point).review(review).code(PointCode.PA00).paidAt(paidTime).build(),
                    PointLog.builder().id(5L).point(point).review(review).code(PointCode.PA02).paidAt(paidTime).build(),
                    PointLog.builder().id(6L).point(point).review(review).code(PointCode.PA00).paidAt(paidTime).build(),
                    PointLog.builder().id(7L).point(point).review(review).code(PointCode.PA01).paidAt(paidTime).build(),
                    PointLog.builder().id(8L).point(point).review(review).code(PointCode.PA03).paidAt(paidTime).build()
            );

            // When
            Mockito.when(pointRepository.findByMemberId(ArgumentMatchers.anyLong()))
                    .thenReturn(Optional.of(point));

            Mockito.when(pointLogRepository.findDateRangeOfPointLogByPointId(
                    ArgumentMatchers.anyLong(),
                    ArgumentMatchers.any(LocalDateTime.class),
                    ArgumentMatchers.any(LocalDateTime.class)
            )).thenReturn(MockList);

            // Then
            Assertions.assertThrows(GlobalRuntimeException.class, () -> pointService.getAchievementPoint(memberId, requestTime));
            try {
                pointService.getAchievementPoint(memberId, requestTime);
            } catch (GlobalRuntimeException exception) {
                Assertions.assertEquals(PointErrorCode.ACHIEVEMENT_POINT_PAID_ALREADY.getMessage(), exception.getMessage());
            }
        }

        @DisplayName("실패 - 목표는 달성 했지만 목표 달성일 이후 포인트 지급을 요청한 경우, 예외를 발생한다.")
        @Test
        void failure_Test_GivenRequestNotAchievingDate_ThenThrowError() {
            // Given
            Long memberId = 1L;
            Point point = Point.builder().id(1L).balance(10).build();
            Review review = Review.builder().id(1L).build();

            LocalDateTime requestTime = LocalDateTime.of(2023, 1, 4, 0, 0, 0);
            LocalDateTime paidTime = LocalDateTime.of(2023, 1, 1, 0, 0, 0);
            List<PointLog> MockList = List.of(
                    PointLog.builder().id(1L).point(point).review(review).code(PointCode.PA00).paidAt(paidTime).build(),
                    PointLog.builder().id(2L).point(point).review(review).code(PointCode.PA01).paidAt(paidTime).build(),
                    PointLog.builder().id(3L).point(point).review(review).code(PointCode.PA00).paidAt(paidTime).build(),
                    PointLog.builder().id(4L).point(point).review(review).code(PointCode.PA00).paidAt(paidTime).build(),
                    PointLog.builder().id(5L).point(point).review(review).code(PointCode.PA02).paidAt(paidTime).build(),
                    PointLog.builder().id(6L).point(point).review(review).code(PointCode.PA00).paidAt(paidTime).build(),
                    PointLog.builder().id(7L).point(point).review(review).code(PointCode.PA01).paidAt(paidTime).build()
            );

            // When
            Mockito.when(pointRepository.findByMemberId(ArgumentMatchers.anyLong()))
                    .thenReturn(Optional.of(point));

            Mockito.when(pointLogRepository.findDateRangeOfPointLogByPointId(
                    ArgumentMatchers.anyLong(),
                    ArgumentMatchers.any(LocalDateTime.class),
                    ArgumentMatchers.any(LocalDateTime.class)
            )).thenReturn(MockList);

            Mockito.when(dateFactory.getPureDate(requestTime))
                    .thenReturn(LocalDate.of(2023, 1, 4));

            // Then
            Assertions.assertThrows(GlobalRuntimeException.class, () -> pointService.getAchievementPoint(memberId, requestTime));
            try {
                pointService.getAchievementPoint(memberId, requestTime);
            } catch (GlobalRuntimeException exception) {
                Assertions.assertEquals(PointErrorCode.ACHIEVEMENT_POINT_PAID_FAILED.getMessage(), exception.getMessage());
            }
        }
    }
}

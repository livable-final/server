package com.livable.server.point.service;

import com.livable.server.core.exception.GlobalRuntimeException;
import com.livable.server.entity.Point;
import com.livable.server.entity.PointCode;
import com.livable.server.entity.PointLog;
import com.livable.server.entity.Review;
import com.livable.server.point.domain.DateFactory;
import com.livable.server.point.domain.DateRange;
import com.livable.server.point.domain.PointAchievement;
import com.livable.server.point.domain.PointErrorCode;
import com.livable.server.point.dto.PointResponse;
import com.livable.server.point.repository.PointLogRepository;
import com.livable.server.point.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PointService {

    private final PointRepository pointRepository;
    private final PointLogRepository pointLogRepository;
    private final DateFactory dateFactory;

    @Transactional(readOnly = true)
    public PointResponse.ReviewCountDTO getMyReviewCount(Long memberId, LocalDateTime currentDate) {

        Point point = pointRepository.findByMemberId(memberId).orElseThrow(() ->
                new GlobalRuntimeException(PointErrorCode.POINT_NOT_EXIST));

        DateRange dateRange = dateFactory.getMonthRangeOf(currentDate);

        return pointRepository.findPointCountById(
                point.getId(),
                dateRange.getStartDate(),
                dateRange.getEndDate(),
                PointCode.getReviewPointCodes()
        );
    }

    @Transactional
    public void getAchievementPoint(Long memberId, LocalDateTime requestDateTime) {

        // 회원 정보가 유효한지 검증
        final Point point = pointRepository.findByMemberId(memberId).orElseThrow(() ->
                new GlobalRuntimeException(PointErrorCode.POINT_NOT_EXIST));

        // 회원의 요청 날짜에 대한 한달 범위의 포인트 로그를 검색
        final List<PointLog> pointLogs = this.getPointLogPerMonthBy(requestDateTime, point);
        PointLog recentPointLog = this.getRecentPointLogFrom(pointLogs);
        LocalDate requestDate = dateFactory.getPureDate(requestDateTime);

        // 금일 이미 목표 달성 포인트를 지급받았는지 확인
        this.validationAchievementPointAlreadyPaid(pointLogs, requestDate);

        // 리뷰 개수가 목표 달성으로 치환되는지 확인 (목표를 달성했는지 확인)
        PointAchievement pointAchievement = this.getPointAchievementFrom(pointLogs);

        // 목표 포인트 지급 요청 날짜가 포인트를 지급받을 수 있는 날짜인지 확인
        if (!recentPointLog.isPaid(requestDate)) {
            throw new GlobalRuntimeException(PointErrorCode.ACHIEVEMENT_POINT_PAID_FAILED);
        }

        // 포인트 지급
        this.paidPoints(point, pointAchievement, recentPointLog.getReview());
    }

    /**
     * point의 포인트 로그중
     * requestDateTime의 year-month에 해당하는 한달 간의 포인트 로그를 반환한다. <br>
     * 최신 순으로 정렬
     *
     * @param requestDateTime LocalDateTime
     * @param point           Point
     * @return 포인트 로그의 리스트
     */
    private List<PointLog> getPointLogPerMonthBy(LocalDateTime requestDateTime, Point point) {

        DateRange dateRangeOfMonth = dateFactory.getMonthRangeOf(requestDateTime);

        return pointLogRepository.findDateRangeOfPointLogByPointId(
                point.getId(), dateRangeOfMonth.getStartDate(), dateRangeOfMonth.getEndDate());
    }

    /**
     * 최신 순으로 정렬된 PointLog 리스트 중 가장 최근의 포인트 로그를 반환한다.<br>
     * 포인트 로그 리스트가 비어있다면 예외를 발생한다.
     *
     * @param pointLogs
     * @return PointLog
     */
    private PointLog getRecentPointLogFrom(List<PointLog> pointLogs) throws GlobalRuntimeException {
        return pointLogs.stream().findFirst()
                .orElseThrow(() -> new GlobalRuntimeException(PointErrorCode.POINT_NOT_EXIST_FOR_CURRENT_MONTH));
    }

    /**
     * PointLog 리스트 중 requestDate에 목표달성 포인트를 지급 받은 이력이 있는지 확인한다.
     *
     * @param pointLogs   List
     * @param requestDate LocalDate
     */
    private void validationAchievementPointAlreadyPaid(List<PointLog> pointLogs, LocalDate requestDate) {
        pointLogs.stream()
                .filter(pointLog -> pointLog.isPaid(requestDate))
                .forEach(pointLog -> {
                    if (PointAchievement.POINT_CODES.contains(pointLog.getCode())) {
                        throw new GlobalRuntimeException(PointErrorCode.ACHIEVEMENT_POINT_PAID_ALREADY);
                    }
                });
    }

    /**
     * PointLog 리스트 중 리뷰를 통해 얻은 포인트 로그의 개수를
     * PointAchievement 객체로 치환하여 반환한다. <br>
     * 치환이 불가능한 경우 예외를 발생한다.
     *
     * @param pointLogs List
     * @return PointAchievement
     */
    private PointAchievement getPointAchievementFrom(List<PointLog> pointLogs) throws GlobalRuntimeException {
        try {
            int count = (int) pointLogs.stream().filter(pointLog -> {
                PointCode code = pointLog.getCode();
                return PointCode.getReviewPointCodes().contains(code);
            }).count();

            return PointAchievement.valueOf(count);
        } catch (InputMismatchException exception) {
            throw new GlobalRuntimeException(PointErrorCode.ACHIEVEMENT_POINT_NOT_MATCHED);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    public void paidPoints(Point point, PointAchievement pointAchievement, Review review) {

        Integer amount = pointAchievement.getAmount();
        point.plusPoint(amount);

        PointLog pointLog = PointLog.builder()
                .point(point)
                .review(review)
                .code(pointAchievement.getPointCode())
                .amount(pointAchievement.getAmount())
                .build();

        pointLogRepository.save(pointLog);
    }
}

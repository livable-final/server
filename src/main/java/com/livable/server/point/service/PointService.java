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
import com.livable.server.point.dto.PointProjection;
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

        System.out.println("requestDateTime:" + requestDateTime);

        // 회원 정보가 유효한지 검증
        Point point = pointRepository.findByMemberId(memberId).orElseThrow(() ->
                new GlobalRuntimeException(PointErrorCode.POINT_NOT_EXIST));

        // 금일 이미 목표 달성 포인트를 지급받았는지 확인
        LocalDate requestDate = dateFactory.getPureDate(requestDateTime);
        List<PointLog> logsByDate = pointLogRepository.findLogsByDate(requestDate);
        System.out.println(logsByDate.get(0).getCode());
        logsByDate.forEach(pointLog -> {
            if (PointAchievement.POINT_CODES.contains(pointLog.getCode())) {
                throw new GlobalRuntimeException(PointErrorCode.ACHIEVEMENT_POINT_PAID_ALREADY);
            }
        });

        // 현재의 년-월 범위에 해당하는 리뷰를 조회
        DateRange requestedDateOfMonthRange = dateFactory.getMonthRangeOf(requestDateTime);
        List<PointProjection.ReviewAndDateDTO> reviewAndDates = pointRepository.findReviewAndDateById(
                point.getId(),
                requestedDateOfMonthRange.getStartDate(),
                requestedDateOfMonthRange.getEndDate(),
                PointCode.getReviewPointCodes()
        );

        PointProjection.ReviewAndDateDTO lastReview = reviewAndDates.get(0);

        Review review = lastReview.getReview();
        LocalDateTime lastCreatedDate = lastReview.getCreatedAt();
        Integer count = reviewAndDates.size();
        PointAchievement pointAchievement;

        // 리뷰 개수가 목표 달성으로 치환되는지 확인
        try {
            pointAchievement = PointAchievement.valueOf(count);
        } catch (InputMismatchException exception) {
            throw new GlobalRuntimeException(PointErrorCode.ACHIEVEMENT_POINT_NOT_MATCHED);
        }

        // 목표 포인트 지급 요청 날짜가 지급받을 수 있는 날짜인지 확인
        if (lastCreatedDate.getDayOfMonth() != requestDateTime.getDayOfMonth()) {
            throw new GlobalRuntimeException(PointErrorCode.ACHIEVEMENT_POINT_PAID_FAILED);
        }

        // 포인트 지급
        this.paidPoints(point, pointAchievement, review);
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

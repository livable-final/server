package com.livable.server.point.service;

import com.livable.server.core.exception.GlobalRuntimeException;
import com.livable.server.entity.Point;
import com.livable.server.entity.PointCode;
import com.livable.server.member.domain.MemberErrorCode;
import com.livable.server.point.domain.DateFactory;
import com.livable.server.point.domain.DateRange;
import com.livable.server.point.domain.PointErrorCode;
import com.livable.server.point.dto.PointResponse;
import com.livable.server.point.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PointService {

    private final PointRepository pointRepository;
    private final DateFactory dateFactory;

    @Transactional(readOnly = true)
    public PointResponse.ReviewCountDTO getMyReviewCount(Long memberId, LocalDateTime currentDate) {

        Point point = pointRepository.findByMember_Id(memberId).orElseThrow(() ->
                new GlobalRuntimeException(PointErrorCode.POINT_NOT_EXIST));

        DateRange dateRange = dateFactory.getMonthRangeOf(currentDate);

        return pointRepository.findPointCountById(
                point.getId(),
                dateRange.getStartDate(),
                dateRange.getEndDate(),
                List.of(PointCode.PA00, PointCode.PA01, PointCode.PA02)
        );
    }
}

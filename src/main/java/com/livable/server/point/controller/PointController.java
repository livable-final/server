package com.livable.server.point.controller;

import com.livable.server.core.response.ApiResponse;
import com.livable.server.core.util.Actor;
import com.livable.server.core.util.JwtTokenProvider;
import com.livable.server.core.util.LoginActor;
import com.livable.server.point.dto.PointResponse;
import com.livable.server.point.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
public class PointController {

    private final PointService pointService;

    @GetMapping("/api/points/logs/members")
    public ResponseEntity<ApiResponse.Success<PointResponse.ReviewCountDTO>> getMyReviewCount(@LoginActor Actor actor) {

        JwtTokenProvider.checkMemberToken(actor);

        Long memberId = actor.getId(); // TODO: 토큰에서 값을 추출할 것
        LocalDateTime currentDate = LocalDateTime.now();

        PointResponse.ReviewCountDTO myReviewCount = pointService.getMyReviewCount(memberId, currentDate);
        return ApiResponse.success(myReviewCount, HttpStatus.OK);
    }

    @PostMapping("/api/points/logs/members")
    public ResponseEntity<ApiResponse.Success<Object>> getAchievementPoint() {

        Long memberId = 1L; // TODO: 토큰에서 값을 추출할 것
        LocalDateTime requestDateTime = LocalDateTime.now();

        pointService.getAchievementPoint(memberId, requestDateTime);
        return ApiResponse.success(HttpStatus.CREATED);
    }
}

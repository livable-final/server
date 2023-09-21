package com.livable.server.review.controller;

import com.livable.server.core.response.ApiResponse;
import com.livable.server.review.dto.MyReviewResponse;
import com.livable.server.review.service.MyReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MyReviewController {

    private final MyReviewService myReviewService;

    @GetMapping("/api/reviews/restaurant/{reviewId}/members")
    public ResponseEntity<ApiResponse.Success<MyReviewResponse.DetailDTO>> getMyRestaurantReview(
            @PathVariable Long reviewId) {

        // TODO: JWT 구현완료 시 토큰으로부터 값을 꺼내올 것
        Long memberId = 1L;

        MyReviewResponse.DetailDTO myRestaurantReview = myReviewService.getMyRestaurantReview(reviewId, memberId);
        return ApiResponse.success(myRestaurantReview, HttpStatus.OK);
    }

    @GetMapping("/api/reviews/cafeteria/{reviewId}/members")
    public ResponseEntity<ApiResponse.Success<MyReviewResponse.DetailDTO>> getMyCafeteriaReviewDetail(
            @PathVariable Long reviewId) {

        // TODO: JWT 구현완료 시 토큰으로부터 값을 꺼내올 것
        Long memberId = 1L;

        MyReviewResponse.DetailDTO myCafeteriaReview = myReviewService.getMyCafeteriaReview(reviewId, memberId);
        return ApiResponse.success(myCafeteriaReview, HttpStatus.OK);
    }

    @GetMapping("/api/reviews/lunchbox/{reviewId}/members")
    public ResponseEntity<ApiResponse.Success<MyReviewResponse.DetailDTO>> getMyLunchboxReviewDetail(
            @PathVariable Long reviewId) {

        // TODO: JWT 구현완료 시 토큰으로부터 값을 꺼내올 것
        Long memberId = 1L;

        MyReviewResponse.DetailDTO myLunchBoxReview = myReviewService.getMyLunchBoxReview(reviewId, memberId);
        return ApiResponse.success(myLunchBoxReview, HttpStatus.OK);
    }
}

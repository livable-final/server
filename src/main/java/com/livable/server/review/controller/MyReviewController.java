package com.livable.server.review.controller;

import com.livable.server.core.response.ApiResponse;
import com.livable.server.core.util.Actor;
import com.livable.server.core.util.JwtTokenProvider;
import com.livable.server.core.util.LoginActor;
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
            @PathVariable Long reviewId, @LoginActor Actor actor) {

        JwtTokenProvider.checkMemberToken(actor);

        Long memberId = actor.getId();

        MyReviewResponse.DetailDTO myRestaurantReview = myReviewService.getMyRestaurantReview(reviewId, memberId);
        return ApiResponse.success(myRestaurantReview, HttpStatus.OK);
    }

    @GetMapping("/api/reviews/cafeteria/{reviewId}/members")
    public ResponseEntity<ApiResponse.Success<MyReviewResponse.DetailDTO>> getMyCafeteriaReviewDetail(
            @PathVariable Long reviewId, @LoginActor Actor actor) {

        JwtTokenProvider.checkMemberToken(actor);

        Long memberId = actor.getId();

        MyReviewResponse.DetailDTO myCafeteriaReview = myReviewService.getMyCafeteriaReview(reviewId, memberId);
        return ApiResponse.success(myCafeteriaReview, HttpStatus.OK);
    }

    @GetMapping("/api/reviews/lunchbox/{reviewId}/members")
    public ResponseEntity<ApiResponse.Success<MyReviewResponse.DetailDTO>> getMyLunchboxReviewDetail(
            @PathVariable Long reviewId, @LoginActor Actor actor) {

        JwtTokenProvider.checkMemberToken(actor);

        Long memberId = actor.getId();

        MyReviewResponse.DetailDTO myLunchBoxReview = myReviewService.getMyLunchBoxReview(reviewId, memberId);
        return ApiResponse.success(myLunchBoxReview, HttpStatus.OK);
    }
}

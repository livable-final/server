package com.livable.server.review.controller;

import com.livable.server.core.response.ApiResponse;
import com.livable.server.core.response.ApiResponse.Success;
import com.livable.server.core.util.Actor;
import com.livable.server.core.util.JwtTokenProvider;
import com.livable.server.core.util.LoginActor;
import com.livable.server.review.dto.ReviewRequest;
import com.livable.server.review.dto.ReviewResponse;
import com.livable.server.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping(value = "/lunch-box", consumes = "multipart/form-data")
    public ResponseEntity<?> createLunchBoxReview(
            @Valid @RequestPart("data") ReviewRequest.LunchBoxCreateDTO lunchBoxCreateDTO,
            @RequestPart(value = "imageFiles") List<MultipartFile> files,
            @LoginActor Actor actor
            ) throws IOException {

        JwtTokenProvider.checkMemberToken(actor);
        Long memberId = actor.getId();

        reviewService.createLunchBoxReview(lunchBoxCreateDTO, memberId, files);

        return ApiResponse.success(HttpStatus.CREATED);
    }

    @PostMapping(value = "/cafeteria", consumes = "multipart/form-data")
    public ResponseEntity<?> createCafeteriaReview(
            @Valid @RequestPart("data") ReviewRequest.CafeteriaCreateDTO CafeteriaCreateDTO,
            @RequestPart(value = "imageFiles") List<MultipartFile> files,
            @LoginActor Actor actor
    ) throws IOException {

        JwtTokenProvider.checkMemberToken(actor);
        Long memberId = actor.getId();

        reviewService.createCafeteriaReview(CafeteriaCreateDTO, memberId, files);

        return ApiResponse.success(HttpStatus.CREATED);
    }

    @PostMapping(value = "/restaurant", consumes = "multipart/form-data")
    public ResponseEntity<?> createRestaurantReview(
            @Valid @RequestPart("data") ReviewRequest.RestaurantCreateDTO restaurantCreateDTO,
            @RequestPart(value = "imageFiles") List<MultipartFile> files,
            @LoginActor Actor actor
    ) throws IOException {

        JwtTokenProvider.checkMemberToken(actor);
        Long memberId = actor.getId();

        reviewService.createRestaurantReview(restaurantCreateDTO, memberId, files);

        return ApiResponse.success(HttpStatus.CREATED);
    }

    @GetMapping("/members")
    public ResponseEntity<Success<List<ReviewResponse.CalendarListDTO>>> calendarListReview(
        @RequestParam("year") String year,
        @RequestParam("month") String month,
        @LoginActor Actor actor
    ) {

        JwtTokenProvider.checkMemberToken(actor);
        Long memberId = actor.getId();

        List<ReviewResponse.CalendarListDTO> result = reviewService.findCalendarList(memberId, year, month);

        return ApiResponse.success(result, HttpStatus.OK);
    }

    @GetMapping("/detail/members")
    public ResponseEntity<Success<List<ReviewResponse.DetailListDTO>>> findAllReviewDetail(
            @RequestParam Integer year,
            @RequestParam Integer month,
            @LoginActor Actor actor) {

        JwtTokenProvider.checkMemberToken(actor);
        Long memberId = actor.getId();

        List<ReviewResponse.DetailListDTO> result = reviewService.findAllReviewDetailList(memberId, year, month);
        return ApiResponse.success(result, HttpStatus.OK);
    }
}

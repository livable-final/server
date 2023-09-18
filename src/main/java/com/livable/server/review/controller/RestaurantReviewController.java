package com.livable.server.review.controller;

import com.livable.server.core.response.ApiResponse;
import com.livable.server.review.dto.RestaurantReviewResponse;
import com.livable.server.review.service.RestaurantReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/reviews")
public class RestaurantReviewController {

    private final RestaurantReviewService restaurantReviewService;

    @GetMapping("/buildings/{buildingId}")
    public ResponseEntity<ApiResponse.Success<Page<RestaurantReviewResponse.ListDTO>>> list(
            @PathVariable Long buildingId,
            @PageableDefault Pageable pageable) {

        Page<RestaurantReviewResponse.ListDTO> list =
                restaurantReviewService.getAllList(buildingId, pageable);

        return ApiResponse.success(list, HttpStatus.OK);
    }
}
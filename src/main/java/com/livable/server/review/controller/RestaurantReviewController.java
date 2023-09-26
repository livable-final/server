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

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/reviews")
public class RestaurantReviewController {

    private final RestaurantReviewService restaurantReviewService;

    @GetMapping("/buildings/{buildingId}")
    public ResponseEntity<ApiResponse.Success<List<RestaurantReviewResponse.ListForBuildingDTO>>> list(
            @PathVariable Long buildingId,
            @PageableDefault Pageable pageable) {

        List<RestaurantReviewResponse.ListForBuildingDTO> allListForBuilding =
                restaurantReviewService.getAllListForBuilding(buildingId, pageable);

        return ApiResponse.success(allListForBuilding, HttpStatus.OK);
    }

    @GetMapping("/menus/{menuId}")
    public ResponseEntity<ApiResponse.Success<Page<RestaurantReviewResponse.ListForMenuDTO>>> listForMenu(
            @PathVariable Long menuId,
            @PageableDefault Pageable pageable) {

        Page<RestaurantReviewResponse.ListForMenuDTO> allListForMenu =
                restaurantReviewService.getAllListForMenu(menuId, pageable);

        return ApiResponse.success(allListForMenu, HttpStatus.OK);
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ApiResponse.Success<RestaurantReviewResponse.DetailDTO>> detail(@PathVariable Long reviewId) {

        RestaurantReviewResponse.DetailDTO detail = restaurantReviewService.getDetail(reviewId);

        return ApiResponse.success(detail, HttpStatus.OK);
    }
}

package com.livable.server.review.repository;

import com.livable.server.review.dto.Projection;
import com.livable.server.review.dto.RestaurantReviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RestaurantReviewCustomRepository {

    Page<RestaurantReviewResponse.ListDTO> findRestaurantReviewByBuildingId(Long buildingId, Pageable pageable);

    Page<RestaurantReviewResponse.ListForMenuDTO> findRestaurantReviewByMenuId(Long menuId, Pageable pageable);

    List<Projection.RestaurantReview> findRestaurantReviewById(Long reviewId);
}

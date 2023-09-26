package com.livable.server.review.service;

import com.livable.server.core.exception.GlobalRuntimeException;
import com.livable.server.core.util.ImageSeparator;
import com.livable.server.review.domain.MyReviewErrorCode;
import com.livable.server.review.dto.Projection;
import com.livable.server.review.dto.RestaurantReviewProjection;
import com.livable.server.review.dto.RestaurantReviewResponse;
import com.livable.server.review.repository.RestaurantReviewProjectionRepository;
import com.livable.server.review.repository.RestaurantReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RestaurantReviewService {

    private final RestaurantReviewRepository restaurantReviewRepository;
    private final RestaurantReviewProjectionRepository restaurantProjectionRepository;
    private final ImageSeparator imageSeparator;

    @Transactional(readOnly = true)
    public List<RestaurantReviewResponse.ListForBuildingDTO> getAllListForBuilding(Long buildingId, Pageable pageable) {

        List<RestaurantReviewProjection> restaurantReviewLists =
                restaurantProjectionRepository.findRestaurantReviewProjectionByBuildingId(buildingId, pageable);

        return restaurantReviewLists.stream()
                .map(restaurantReviewList ->
                        RestaurantReviewResponse.ListForBuildingDTO.valueOf(restaurantReviewList, imageSeparator))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RestaurantReviewResponse.ListForRestaurantDTO> getAllListForRestaurant(Long restaurantId, Pageable pageable) {

        List<RestaurantReviewProjection> reviewProjections =
                restaurantProjectionRepository.findRestaurantReviewProjectionByRestaurantId(restaurantId, pageable);

        return reviewProjections.stream()
                .map(reviewProjection ->
                        RestaurantReviewResponse.ListForRestaurantDTO.valueOf(reviewProjection, imageSeparator))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<RestaurantReviewResponse.ListForMenuDTO> getAllListForMenu(Long menuId, Pageable pageable) {
        return restaurantReviewRepository.findRestaurantReviewByMenuId(menuId, pageable);
    }

    @Transactional(readOnly = true)
    public RestaurantReviewResponse.DetailDTO getDetail(Long reviewId) {

        List<Projection.RestaurantReview> restaurantReviews
                = restaurantReviewRepository.findRestaurantReviewById(reviewId);

        if (restaurantReviews.isEmpty()) {
            throw new GlobalRuntimeException(MyReviewErrorCode.REVIEW_NOT_EXIST);
        }

        Projection.RestaurantReview restaurantReview = restaurantReviews.get(0);
        List<String> reviewImages = restaurantReviews.stream()
                .map(Projection.RestaurantReview::getImages)
                .collect(Collectors.toList());

        return RestaurantReviewResponse.DetailDTO.from(restaurantReview, reviewImages);
    }
}

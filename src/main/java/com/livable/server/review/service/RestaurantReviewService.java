package com.livable.server.review.service;

import com.livable.server.review.dto.RestaurantReviewResponse;
import com.livable.server.review.repository.RestaurantReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class RestaurantReviewService {

    private final RestaurantReviewRepository restaurantReviewRepository;

    @Transactional(readOnly = true)
    public Page<RestaurantReviewResponse.ListDTO> getAllList(Long buildingId, Pageable pageable) {
        return restaurantReviewRepository.findRestaurantReviewByBuildingId(buildingId, pageable);
    }
}

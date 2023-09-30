package com.livable.server.review.dto;

import com.livable.server.entity.Evaluation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class RestaurantReviewProjection {

    private String memberName;
    private String memberProfileImage;

    private Long restaurantId;
    private String restaurantName;

    private Long reviewId;
    private LocalDateTime reviewCreatedAt;
    private String reviewDescription;
    private Evaluation reviewTaste;
    private Evaluation reviewAmount;
    private Evaluation reviewService;
    private Evaluation reviewSpeed;

    private String images;

    public RestaurantReviewProjection(String memberName,
                                      String memberProfileImage,
                                      Long restaurantId,
                                      String restaurantName,
                                      Long reviewId,
                                      LocalDateTime reviewCreatedAt,
                                      String reviewDescription,
                                      String reviewTaste,
                                      String reviewAmount,
                                      String reviewService,
                                      String reviewSpeed,
                                      String images) {

        this.memberName = memberName;
        this.memberProfileImage = memberProfileImage;
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.reviewId = reviewId;
        this.reviewCreatedAt = reviewCreatedAt;
        this.reviewDescription = reviewDescription;
        this.reviewTaste = Evaluation.valueOf(reviewTaste);
        this.reviewAmount = Evaluation.valueOf(reviewAmount);
        this.reviewService = Evaluation.valueOf(reviewService);
        this.reviewSpeed = Evaluation.valueOf(reviewSpeed);
        this.images = images;
    }
}

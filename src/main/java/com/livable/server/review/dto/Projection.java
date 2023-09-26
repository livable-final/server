package com.livable.server.review.dto;

import com.livable.server.entity.Evaluation;
import lombok.*;

import java.time.LocalDateTime;

public class Projection {

    @Getter
    @Builder
    @AllArgsConstructor
    static public class RestaurantReview {

        private String memberName;

        private Long restaurantId;
        private String restaurantName;

        private LocalDateTime reviewCreatedAt;
        private String reviewDescription;
        private Evaluation reviewTaste;
        private Evaluation reviewAmount;
        private Evaluation reviewService;
        private Evaluation reviewSpeed;

        private String images;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class RestaurantReviewList {

        private Long memberId;
        private String memberName;

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
    }
}

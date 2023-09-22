package com.livable.server.review.dto;

import com.livable.server.entity.Evaluation;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RestaurantReviewResponse {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ListDTO {

        private Long reviewId;
        private LocalDateTime reviewCreatedAt;
        private String reviewDescription;

        private Evaluation reviewTaste;
        private Evaluation reviewAmount;
        private Evaluation reviewService;
        private Evaluation reviewSpeed;

        private Long restaurantId;
        private String restaurantName;

        private Long memberId;
        private String memberName;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ListForMenuDTO {

        private Long reviewId;
        private LocalDateTime reviewCreatedAt;
        private String reviewDescription;

        private Evaluation reviewTaste;
        private Evaluation reviewAmount;
        private Evaluation reviewService;
        private Evaluation reviewSpeed;

        private Long restaurantId;
        private String restaurantName;

        private Long memberId;
        private String memberName;
    }

    @Getter
    @Builder
    public static class DetailDTO {

        private String memberName;

        private Long restaurantId;
        private String restaurantName;

        private LocalDateTime reviewCreatedAt;
        private String reviewDescription;
        private Evaluation reviewTaste;
        private Evaluation reviewAmount;
        private Evaluation reviewService;
        private Evaluation reviewSpeed;

        private List<String> reviewImages;

        public static DetailDTO from(Projection.RestaurantReview restaurantReview, List<String> reviewImages) {
            return DetailDTO.builder()
                    .memberName(restaurantReview.getMemberName())
                    .restaurantId(restaurantReview.getRestaurantId())
                    .restaurantName(restaurantReview.getRestaurantName())
                    .reviewCreatedAt(restaurantReview.getReviewCreatedAt())
                    .reviewDescription(restaurantReview.getReviewDescription())
                    .reviewTaste(restaurantReview.getReviewTaste())
                    .reviewAmount(restaurantReview.getReviewAmount())
                    .reviewService(restaurantReview.getReviewService())
                    .reviewSpeed(restaurantReview.getReviewSpeed())
                    .reviewImages(reviewImages)
                    .build();
        }
    }
}

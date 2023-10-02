package com.livable.server.review.dto;

import com.livable.server.entity.Evaluation;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

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

    @Getter
    @Builder
    @AllArgsConstructor
    public static class AllReviewDetailDTO {

        private Long reviewId;
        private String reviewTitle;
        private Evaluation reviewTaste;
        private String reviewDescription;
        private String reviewCreatedAt;
        private String location;
        private String images;
        private String reviewType;

        public AllReviewDetailDTO(Long reviewId, String reviewTitle, String reviewTaste, String reviewDescription, String reviewCreatedAt, String location, String images, String reviewType) {

            this.reviewId = reviewId;
            this.reviewTitle = reviewTitle;
            this.reviewTaste = Objects.isNull(reviewTaste) ? null : Evaluation.valueOf(reviewTaste);
            this.reviewDescription = reviewDescription;
            this.reviewCreatedAt = reviewCreatedAt;
            this.location = location;
            this.images = images;
            this.reviewType = reviewType;
        }
    }
}

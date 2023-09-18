package com.livable.server.review.dto;

import com.livable.server.entity.Evaluation;
import lombok.*;

import java.time.LocalDateTime;

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
}

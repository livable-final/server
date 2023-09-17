package com.livable.server.review.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RestaurantReviewResponse {

    @Getter
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ListDTO {

        private Long reviewId;
        private LocalDateTime reviewCreatedAt;
        private String reviewDescription;

        private Long restaurantId;
        private String restaurantName;

        private Long memberId;
        private String memberName;
    }
}

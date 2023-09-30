package com.livable.server.review.dto;

import com.livable.server.core.util.ImageSeparator;
import com.livable.server.entity.Evaluation;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RestaurantReviewResponse {

    @Getter
    @Builder
    public static class ListForBuildingDTO {

        private final String memberName;
        private final String memberProfileImage;

        private final Long restaurantId;
        private final String restaurantName;

        private final Long reviewId;
        private final LocalDateTime reviewCreatedAt;
        private final String reviewDescription;
        private final Evaluation reviewTaste;
        private final Evaluation reviewAmount;
        private final Evaluation reviewService;
        private final Evaluation reviewSpeed;

        private List<String> reviewImages;

        public static ListForBuildingDTO valueOf(RestaurantReviewProjection restaurantReviewList, ImageSeparator imageSeparator) {
            return ListForBuildingDTO.builder()
                    .memberName(restaurantReviewList.getMemberName())
                    .memberProfileImage(restaurantReviewList.getMemberProfileImage())
                    .restaurantId(restaurantReviewList.getRestaurantId())
                    .restaurantName(restaurantReviewList.getRestaurantName())
                    .reviewId(restaurantReviewList.getReviewId())
                    .reviewCreatedAt(restaurantReviewList.getReviewCreatedAt())
                    .reviewDescription(restaurantReviewList.getReviewDescription())
                    .reviewTaste(restaurantReviewList.getReviewTaste())
                    .reviewAmount(restaurantReviewList.getReviewAmount())
                    .reviewService(restaurantReviewList.getReviewService())
                    .reviewSpeed(restaurantReviewList.getReviewSpeed())
                    .reviewImages(imageSeparator.separateConcatenatedImages(restaurantReviewList.getImages()))
                    .build();
        }
    }

    @Getter
    @Builder
    public static class ListForRestaurantDTO {

        private final String memberName;
        private final String memberProfileImage;

        private final Long restaurantId;
        private final String restaurantName;

        private final Long reviewId;
        private final LocalDateTime reviewCreatedAt;
        private final String reviewDescription;
        private final Evaluation reviewTaste;
        private final Evaluation reviewAmount;
        private final Evaluation reviewService;
        private final Evaluation reviewSpeed;

        private List<String> reviewImages;

        public static ListForRestaurantDTO valueOf(RestaurantReviewProjection restaurantReviewList, ImageSeparator imageSeparator) {
            return ListForRestaurantDTO.builder()
                    .memberName(restaurantReviewList.getMemberName())
                    .memberProfileImage(restaurantReviewList.getMemberProfileImage())
                    .restaurantId(restaurantReviewList.getRestaurantId())
                    .restaurantName(restaurantReviewList.getRestaurantName())
                    .reviewId(restaurantReviewList.getReviewId())
                    .reviewCreatedAt(restaurantReviewList.getReviewCreatedAt())
                    .reviewDescription(restaurantReviewList.getReviewDescription())
                    .reviewTaste(restaurantReviewList.getReviewTaste())
                    .reviewAmount(restaurantReviewList.getReviewAmount())
                    .reviewService(restaurantReviewList.getReviewService())
                    .reviewSpeed(restaurantReviewList.getReviewSpeed())
                    .reviewImages(imageSeparator.separateConcatenatedImages(restaurantReviewList.getImages()))
                    .build();
        }
    }

    @Getter
    @Builder
    public static class ListForMenuDTO {

        private final String memberName;
        private final String memberProfileImage;

        private final Long restaurantId;
        private final String restaurantName;

        private final Long reviewId;
        private final LocalDateTime reviewCreatedAt;
        private final String reviewDescription;
        private final Evaluation reviewTaste;
        private final Evaluation reviewAmount;
        private final Evaluation reviewService;
        private final Evaluation reviewSpeed;

        private List<String> reviewImages;

        public static ListForMenuDTO valueOf(RestaurantReviewProjection restaurantReviewList, ImageSeparator imageSeparator) {
            return ListForMenuDTO.builder()
                    .memberName(restaurantReviewList.getMemberName())
                    .memberProfileImage(restaurantReviewList.getMemberProfileImage())
                    .restaurantId(restaurantReviewList.getRestaurantId())
                    .restaurantName(restaurantReviewList.getRestaurantName())
                    .reviewId(restaurantReviewList.getReviewId())
                    .reviewCreatedAt(restaurantReviewList.getReviewCreatedAt())
                    .reviewDescription(restaurantReviewList.getReviewDescription())
                    .reviewTaste(restaurantReviewList.getReviewTaste())
                    .reviewAmount(restaurantReviewList.getReviewAmount())
                    .reviewService(restaurantReviewList.getReviewService())
                    .reviewSpeed(restaurantReviewList.getReviewSpeed())
                    .reviewImages(imageSeparator.separateConcatenatedImages(restaurantReviewList.getImages()))
                    .build();
        }
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

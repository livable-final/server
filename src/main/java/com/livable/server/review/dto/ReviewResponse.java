package com.livable.server.review.dto;

import com.livable.server.core.util.ImageSeparator;
import com.livable.server.entity.Evaluation;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

public class ReviewResponse {

    @Getter
    public static class CalendarListDTO {
        private Long reviewId;
        private String type;
        private String reviewImageUrl;
        private LocalDate reviewDate;

        public CalendarListDTO(Long reviewId, String type, String reviewImageUrl, LocalDate reviewDate) {
            this.reviewId = reviewId;
            this.type = type;
            this.reviewImageUrl = reviewImageUrl;
            this.reviewDate = reviewDate;
        }
    }

    @Getter
    @Builder
    public static class DetailListDTO {

        private String reviewTitle;
        private Evaluation reviewTaste;
        private String reviewDescription;
        private String reviewCreatedAt;
        private String location;
        private List<String> images;
        private String reviewType;

        public static DetailListDTO valueOf(Projection.AllReviewDetailDTO detailDTO, ImageSeparator imageSeparator) {
            return DetailListDTO.builder()
                    .reviewTitle(detailDTO.getReviewTitle())
                    .reviewTaste(detailDTO.getReviewTaste())
                    .reviewDescription(detailDTO.getReviewDescription())
                    .reviewCreatedAt(detailDTO.getReviewCreatedAt())
                    .location(detailDTO.getLocation())
                    .images(imageSeparator.separateConcatenatedImages(detailDTO.getImages()))
                    .reviewType(detailDTO.getReviewType())
                    .build();
        }
    }
}

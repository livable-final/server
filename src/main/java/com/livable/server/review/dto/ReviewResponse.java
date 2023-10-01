package com.livable.server.review.dto;

import lombok.Getter;

import java.time.LocalDate;

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
}

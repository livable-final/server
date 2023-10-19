package com.livable.server.review.dto;

import com.livable.server.entity.Evaluation;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MyReviewResponse {

    @Getter
    @Builder
    public static class DetailDTO {

        private String reviewTitle;
        private Evaluation reviewTaste;
        private String reviewDescription;
        private LocalDateTime reviewCreatedAt;
        private List<String> reviewImg;
        private String location;
    }
}

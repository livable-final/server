package com.livable.server.review.dto;

import com.livable.server.entity.Evaluation;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MyReviewProjection {

    private String reviewTitle;
    private Evaluation reviewTaste;
    private String reviewDescription;
    private LocalDateTime reviewCreatedAt;
    private String location;
    private String reviewImg;

    public MyReviewProjection(String reviewTitle, String reviewDescription, LocalDateTime reviewCreatedAt, String reviewImg) {
        this.reviewTitle = reviewTitle;
        this.reviewDescription = reviewDescription;
        this.reviewCreatedAt = reviewCreatedAt;
        this.reviewImg = reviewImg;
    }
}

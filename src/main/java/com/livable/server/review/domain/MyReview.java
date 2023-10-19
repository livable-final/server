package com.livable.server.review.domain;

import com.livable.server.core.exception.GlobalRuntimeException;
import com.livable.server.review.dto.MyReviewProjection;
import com.livable.server.review.dto.MyReviewResponse;

import java.util.List;
import java.util.stream.Collectors;

public class MyReview {

    private final List<MyReviewProjection> reviews;

    private MyReview(List<MyReviewProjection> reviews) {
        validationReviews(reviews);
        this.reviews = reviews;
    }

    private void validationReviews(List<MyReviewProjection> reviews) {
        if (reviews.isEmpty()) {
            throw new GlobalRuntimeException(MyReviewErrorCode.REVIEW_NOT_EXIST);
        }
    }

    public static MyReview from(List<MyReviewProjection> reviews) {
        return new MyReview(reviews);
    }

    public MyReviewResponse.DetailDTO toResponseDTO() {

        MyReviewProjection myReviewDTO = this.getTopOne();
        List<String> images = this.getImages();

        return MyReviewResponse.DetailDTO.builder()
                .reviewTitle(myReviewDTO.getReviewTitle())
                .reviewTaste(myReviewDTO.getReviewTaste())
                .reviewDescription(myReviewDTO.getReviewDescription())
                .reviewCreatedAt(myReviewDTO.getReviewCreatedAt())
                .location(myReviewDTO.getLocation())
                .reviewImg(images)
                .build();
    }

    private MyReviewProjection getTopOne() {
        return reviews.get(0);
    }

    private List<String> getImages() {
        return reviews.stream()
                .map(MyReviewProjection::getReviewImg)
                .collect(Collectors.toList());
    }
}

package com.livable.server.review.service;

import com.livable.server.review.domain.MyReview;
import com.livable.server.review.dto.MyReviewProjection;
import com.livable.server.review.dto.MyReviewResponse;
import com.livable.server.review.repository.MyReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MyReviewService {

    private final MyReviewRepository myReviewRepository;

    public MyReviewResponse.DetailDTO getMyRestaurantReview(Long reviewId, Long memberId) {

        List<MyReviewProjection> myReviewProjections
                = myReviewRepository.findRestaurantReviewByReviewId(reviewId, memberId);

        return this.convertToDTO(myReviewProjections);
    }

    public MyReviewResponse.DetailDTO getMyCafeteriaReview(Long reviewId, Long memberId) {

        List<MyReviewProjection> myReviewProjections
                = myReviewRepository.findCafeteriaReviewByReviewId(reviewId, memberId);

        return this.convertToDTO(myReviewProjections);
    }

    public MyReviewResponse.DetailDTO getMyLunchBoxReview(Long reviewId, Long memberId) {

        List<MyReviewProjection> myReviewProjections
                = myReviewRepository.findLunchBoxReviewByReviewId(reviewId, memberId);

        return this.convertToDTO(myReviewProjections);
    }

    private MyReviewResponse.DetailDTO convertToDTO(List<MyReviewProjection> myReviewProjections) {

        MyReview myReview = MyReview.from(myReviewProjections);
        return myReview.toResponseDTO();
    }
}

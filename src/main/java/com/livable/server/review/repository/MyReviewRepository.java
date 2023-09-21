package com.livable.server.review.repository;

import com.livable.server.review.dto.MyReviewProjection;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MyReviewRepository {

    List<MyReviewProjection> findRestaurantReviewByReviewId(Long reviewId, Long memberId);

    List<MyReviewProjection> findLunchBoxReviewByReviewId(Long reviewId, Long memberId);

    List<MyReviewProjection> findCafeteriaReviewByReviewId(Long reviewId, Long memberId);
}

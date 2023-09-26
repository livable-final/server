package com.livable.server.review.repository;

import com.livable.server.entity.RestaurantReview;
import com.livable.server.review.repository.querydsl.RestaurantReviewQueryDslRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantReviewRepository
        extends JpaRepository<RestaurantReview, Long>, RestaurantReviewQueryDslRepository {
}

package com.livable.server.review.repository;

import com.livable.server.entity.RestaurantReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantReviewRepository extends JpaRepository<RestaurantReview, Long>, RestaurantReviewCustomRepository {

}

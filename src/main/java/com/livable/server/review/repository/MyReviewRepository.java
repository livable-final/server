package com.livable.server.review.repository;

import com.livable.server.entity.Review;
import com.livable.server.review.repository.querydsl.MyReviewQueryDslRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyReviewRepository
        extends JpaRepository<Review, Long>, MyReviewQueryDslRepository {
}

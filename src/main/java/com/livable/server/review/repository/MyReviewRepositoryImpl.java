package com.livable.server.review.repository;

import com.livable.server.review.dto.MyReviewProjection;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.livable.server.entity.QBuilding.building;
import static com.livable.server.entity.QCafeteriaReview.cafeteriaReview;
import static com.livable.server.entity.QRestaurant.restaurant;
import static com.livable.server.entity.QRestaurantReview.restaurantReview;
import static com.livable.server.entity.QReview.review;
import static com.livable.server.entity.QReviewImage.reviewImage;

@Component
@RequiredArgsConstructor
public class MyReviewRepositoryImpl implements MyReviewRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<MyReviewProjection> findRestaurantReviewByReviewId(Long reviewId, Long memberId) {
        return jpaQueryFactory
                .select(Projections.constructor(MyReviewProjection.class,
                        review.selectedDishes,
                        restaurantReview.taste,
                        review.description,
                        review.createdAt,
                        restaurant.name,
                        reviewImage.url
                ))
                .from(review)
                .leftJoin(reviewImage).on(reviewImage.review.id.eq(review.id))
                .innerJoin(restaurantReview).on(restaurantReview.id.eq(review.id))
                .innerJoin(restaurant).on(restaurant.id.eq(restaurantReview.restaurant.id))
                .where(review.id.eq(reviewId).and(review.member.id.eq(memberId)))
                .fetch();
    }

    @Override
    public List<MyReviewProjection> findLunchBoxReviewByReviewId(Long reviewId, Long memberId) {
        return jpaQueryFactory
                .select(Projections.constructor(MyReviewProjection.class,
                        review.selectedDishes,
                        review.description,
                        review.createdAt,
                        reviewImage.url
                ))
                .from(review)
                .leftJoin(reviewImage).on(reviewImage.review.id.eq(review.id))
                .where(review.id.eq(reviewId).and(review.member.id.eq(memberId)))
                .fetch();
    }

    @Override
    public List<MyReviewProjection> findCafeteriaReviewByReviewId(Long reviewId, Long memberId) {
        return jpaQueryFactory
                .select(Projections.constructor(MyReviewProjection.class,
                        review.selectedDishes,
                        cafeteriaReview.taste,
                        review.description,
                        review.createdAt,
                        building.name,
                        reviewImage.url
                ))
                .from(review)
                .leftJoin(reviewImage).on(reviewImage.review.id.eq(review.id))
                .innerJoin(cafeteriaReview).on(cafeteriaReview.id.eq(review.id))
                .innerJoin(building).on(building.id.eq(cafeteriaReview.building.id))
                .where(review.id.eq(reviewId).and(review.member.id.eq(memberId)))
                .fetch();
    }
}

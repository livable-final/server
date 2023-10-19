package com.livable.server.review.repository.querydsl;

import com.livable.server.review.dto.Projection;
import com.livable.server.review.dto.RestaurantReviewResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.livable.server.entity.QMember.member;
import static com.livable.server.entity.QRestaurant.restaurant;
import static com.livable.server.entity.QRestaurantReview.restaurantReview;
import static com.livable.server.entity.QReview.review;
import static com.livable.server.entity.QReviewImage.reviewImage;
import static com.livable.server.entity.QReviewMenuMap.reviewMenuMap;

@RequiredArgsConstructor
public class RestaurantReviewQueryDslRepositoryImpl implements RestaurantReviewQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<RestaurantReviewResponse.ListForMenuDTO> findRestaurantReviewByMenuId(Long menuId, Pageable pageable) {

        JPAQuery<RestaurantReviewResponse.ListForMenuDTO> query = queryFactory
                .select(Projections.constructor(RestaurantReviewResponse.ListForMenuDTO.class,
                        review.id,
                        review.createdAt,
                        review.description,
                        restaurantReview.taste,
                        restaurantReview.amount,
                        restaurantReview.service,
                        restaurantReview.speed,
                        restaurantReview.restaurant.id,
                        restaurant.name,
                        review.member.id,
                        member.name
                ))
                .from(review)
                .innerJoin(restaurantReview).on(restaurantReview.id.eq(review.id))
                .innerJoin(member).on(review.member.id.eq(member.id))
                .innerJoin(restaurant).on(restaurantReview.restaurant.id.eq(restaurant.id))
                .where(review.id.in(
                        JPAExpressions
                                .select(reviewMenuMap.review.id)
                                .from(reviewMenuMap)
                                .where(reviewMenuMap.menu.id.eq(menuId))
                ))
                .orderBy(review.createdAt.desc());

        List<RestaurantReviewResponse.ListForMenuDTO> content = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchJoin().fetch();

        long total = query.fetchCount();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public List<Projection.RestaurantReview> findRestaurantReviewById(Long reviewId) {
        return queryFactory
                .select(Projections.constructor(Projection.RestaurantReview.class,
                        member.name,
                        restaurant.id,
                        restaurant.name,
                        review.createdAt,
                        review.description,
                        restaurantReview.taste,
                        restaurantReview.amount,
                        restaurantReview.service,
                        restaurantReview.speed,
                        reviewImage.url
                ))
                .from(review)
                .innerJoin(restaurantReview).on(restaurantReview.id.eq(review.id))
                .innerJoin(member).on(member.id.eq(review.member.id))
                .innerJoin(restaurant).on(restaurant.id.eq(restaurantReview.restaurant.id))
                .leftJoin(reviewImage).on(reviewImage.review.id.eq(review.id))
                .where(review.id.eq(reviewId))
                .fetch();
    }
}

package com.livable.server.review.repository;

import com.livable.server.entity.*;
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

@RequiredArgsConstructor
public class RestaurantReviewCustomRepositoryImpl implements RestaurantReviewCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<RestaurantReviewResponse.ListDTO> findRestaurantReviewByBuildingId(Long buildingId, Pageable pageable) {

        final QReview review = QReview.review;
        final QRestaurantReview restaurantReview = QRestaurantReview.restaurantReview;
        final QMember member = QMember.member;
        final QRestaurant restaurant = QRestaurant.restaurant;
        final QBuildingRestaurantMap buildingRestaurantMap = QBuildingRestaurantMap.buildingRestaurantMap;

        JPAQuery<RestaurantReviewResponse.ListDTO> query = queryFactory
                .select(Projections.constructor(RestaurantReviewResponse.ListDTO.class,
                        review.id,
                        review.createdAt,
                        review.description,
                        restaurantReview.restaurant.id,
                        restaurant.name,
                        review.member.id,
                        member.name
                ))
                .from(review)
                .innerJoin(restaurantReview).on(review.id.eq(restaurantReview.id))
                .innerJoin(member).on(review.member.id.eq(member.id))
                .innerJoin(restaurant).on(restaurantReview.restaurant.id.eq(restaurant.id))
                .where(restaurantReview.restaurant.id.in(
                        JPAExpressions
                                .select(buildingRestaurantMap.restaurant.id)
                                .from(buildingRestaurantMap)
                                .where(buildingRestaurantMap.building.id.eq(buildingId))
                ))
                .orderBy(review.createdAt.desc());

        List<RestaurantReviewResponse.ListDTO> content = query.offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchJoin().fetch();

        long total = query.fetchCount();

        return new PageImpl<>(content, pageable, total);
    }
}

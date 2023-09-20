package com.livable.server.restaurant.repository;

import com.livable.server.entity.*;
import com.livable.server.restaurant.dto.RestaurantResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RestaurantCustomRepositoryImpl implements RestaurantCustomRepository {

    private final JPAQueryFactory queryFactory;
    private static final int DISTANCE_PER_TIME = 80;
    private static final int DEFAULT_SIZE = 5;

    @Override
    public List<RestaurantResponse.NearRestaurantDto> findRestaurantByBuildingIdAndRestaurantCategory(
            final Long buildingId,
            final RestaurantCategory category,
            final Pageable pageable
    ) {

        final QRestaurant restaurant = QRestaurant.restaurant;
        final QBuildingRestaurantMap buildingRestaurantMap = QBuildingRestaurantMap.buildingRestaurantMap;
        final QVisitor visitor = QVisitor.visitor;
        final QBuilding building = QBuilding.building;

        StringExpression extractFloorFromAddressTemplate = Expressions.stringTemplate(
                "REPLACE(REPLACE({0}, '지하', '-'), '층', '')",
                building.address.substring(building.address.indexOf(" ", -1))
        );

        JPAQuery<RestaurantResponse.NearRestaurantDto> query = queryFactory
                .selectDistinct(Projections.constructor(RestaurantResponse.NearRestaurantDto.class,
                        restaurant.restaurantCategory,
                        restaurant.name,
                        restaurant.thumbnailImageUrl,
                        buildingRestaurantMap.inBuilding,
                        new CaseBuilder()
                                .when(buildingRestaurantMap.inBuilding.eq(true))
                                .then(0)
                                .otherwise(buildingRestaurantMap.distance
                                        .divide(DISTANCE_PER_TIME)
                                        .round()
                                        .castToNum(Integer.class)
                                ),
                        new CaseBuilder()
                                .when(buildingRestaurantMap.inBuilding.eq(false))
                                .then(0)
                                .otherwise(
                                        extractFloorFromAddressTemplate
                                                .castToNum(Integer.class)
                                ),
                        restaurant.restaurantUrl
                ))
                .from(visitor)
                .from(building)
                .innerJoin(buildingRestaurantMap).on(buildingRestaurantMap.building.id.eq(building.id))
                .innerJoin(restaurant).on(buildingRestaurantMap.restaurant.id.eq(restaurant.id))
                .where(building.id.eq(buildingId).and(restaurant.restaurantCategory.eq(category)))
                .offset(pageable.getPageNumber())
                .limit(pageable.getPageSize());

        return query.fetchJoin().fetch();
    }
}

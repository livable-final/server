package com.livable.server.restaurant.repository;

import com.livable.server.entity.*;
import com.livable.server.restaurant.dto.RestaurantResponse;
import com.livable.server.restaurant.dto.RestaurantResponse.ListMenuDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.livable.server.entity.QBuilding.building;
import static com.livable.server.entity.QBuildingRestaurantMap.buildingRestaurantMap;
import static com.livable.server.entity.QMenu.menu;
import static com.livable.server.entity.QRestaurant.restaurant;
import static com.livable.server.entity.QRestaurantMenuMap.restaurantMenuMap;


@Repository
@RequiredArgsConstructor
public class RestaurantCustomRepositoryImpl implements RestaurantCustomRepository {

    private final JPAQueryFactory queryFactory;
    private static final int DISTANCE_PER_TIME = 80;

    @Override
    public List<RestaurantResponse.NearRestaurantDto> findRestaurantByBuildingIdAndRestaurantCategory(
            final Long buildingId,
            final RestaurantCategory category,
            final Pageable pageable
    ) {

        final QRestaurant restaurant = QRestaurant.restaurant;
        final QBuildingRestaurantMap buildingRestaurantMap = QBuildingRestaurantMap.buildingRestaurantMap;
        final QBuilding building = QBuilding.building;

        StringExpression extractFloorFromAddressTemplate = Expressions.stringTemplate(
                "REPLACE(REPLACE(SUBSTRING_INDEX({0}, ' ', -1), '지하', '-'), '층', '')",
                restaurant.address
        );


        JPAQuery<RestaurantResponse.NearRestaurantDto> query = queryFactory
                .select(Projections.constructor(RestaurantResponse.NearRestaurantDto.class,
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
                .from(building)
                .innerJoin(buildingRestaurantMap).on(buildingRestaurantMap.building.id.eq(building.id))
                .innerJoin(restaurant).on(buildingRestaurantMap.restaurant.id.eq(restaurant.id))
                .where(building.id.eq(buildingId).and(restaurant.restaurantCategory.eq(category)))
                .offset(pageable.getPageNumber())
                .limit(pageable.getPageSize());

        return query.fetchJoin().fetch();
    }

    @Override
    public List<ListMenuDTO> findMenuList(Long restaurantId) {

        final QMenu menu = QMenu.menu;
        final QRestaurantMenuMap restaurantMenuMap = QRestaurantMenuMap.restaurantMenuMap;

        JPAQuery<ListMenuDTO> query = queryFactory
                .select(Projections.constructor(ListMenuDTO.class,
                        menu.id,
                        menu.name
                        ))
                .from(menu)
                .innerJoin(restaurantMenuMap)
                .on(menu.id.eq(restaurantMenuMap.menu.id))
                .where(restaurantMenuMap.restaurant.id.eq(restaurantId));

        return query.fetch();
    }

    @Override
    public List<RestaurantResponse.SearchRestaurantsDTO> findRestaurantByKeyword(Long buildingId, String keyword) {

        // TODO : 성능테스트 필요

//        List<Long> subQuery = queryFactory
//                .select(restaurantMenuMap.restaurant.id)
//                .from(menu)
//                .innerJoin(restaurantMenuMap).on(menu.id.eq(restaurantMenuMap.menu.id))
//                .where(menu.name.contains(keyword)
//                        .or(restaurant.name.contains(keyword)))
//                .fetch();

        return queryFactory
                .select(Projections.constructor(RestaurantResponse.SearchRestaurantsDTO.class,
                        restaurant.id,
                        restaurant.name,
                        restaurant.restaurantCategory,
                        buildingRestaurantMap.inBuilding,
                        restaurant.thumbnailImageUrl,
                        buildingRestaurantMap.distance,
                        restaurant.address
                ))
                .from(building)
                .innerJoin(buildingRestaurantMap).on(buildingRestaurantMap.building.id.eq(building.id))
                .innerJoin(restaurant).on(restaurant.id.eq(buildingRestaurantMap.restaurant.id))
                .where(
                        restaurant.id.in(
                                JPAExpressions
                                        .select(restaurantMenuMap.restaurant.id)
                                        .from(menu)
                                        .innerJoin(restaurantMenuMap).on(menu.id.eq(restaurantMenuMap.menu.id))
                                        .where(menu.name.contains(keyword)
                                                .or(restaurant.name.contains(keyword)))
                        )
                )
                .fetch();
    }
}

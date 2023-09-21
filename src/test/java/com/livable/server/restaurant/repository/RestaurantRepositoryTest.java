package com.livable.server.restaurant.repository;

import com.livable.server.core.config.QueryDslConfig;
import com.livable.server.entity.Building;
import com.livable.server.entity.BuildingRestaurantMap;
import com.livable.server.entity.Restaurant;
import com.livable.server.entity.RestaurantCategory;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;
import java.time.LocalTime;
import java.util.stream.IntStream;

@DataJpaTest
@Import(QueryDslConfig.class)
class RestaurantRepositoryTest {

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    EntityManager entityManager;

    @Autowired
    BuildingRestaurantMapRepository buildingRestaurantMapRepository;

    @BeforeEach
    void dataInit() {
        Building building = Building.builder()
                .name("63빌딩")
                .scale("지하 3층, 지상 63층")
                .representativeImageUrl("./thumbnailImage.jpg")
                .endTime(LocalTime.of(10, 30))
                .startTime(LocalTime.of(18, 30))
                .parkingCostInformation("10분당 1000원")
                .longitude("10.10.10.10")
                .latitude("123.123.123")
                .hasCafeteria(false)
                .address("서울시 강남구 서초대로 61길 7, 392")
                .subwayStation("석촌역")
                .build();

        entityManager.persist(building);

        IntStream.range(1, 10)
                .forEach(idx -> {
                    Restaurant restaurant = Restaurant.builder()
                            .name("restaurant" + idx)
                            .address("서울시 강동구 태윤빌딩 " + (idx % 2 == 0 ? "지하" : "") + idx + "층")
                            .contact("contact" + idx)
                            .restaurantCategory(idx % 3 == 0 ? RestaurantCategory.CAFE : RestaurantCategory.RESTAURANT)
                            .restaurantUrl("url" + idx)
                            .thumbnailImageUrl("thumbnail" + idx)
                            .representativeCategory("ui category" + idx)
                            .build();
                    restaurantRepository.save(restaurant);
                    BuildingRestaurantMap restaurantMap = BuildingRestaurantMap.builder()
                            .restaurant(restaurant)
                            .building(building)
                            .inBuilding(idx % 2 == 0)
                            .distance(idx * 80)
                            .build();
                    buildingRestaurantMapRepository.save(restaurantMap);
                });
    }
}
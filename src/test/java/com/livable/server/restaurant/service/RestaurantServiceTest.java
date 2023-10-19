package com.livable.server.restaurant.service;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.livable.server.core.exception.GlobalRuntimeException;
import com.livable.server.entity.RestaurantCategory;
import com.livable.server.restaurant.domain.RandomGenerator;
import com.livable.server.restaurant.dto.RestaurantByMenuProjection;
import com.livable.server.restaurant.dto.RestaurantResponse;
import com.livable.server.restaurant.dto.RestaurantResponse.RestaurantsDto;
import com.livable.server.restaurant.repository.BuildingRestaurantMapRepository;
import com.livable.server.restaurant.repository.RestaurantGroupByMenuProjectionRepository;
import com.livable.server.restaurant.repository.RestaurantRepository;
import com.livable.server.visitation.domain.VisitationErrorCode;
import com.livable.server.visitation.repository.VisitorRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

    @InjectMocks
    RestaurantService restaurantService;

    @Mock
    RandomGenerator<Pageable> randomGenerator;
    @Mock
    RestaurantRepository restaurantRepository;
    @Mock
    RestaurantGroupByMenuProjectionRepository restaurantGroupByMenuProjectionRepository;
    @Mock
    VisitorRepository visitorRepository;
    @Mock
    BuildingRestaurantMapRepository buildingRestaurantMapRepository;

    @DisplayName("RestaurantService.findNearRestaurantByVisitorIdAndRestaurantCategory 성공 테스트")
    @Test
    void findNearRestaurantByVisitorIdAndRestaurantCategorySuccessTest() {
        // given
        Long visitorId = 1L;
        RestaurantCategory category = RestaurantCategory.RESTAURANT;
        List<RestaurantResponse.NearRestaurantDto> dtos = IntStream.range(0, 5)
                .mapToObj(idx -> new RestaurantResponse.NearRestaurantDto())
                .collect(Collectors.toList());

        Pageable pageRequest = PageRequest.of(1, 5);

        given(visitorRepository.findBuildingIdById(anyLong())).willReturn(Optional.of(1L));
        given(buildingRestaurantMapRepository.countBuildingRestaurantMapByBuildingIdAndRestaurant_RestaurantCategory(
                anyLong(),
                any(RestaurantCategory.class)
        )).willReturn(10);
        given(randomGenerator.getRandom(anyInt())).willReturn(pageRequest);
        given(restaurantRepository.findRestaurantByBuildingIdAndRestaurantCategory(
                anyLong(), any(RestaurantCategory.class), any(Pageable.class)
        )).willReturn(dtos);

        // when
        List<RestaurantResponse.NearRestaurantDto> result =
                restaurantService.findNearRestaurantByVisitorIdAndRestaurantCategory(visitorId, category);

        // then
        then(visitorRepository).should(times(1)).findBuildingIdById(anyLong());
        then(buildingRestaurantMapRepository).should(times(1))
                .countBuildingRestaurantMapByBuildingIdAndRestaurant_RestaurantCategory(
                        anyLong(),
                        any(RestaurantCategory.class)
                );
        then(randomGenerator).should(times(1)).getRandom(anyInt());
        then(restaurantRepository).should(times(1))
                .findRestaurantByBuildingIdAndRestaurantCategory(
                        anyLong(),
                        any(RestaurantCategory.class),
                        any(Pageable.class)
                );
        assertThat(dtos).usingRecursiveComparison().isEqualTo(result);
    }

    @DisplayName("RestaurantService.findNearRestaurantByVisitorIdAndRestaurantCategory 실패 테스트")
    @Test
    void findNearRestaurantByVisitorIdAndRestaurantCategoryFailTest() {
        // given
        Long visitorId = 1L;
        RestaurantCategory category = RestaurantCategory.RESTAURANT;
        List<RestaurantResponse.NearRestaurantDto> dtos = IntStream.range(0, 5)
                .mapToObj(idx -> new RestaurantResponse.NearRestaurantDto())
                .collect(Collectors.toList());

        given(visitorRepository.findBuildingIdById(anyLong())).willReturn(Optional.empty());

        // when
        GlobalRuntimeException globalRuntimeException = assertThrows(
                GlobalRuntimeException.class,
                () -> restaurantService.findNearRestaurantByVisitorIdAndRestaurantCategory(visitorId, category)
        );

        // then
        assertThat(globalRuntimeException.getErrorCode()).isEqualTo(VisitationErrorCode.NOT_FOUND);
        then(visitorRepository).should(times(1)).findBuildingIdById(anyLong());
    }

    @DisplayName("SUCCESS - 메뉴별 식당 목록 응답 서비스 테스트")
    @Test
    void findRestaurantByMenuSuccess() {

        //given
        Long restaurantId = 1L;
        String restaurantName = "레스토랑";
        String restaurantThumbnailUrl = "/restaurantImg.com";
        String address ="address for restaurant";
        Boolean inBuilding = true;
        Integer distance = 100;
        String review = "this is review";
        Integer tastePercentage = 30;

        List<RestaurantByMenuProjection> projections = List.of(
            new RestaurantByMenuProjection(restaurantId, restaurantName, restaurantThumbnailUrl,
                address, inBuilding, distance, review, tastePercentage)
        );

        given(restaurantGroupByMenuProjectionRepository.findRestaurantByMenuId(anyLong(), anyLong()))
            .willReturn(projections);

        // when
        List<RestaurantsDto> actual =
            restaurantService.findRestaurantByMenuId(anyLong(), anyLong());

        // then
        assertAll(
            () -> assertEquals(restaurantId, actual.get(0).getRestaurantId()),
            () -> assertEquals(restaurantName, actual.get(0).getRestaurantName()),
            () -> assertEquals(restaurantThumbnailUrl, actual.get(0).getRepresentativeImageUrl()),
            () -> assertEquals(address, actual.get(0).getAddress()),
            () -> assertEquals(inBuilding, actual.get(0).getInBuilding()),
            () -> assertEquals(distance / 80, actual.get(0).getEstimatedTime()),
            () -> assertEquals(review, actual.get(0).getReview()),
            () -> assertEquals(tastePercentage, actual.get(0).getTastePercentage())
        );

    }

    @DisplayName("FAIELD - 메뉴별 식당 목록 응답 서비스 테스트")
    @Test
    void findRestaurantByMenuFailed() {

        // given


        // when
        given(restaurantGroupByMenuProjectionRepository.findRestaurantByMenuId(anyLong(), anyLong()))
            .willReturn(new ArrayList<>());

        // then
        Assertions.assertThrows(GlobalRuntimeException.class, () ->
            restaurantService.findRestaurantByMenuId(1L, 1L));

    }

}
package com.livable.server.restaurant.service;

import com.livable.server.core.exception.GlobalRuntimeException;
import com.livable.server.entity.RestaurantCategory;
import com.livable.server.restaurant.domain.RandomGenerator;
import com.livable.server.restaurant.dto.RestaurantResponse;
import com.livable.server.restaurant.repository.BuildingRestaurantMapRepository;
import com.livable.server.restaurant.repository.RestaurantRepository;
import com.livable.server.visitation.domain.VisitationErrorCode;
import com.livable.server.visitation.repository.VisitorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

    @InjectMocks
    RestaurantService restaurantService;

    @Mock
    RandomGenerator<Pageable> randomGenerator;
    @Mock
    RestaurantRepository restaurantRepository;
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
}
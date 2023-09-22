package com.livable.server.core.util;

import com.livable.server.core.exception.GlobalRuntimeException;
import com.livable.server.entity.RestaurantCategory;
import com.livable.server.restaurant.domain.RestaurantErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class StringToRestaurantCategoryConverterTest {

    public static StringToRestaurantCategoryConverter converter = new StringToRestaurantCategoryConverter();

    @DisplayName("StringToRestaurantConverter 성공 테스트_1")
    @CsvSource({"RESTAURANT", "restaurant", "Restaurant"})
    @ParameterizedTest(name = "[{index}] 입력문자: {0}")
    void convertSuccessTest_RESTAURANT(String symbol) {

        // Given
        // When
        RestaurantCategory restaurantCategory = converter.convert(symbol);

        // Then
        assertThat(restaurantCategory).isEqualTo(RestaurantCategory.RESTAURANT);
    }

    @DisplayName("StringToRestaurantConverter 성공 테스트_2")
    @CsvSource({"CAFE", "cafe", "Cafe"})
    @ParameterizedTest(name = "[{index}] 입력문자: {0}")
    void convertSuccessTest_CAFE(String symbol) {

        // Given
        // When
        RestaurantCategory restaurantCategory = converter.convert(symbol);

        // Then
        assertThat(restaurantCategory).isEqualTo(RestaurantCategory.CAFE);
    }

    @DisplayName("StringToRestaurantConverter 실패 테스트")
    @CsvSource({"CAFe", "caFe", "CAfe", "123", "zz", "restAuRant"})
    @ParameterizedTest(name = "[{index}] 입력문자: {0}")
    void convertFailTest(String symbol) {

        // Given
        // When
        GlobalRuntimeException globalRuntimeException =
                assertThrows(GlobalRuntimeException.class, () -> converter.convert(symbol));

        // Then
        assertThat(globalRuntimeException.getErrorCode()).isEqualTo(RestaurantErrorCode.NOT_FOUND_CATEGORY);
    }
}
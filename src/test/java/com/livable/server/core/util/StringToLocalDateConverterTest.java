package com.livable.server.core.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class StringToLocalDateConverterTest {

    @DisplayName("StringToLocalDateConverter.convert 성공 테스트")
    @Test
    void convertSuccessTest() {
        StringToLocalDateConverter converter = new StringToLocalDateConverter();

        String query = "2023-04-23";

        assertThat(LocalDate.of(2023, 4, 23)).isEqualTo(converter.convert(query));
    }
}
package com.livable.server.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ParkingLogTest {

    @DisplayName("ParkingLog.create 성공 테스트")
    @Test
    void createSuccessTest() {
        Visitor visitor = Visitor.builder()
                .build();
        String carNumber = "12가3456";

        ParkingLog parkingLog = ParkingLog.create(visitor, carNumber);

        assertThat(parkingLog.getVisitor()).isEqualTo(visitor);
        assertThat(parkingLog.getCarNumber()).isEqualTo(carNumber);
    }

}
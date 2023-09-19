package com.livable.server.visitation.mock;

import com.livable.server.visitation.dto.VisitationRequest;

public class MockRegisterParkingDto extends VisitationRequest.RegisterParkingDto {

    private final String carNumber;

    public MockRegisterParkingDto(String carNumber) {
        this.carNumber = carNumber;
    }

    @Override
    public String getCarNumber() {
        return carNumber;
    }
}

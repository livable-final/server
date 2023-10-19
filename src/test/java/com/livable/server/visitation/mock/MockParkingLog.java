package com.livable.server.visitation.mock;

import com.livable.server.entity.ParkingLog;
import com.livable.server.entity.Visitor;

import java.time.LocalDateTime;

public class MockParkingLog extends ParkingLog {

    public MockParkingLog(Long id, Visitor visitor, String carNumber, LocalDateTime inTime, LocalDateTime outTime, Integer stayTime) {
        super(id, visitor, carNumber, inTime, outTime, stayTime);
    }
}

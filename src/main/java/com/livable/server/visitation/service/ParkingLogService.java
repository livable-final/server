package com.livable.server.visitation.service;

import com.livable.server.entity.ParkingLog;
import com.livable.server.entity.Visitor;
import com.livable.server.visitation.repository.ParkingLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ParkingLogService {

    private final ParkingLogRepository parkingLogRepository;

    public Optional<ParkingLog> findParkingLogByVisitorId(Long visitorId) {
        return parkingLogRepository.findParkingLogByVisitorId(visitorId);
    }

    public void registerParkingLog(Visitor visitor, String carNumber) {
        ParkingLog parkingLog = ParkingLog.builder()
                .carNumber(carNumber)
                .visitor(visitor)
                .build();

        parkingLogRepository.save(parkingLog);
    }
}

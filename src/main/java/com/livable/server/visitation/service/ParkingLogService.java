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

    public Optional<ParkingLog> findParkingLogByVisitorId(final Long visitorId) {
        return parkingLogRepository.findParkingLogByVisitorId(visitorId);
    }

    public Optional<String> findCarNumberByVisitorId(final Long visitorId) {
        return parkingLogRepository.findCarNumberByVisitorId(visitorId);
    }

    public void registerParkingLog(final Visitor visitor, final String carNumber) {
        ParkingLog parkingLog = ParkingLog.create(visitor, carNumber);

        parkingLogRepository.save(parkingLog);
    }
}

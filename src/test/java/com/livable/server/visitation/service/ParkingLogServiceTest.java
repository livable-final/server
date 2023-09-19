package com.livable.server.visitation.service;

import com.livable.server.entity.ParkingLog;
import com.livable.server.entity.Visitor;
import com.livable.server.visitation.repository.ParkingLogRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ParkingLogServiceTest {

    @InjectMocks
    ParkingLogService parkingLogService;

    @Mock
    ParkingLogRepository parkingLogRepository;

    @DisplayName("ParkingLogService.findParkingLogByVisitorId 성공 테스트")
    @Test
    void findParkingLogByVisitorIdSuccessTest() {

        // Given
        ParkingLog parkingLog = ParkingLog.builder()
                .build();

        Optional<ParkingLog> expectedOptionalParkingLog = Optional.of(parkingLog);

        given(parkingLogRepository.findParkingLogByVisitorId(anyLong())).willReturn(Optional.of(parkingLog));

        // When
        Optional<ParkingLog> optionalParkingLog = parkingLogService.findParkingLogByVisitorId(1L);

        // Then
        assertThat(optionalParkingLog).isEqualTo(expectedOptionalParkingLog);
        then(parkingLogRepository).should(times(1)).findParkingLogByVisitorId(anyLong());
    }

    @DisplayName("ParkingLogService.registerParkingLog 성공 테스트")
    @Test
    void registerParkingLogSuccessTest() {
        ParkingLog parkingLog = ParkingLog.builder()
                .build();

        Visitor visitor = Visitor.builder()
                .build();

        String carNumber = "testCarNumber";

        // Given
        given(parkingLogRepository.save(any())).willReturn(parkingLog);

        // When
        parkingLogService.registerParkingLog(visitor, carNumber);

        // Then
        then(parkingLogRepository).should(times(1)).save(any());
    }
}
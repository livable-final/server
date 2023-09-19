package com.livable.server.visitation.repository;

import com.livable.server.entity.ParkingLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ParkingLogRepository extends JpaRepository<ParkingLog, Long> {

    @Query("select p from ParkingLog p" +
            " where p.visitor.id = :visitorId")
    Optional<ParkingLog> findParkingLogByVisitorId(@Param("visitorId") Long visitorId);
}

package com.livable.server.visitation.repository;

import com.livable.server.entity.ParkingLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ParkingLogRepository extends JpaRepository<ParkingLog, Long> {

    @Query("select p from ParkingLog p" +
            " where p.visitor.id = :visitorId")
    Optional<ParkingLog> findParkingLogByVisitorId(@Param("visitorId") final Long visitorId);

    @Modifying
    @Query("delete from ParkingLog p where p.visitor.id in :visitorIds")
    void deleteByVisitorIdsIn(@Param("visitorIds") List<Long> visitorIds);

    @Query("select p.carNumber from ParkingLog p where p.visitor.id in :visitorId")
    Optional<String> findCarNumberByVisitorId(@Param("visitorId") final Long visitorId);
}

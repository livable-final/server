package com.livable.server.point.repository;

import com.livable.server.entity.PointLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PointLogRepository extends JpaRepository<PointLog, Long> {

    @Query(value = "SELECT * FROM point_log WHERE DATE(created_at) = :date", nativeQuery = true)
    List<PointLog> findLogsByDate(@Param("date") LocalDate date);
}

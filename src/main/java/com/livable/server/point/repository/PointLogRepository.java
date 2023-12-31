package com.livable.server.point.repository;

import com.livable.server.entity.Point;
import com.livable.server.entity.PointLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PointLogRepository extends JpaRepository<PointLog, Long> {

    @Query(value = "SELECT * " +
            "FROM point_log " +
            "WHERE point_id = :pointId " +
            "AND paid_at BETWEEN :startDate AND :endDate " +
            "ORDER BY paid_at DESC", nativeQuery = true)
    List<PointLog> findDateRangeOfPointLogByPointId(
            @Param("pointId") Long pointId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );


    @Query(value = "SELECT p " +
            "FROM Point p " +
            "WHERE p.member.id = :memberId"

    )
    Point findByMemberId(@Param("memberId") Long memberId);
}

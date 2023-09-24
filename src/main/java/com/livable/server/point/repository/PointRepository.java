package com.livable.server.point.repository;

import com.livable.server.entity.Point;
import com.livable.server.entity.PointCode;
import com.livable.server.point.dto.PointResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PointRepository extends JpaRepository<Point, Long> {

    Optional<Point> findByMember_Id(Long memberId);

    @Query("SELECT new com.livable.server.point.dto.PointResponse$ReviewCountDTO(COUNT(pl.id)) " +
            "FROM PointLog pl " +
            "WHERE pl.point.id = :pointId " +
            "AND pl.createdAt BETWEEN :startDate AND :endDate " +
            "AND pl.code IN (:codes)")
    PointResponse.ReviewCountDTO findPointCountById(
            @Param("pointId") Long pointId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("codes") List<PointCode> codes);
}

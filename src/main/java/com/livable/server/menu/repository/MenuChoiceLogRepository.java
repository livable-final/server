package com.livable.server.menu.repository;

import com.livable.server.entity.MenuChoiceLog;
import com.livable.server.menu.dto.MenuChoiceProjection;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuChoiceLogRepository extends JpaRepository<MenuChoiceLog, Long> {

    Optional<MenuChoiceLog> findByMemberIdAndDate(Long memberId, LocalDate date);

    @Query("SELECT new com.livable.server.menu.dto.MenuChoiceProjection(mcl.building, mcl.menu, mcl.date, COUNT(mcl.menu.id)) " +
        "FROM MenuChoiceLog mcl " +
        "WHERE mcl.date = :referenceDate " +
        "GROUP BY mcl.building, mcl.menu, mcl.date " +
        "ORDER BY count(mcl.menu.id) DESC")
    List<MenuChoiceProjection> findMenuChoiceLog(@Param("referenceDate") LocalDate referenceDate);

}
